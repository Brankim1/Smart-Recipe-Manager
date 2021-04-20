package com.example.smartrecipemanager.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.Concept;
import com.clarifai.grpc.api.Data;
import com.clarifai.grpc.api.Image;
import com.clarifai.grpc.api.Input;
import com.clarifai.grpc.api.MultiOutputResponse;
import com.clarifai.grpc.api.PostModelOutputsRequest;
import com.clarifai.grpc.api.V2Grpc;
import com.clarifai.grpc.api.status.StatusCode;
import com.example.smartrecipemanager.Adapter.AiListRecyclerAdapter;
import com.example.smartrecipemanager.R;
import com.example.smartrecipemanager.SearchResultActivity;
import com.google.protobuf.ByteString;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**AiFragment
 * AI fragment in search Activity
 * it can upload image to clarifai api to get predicted ingredients
 */
public class AiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Uri imgPath;
    private Button uploadImg;
    private Button searchRecipe;
    private Button searchAi;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private List<String> predictName;
    private List<String> predictText;
    private List<Boolean> predictSelect;
    private List<String> SelectText;
    private View inflate;
    private TextView camera;
    private TextView pic;
    private TextView cancel;
    private Dialog dialog;
    private Intent searchResultIntent;
    private AiListRecyclerAdapter myAdapter;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    public AiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AiFragment newInstance(String param1, String param2) {
        AiFragment fragment = new AiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_ai, container, false);
        //init variable and layout
        recyclerView = (RecyclerView) root.findViewById(R.id.AiRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);

        imageView = (ImageView)root.findViewById(R.id.AiImage);
        predictName=new ArrayList<String>();
        predictText=new ArrayList<String>();

        uploadImg = (Button) root.findViewById(R.id.uploadImage);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(null);
                predictName.clear();
                predictText.clear();
                //choose image from gallery or camera
                selectImage();
            }
        });
        searchAi = (Button) root.findViewById(R.id.searchAi);
        searchAi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageView.getDrawable()==null){
                    Toast.makeText(getContext(),"Please select Image or waiting for Image load",Toast.LENGTH_SHORT).show();
                }else{
                    //upload image to clarifai API to predict
                    new Thread(networkTask).start();
                }

            }
        });
        searchRecipe = (Button) root.findViewById(R.id.searchAiRecipe);
        searchRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (predictName.size() == 0) {
                    Toast.makeText(getContext(),"Please waiting for AI recognise",Toast.LENGTH_SHORT).show();
                } else {
                    SelectText = new ArrayList<String>();
                    //get selected ingredients
                    predictSelect = AiListRecyclerAdapter.predictSelect;
                    for (int i = 0; i < predictSelect.size(); i++) {
                        if (predictSelect.get(i) == true) {
                            SelectText.add(predictName.get(i));
                        }
                    }
                    //reshape selected ingredients to string
                    String queryData = "";
                    for (int i = 0; i < SelectText.size(); i++) {
                        if (i == 0) {
                            queryData = SelectText.get(i).toString();
                        } else {
                            //reshape data that what ingredients were selected
                            queryData = queryData + "," + SelectText.get(i).toString();
                        }
                    }
                    //go to SearchResultActivity
                    searchResultIntent = new Intent(getActivity(), SearchResultActivity.class);
                    searchResultIntent.putExtra("data", queryData);
                    searchResultIntent.putExtra("ingredient","ingredientSearch");
                    searchResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(searchResultIntent);
                }
            }
        });
        return root;
    }

    /**
     * Select Image
     *  through call gallery or camera
     * */
    public void selectImage(){
        //custom dialog
        dialog = new Dialog(getContext(),R.style.DialogTheme);
        inflate = LayoutInflater.from(getContext()).inflate(R.layout.choosephoto_dialog, null);
        camera = (TextView) inflate.findViewById(R.id.takePhoto);
        pic = (TextView) inflate.findViewById(R.id.choosePhoto);
        cancel = (TextView) inflate.findViewById(R.id.cancel);
        //add listener for dialog text
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
        //go to the device default gallery application
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    private void takePic(){
         //go to the device default camera application
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(),"Open Camera fail, Please use Gallery",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //get returned image data from gallery or camera
        if (requestCode == PICK_IMAGE&&resultCode==RESULT_OK) {
            imgPath = data.getData();
            //load image to imageView
            Picasso.get().load(imgPath).into(imageView);
            dialog.dismiss();
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //load image to imageView
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            dialog.dismiss();
        }else{
            Toast.makeText(getContext(),"Image Select Cancel",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
    /**
     * upload image to clarifai api to get predicted ingredients
     * use child thread to get predicted information
     * */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getJsonChannel())
                            .withCallCredentials(new ClarifaiCallCredentials("a742e6469bd24d3da69ff7f365d40bb2"));
            MultiOutputResponse response;
            //get image form imageView, and reshape to ByteArray
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            //food model key=bd367be194cf45149e75f01d59f77ba7
            byte[] data = baos.toByteArray();
            response = stub.postModelOutputs(
                    PostModelOutputsRequest.newBuilder()
                            .setModelId("bd367be194cf45149e75f01d59f77ba7")
                            .addInputs(
                                    Input.newBuilder().setData(
                                            Data.newBuilder().setImage(
                                                    Image.newBuilder()
                                                            .setBase64(ByteString.copyFrom(data))
                                    )
                            ))
                            .build()
            );
            //handle response
            if (response.getStatus().getCode() != StatusCode.SUCCESS) {
                Toast.makeText(getContext(),"Predicted fail, Please check Internet",Toast.LENGTH_SHORT).show();
                throw new RuntimeException("Request failed, status: " + response.getStatus());
            }
            Log.d("AIFragment","Image predict result");
            for (Concept c : response.getOutputs(0).getData().getConceptsList()) {
                String name=c.getName();
                String value=String.valueOf(c.getValue());
                Log.d("AIFragment",""+name+":"+value);
                predictName.add(name);
                predictText.add(name+" : " +value);
            }
            //run UI task to show predicted name
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);

        }
    };
    /**
     * run UI task to show predicted ingredient
     * */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // UI task, set recyclerView
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    AiListRecyclerAdapter myAdapter = new AiListRecyclerAdapter(getContext(),predictName,predictText);
                    recyclerView.setAdapter(myAdapter);
                    break;

            }
        };
    };

}