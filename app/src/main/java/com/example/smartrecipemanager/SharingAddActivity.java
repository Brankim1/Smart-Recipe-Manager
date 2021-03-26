package com.example.smartrecipemanager;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**SharingAddActivity
* SharingAddActivity, upload user's post
 * at first get post id
 * then upload image to server, and get image url from server.
 * finally upload post
* */
public class SharingAddActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    private Uri selectedImageURI;
    private View inflate;
    private TextView camera;
    private TextView pic;
    private TextView cancel;
    private Dialog dialog;
    private FirebaseAuth mAuth;
    private TextInputLayout title;
    private TextInputLayout content;
    private String titleText;
    private String contentText;
    private ImageView imageView;
    private Button post;
    private Button uploadImg;
    private long postid;
    private String userid;
    private DatabaseReference mRootRef1;
    private DatabaseReference mRootRef2;
    private FirebaseStorage storage;
    private Uri downLoadUrl;
    public Post post2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_add);
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            setToolBar();
            getInformation();
            storage = FirebaseStorage.getInstance();
            mAuth = FirebaseAuth.getInstance();
            title=(TextInputLayout)findViewById(R.id.title);
            content=(TextInputLayout)findViewById(R.id.content);
            imageView = (ImageView) findViewById(R.id.picture);

            //register listener for post button
            post=(Button)findViewById(R.id.post);
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //data check
                    titleText = title.getEditText().getText().toString();
                    contentText = content.getEditText().getText().toString();
                    if (titleText.length() == 0) {
                        Toast.makeText(SharingAddActivity.this, "Please input title", Toast.LENGTH_SHORT).show();
                    } else if (contentText.length() == 0) {
                        Toast.makeText(SharingAddActivity.this, "Please input content", Toast.LENGTH_SHORT).show();
                    } else if(imageView.getDrawable()==null){
                        Toast.makeText(SharingAddActivity.this,"Please select Image or waiting for Image load",Toast.LENGTH_SHORT).show();
                    }
                    mRootRef2 = mRootRef1 = FirebaseDatabase.getInstance().getReference().child("sharing").child(String.valueOf(postid));
                    if (titleText.length() != 0 && contentText.length() != 0&&imageView.getDrawable()!=null) {
                        uploadImage();
                    }
                }
            });

            //register listener for upload Img button
            uploadImg = (Button) findViewById(R.id.ImageBut);
            uploadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
        }else{
            Toast.makeText(SharingAddActivity.this, "Network Connect Fail", Toast.LENGTH_LONG).show();

        }

    }

    /**
     * upload image to server, and get image url from server
     * */
    private void uploadImage() {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to img
        final StorageReference mountainsRef = storageRef.child(String.valueOf(postid)).child(String.valueOf(selectedImageURI));

        //upload image by byte
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SharingAddActivity.this,"Image upload fail",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //get image url in server
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return mountainsRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downLoadUrl = task.getResult();
                            uploadPost();
                        } else {

                        }
                    }
                });
            }
        });

    }
    /**
     * after get image url from server, then post to server
     * */
    private void uploadPost() {
        mRootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                post2 = new Post(postid,userid,"author", titleText, String.valueOf(downLoadUrl),contentText);
                mRootRef2.setValue(post2);
                Toast.makeText(SharingAddActivity.this, "Post Successful", Toast.LENGTH_SHORT).show();
                title.getEditText().getText().clear();
                content.getEditText().getText().clear();
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SharingAddActivity.this,"Post fail",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * get post id, it can identify posts
     * */
    private void getInformation() {
        List id=new ArrayList<Long>();
        //get post id
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        userid=uid;
        mRootRef1 = FirebaseDatabase.getInstance().getReference().child("sharing");
        mRootRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //get post ids
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Post post=child.getValue(Post.class);
                        id.add(post.getPostid());
                    }
                    postid= (long) Collections.max(id)+1;
                }else{
                    postid=1;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SharingAddActivity.this,"Post id get fail",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * select image from gallery and camera
     * */
    public void selectImage(){
        dialog = new Dialog(this,R.style.DialogTheme);
        inflate = LayoutInflater.from(this).inflate(R.layout.choosephoto_dialog, null);
        camera = (TextView) inflate.findViewById(R.id.takePhoto);
        pic = (TextView) inflate.findViewById(R.id.choosePhoto);
        cancel = (TextView) inflate.findViewById(R.id.cancel);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAlbum();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //layout to dialog
        dialog.setContentView(inflate);
        //get activity window
        Window dialogWindow = dialog.getWindow();
        //dialog pop up from bottom
        dialogWindow.setGravity( Gravity.BOTTOM);
        //show dialog
        dialog.show();
    }

    private void pickAlbum(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    private void takePic(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(),"Open Camera fail, Please use Gallery",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE&&resultCode==RESULT_OK) {
            selectedImageURI = data.getData();
            Picasso.get().load(selectedImageURI).into(imageView);
            dialog.dismiss();
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            dialog.dismiss();
        }else{
            Toast.makeText(getApplicationContext(),"Image Select Cancel",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    /**
     * set toolbar*/
    public void setToolBar() {
        //component initialize
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //change toolbar name and add button
        if (toolbar != null) {
            toolbar.setTitle("Post");
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        }
        //toolbar button set listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
}}