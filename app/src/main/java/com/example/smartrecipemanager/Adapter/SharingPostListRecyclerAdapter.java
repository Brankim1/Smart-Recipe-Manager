package com.example.smartrecipemanager.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartrecipemanager.Post;
import com.example.smartrecipemanager.PostDetailActivity;
import com.example.smartrecipemanager.R;
import com.example.smartrecipemanager.Recipe;
import com.example.smartrecipemanager.RecipeActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SharingPostListRecyclerAdapter extends RecyclerView.Adapter<SharingPostListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Post> mPostList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        ImageView recipeImage;
        TextView recipeName;

        public ViewHolder(View view) {
            super(view);
            cardview=(CardView)view.findViewById(R.id.cardviewHome);
            recipeImage = (ImageView) view.findViewById(R.id.recipe_image);
            recipeName = (TextView) view.findViewById(R.id.recipe_name);
        }
    }


    public SharingPostListRecyclerAdapter(Context context, List<Post> postList) {
        this.context=context;
        mPostList = postList;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Post post = mPostList.get(position);
        //防止无图片闪退
        if (mPostList.get(position).getPic() != "") {
            Picasso.get().load(mPostList.get(position).getPic()).fit().into(holder.recipeImage);
        }

        holder.recipeName.setText(post.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PostDetailActivityIntent = new Intent(context, PostDetailActivity.class);
                PostDetailActivityIntent.putExtra("postid",String.valueOf(mPostList.get(position).getPostid()));
                PostDetailActivityIntent.putExtra("userid",mPostList.get(position).getUserid());
                PostDetailActivityIntent.putExtra("name",mPostList.get(position).getName());
                PostDetailActivityIntent.putExtra("title",mPostList.get(position).getTitle());
                PostDetailActivityIntent.putExtra("pic",mPostList.get(position).getPic());
                PostDetailActivityIntent.putExtra("content",mPostList.get(position).getContent());
                PostDetailActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(PostDetailActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }



    @Override
    public int getItemViewType(int position) {

        return position;
    }
}