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
import com.example.smartrecipemanager.SearchResultActivity;
import com.squareup.picasso.Picasso;


/**StyleListRecyclerAdapter
 *  for Search activity StyleFragment recyclerview
 *  it can register listener for Search activity Style fragment recyclerview
 * */
public class StyleListRecyclerAdapter extends RecyclerView.Adapter<StyleListRecyclerAdapter.ViewHolder> {

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView styleImage;
        TextView styleName;
        public ViewHolder(View view) {
            super(view);
            styleImage = (ImageView) view.findViewById(R.id.style_image);
            styleName = (TextView) view.findViewById(R.id.style_name);
        }
    }

    public StyleListRecyclerAdapter(Context context){
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //binding style_list layout for recyclerview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //register click listener for cardView, which contains style image and name
        if(position==0){
            Picasso.get().load(R.drawable.american).into(holder.styleImage);
            holder.styleName.setText("American");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to SearchResult Activity
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","American");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==1){
            Picasso.get().load(R.drawable.turkish).into(holder.styleImage);
            holder.styleName.setText("Turkish");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","Turkish");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==2){
            Picasso.get().load(R.drawable.french).into(holder.styleImage);
            holder.styleName.setText("French");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","French");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==3){
            Picasso.get().load(R.drawable.indian).into(holder.styleImage);
            holder.styleName.setText("Indian");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","Indian");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==4){
            Picasso.get().load(R.drawable.chinese).into(holder.styleImage);
            holder.styleName.setText("Chinese");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","Chinese");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==5){
            Picasso.get().load(R.drawable.italian).into(holder.styleImage);
            holder.styleName.setText("Italian");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","Italian");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==6){
            Picasso.get().load(R.drawable.dessert).into(holder.styleImage);
            holder.styleName.setText("Dessert");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","Dessert");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==7){
            Picasso.get().load(R.drawable.vegan).into(holder.styleImage);
            holder.styleName.setText("Vegan");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","vegan");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==8){
            Picasso.get().load(R.drawable.salad).into(holder.styleImage);
            holder.styleName.setText("Salad");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","Salad");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }else if(position==9){
            Picasso.get().load(R.drawable.healthy).into(holder.styleImage);
            holder.styleName.setText("Healthy");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ResultIntent = new Intent(context, SearchResultActivity.class);
                    ResultIntent.putExtra("data","Healthy");
                    ResultIntent.putExtra("ingredient","");
                    ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(ResultIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) { return position; }

}
