package com.example.smartrecipemanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.R;
import com.example.smartrecipemanager.SearchResultActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class IngredientListRecyclerAdapter extends RecyclerView.Adapter<IngredientListRecyclerAdapter.ViewHolder> {

    private Context context;
    public static List<String> ingredientsList;
    public boolean aubergine,bacon,beef,bread, cheese,carrot,chicken,cucumber,duck,egg,fish,flour,honey,lamb,lemon,milk,mushroom,nuts,pork,potato,rice,salmon,tofu,tomato,wine=false;
    static class ViewHolder extends RecyclerView.ViewHolder {
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


    public IngredientListRecyclerAdapter(Context context){
        this.context=context;
        ingredientsList = new ArrayList<>();
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(position==0){
            Picasso.get().load(R.drawable.bacon).into(holder.ingredient_image);
            holder.ingredient_name.setText("bacon");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( bacon== false) {
                        bacon = true;
                        ingredientsList.add("bacon");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        bacon = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("bacon"));
                    }
                }
            });
        }else if(position==1){
            Picasso.get().load(R.drawable.beef).into(holder.ingredient_image);
            holder.ingredient_name.setText("beef");
            holder.cardview.setCardBackgroundColor(Color.WHITE);
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (beef == false) {
                        beef = true;
                        ingredientsList.add("beef");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        beef = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("beef"));
                    }
                }
            });
        }else if(position==2){
            Picasso.get().load(R.drawable.bread).into(holder.ingredient_image);
            holder.ingredient_name.setText("bread");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bread == false) {
                        bread  = true;
                        ingredientsList.add("bread");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        bread = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("bread"));
                    }
                }
            });
        }else if(position==3){
            Picasso.get().load(R.drawable.cheese).into(holder.ingredient_image);
            holder.ingredient_name.setText("cheese");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( cheese== false) {
                        cheese = true;
                        ingredientsList.add("cheese");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        cheese = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("cheese"));
                    }
                }
            });
        }else if(position==4){
            Picasso.get().load(R.drawable.carrot).into(holder.ingredient_image);
            holder.ingredient_name.setText("carrot");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( carrot== false) {
                        carrot = true;
                        ingredientsList.add("carrot");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        carrot = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("carrot"));
                    }
                }
            });
        }else if(position==5){
            Picasso.get().load(R.drawable.chicken).into(holder.ingredient_image);
            holder.ingredient_name.setText("chicken");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chicken == false) {
                        chicken = true;
                        ingredientsList.add("chicken");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        chicken = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("chicken"));
                    }
                }
            });
        }else if(position==6){
            Picasso.get().load(R.drawable.cucumber).into(holder.ingredient_image);
            holder.ingredient_name.setText("cucumber");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cucumber == false) {
                        cucumber = true;
                        ingredientsList.add("cucumber");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        cucumber = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("cucumber"));
                    }
                }
            });
        }else if(position==7){
            Picasso.get().load(R.drawable.duck).into(holder.ingredient_image);
            holder.ingredient_name.setText("duck");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (duck == false) {
                        duck= true;
                        ingredientsList.add("duck");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        duck  = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("duck"));
                    }
                }
            });
        }else if(position==8){
            Picasso.get().load(R.drawable.egg).into(holder.ingredient_image);
            holder.ingredient_name.setText("egg");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (egg == false) {
                        egg = true;
                        ingredientsList.add("egg");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        egg = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("egg"));
                    }
                }
            });

        }else if(position==9){
            Picasso.get().load(R.drawable.fish).into(holder.ingredient_image);
            holder.ingredient_name.setText("fish");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fish == false) {
                        fish = true;
                        ingredientsList.add("fish");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        fish = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("fish"));
                    }
                }
            });
        }else if(position==10){
            Picasso.get().load(R.drawable.flour).into(holder.ingredient_image);
            holder.ingredient_name.setText("flour");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flour == false) {
                        flour = true;
                        ingredientsList.add("flour");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        flour = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("flour"));
                    }
                }
            });
        }else if(position==11){
            Picasso.get().load(R.drawable.honey).into(holder.ingredient_image);
            holder.ingredient_name.setText("honey");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( honey== false) {
                        honey= true;
                        ingredientsList.add("honey");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        honey = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("honey"));
                    }
                }
            });
        }else if(position==12){
            Picasso.get().load(R.drawable.lamb).into(holder.ingredient_image);
            holder.ingredient_name.setText("lamb");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lamb == false) {
                        lamb = true;
                        ingredientsList.add("lamb");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        lamb = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("lamb"));
                    }
                }
            });
        }else if(position==13){
            Picasso.get().load(R.drawable.lemon).into(holder.ingredient_image);
            holder.ingredient_name.setText("lemon");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lemon == false) {
                        lemon = true;
                        ingredientsList.add("lemon");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        lemon = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("lemon"));
                    }
                }
            });
        }else if(position==14){
            Picasso.get().load(R.drawable.milk).into(holder.ingredient_image);
            holder.ingredient_name.setText("milk");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( milk== false) {
                        milk= true;
                        ingredientsList.add("milk");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        milk = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("milk"));
                    }
                }
            });
        }else if(position==15){
            Picasso.get().load(R.drawable.mushroom).into(holder.ingredient_image);
            holder.ingredient_name.setText("mushroom");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mushroom == false) {
                        mushroom = true;
                        ingredientsList.add("mushroom");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        mushroom = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("mushroom"));
                    }
                }
            });
        }else if(position==16){
            Picasso.get().load(R.drawable.nuts).into(holder.ingredient_image);
            holder.ingredient_name.setText("nuts");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nuts == false) {
                        nuts = true;
                        ingredientsList.add("nuts");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        nuts= false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("nuts"));
                    }
                }
            });
        }else if(position==17){
            Picasso.get().load(R.drawable.pork).into(holder.ingredient_image);
            holder.ingredient_name.setText("pork");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pork == false) {
                        pork = true;
                        ingredientsList.add("pork");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        pork = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("pork"));
                    }
                }
            });
        }else if(position==18){
            Picasso.get().load(R.drawable.potato).into(holder.ingredient_image);
            holder.ingredient_name.setText("potato");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (potato == false) {
                        potato = true;
                        ingredientsList.add("potato");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        potato = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("potato"));
                    }
                }
            });
        }else if(position==19){
            Picasso.get().load(R.drawable.rice).into(holder.ingredient_image);
            holder.ingredient_name.setText("rice");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( rice== false) {
                        rice = true;
                        ingredientsList.add("rice");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        rice = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("rice"));
                    }
                }
            });
        }else if(position==20){
            Picasso.get().load(R.drawable.salmon).into(holder.ingredient_image);
            holder.ingredient_name.setText("salmon");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (salmon== false) {
                        salmon = true;
                        ingredientsList.add("salmon");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        salmon = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("salmon"));
                    }
                }
            });
        }else if(position==21){
            Picasso.get().load(R.drawable.tofu).into(holder.ingredient_image);
            holder.ingredient_name.setText("tofu");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tofu == false) {
                        tofu = true;
                        ingredientsList.add("tofu");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        tofu= false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("tofu"));
                    }
                }
            });
        }else if(position==22){
            Picasso.get().load(R.drawable.tomato).into(holder.ingredient_image);
            holder.ingredient_name.setText("tomato");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tomato == false) {
                        tomato = true;
                        ingredientsList.add("tomato");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        tomato = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("tomato"));
                    }
                }
            });
        }else if(position==23){
            Picasso.get().load(R.drawable.wine).into(holder.ingredient_image);
            holder.ingredient_name.setText("wine");
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wine == false) {
                        wine = true;
                        ingredientsList.add("wine");
                        holder.cardview.setCardBackgroundColor(Color.parseColor("#c9c9c9"));
                    } else {
                        wine = false;
                        holder.cardview.setCardBackgroundColor(Color.WHITE);
                        ingredientsList.remove(ingredientsList.indexOf("wine"));
                    }
                }
            });

        }


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
