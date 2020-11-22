/* Assignment: 3
Campus: Ashdod
Authors:
Eliran Naduyev 312089105
Maria Garber
*/

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

import java.util.ArrayList;
import java.util.List;

public class RecipeInstructionAdapter extends RecyclerView.Adapter<RecipeInstructionAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mData;
    public static List<String> ingredientsList;

    public RecipeInstructionAdapter(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
        ingredientsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecipeInstructionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_ingredient, parent, false);
        return new RecipeInstructionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeInstructionAdapter.MyViewHolder holder, final int position) {
        holder.ingredient_name.setText(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
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

}
