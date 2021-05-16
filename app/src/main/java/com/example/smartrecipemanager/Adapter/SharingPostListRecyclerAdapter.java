package com.example.smartrecipemanager.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.Post;
import com.example.smartrecipemanager.PostDetailActivity;
import com.example.smartrecipemanager.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**SharingPostListRecyclerAdapter
 *  for Sharing activity  recyclerview
 *  it can show sharing posts image and name in recyclerview
 * */
public class SharingPostListRecyclerAdapter extends RecyclerView.Adapter<SharingPostListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;

    public SharingPostListRecyclerAdapter(Context context, List<Post> postList) {
        this.context=context;
        this.postList = postList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeName;

        public ViewHolder(View view) {
            super(view);
            recipeImage = (ImageView) view.findViewById(R.id.recipe_image);
            recipeName = (TextView) view.findViewById(R.id.recipe_name);
        }
    }


    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //binding recipe_list layout for recyclerview
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Post post = postList.get(position);
        //handle empty image
        if (postList.get(position).getPic() != "") {
            //load post image for layout
            Picasso.get().load(postList.get(position).getPic()).fit().into(holder.recipeImage);
        }
        //load post title for layout
        holder.recipeName.setText(post.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to PostDetailActivity
                Intent PostDetailActivityIntent = new Intent(context, PostDetailActivity.class);
                PostDetailActivityIntent.putExtra("postid",String.valueOf(postList.get(position).getPostid()));
                PostDetailActivityIntent.putExtra("userid",postList.get(position).getUserid());
                PostDetailActivityIntent.putExtra("name",postList.get(position).getName());
                PostDetailActivityIntent.putExtra("title",postList.get(position).getTitle());
                PostDetailActivityIntent.putExtra("pic",postList.get(position).getPic());
                PostDetailActivityIntent.putExtra("content",postList.get(position).getContent());
                PostDetailActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(PostDetailActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public int getItemViewType(int position) { return position; }

}