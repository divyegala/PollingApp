package shd.pollingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataGraph extends AppCompatActivity {

    Questions questions;
    BarChart barChart;
    BarData barData;
    ArrayList<IBarDataSet> barDataSet;
    FirebaseHelper firebaseHelper;
    ArrayList<Integer> ansCount;
    int totalVotes;
    int x_axis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_graph);
        barChart = (BarChart) findViewById(R.id.chart);
        firebaseHelper = new FirebaseHelper();
        ValueEventListener getAnswerData = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                barDataSet = new ArrayList<>();
                ansCount = new ArrayList<>();
                totalVotes = 0;
                x_axis = 0;
                HashMap<String, Object> questionsData = (HashMap<String, Object>) dataSnapshot.getValue();
                HashMap<String, Object> questionData =  (HashMap<String, Object>) questionsData.get("-KgDYZMCpPwHiZtKSXZK");
                ArrayList<String> temp = (ArrayList<String>) questionData.get("questions");
                for (int i = 0; i < temp.size(); i++) {
                    ansCount.add(0);
                }
//                System.out.println("ansCount size" + ansCount.size());
                for (Map.Entry<String, Object> entry: questionData.entrySet()) {
                    if (!entry.getKey().equals("questions") && !entry.getKey().equals("name")) {
                        ArrayList<String> temp1 = (ArrayList<String>) entry.getValue();
                        System.out.println("temp1 size" + temp1.size());
                        System.out.println("User" + entry.getKey());
                        for (int i = 0; i < temp1.size(); i++) {
                            if (temp1.get(i).equals("Yes")) {
                                Integer temp2 = ansCount.get(i);
                                temp2++;
                                ansCount.remove(i);
                                ansCount.add(i, temp2);
                            }
                            totalVotes++;
                        }
                    }
                }
//                System.out.println("total count" + totalVotes);
//                System.out.println("ansCount size" + ansCount.size());
                for (int i = 0;i < ansCount.size(); i++) {
//                    System.out.println(ansCount.get(i));
                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                    barEntries.add(new BarEntry(x_axis, ansCount.get(i)));
                    x_axis+= 2;
                    barEntries.add(new BarEntry(x_axis, totalVotes - ansCount.get(i)));
                    x_axis+= 2;
                    int inttemp[] = {R.color.red, R.color.green};
                    BarDataSet barDataTemp = new BarDataSet(barEntries, "Q" + (i + 1));
                    barDataTemp.setColors(inttemp, getApplicationContext());
                    barDataSet.add(barDataTemp);
                }
                barData = new BarData(barDataSet);
                barChart.setData(barData);
//                barChart.groupBars(0, 0.8f, 0.05f);
                barChart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        };
        firebaseHelper.getQuestionsDatabaseReference().addValueEventListener(getAnswerData);
        System.out.println("Last line");
    }
}
