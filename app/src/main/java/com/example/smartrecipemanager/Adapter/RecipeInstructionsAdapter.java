package com.example.smartrecipemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartrecipemanager.R;
import java.util.List;

/*
 * for show recipe detail Instructions and order image in RecipeInstructionsFragment recyclerview
 * it can load image and text in recyclerview
 * */
public class RecipeInstructionsAdapter extends RecyclerView.Adapter<RecipeInstructionsAdapter.MyViewHolder> {
    private Context context;
    private List<String> mData;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ingredient_name;
        ImageView ingredient_pic;

        public MyViewHolder(View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.recipe_ingredient_name);
            ingredient_pic = itemView.findViewById(R.id.recipe_ingredient_img);
        }
    }

    public RecipeInstructionsAdapter(Context context, List<String> mData) {
        this.context=context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecipeInstructionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //binding item_ingredient layout for recyclerview(recipe ingredient and instruction use same layout)
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.item_ingredient, parent, false);
        return new RecipeInstructionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeInstructionsAdapter.MyViewHolder holder, final int position) {
        holder.ingredient_name.setText(mData.get(position));
        //add number for the order of instructions
        if(position==0){
            holder.ingredient_pic.setImageResource(R.drawable.number1);
        }else if(position==1){
            holder.ingredient_pic.setImageResource(R.drawable.number2);
        }else if(position==2){
            holder.ingredient_pic.setImageResource(R.drawable.number3);
        }else if(position==3){
            holder.ingredient_pic.setImageResource(R.drawable.number4);
        }else if(position==4){
            holder.ingredient_pic.setImageResource(R.drawable.number5);
        }else if(position==5){
            holder.ingredient_pic.setImageResource(R.drawable.number6);
        }else if(position==6){
            holder.ingredient_pic.setImageResource(R.drawable.number7);
        }else if(position==7){
            holder.ingredient_pic.setImageResource(R.drawable.number8);
        }else if(position==8){
            holder.ingredient_pic.setImageResource(R.drawable.number9);
        }else{
            holder.ingredient_pic.setImageResource(R.drawable.number0);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}