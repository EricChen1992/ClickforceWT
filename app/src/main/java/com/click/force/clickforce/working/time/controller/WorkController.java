package com.click.force.clickforce.working.time.controller;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.click.force.clickforce.working.time.HistoryListView;
import com.click.force.clickforce.working.time.HttpConnectPost;
import com.click.force.clickforce.working.time.R;
import com.click.force.clickforce.working.time.RotateAnim;
import com.click.force.clickforce.working.time.WorkActivity;
import com.click.force.clickforce.working.time.sqldatabase.WorkingTimeService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class WorkController {

    private AppCompatActivity controller_ac;
    private WorkingTimeService controller_wt;
    private Context controller_context;
    private EditText editText_email, editText_password;
    private TextView type, text_name, text_date,text_timeclockin, text_timeclockout, show_history_name, show_history_id;
    private Button punchBtn;
    private LinearLayout showContent, show_history_column;
    private RelativeLayout show_history_content;
    private ImageView showBottomType;
    private ListView show_history_list;
    private HistoryListView historyListView;
    private AlertDialog historyDialog;
    private int clicknb=0;
    Notification.Builder builder;
    NotificationManager notificationManager;
    NotificationChannel channel;

    public  WorkController(AppCompatActivity input_ac, Context input_context){
        this.controller_ac = input_ac;
        this.controller_context = input_context;
    }

    public  WorkController(AppCompatActivity input_ac, Context input_context, WorkingTimeService input_wt){
        this.controller_ac = input_ac;
        this.controller_context = input_context;
        this.controller_wt = input_wt;
    }

    public void init(){
        type = (TextView) this.controller_ac.findViewById(R.id.typMode);
        punchBtn = (Button) this.controller_ac.findViewById(R.id.punchBtn);
        text_date = (TextView) this.controller_ac.findViewById(R.id.textDate);
        text_name = (TextView) this.controller_ac.findViewById(R.id.textName);
        text_timeclockin = (TextView) this.controller_ac.findViewById(R.id.textTimeclockin);
        text_timeclockout = (TextView) this.controller_ac.findViewById(R.id.textTimeclockout);
        showContent = (LinearLayout) this.controller_ac.findViewById(R.id.showContent);
        showBottomType = (ImageView) this.controller_ac.findViewById(R.id.showBottomType);
        getPermissions(controller_ac);
    }

    private void getPermissions(Activity ac){
        ActivityCompat.requestPermissions(ac,
                new String[]{Manifest.permission.WRITE_CALENDAR,
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.ACCOUNT_MANAGER
                },
                1);
    }

    public void loginView(){
        Map<String, String> userInfo = this.controller_wt.getFirstNameAndIdData();
        if (!userInfo.isEmpty()){//確認是否有使用者資料
            setShow_user_status(userInfo);
        } else {
            setShow_login_view();
        }
    }

    private void setShow_user_status(Map<String, String> uInfo){
        text_name.setText(uInfo.get(controller_wt.fCuaName));
        String today = getStrDate();
        Map<String, String> status = this.controller_wt.getFindUsetStatus();
        if (status != null && !status.isEmpty()){
            if (today.equals(status.get(this.controller_wt.wkdate))) {
                //這邊也是利用DB內存的 post response String(Map<String, String>)
                JSONObject jsonObject = new JSONObject(status);
                setStuff_user_status_view(jsonObject, jsonObject.optString(controller_wt.wkResponse), 0);
            } else {
                this.controller_wt.delPunchTime();//如果手機時間與DB不一致的話刪除DB
                //這邊需要post得到狀態(JsonObject)
                connectDB(uInfo, 2);
            }

        } else {
            //這邊需要post得到狀態(JsonObject)
            connectDB(uInfo, 2);
        }
//        createNotificationChannel();
    }


    private void setStuff_user_status_view(JSONObject userJsonStatus, String userResponse, int checkInsert){
        try {
            JSONObject response = new JSONObject(userResponse);//利用response 進行 JsonObject 初始
            if (response.optString(controller_wt.wResult).equals("1")){

                if (response.optString(controller_wt.wkType).equals("1")){
                    //上班
                    if (checkInsert == 1) {
                        controller_wt.insertData("",
                                                userJsonStatus.getString(controller_wt.wkdate),
                                                userJsonStatus.getString(controller_wt.wkTimeClockIn),
                                                userJsonStatus.getString(controller_wt.wkTimeClockOut),
                                                userJsonStatus.getString(controller_wt.wkType),
                                                userResponse);
                    }//Insert資料到SQLite
                    type.setText("上班...");
                    text_date.setText(userJsonStatus.optString(controller_wt.wkdate));
                    text_timeclockin.setText(userJsonStatus.optString(controller_wt.wkTimeClockIn));
                    setShowBottomView(1);
                    //組好日期與時間
                    if (checkInsert == 1) setWorkOutAlarm(userJsonStatus.optString(controller_wt.wkdate), userJsonStatus.optString(controller_wt.wkTimeClockIn));

                } else if (response.optString(controller_wt.wkType).equals("0")){
                    //下班
                    if (checkInsert == 1) {
                        controller_wt.insertData("",
                                                userJsonStatus.getString(controller_wt.wkdate),
                                                userJsonStatus.getString(controller_wt.wkTimeClockIn),
                                                userJsonStatus.getString(controller_wt.wkTimeClockOut),
                                                userJsonStatus.getString(controller_wt.wkType),
                                                userResponse);
                    }//Insert資料到SQLite
                    type.setText("下班...");
                    text_date.setText(userJsonStatus.optString(controller_wt.wkdate));
                    text_timeclockin.setText(userJsonStatus.optString(controller_wt.wkTimeClockIn));
                    text_timeclockout.setText(userJsonStatus.optString(controller_wt.wkTimeClockOut));
                    setShowBottomView(2);
                }


            } else {
                //尚未打卡
                if (checkInsert == 1) {
                    controller_wt.insertData("",
                                            response.getString(controller_wt.wkdate),
                                            response.getString(controller_wt.wkTimeClockIn),
                                            response.getString(controller_wt.wkTimeClockOut),
                                            response.getString(controller_wt.wkType),
                                            userResponse);
                }//Insert資料到SQLite
                type.setText("尚未打卡");
                text_date.setText(response.optString(controller_wt.wkdate));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clickAction(){
        try {
            Map<String, String> status = this.controller_wt.getFindUsetStatus();
            JSONObject response = new JSONObject(status.get(controller_wt.wkResponse));
            Map<String, String> info = this.controller_wt.getFirstNameAndIdData();
            if (info != null && !info.isEmpty()) {
                if (response.optString(controller_wt.wResult).equals("0") && status.get(controller_wt.wkType).equals("") && status.get(controller_wt.wkTimeClockIn).equals("")){
                    //打卡上班
//                    Log.e("TEST","Type: " + status.get(controller_wt.wkType) + "ClockIn: " + status.get(controller_wt.wkTimeClockIn) + "ClockOut: " + status.get(controller_wt.wkTimeClockOut));
                    connectDB(info, 1);

                } else if (response.optString(controller_wt.wResult).equals("1") && status.get(controller_wt.wkType).equals("1") && !status.get(controller_wt.wkTimeClockIn).equals("")) {
                    //打卡下班
//                    Log.e("TEST","Type: " + status.get(controller_wt.wkType) + "ClockIn: " + status.get(controller_wt.wkTimeClockIn) + "ClockOut: " + status.get(controller_wt.wkTimeClockOut));
//                    connectDB(info, 0);
                    showDoubleConfirmDialog(info, status.get(controller_wt.wkTimeClockIn));

                } else if (response.optString(controller_wt.wResult).equals("1") && status.get(controller_wt.wkType).equals("0") && !status.get(controller_wt.wkTimeClockIn).equals("") && !status.get(controller_wt.wkTimeClockOut).equals("")) {
                    //已打卡下班後點擊
                    clicknb++;
                    if (clicknb == 10){
                        Toast.makeText(controller_ac, "點夠了嗎?已經下班了", Toast.LENGTH_SHORT).show();
                        clicknb = 0;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDoubleConfirmDialog(final Map<String, String > user_info, final String dbWorkInTime){
        String[] timesplit = dbWorkInTime.split(":");
        String[] nowtsplit = getStrTime().split(":");
        //先進行先加九小時半上班時間
        int canTimeclockoutH = Integer.parseInt(timesplit[0]) + 9;
        int canTimeclockoutM = Integer.parseInt(timesplit[1]) + 30;

        //整理加時後的可下班時間
        if (canTimeclockoutM >= 60){
            canTimeclockoutM = canTimeclockoutM - 60 ;
            canTimeclockoutH = canTimeclockoutH + 1;
        }
        int nowTimeclockoutH = Integer.parseInt(nowtsplit[0]);
        int nowIimeclockoutM = Integer.parseInt(nowtsplit[1]);

//        Log.e("TEST", "canTimeclockoutH: " + canTimeclockoutH + " canTimeclockoutM: " + canTimeclockoutM);
//        Log.e("TEST2", "nowTimeclockoutH: " + nowTimeclockoutH + " nowIimeclockoutM: " + nowIimeclockoutM);
        AlertDialog.Builder builder = new AlertDialog.Builder(controller_ac);
        String msg = "您確定要下班了嗎？ \n您下班時間為 " + canTimeclockoutH + ":" + canTimeclockoutM + " 唷!";
        if (nowTimeclockoutH > canTimeclockoutH|| (canTimeclockoutH == nowTimeclockoutH && nowIimeclockoutM > canTimeclockoutM) || canTimeclockoutH > 19 ){//如果超過七點半就不詢問了
            msg = "您確定要下班了嗎？";
        } else {
            String tempM ="";
            if (canTimeclockoutM < 10) {
                tempM = "0" + String.valueOf(canTimeclockoutM);
            } else {
                tempM = String.valueOf(canTimeclockoutM);
            }
            msg = String.format("您確定要下班了嗎？\n您下班時間為 %d:%s 唷", canTimeclockoutH, tempM);
        }
        builder.setMessage(msg);
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                connectDB(user_info, 0);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clickHistoryView(){

        String result = connectPullDB();
//        Log.e("TAG", "showHistory: "+result );
        if (result.isEmpty()){
            Toast.makeText(controller_ac, "取得打卡記錄失敗.", Toast.LENGTH_LONG).show();
            return;
        } else if (result.equals("No data")){
            Toast.makeText(controller_ac, "尚未有打卡記錄.", Toast.LENGTH_LONG).show();
            return;
        }
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            jsonObject.optString(controller_wt.wResult);
            jsonArray = new JSONArray(jsonObject.optString(controller_wt.wItem));
            setStuffViewToShowHistory(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        setWorkOutAlarm("2021-09-06", "14:13:50");
//        testNotification();
    }

    private void testNotification() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(controller_context, WorkActivity.class);//需顯示的頁面
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//呈現是需要在最上層，最新的頁面
        PendingIntent pendingIntent = PendingIntent.getActivity(controller_context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(controller_context, "Test123")//Channel id Android api 26 以上需要設定
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);//如果點擊後並刪除通知
            Notification notification = builder.build();
            notificationManager.notify(0, notification);
        } else {
            builder = new Notification.Builder(controller_context)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Test")
                    .setContentText("Test Body")
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            notificationManager.notify(0, notification);
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("in","CreateNotificationChannel");
            CharSequence name = "Name";//app通知設定裡面的名稱
            String description = "I'm description";//通知類別裡面最下面註解
            int importance = NotificationManager.IMPORTANCE_HIGH;//通知類別
            channel = new NotificationChannel("Test123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = controller_context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } else {
            Log.e("in","Before version");
            notificationManager = (NotificationManager) controller_context.getSystemService(Context.NOTIFICATION_SERVICE);

        }
    }

    @SuppressLint("NewApi")
    private void setStuffViewToShowHistory(JSONArray jsonArray){

        try {
            if (jsonArray == null || jsonArray.length() == 0){
                Toast.makeText(controller_context, "您沒有打卡記錄，快打卡吧！", Toast.LENGTH_LONG).show();
                return;
            }
            LayoutInflater layoutInflater = controller_ac.getLayoutInflater();
            View showHistory = layoutInflater.inflate(R.layout.activity_historylist, null);
            show_history_content = showHistory.findViewById(R.id.show_history_content);
            show_history_name = showHistory.findViewById(R.id.show_history_name);
            show_history_id = showHistory.findViewById(R.id.show_history_id);
            show_history_name.setText("姓名: "+jsonArray.getJSONObject(0).optString("name"));
            show_history_id.setText("ID: "+jsonArray.getJSONObject(0).optString("cua_id"));

            show_history_list = showHistory.findViewById(R.id.show_history_list);
            historyListView = new HistoryListView();
            show_history_list.setAdapter(historyListView);

            historyListView.clear();

            for (int i=0 ; i<jsonArray.length(); i++){
//                        Log.e(TAG, "popViewToShowHistory: ("+i+")"+jsonArray.getJSONObject(i).optString("time_clock_in"));
                //                    LayoutInflater layoutInflater = LayoutInflater.from(this);
                show_history_column = (LinearLayout) layoutInflater.inflate(R.layout.activity_historycolumn, null);

                TextView column_date = show_history_column.findViewById(R.id.show_history_column_date);
                TextView column_timeclockin = show_history_column.findViewById(R.id.show_history_column_timeclockin);
                TextView column_timeclockout = show_history_column.findViewById(R.id.show_history_column_timeclockout);
                column_date.setText(jsonArray.getJSONObject(i).optString("date"));
                column_timeclockin.setText(jsonArray.getJSONObject(i).optString("time_clock_in"));
                column_timeclockout.setText(jsonArray.getJSONObject(i).optString("time_clock_out"));

                historyListView.addView(show_history_column);

            }
            historyListView.notifyDataSetChanged();

            //close icon
            RelativeLayout.LayoutParams rllpCancel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rllpCancel.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, show_history_content.getId());
            rllpCancel.addRule(RelativeLayout.ALIGN_TOP, show_history_content.getId());

            ImageView ivCancel = new ImageView(controller_context);
            ivCancel.setImageResource(R.drawable.close_history_dialog);
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (historyDialog != null) historyDialog.dismiss();
                }
            });
            show_history_content.addView(ivCancel, rllpCancel);//加入到History view

            AlertDialog.Builder builder = new AlertDialog.Builder(controller_ac);
            builder.setView(showHistory);
            historyDialog = builder.create();
//            historyDialog.getWindow().setBackgroundDrawable(controller_ac.getDrawable(R.drawable.activity_history_black));
            historyDialog.getWindow().setBackgroundDrawableResource(R.drawable.activity_history_black);

            historyDialog.show();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String connectPullDB(){
        String workTypeUrl = HttpConnectPost.workurl + HttpConnectPost.all_data;
        String result = "";
        HttpConnectPost post = new HttpConnectPost();
        post.params = controller_wt.getFirstNameAndIdData();//取得打到後端資料;
        if (post.params.isEmpty()){
            Toast.makeText(controller_ac, "取得DB資料失敗.", Toast.LENGTH_LONG).show();
        }
        try {
            return post.execute(workTypeUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "No data";
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    private void setShow_login_view(){
        //Creat Login dialog view;
        final LayoutInflater inflater = this.controller_ac.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.controller_ac);
        final View contentView = inflater.inflate(R.layout.creatname, null);
        editText_email = (EditText) contentView.findViewById(R.id.enter_email);
        editText_password = (EditText) contentView.findViewById(R.id.enter_password);
        builder.setView(contentView);
        builder.setPositiveButton(" ",null);
        builder.setNegativeButton(" ",null);

        final InputMethodManager inputMethodManager = (InputMethodManager) this.controller_ac.getSystemService(Context.INPUT_METHOD_SERVICE);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.open_dialog_background_15);
        dialog.show();
        //右邊按鈕(確定)
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setBackground(this.controller_ac.getDrawable(R.drawable.open_dialog_r_btn16));
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_email != null && editText_password !=null){
//                            Log.e("Main", editText_email.getText().toString() + "\np:" + editText_password.getText().toString());
//                            workingTimeService.insertFirstNameData(spinnerSelect.toString());
                    String emilStr = editText_email.getText().toString().contains("@") ? editText_email.getText().toString() : editText_email.getText().toString() + "@clickforce.com.tw";
                    String pwdStr = editText_password.getText().toString();
                    inputMethodManager.hideSoftInputFromWindow(editText_password.getWindowToken(),0);//鍵盤收起
                    if (connectCuaAuth(emilStr, pwdStr)){
                        dialog.dismiss();
                        rotateAnim(showContent);
                        setShowBottomView(0);
                        setShow_user_status(controller_wt.getFirstNameAndIdData());
                    }else {
                        Toast.makeText(controller_context, "User Auth Fail.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        //左邊按鈕(取消)
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setBackgroundColor(Color.GREEN);
        negativeButton.setBackground(this.controller_ac.getDrawable(R.drawable.open_dialog_l_btn));
        negativeButton.setOnClickListener(new View.OnClickListener() {
            Boolean close = false;
            @Override
            public void onClick(View view) {
                if (!close){
                    Toast.makeText(controller_ac,"提示:再一次取消就退出App摟!!",Toast.LENGTH_SHORT).show();
                    close = true;
                }else {
                    controller_ac.finish();
                }

            }
        });
        dialog.setCanceledOnTouchOutside(false);//取消點背景會消失


        getEmailAndType();
    }

    /***
     * @param sw 1.work in 2.work out 3. none
     */
    private void setShowBottomView(int sw){
        switch (sw){
            case 1:
                showBottomType.setImageResource(R.drawable.activity_bottom3);
                showBottomType.setVisibility(View.VISIBLE);
                break;
            case 2:
                showBottomType.setImageResource(R.drawable.activity_bottom7);
                showBottomType.setVisibility(View.VISIBLE);
                break;
            default:
                showBottomType.setVisibility(View.GONE);
                break;
        }
    }

    private boolean connectCuaAuth(String cua_eamil, String cua_password){
        Map<String, String> json = new HashMap<>();
        json.put("cuaemail", cua_eamil);
        json.put("cuapassword",cua_password);
        String url = HttpConnectPost.workurl + HttpConnectPost.cua;
        HttpConnectPost post = new HttpConnectPost();
        post.params = json;
        try {
            JSONObject jsonresult = new JSONObject(post.execute(url).get());
            String jStatus = jsonresult.getString("status");
            if (jStatus.equals("1")){
                String jcua_id = jsonresult.getString("id");
                String jName = jsonresult.getString("name");
                String jResult = jsonresult.toString();
                this.controller_wt.insertFirstNameData(jcua_id, jName, jResult);
                return true;
            } else {
                Toast.makeText(this.controller_ac, "提示:帳號或密碼錯誤. \nCode: "+jsonresult.optString("msg"), Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @param data UserInfo
     * @param type 1.上班 0.下班 2.狀態
     * */
    private String connectDB(Map<String, String> data, int type) {

        try {
            String workTypeUrl ="";
            if (type == 1){
                workTypeUrl = HttpConnectPost.workurl + HttpConnectPost.workIn;
            } else if (type == 0){
                workTypeUrl = HttpConnectPost.workurl + HttpConnectPost.workOut;
            } else if (type == 2){
                workTypeUrl = HttpConnectPost.workurl + HttpConnectPost.checkStatus;
            }

            Map<String,String> json = new HashMap<>();
            json.put(controller_wt.fCuaId, data.get(controller_wt.fCuaId));
            json.put( type == 2 ? controller_wt.fCuaName : controller_wt.wkName, data.get(controller_wt.fCuaName));

            if (!(type == 2))json.put(controller_wt.wkType, String.valueOf(type));

    //        httpConnectonPost(workTypeUrl, json);
//            Log.e("URL", workTypeUrl + "\nJson: " + json.toString());
            HttpConnectPost post = new HttpConnectPost();
            post.params = json;
            String post_result = post.execute(workTypeUrl).get();
//            Log.e("URL_Result", ">>>: " + post_result);
            //顯示view
            JSONObject jsonObject = new JSONObject(post_result);
            setStuff_user_status_view(jsonObject, post_result, 1);

            return post_result;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return "";
    }

    //翻轉動畫
    @SuppressLint("NewApi")
    private void rotateAnim (final ViewGroup v){

        final ScaleAnimation animation_suofang =new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation_suofang.setDuration(1000);
        animation_suofang.setRepeatMode(Animation.REVERSE);

        RotateAnim anim = new RotateAnim();
        anim.isZhengfangxiang = true;
        anim.direction = anim.X;
        anim.setDuration(2000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

//                v.setAnimation(animation_suofang);
                showBottomType.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.setAnimation(anim);
    }


    private String getStrDate(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        sdfDate.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        String strDate = sdfDate.format(new Date());
//        Log.e("WorkController", "Date: " + strDate);
        return strDate;
    }

    private String getStrTime(){
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        sdfTime.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        String strTime = sdfTime.format(new Date());

        return strTime;
    }

    //取得Google Account and 設定 日曆活動
    private void setWorkOutAlarm(String date, String workintime) {
        Log.e("date", date + "---- " + workintime);
        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
        };

        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

       long timeMillers = getStrToMillers(date, workintime);//取得可下班時間的Millers
//        Log.e("TEST", String.valueOf(timeMillers));


        //得到查詢結果,最主要拿到結果的東西就是CalID,要利用這ID去加入活動才會有作用
        long calID = -1;
        String possibleEmail = "";
        String possibleType = "";
        Account[] accounts = AccountManager.get(controller_context).getAccountsByType("com.google");
//        Log.e("Account", Arrays.toString(accounts));
        for (Account account : accounts) {
//            Log.e("TEST","name >>> " + account.name + " Type: " + account.type);
            possibleEmail = account.name;
            possibleType = account.type;
        }
        //如果等於空白並且也是大於25版以上
        if ((possibleEmail.isEmpty() || possibleType.isEmpty()) && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
            ContentResolver cr = controller_ac.getContentResolver();
            Cursor cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null);

            String displayName = "";
            String accountName = "";
            String accountType = "";
            while (cursor != null && cursor.moveToNext()){
                calID = cursor.getLong(0);
                displayName = cursor.getString(12);
                accountName = cursor.getString(33);
                accountType = cursor.getString(23);
            }
            if (cursor != null) cursor.close();
//            Log.e("D", "calID: " + calID + " displayName: " + displayName + " accountName: " + accountName + " accountType: " + accountType);
            if (!accountName.isEmpty() && !accountType.isEmpty()) {
                possibleEmail = accountName;
                possibleType = accountType;
            }
        //如果不為空並查詢cal_id
        } else if(!possibleEmail.isEmpty() || !possibleType.isEmpty()){
            /**檢查使用者有哪種日曆*/
            //先查詢日曆
            Cursor cal_result = null;
            ContentResolver crs = controller_ac.getContentResolver();
            Uri uris = CalendarContract.Calendars.CONTENT_URI;
            String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                    + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                    + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[]{possibleEmail, possibleType,
                    possibleEmail};

            // Submit the query and get a Cursor object back.
            cal_result = crs.query(uris, EVENT_PROJECTION, selection, selectionArgs, null);


            String displayName = null;
            String accountName = null;
            String ownerName = null;
            while (cal_result.moveToNext()) {
                // Get the field values
                calID = cal_result.getLong(PROJECTION_ID_INDEX);
                displayName = cal_result.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cal_result.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cal_result.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

//                Log.e("TAET", "calID: " + calID + " displayName: " + displayName + " accountName: " + accountName + " ownerName: " + ownerName);

            }
        } else {

            return;
        }

        if (calID != -1) {
            ContentResolver cr = controller_ac.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, timeMillers);
            values.put(CalendarContract.Events.DTEND, timeMillers);
            values.put(CalendarContract.Events.TITLE, "Clickforce-" + date + "-下班");
            values.put(CalendarContract.Events.DESCRIPTION, "現在可以下班摟，快跑啊～");
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Taipei");
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);//寫入
            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());
//                long eventID = ContentUris.parseId(uri);
//            Log.e("EventID", ">>>>" + eventID);

            ContentValues values_prompt = new ContentValues();
            values_prompt.put(CalendarContract.Reminders.MINUTES, 0);
            values_prompt.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values_prompt.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri_prompt = cr.insert(CalendarContract.Reminders.CONTENT_URI, values_prompt);
            long eventID_prompt = Long.parseLong(uri_prompt.getLastPathSegment());
//            Log.e("EventID", ">>>>" + eventID_prompt);

            ContentValues values_update = new ContentValues();
            values_update.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            values_update.put(CalendarContract.Calendars.VISIBLE, 1);
            int update = cr.update(ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, eventID), values_update, null, null);
//            Log.e("update", ""+update);

            asSyncAdapter(uri, possibleEmail, possibleType);
        } else {
            Log.e("WorkController", "Cal ID is empty.");
        }
    }

    //同步google帳號
    static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
//                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
//                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    private long getStrToMillers(String date, String time){
        String workInTime = date + " " + time;

        String[] timesplit = time.split(":");
        //先進行先加九小時半上班時間
        int canWorkOutH = Integer.parseInt(timesplit[0]) + 9;
        int canWorkOutM = Integer.parseInt(timesplit[1]) + 30;

        //整理加時後的可下班時間
        if (canWorkOutM >= 60){
            canWorkOutM = canWorkOutM - 60 ;
            canWorkOutH = canWorkOutH + 1;
        }

        //處理可下班時間分小於十需要轉字串加零
        String canWorkOutMStr = String.valueOf(canWorkOutM);
        if (canWorkOutM < 10) canWorkOutMStr = "0"+canWorkOutMStr;

        String workOutStr = canWorkOutH + ":" + canWorkOutMStr;
        String workOutDateTime = date + " " + workOutStr + ":00";//日期與時間合併(必須加上秒數，不然會有問題)
        String referenceTime = date + " " + "19:30:59";//基準下班時間點
        String referenceWorkIn = date + " " + "08:30:00";//基準上班時間點

        Date workOut = new Date();
        Date referenceAfter = new Date();
        Date workin = new Date();
        Date referenceBefore = new Date();
        //取得手機USER Account and Type 已便提供寫入google日曆
        try {
            //將可下班時間String to Date格式再轉Millis
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            workOut = sdf.parse(workOutDateTime);//Work Out time 轉換 Date格式
            referenceAfter = sdf.parse(referenceTime);

            //比對ReferenceAfterTime
            if (referenceAfter.compareTo(workOut) < 0 || referenceAfter.compareTo(workOut) == 0) return referenceAfter.getTime();

            //比對ReferenceBeforeTine
            workin = sdf.parse(workInTime);
            referenceBefore = sdf.parse(referenceWorkIn);
            if (referenceBefore.compareTo(workin) > 0 || referenceBefore.compareTo(workin) == 0) {
                String canReferenceWorkOut = date + " " + "18:00:00";
                Date canRWO = sdf.parse(canReferenceWorkOut);
                return canRWO.getTime();
            }

        }catch (Exception e){
            e.getStackTrace();
        }

        return workOut.getTime();
    }

    //取得應用程式可以抓取Google Account 的權限
    private void getEmailAndType(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(controller_ac);
        builder.setMessage("要允許「CFWorkingTime」\n使用您的 Google 帳號提供日曆存取使用嗎？")
                .setPositiveButton("允許", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Google帳號選擇器
                        Intent googlePicker = AccountManager.newChooseAccountIntent(null, null,
                                new String[] { "com.google"}, true, null, null, null, null);
                        controller_ac.startActivityForResult(googlePicker, 66);
                    }
                })
                .setNegativeButton("拒絕", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Toast.makeText(controller_ac, "So Sad! \n這樣就沒辦法提示你下班時間了.\n那你使用這App意義在哪?" ,Toast.LENGTH_SHORT).show();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();

    }


//    public void setonResume(){
//        loginView();
//        if (null != historyDialog && historyDialog.isShowing()) historyDialog.dismiss();
//    }
}
