package com.example.smartrecipemanager;
/*
* constructor for store users' calorie information to server
* */
public class Calorie {
    private String time;
    private String calorieData;

    public Calorie(){}
    public String getTime() {
        return time;
    }
    public void setTime(String time){
        this.time=time;
    }
    public String getCalorieData() {
        return calorieData;
    }
    public void setCalorieData(String calorieData){
        this.calorieData=calorieData;
    }


    public Calorie(String time, String calorieData) {
        this.time = time;
        this.calorieData = calorieData;

    }
}
