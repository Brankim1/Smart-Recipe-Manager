package com.example.smartrecipemanager.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.R;
import com.example.smartrecipemanager.Recipe;
import com.example.smartrecipemanager.RecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/** SearchListRecyclerAdapter
*  for SearchResult activity and home activity  recyclerview
*  it can show search result image and name in recyclerview
* */
public class SearchListRecyclerAdapter extends RecyclerView.Adapter<SearchListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Recipe> recipeList;

    public SearchListRecyclerAdapter(Context context, List<Recipe> recipeList) {
        this.context=context;
        this.recipeList = recipeList;
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
        Recipe recipe = recipeList.get(position);
        // handle empty image
        if (recipeList.get(position).getPic().isEmpty()) {
        } else{
            //load food image for layout
            Picasso.get().load(recipeList.get(position).getPic()).into(holder.recipeImage);
        }

        //load food title for layout
        holder.recipeName.setText(recipe.getTitle());
        //register click listener for cardView, which contains food image and title
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to RecipeActivity to show recipe details
                Intent RecipeActivityIntent = new Intent(context, RecipeActivity.class);
                RecipeActivityIntent.putExtra("id",recipeList.get(position).getId());
                RecipeActivityIntent.putExtra("title",recipeList.get(position).getTitle());
                RecipeActivityIntent.putExtra("pic",recipeList.get(position).getPic());
                RecipeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(RecipeActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() { return recipeList.size(); }

    @Override
    public int getItemViewType(int position) { return position; }

}