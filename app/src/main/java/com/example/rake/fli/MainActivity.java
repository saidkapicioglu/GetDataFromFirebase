package com.example.rake.fli;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private LineChart mChart;
    private LineChart mChart2;
    static int asd=0;
    Button refresh;
    List<Float> arrayyim = new ArrayList<Float>();
    List<Float> arrayyim2 = new ArrayList<Float>();

    //    public float[] arrayim=new float[50];
    private DatabaseReference myDatabaseReference;
    int x=0;
    private Runnable mTimer1;
    private Runnable mTimer2;
    private final Handler mHandler = new Handler();
    ArrayList<Entry> yvalues=new ArrayList<>();
    ArrayList<Entry> yvalues2=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refresh=findViewById(R.id.refresh);
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart2 = (LineChart) findViewById(R.id.linechart2);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart2.setDragEnabled(true);
        mChart2.setScaleEnabled(true);

        XAxis xAxis2 = mChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTextSize(12f);
        xAxis2.setTextColor(Color.BLUE);
        xAxis2.setGranularity(1f); //X ekseni artış miktarı
        xAxis2.setDrawAxisLine(true);
        xAxis2.setDrawGridLines(true);

        YAxis left2 = mChart2.getAxisLeft();
        left2.setTextSize(12f);
        left2.setTextColor(Color.BLUE);
        left2.setGranularity(20f);
        left2.setDrawLabels(true); // no axis labels
        left2.setDrawAxisLine(true); // no axis line
        left2.setDrawGridLines(true); // no grid lines
        left2.setDrawZeroLine(true); // draw a zero line
        mChart2.getAxisRight().setEnabled(false); // no right axis

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLUE);
        xAxis.setGranularity(1f); //X ekseni artış miktarı
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);

        YAxis left = mChart.getAxisLeft();
        left.setTextSize(12f);
        left.setTextColor(Color.BLUE);
        left.setGranularity(20f);
        left.setDrawLabels(true); // no axis labels
        left.setDrawAxisLine(true); // no axis line
        left.setDrawGridLines(true); // no grid lines
        left.setDrawZeroLine(true); // draw a zero line
        mChart.getAxisRight().setEnabled(false); // no right axis

        yvalues.add(new Entry(0,0));
        yvalues2.add(new Entry(0,0));


        LineDataSet set1=new LineDataSet(yvalues,"Data Set 1");
        set1.setFillAlpha(110);

        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(12f);

        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(set1);

        LineData data=new LineData(dataSets);
        mChart.setData(data);

        LineDataSet set2=new LineDataSet(yvalues2,"Data Set 1");
        set2.setFillAlpha(110);

        set2.setColor(Color.RED);
        set2.setLineWidth(3f);
        set2.setValueTextSize(12f);

        ArrayList<ILineDataSet> dataSets2=new ArrayList<>();
        dataSets2.add(set2);

        LineData data2=new LineData(dataSets2);
        mChart2.setData(data2);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDatabaseReference= FirebaseDatabase.getInstance().getReference("sicaklik");
                x=0;
                mChart.clear();
                yvalues.clear();
                yvalues2.clear();
                myDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String[] sicaklikArray = snapshot.getValue().toString().split(",");
                            String[] tmp=sicaklikArray[1].split("=");
                            String[] nem=sicaklikArray[2].split("=");
                            Float nemim=Float.parseFloat(nem[1]);
                            Float sicaklik=Float.parseFloat(tmp[1]);
                            if(sicaklik>0.0){
                                arrayyim.add(sicaklik);
                                x++;
                            }
                            arrayyim2.add(nemim);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("said","onCancelled");
                    }
                });

                yvalues2.add(new Entry(0,0));
                int i=1;
                for (Float s : arrayyim2) {
                    i++;
                    if(s>0){
                        yvalues2.add(new Entry(i,s));
                        Log.d("said","nem"+s);
                    }
                }
                i=1;
                yvalues.add(new Entry(0,0));
                for (Float s : arrayyim) {
                    i++;
                    if(s>0){
                        yvalues.add(new Entry(i,s));
                        Log.d("said","sicaklik"+s);
                    }
                }
                LineDataSet set2=new LineDataSet(yvalues2,"NEM");

                set2.setFillAlpha(110);

                set2.setColor(Color.GREEN);
                set2.setLineWidth(3f);
                set2.setValueTextSize(12f);

                ArrayList<ILineDataSet> dataSets2=new ArrayList<>();
                dataSets2.add(set2);

                LineData data2=new LineData(dataSets2);
                mChart2.setData(data2);
                mChart2.notifyDataSetChanged(); // let the chart know it's data changed
                mChart2.invalidate(); // refresh
                LineDataSet set1=new LineDataSet(yvalues,"SICAKLIK");

                set1.setFillAlpha(110);

                set1.setColor(Color.RED);
                set1.setLineWidth(3f);
                set1.setValueTextSize(12f);

                ArrayList<ILineDataSet> dataSets=new ArrayList<>();
                dataSets.add(set1);

                LineData data=new LineData(dataSets);
                mChart.setData(data);

                // data.notifyDataChanged(); // let the data know a dataSet changed
                mChart.notifyDataSetChanged(); // let the chart know it's data changed
                mChart.invalidate(); // refresh
                arrayyim.clear();
                arrayyim2.clear();

            }
        });
        String tokenfordb = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        Log.d("said", "Refreshed token for Server: " + tokenfordb);
        myRef.setValue(tokenfordb);

    }

}
