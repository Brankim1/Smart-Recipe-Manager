package com.example.smartrecipemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartrecipemanager.Ingredient;
import com.example.smartrecipemanager.R;
import com.squareup.picasso.Picasso;
import java.util.List;

/**RecipeIngredientsAdapter
* for RecipeActivity RecipeIngredientsFragment recyclerview
* it can load recipe Ingredients' name and image in recyclerview
* */
public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.MyViewHolder> {

    private Context context;
    private List<Ingredient> data;

    public RecipeIngredientsAdapter(Context context, List<Ingredient> data) {
        this.context=context;
        this.data = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ingredient_name;
        ImageView ingredient_pic;

        public MyViewHolder(View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.recipe_ingredient_name);
            ingredient_pic = itemView.findViewById(R.id.recipe_ingredient_img);
        }
    }

    @NonNull
    @Override
    public RecipeIngredientsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //binding item_ingredient layout for recyclerview
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.item_ingredient, parent, false);
        return new RecipeIngredientsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeIngredientsAdapter.MyViewHolder holder, final int position) {
        //load ingredients name for layout
        holder.ingredient_name.setText(data.get(position).getName());
        //load ingredients image for layout
        Picasso.get().load(data.get(position).getPic()).into(holder.ingredient_pic);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
