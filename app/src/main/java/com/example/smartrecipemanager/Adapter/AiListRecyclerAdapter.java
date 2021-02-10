package com.example.smartrecipemanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.R;

import java.util.ArrayList;
import java.util.List;

public class AiListRecyclerAdapter extends RecyclerView.Adapter<AiListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<String> predictName;
    private List<String> predictText;
    public static List<Boolean> predictSelect;
    public AiListRecyclerAdapter(Context context, List<String> predictName, List<String>predictText){
        this.context=context;
        this.predictName=predictName;
        this.predictText=predictText;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //binding style_list layout for recyclerview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ai_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        predictSelect=new ArrayList<Boolean>();
        for(int i = 0;i<predictName.size();i++){
            predictSelect.add(false);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //register click listener for cardView, which contains ingredients name
        holder.IngredientName.setText(predictText.get(position));
        holder.itemView.setBackgroundColor(Color.WHITE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(predictSelect.get(position)==false){
                    holder.itemView.setBackgroundColor(Color.parseColor("#c9c9c9"));
                    predictSelect.set(position,true);
                }else{
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    predictSelect.set(position,false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return predictName.size();
    }

    @Override
    public int getItemViewType(int position) { return position; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView IngredientName;
        public ViewHolder(View view) {
            super(view);
            IngredientName = (TextView) view.findViewById(R.id.ai_ingredient_name);
        }
    }
}
