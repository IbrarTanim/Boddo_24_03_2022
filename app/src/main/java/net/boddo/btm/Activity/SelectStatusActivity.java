package net.boddo.btm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.boddo.btm.Adepter.SelectStatusAdepter;
import net.boddo.btm.Model.SelectStatusModel;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;

import java.util.ArrayList;
import java.util.List;

public class SelectStatusActivity extends AppCompatActivity {

    private RecyclerView rvSelectStatusActivity;
    private ArrayList<SelectStatusModel> statusList;
    private SelectStatusModel selectStatusModel;
    private SelectStatusAdepter selectStatusAdepter;
    private TextView tvBackSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_status);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        rvSelectStatusActivity = findViewById(R.id.rvSelectStatusActivity);
        tvBackSettings = findViewById(R.id.tvBackSettings);
        statusList = new ArrayList<>();
        selectStatusAdepter = new SelectStatusAdepter(this,statusList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvSelectStatusActivity.setLayoutManager(llm);
        rvSelectStatusActivity.setAdapter(selectStatusAdepter);


        tvBackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        createStatusData();
        Log.e("statusList", "onCreate: "+statusList.get(0).getStatus() );

    }

    private void createStatusData() {

       statusList.add(new SelectStatusModel("I'm busy"));
       statusList.add(new SelectStatusModel("I'm happy"));
       statusList.add(new SelectStatusModel("I'm sad"));
       statusList.add(new SelectStatusModel("Feeling lonely"));
       statusList.add(new SelectStatusModel("At work"));
       statusList.add(new SelectStatusModel("Driving"));
       statusList.add(new SelectStatusModel("Playing"));
       statusList.add(new SelectStatusModel("Sleeping"));
       statusList.add(new SelectStatusModel("Doing party"));
       statusList.add(new SelectStatusModel("Watching movie"));
       statusList.add(new SelectStatusModel("Listening music"));
       statusList.add(new SelectStatusModel("Looking for my soulmate"));
       statusList.add(new SelectStatusModel("Looking for new friends"));
       statusList.add(new SelectStatusModel("I'm new here, let's chat"));
       statusList.add(new SelectStatusModel("Outing with friends"));
       statusList.add(new SelectStatusModel("Just chill"));
       statusList.add(new SelectStatusModel("Today is my birthday"));
       statusList.add(new SelectStatusModel("Never give up"));
       statusList.add(new SelectStatusModel("Busy at chat room"));
       statusList.add(new SelectStatusModel("I don't like fake peoples"));

    }
}