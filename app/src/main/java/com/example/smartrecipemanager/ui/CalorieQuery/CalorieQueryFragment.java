package com.example.smartrecipemanager.ui.CalorieQuery;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartrecipemanager.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;

/**CalorieQueryFragment
 * CalorieQueryFragment for calorieActivity
 * it can query calorie data from Edamam Nutrition Analysis api
 */
public class CalorieQueryFragment extends Fragment {

    private CalorieQueryViewModel CalorieQueryViewModel;
    private JSONObject NutrientsObject;
    private String text;
    private String weight,calorie,totalFat,saturatedFat,polyFat, monFat,cholesterol,
            sodium,potassium,carbohydrates,protein,va,vc,calcium,iron;
    private TextInputLayout editText;
    private Button query;
    private TextView nameView,weightView,calorieView,totalFatView,saturatedFatView,polyFatView, monFatView,cholesterolView,
            sodiumView,potassiumView,carbohydratesView,proteinView,vaView,vcView,calciumView,ironView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalorieQueryViewModel =
                ViewModelProviders.of(this).get(CalorieQueryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_caloriequery, container, false);
        initView(root);
        return root;
    }
    public void initView(View root){
        editText=(TextInputLayout)root.findViewById(R.id.input);
        nameView = root.findViewById(R.id.name);
        query=(Button)root.findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=editText.getEditText().getText().toString();
                if(text.equals("")) {
                    Toast.makeText(getContext(), "Input Empty", Toast.LENGTH_SHORT).show();
                }else{
                    nameView.setText(text);
                    getNutrition();
                }
            }
        });
        weightView=(TextView)root.findViewById(R.id.weight);
        calorieView=(TextView)root.findViewById(R.id.Calorie);
        totalFatView =(TextView)root.findViewById(R.id.TotalFat);
        saturatedFatView=(TextView)root.findViewById(R.id.SaturatedFat);
        polyFatView=(TextView)root.findViewById(R.id.PolyunsaturatedFat);
        monFatView=(TextView)root.findViewById(R.id.MonounsaturatedFat);
        cholesterolView=(TextView)root.findViewById(R.id.Cholesterol);
        sodiumView=(TextView)root.findViewById(R.id.Sodium);
        potassiumView=(TextView)root.findViewById(R.id.PolyunsaturatedFat);
        carbohydratesView=(TextView)root.findViewById(R.id.Carbohydrates);
        proteinView=(TextView)root.findViewById(R.id.Protein);
        vaView=(TextView)root.findViewById(R.id.VitaminA);
        vcView=(TextView)root.findViewById(R.id.VitaminC);
        calciumView =(TextView)root.findViewById(R.id.Calcium);
        ironView =(TextView)root.findViewById(R.id.Iron);
    }

    /**
     * get nutrition analysis from edamam api
     * */
    public void getNutrition(){
        //get nutrition information
        final DecimalFormat format = new DecimalFormat("0.00");
        String url = "https://api.edamam.com/api/nutrition-data?app_id=6143c7de&app_key=0738a0a63dd93ea2e3e294c72def53c3&ingr=1%20"+ Uri.parse(text);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String key;
                            weight=response.optString("totalWeight")+"g";
                            NutrientsObject = (JSONObject) response.get("totalNutrients");
                            Iterator iterator = NutrientsObject.keys();
                            while(iterator.hasNext()){
                                //handle data from api
                                key = (String) iterator.next();
                                String label=NutrientsObject.getJSONObject(key).optString("label");
                                String quantity=NutrientsObject.getJSONObject(key).optString("quantity");
                                String unit=NutrientsObject.getJSONObject(key).optString("unit");
                                Log.d("calorieQueryFragment","json is "+label+quantity+unit);
                                if(label.equals("Energy")){
                                    calorie=format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Fat")){
                                    totalFat=format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Saturated")){
                                    saturatedFat =format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Polyunsaturated")){
                                    polyFat=format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Monounsaturated")){
                                    monFat=format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Cholesterol")){
                                    cholesterol=format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Sodium")){
                                    sodium=format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Potassium")){
                                    potassium=format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Carbs")){
                                    carbohydrates =format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Protein")){
                                    protein=format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Vitamin A")){
                                    va =format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Vitamin C")){
                                    vc =format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Calcium")){
                                    calcium =format.format(new BigDecimal(quantity))+unit;
                                }else if(label.equals("Iron")){
                                    iron =format.format(new BigDecimal(quantity))+unit;
                                }
                            }
                            //set nutrition text
                            weightView.setText(weight);
                            calorieView.setText(calorie);
                            totalFatView.setText(totalFat);
                            saturatedFatView.setText(saturatedFat);
                            polyFatView.setText(polyFat);
                            monFatView.setText(monFat);
                            cholesterolView.setText(cholesterol);
                            sodiumView.setText(sodium);
                            potassiumView.setText(potassium);
                            carbohydratesView.setText(carbohydrates);
                            proteinView.setText(protein);
                            vaView.setText(va);
                            vcView.setText(vc);
                            calciumView.setText(calcium);
                            ironView.setText(iron);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Error response", Toast.LENGTH_SHORT).show();
                                Log.i("the res is error:", error.toString());
                            }
                        }
                );
        requestQueue.add(jsonObjectRequest);
    }
}