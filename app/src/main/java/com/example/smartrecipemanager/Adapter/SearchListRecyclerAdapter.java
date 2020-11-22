package com.example.smartrecipemanager.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.R;
import com.example.smartrecipemanager.Recipe;
import com.example.smartrecipemanager.RecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchListRecyclerAdapter extends RecyclerView.Adapter<SearchListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Recipe> mRecipeList;

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


    public SearchListRecyclerAdapter(Context context, List<Recipe> recipeList) {
        this.context=context;
        mRecipeList = recipeList;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Recipe recipe = mRecipeList.get(position);
        //防止无图片闪退
        if (mRecipeList.get(position).getPic().isEmpty()) {

        } else{
            Picasso.get().load(mRecipeList.get(position).getPic()).into(holder.recipeImage);
        }

        holder.recipeName.setText(recipe.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RecipeActivityIntent = new Intent(context, RecipeActivity.class);
                RecipeActivityIntent.putExtra("id",mRecipeList.get(position).getId());
                RecipeActivityIntent.putExtra("title",mRecipeList.get(position).getTitle());
                RecipeActivityIntent.putExtra("pic",mRecipeList.get(position).getPic());
                RecipeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(RecipeActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }



    @Override
    public int getItemViewType(int position) {

        return position;
    }
}