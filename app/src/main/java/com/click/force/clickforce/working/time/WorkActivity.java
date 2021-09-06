package com.click.force.clickforce.working.time;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.click.force.clickforce.working.time.controller.WorkController;
import com.click.force.clickforce.working.time.sqldatabase.WorkingTimeService;

public class WorkActivity extends AppCompatActivity {
    WorkController workController;
    WorkingTimeService workingTimeService;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        //controller 初始化的東西
        workingTimeService= new WorkingTimeService(this);
        context = this;
        workController = new WorkController(this, context, workingTimeService);
        workController.init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //controller 顯示的view
        workController.loginView();
    }

    public void showHistory(View view) {
        workController.clickHistoryView();
    }

    public void clickAction(View view) {
        workController.clickAction();
    }

}
