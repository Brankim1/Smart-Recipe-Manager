package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

/*
* SharingAddActivity, upload user's post
* */
public class SharingAddActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    Uri selectedImageURI;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_add);
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
                titleText = title.getEditText().getText().toString();
                contentText = content.getEditText().getText().toString();
                if (titleText.length() == 0) {
                    Toast.makeText(SharingAddActivity.this, "Please input email", Toast.LENGTH_SHORT).show();
                } else if (contentText.length() == 0) {
                    Toast.makeText(SharingAddActivity.this, "Please input password", Toast.LENGTH_SHORT).show();
                }
                mRootRef2 = mRootRef1 = FirebaseDatabase.getInstance().getReference().child("sharing").child(String.valueOf(postid));
                if (titleText.length() != 0 && contentText.length() != 0) {
                    uploadImage();
                }
            }
        });

        //register listener for upload Img button
        uploadImg = (Button) findViewById(R.id.ImageBut);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getApplicationContext());
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getApplicationContext());
            }
        });
    }



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
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.d("upload image","successful");

            }
        });
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
                    Log.d("download image","url is" +downLoadUrl);
                    uploadPost();
                } else {

                }
            }
        });
    }

    private void uploadPost() {
        mRootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = new Post(postid,userid,"author", titleText, String.valueOf(downLoadUrl),contentText);
                mRootRef2.setValue(post);
                Toast.makeText(SharingAddActivity.this, "Post Successful", Toast.LENGTH_SHORT).show();
                title.getEditText().getText().clear();
                content.getEditText().getText().clear();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("save ","cancelled");
            }
        });
    }

    private void getInformation() {
        //get post id
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        userid=uid;
        mRootRef1 = FirebaseDatabase.getInstance().getReference().child("sharing");
        mRootRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postid = dataSnapshot.getChildrenCount()+1;
                Log.d("post id "," is"+postid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("post id "," is cancelled");
            }
        });
    }
    private void selectImage(Context context) {
        //select image in device
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE&&resultCode==RESULT_OK) {
            selectedImageURI = data.getData();
            Picasso.get().load(selectedImageURI).into(imageView);
        }else{
            Toast.makeText(getApplicationContext(),"Image Select Cancel",Toast.LENGTH_SHORT).show();
        }
    }


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