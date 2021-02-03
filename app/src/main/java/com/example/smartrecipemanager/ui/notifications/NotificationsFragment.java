package com.example.smartrecipemanager.ui.notifications;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartrecipemanager.Calorie;
import com.example.smartrecipemanager.Post;
import com.example.smartrecipemanager.R;

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

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private List<Integer> calorieDateList;
    private List<String> TimeList;
    private TextView todayCal,weekCal;
    private LineChartView lineChart;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private List<Calorie> CalorieList;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisValues = new ArrayList<AxisValue>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        initView(root);
        getData();
        return root;
    }

    private void initView(View root) {
        todayCal=(TextView)root.findViewById(R.id.TodayCalorie);
        weekCal=(TextView)root.findViewById(R.id.WeekCalorie);
        lineChart = (LineChartView)root.findViewById(R.id.chart);
    }
    private void getData() {
        CalorieList=new ArrayList<Calorie>();
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCalorie() {
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
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            TimeList.add(day+"/"+(month+1));
            String data=year+","+month+","+day;
            for(int k = 0; k < CalorieList.size(); k++){
                String[] array2 = CalorieList.get(k).getTime().split("_");
                if(data.equals(array2[0])){
                calorieDateList.set(i,calorieDateList.get(i)+Integer.parseInt(CalorieList.get(k).getCalorieData()));
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
//      reverse list
        Collections.reverse(TimeList);
        Collections.reverse(calorieDateList);
        todayCal.setText(String.valueOf(calorieDateList.get(6)));
        weekCal.setText(String.valueOf(calorieDateList.get(0)+calorieDateList.get(1)+calorieDateList.get(2)+calorieDateList.get(3)+calorieDateList.get(4)+calorieDateList.get(5)+calorieDateList.get(6)));
        drawChart();
        Log.d("NotifitionFragment","calorie data list is "+calorieDateList);
    }
    private void drawChart() {
        getAxisLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
        }
    private void getAxisLables(){
        for (int i = 0; i < TimeList.size(); i++) {
            mAxisValues.add(new AxisValue(i).setLabel(TimeList.get(i)));
        }
    }
    private void getAxisPoints(){
        for (int i = 0; i < calorieDateList.size(); i++) {
            mPointValues.add(new PointValue(i, calorieDateList.get(i)));
        }
    }
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.YELLOW).setCubic(false);  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
        line.setFilled(true);//是否填充曲线的面积
//      line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("Weekly Report");  //表格名称
        axisX.setTextSize(15);//设置字体大小
        axisX.setMaxLabelChars(7);  //最多几个X轴坐标
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//      data.setAxisXTop(axisX);  //x 轴在顶部

        Axis axisY = new Axis();  //Y轴
        axisY.setMaxLabelChars(3); //默认是3，只能看最后三个数字
        axisY.setName("Kcal");//y轴标注
        axisY.setTextSize(13);//设置字体大小
        axisY.setTextColor(Color.BLACK);
        data.setAxisYLeft(axisY);  //Y轴设置在左边

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
    }
}