package com.example.smartrecipemanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartrecipemanager.R;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/** IngredientListRecyclerAdapter
*  for Search activity ingredientsFragment recyclerview
*  it can register listener for Search activity ingredient fragment recyclerview
* */
public class IngredientListRecyclerAdapter extends RecyclerView.Adapter<IngredientListRecyclerAdapter.ViewHolder> {

    private Context context;
    public static List<String> ingredientsList;
    private List<Boolean> Status;
    private List<String> Name;
    private List<Integer> Id;

    public IngredientListRecyclerAdapter(Context context){
        this.context=context;
        ingredientsList = new ArrayList<>();
        Status=new ArrayList<Boolean>();
        for(int i=0;i<24;i++){
            Status.add(false);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardview;
        ImageView ingredient_image;
        TextView ingredient_name;

        public ViewHolder(View view) {
            super(view);
            cardview=(CardView)view.findViewById(R.id.cardviewIngredient);
            ingredient_image = (ImageView) view.findViewById(R.id.ingredient_image);
            ingredient_name = (TextView) view.findViewById(R.id.ingredient_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //initialize variable
        Name = asList("bacon","beef","bread", "cheese","carrot","chicken","cucumber","duck","egg","fish","flour","honey","lamb","lemon","milk","mushroom","nuts","pork","potato","rice","salmon","tofu","tomato","wine");
        Id=asList(R.drawable.bacon,R.drawable.beef,R.drawable.bread, R.drawable.cheese,R.drawable.carrot,R.drawable.chicken,R.drawable.cucumber,R.drawable.duck,R.drawable.egg,R.drawable.fish,R.drawable.flour,R.drawable.honey,R.drawable.lamb,R.drawable.lemon,R.drawable.milk,R.drawable.mushroom,R.drawable.nuts,R.drawable.pork,R.drawable.potato,R.drawable.rice,R.drawable.salmon,R.drawable.tofu,R.drawable.tomato,R.drawable.wine);
        //binding ingredient_list layout for recyclerview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            //load ingredients image for layout(use Glide to improve performance)
            Glide.with(context).load(Id.get(position)).into(holder.ingredient_image);
            //load ingredients name in layout
            holder.ingredient_name.setText(Name.get(position));
            //set color, if it was selected, turn gray
            if (Status.get(position)== false) {
                holder.cardview.setCardBackgroundColor(Color.WHITE);
            } else {
                holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
            }
            //register click listener for cardView, which contains ingredient image and name
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Status.get(position)== false){
                        Status.set(position,true);
                        ingredientsList.add(Name.get(position));
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        Status.set(position,false);
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf(Name.get(position)));
                    }

                }
            });
    }

    @Override
    public int getItemCount() {
        return 24;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
