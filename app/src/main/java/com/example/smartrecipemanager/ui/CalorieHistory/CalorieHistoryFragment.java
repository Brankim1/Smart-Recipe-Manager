package com.example.smartrecipemanager.ui.CalorieHistory;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartrecipemanager.Calorie;
import com.example.smartrecipemanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class CalorieHistoryFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private List<Integer> calorieDateList;
    private List<String> TimeList;
    private TextView todayCal,weekCal;
    private LineChartView lineChart;
    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference mRootRef;
    private List<Calorie> CalorieList;
    private List<PointValue> mPointValues;
    private List<AxisValue> mAxisValues;
    private FloatingActionButton fab1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_caloriehistory, container, false);
        initView(root);
        getData();
        setFloatingActionButton();
        return root;
    }

    private void initView(View root) {
        todayCal=(TextView)root.findViewById(R.id.TodayCalorie);
        weekCal=(TextView)root.findViewById(R.id.WeekCalorie);
        //for draw line chart
        lineChart = (LineChartView)root.findViewById(R.id.chart);
        fab1 = (FloatingActionButton) root.findViewById(R.id.fab);
    }
    private void getData() {
        CalorieList=new ArrayList<Calorie>();
        //get calorie information from server
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference().child(uid).child("calorie");
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Calorie calorie=child.getValue(Calorie.class);
                        Log.d("NotifitionFragment","calorie data is "+calorie.getTime()+","+calorie.getCalorieData());
                        CalorieList.add(calorie);
                    }
                    getCalorie();
                }else{
                    Toast.makeText(getContext(),"Please add at least one calorie data",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCalorie() {
        //get weekly calorie information
        calorieDateList=new ArrayList<Integer>();
        TimeList=new ArrayList<String>();
        calorieDateList.add(0);
        calorieDateList.add(0);
        calorieDateList.add(0);
        calorieDateList.add(0);
        calorieDateList.add(0);
        calorieDateList.add(0);
        calorieDateList.add(0);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        for(int i=0;i<7;i++){
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            TimeList.add(day+"/"+(month));
            String data=year+","+month+","+day;
            for(int k = 0; k < CalorieList.size(); k++){
                String[] array2 = CalorieList.get(k).getTime().split("_");
                if(data.equals(array2[0])){
                calorieDateList.set(i,calorieDateList.get(i)+Integer.parseInt(CalorieList.get(k).getCalorieData()));
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        //reverse list
        Collections.reverse(TimeList);
        Collections.reverse(calorieDateList);
        Log.d("CalorieHistoryFragment","789timeList is "+ TimeList);
        Log.d("CalorieHistoryFragment","789calorieDateList is "+ calorieDateList);
        todayCal.setText(String.valueOf(calorieDateList.get(6)));
        weekCal.setText(String.valueOf(calorieDateList.get(0)+calorieDateList.get(1)+calorieDateList.get(2)+calorieDateList.get(3)+calorieDateList.get(4)+calorieDateList.get(5)+calorieDateList.get(6)));
        drawChart();
        Log.d("NotifitionFragment","calorie data list is "+calorieDateList);
    }
    private void drawChart() {
        getAxisLables();
        getAxisPoints();
        initLineChart();
        }
    private void getAxisLables(){
        mAxisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < 7; i++) {
            mAxisValues.add(new AxisValue(i).setLabel(TimeList.get(i)));
        }
    }
    private void getAxisPoints(){
        mPointValues = new ArrayList<PointValue>();
        for (int i = 0; i < 7; i++) {
            mPointValues.add(new PointValue(i, calorieDateList.get(i)));
        }
    }
    private void initLineChart(){
        //set line chart parameter
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFA500")).setCubic(false);
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);
        line.setFilled(true);
        line.setHasLabelsOnlyForSelected(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);
        axisX.setName("Weekly Report");
        axisX.setTextSize(15);
        axisX.setMaxLabelChars(7);
        axisX.setValues(mAxisValues);
        data.setAxisXBottom(axisX);

        Axis axisY = new Axis();
        axisY.setMaxLabelChars(3);
        axisY.setName("Kcal");
        axisY.setTextSize(13);
        axisY.setTextColor(Color.BLACK);
        data.setAxisYLeft(axisY);

        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
    }

    private void setFloatingActionButton() {
        //for add post
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog();
            }
        });
    }

    private void showListDialog(){
        String[] items=new String[CalorieList.size()];
        String[] time=new String[CalorieList.size()];
        for(int k = 0; k < CalorieList.size(); k++) {
            String array1 = CalorieList.get(k).getTime();
            String array2 = CalorieList.get(k).getCalorieData();
            items[k]=array1+"    "+array2+"kcal";
            time[k]=array1;
        }
        AlertDialog.Builder listDialog = new AlertDialog.Builder(getActivity());
        listDialog.setIcon(R.drawable.delete_icon);
        listDialog.setTitle("Please click data to delete");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference  mRootRef2 = FirebaseDatabase.getInstance().getReference().child(uid).child("calorie").child(time[which]);
                mRootRef2.removeValue();
                getData();
                Toast.makeText(getActivity(),"Remove successful "+items[which],Toast.LENGTH_LONG).show();
            }
        });
        listDialog.show();
    }
}