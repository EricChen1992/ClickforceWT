package com.click.force.clickforce.working.time;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.click.force.clickforce.working.time.sqldatabase.WorkingTimeService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
//    Context context;
//    RelativeLayout main;
//    EditText editText_email, editText_password;
//    Spinner spinner_name;
//    TextView type, text_name, text_date,text_timeclockin, text_timeclockout, title_textdate, title_texttimeclockin, title_texttimeclockout, show_history_name, show_history_id;
//    WorkingTimeService workingTimeService;
//    Button punchBtn;
//    LinearLayout showContent, show_history_column;
//    ImageView showBottomType;
//    ListView show_history_list;
//    HistoryListView historyListView;
//    AlertDialog historyDialog;

//    String TAG="MainActivity";
//    int clicknb = 0;
//    final String workurl = "http://192.168.1.116/laravelEric/public/api/";
////    final String workurl = "http://172.20.10.8/laravelEric/public/api/";
////    final String workurl = "https://cua-new.holmesmind.com/api/";
//    final String workIn = "worktimein";
//    final String workOut = "worktimeout";
//    final String cua = "getcua";
//    final String checkStatus = "getstatus";
//    final String all_data = "getUserAllData";
    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        context = this;
//        main = (RelativeLayout) findViewById(R.id.mainActivity);
//        type = (TextView) findViewById(R.id.typMode);
//        punchBtn = (Button) findViewById(R.id.punchBtn);
//        workingTimeService = new WorkingTimeService(this);
//        title_textdate = (TextView) findViewById(R.id.textTitleDate);
//        title_texttimeclockin = (TextView) findViewById(R.id.textTittleTimeclockin);
//        title_texttimeclockout = (TextView) findViewById(R.id.textTitleTimeclockout);
//        text_date = (TextView) findViewById(R.id.textDate);
//        text_name = (TextView) findViewById(R.id.textName);
//        text_timeclockin = (TextView) findViewById(R.id.textTimeclockin);
//        text_timeclockout = (TextView) findViewById(R.id.textTimeclockout);
//        showContent = (LinearLayout) findViewById(R.id.showContent);
//        showBottomType = (ImageView) findViewById(R.id.showBottomType);

//        Log.e("Main",workingTimeService.getFirstNameDataCount() ? "Y" : "N");

//        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//        TimeZone tz = TimeZone.getDefault();
//        sdfDate.setTimeZone(tz);
//        final String strDate = sdfDate.format(new Date());
//        final String strDate = getStrDate();
//
//        if (workingTimeService.getFTable()){//判斷是否有User資料
//            String name = workingTimeService.getFirstNameAndIdData().get(workingTimeService.fCuaName);
//            if (!"".equals(name) && null != name){
//
//                popView(strDate, null);
//                rotateAnim(showContent);
//
//            }else{
//                //Creat Login dialog view;
//                final LayoutInflater inflater = this.getLayoutInflater();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                final View contentView = inflater.inflate(R.layout.creatname, null);
//                editText_email = (EditText) contentView.findViewById(R.id.enter_email);
//                editText_password = (EditText) contentView.findViewById(R.id.enter_password);
//                builder.setView(contentView);
//                builder.setPositiveButton(" ",null);
//                builder.setNegativeButton(" ",null);
//
//                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                final AlertDialog dialog = builder.create();
//                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.open_dialog_background_15));
//                dialog.show();
//                //右邊按鈕(確定)
//                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positiveButton.setBackground(getDrawable(R.drawable.open_dialog_r_btn16));
//                positiveButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (editText_email != null && editText_password !=null){
////                            Log.e("Main", editText_email.getText().toString() + "\np:" + editText_password.getText().toString());
////                            workingTimeService.insertFirstNameData(spinnerSelect.toString());
//                            String emilStr = editText_email.getText().toString() + "@clickforce.com.tw";
//                            String pwdStr = editText_password.getText().toString();
//                            Boolean ck =  connectCuaAuth(emilStr, pwdStr);
//                            if (ck){
//                                popView(strDate, null);
//                                rotateAnim(showContent);
//                                dialog.dismiss();
//                            }else {
//                                inputMethodManager.hideSoftInputFromWindow(editText_password.getWindowToken(),0);//鍵盤收起
//                            }
//                        }
//                    }
//                });
//                //左邊按鈕(取消)
//                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negativeButton.setBackgroundColor(Color.GREEN);
//                negativeButton.setBackground(getDrawable(R.drawable.open_dialog_l_btn));
//                negativeButton.setOnClickListener(new View.OnClickListener() {
//                    Boolean close = false;
//                    @Override
//                    public void onClick(View view) {
//                        if (!close){
//                            Toast.makeText(context,"提示:再一次取消就退出App摟!!",Toast.LENGTH_SHORT).show();
//                            close = true;
//                        }else {
//                            finish();
//                        }
//
//                    }
//                });
//                dialog.setCanceledOnTouchOutside(false);//取消點背景會消失
//            }
//        }
    }

//    //翻轉動畫
//    @SuppressLint("NewApi")
//    private void rotateAnim (final ViewGroup v){
//
//        final ScaleAnimation animation_suofang =new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation_suofang.setDuration(1000);
//        animation_suofang.setRepeatMode(Animation.REVERSE);
//
//        RotateAnim anim = new RotateAnim();
//        anim.isZhengfangxiang = true;
//        anim.direction = anim.X;
//        anim.setDuration(2000);
//        anim.setInterpolator(new AccelerateDecelerateInterpolator());
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
////                v.setAnimation(animation_suofang);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        v.setAnimation(anim);
//    }
//
//    private void scaleAnim(View v){
//        final ScaleAnimation animation_suofang =new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation_suofang.setDuration(1000);
//        animation_suofang.setRepeatMode(Animation.REVERSE);
//        animation_suofang.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        v.setAnimation(animation_suofang);
//    }
//
//
//    public void clickin(View view) {
//        String name = workingTimeService.getFirstNameAndIdData().get(workingTimeService.fCuaName);
//        if (workingTimeService.getTable()){
////            Boolean ckname = workingTimeService.getQuery(1,"Eric", strDate,"");//確認是否有當天日期紀錄
//            String strDate = getStrDate();
//            String strTime = getStrTime();
//            if (!workingTimeService.getQuery(2,name, strDate,"")){
//                Boolean ck = workingTimeService.insertData(name, strDate, strTime, "","1",""); //check in
//                if (ck){
//                    type.setText("上班...");
//                    Map<String, String> nameandid = workingTimeService.getFirstNameAndIdData();//取得打到後端資料
//                    connectDB(nameandid, "1");//資料打到後端
//                }
////                Log.e("main", ck ? "上班..." : "");
//                popView(strDate,null);
//            } else if (workingTimeService.getQuery(3,name, strDate,"1")){//判斷是否工作中...，利用name,當天日期,工作狀態做判斷
//                showDoubleConfirmDialog(name, strDate, strTime, "0");
//                //修改欄位
//            } else {
//                clicknb++;
//                String msg = "";
//                if (clicknb == 10){
//                    msg = "點夠了嗎？已經下班了，不要再點了\\/";
//                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//                } else if (clicknb == 50){
//                    msg = "Show system db";
//                    clicknb = 0;
//                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
////                    Log.e("Main", workingTimeService.getAllData() + "");
//                }
//            }
//
//        }
//    }
//
//    public void showHistory(View view) {
//
//        String result = connectPullDB();
//        Log.e(TAG, "showHistory: "+result );
//        JSONArray jsonArray = null;
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(result);
//            jsonObject.optString(workingTimeService.wResult);
//            jsonArray = new JSONArray(jsonObject.optString(workingTimeService.wItem));
//            popViewToShowHistory(jsonArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
////
////        jsonObject.toString();
////        show_history_id.setText(jsonObject.optString("name"));
////        popViewToShowHistory();
//
//    }
//
//    private JSONObject parJson(String s){
//        JSONObject j = null;
//        try {
//
//            j = new JSONObject(s);
//        } catch (JSONException e){
//            e.getStackTrace();
//        }
//        return j;
//    }
//
//    @SuppressLint("NewApi")
//    private void popViewToShowHistory(JSONArray jsonArray){
//
//        try {
//                LayoutInflater layoutInflater = this.getLayoutInflater();
//                View showHistory = layoutInflater.inflate(R.layout.activity_historylist, null);
//                show_history_name = showHistory.findViewById(R.id.show_history_name);
//                show_history_id = showHistory.findViewById(R.id.show_history_id);
//                show_history_name.setText("姓名: "+jsonArray.getJSONObject(0).optString("name"));
//                show_history_id.setText("ID: "+jsonArray.getJSONObject(0).optString("cua_id"));
//
//                show_history_list = showHistory.findViewById(R.id.show_history_list);
//                historyListView = new HistoryListView();
//                show_history_list.setAdapter(historyListView);
//
//
//                historyListView.clear();
//                for (int i=0 ; i<jsonArray.length(); i++){
////                        Log.e(TAG, "popViewToShowHistory: ("+i+")"+jsonArray.getJSONObject(i).optString("time_clock_in"));
//    //                    LayoutInflater layoutInflater = LayoutInflater.from(this);
//                        show_history_column = (LinearLayout) layoutInflater.inflate(R.layout.activity_historycolumn, null);
//
//                        TextView column_date = show_history_column.findViewById(R.id.show_history_column_date);
//                        TextView column_timeclockin = show_history_column.findViewById(R.id.show_history_column_timeclockin);
//                        TextView column_timeclockout = show_history_column.findViewById(R.id.show_history_column_timeclockout);
//                        column_date.setText(jsonArray.getJSONObject(i).optString("date"));
//                        column_timeclockin.setText(jsonArray.getJSONObject(i).optString("time_clock_in"));
//                        column_timeclockout.setText(jsonArray.getJSONObject(i).optString("time_clock_out"));
//
//                        historyListView.addView(show_history_column);
//
//                }
//                historyListView.notifyDataSetChanged();
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setView(showHistory);
//                historyDialog = builder.create();
//                historyDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.activity_history_black));
//                historyDialog.show();
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void connectDB(Map<String, String> data, String type) {
//        String workTypeUrl ="";
//        if (type.equals("1")){
//            workTypeUrl = workurl + workIn;
//        }else if (type.equals("0")){
//            workTypeUrl = workurl+ workOut;
//        }
//
//        Map<String,String> json = new HashMap<>();
//        json.put(workingTimeService.fCuaId,data.get(workingTimeService.fCuaId));
//        json.put(workingTimeService.wkName,data.get(workingTimeService.fCuaName));
//        json.put(workingTimeService.wkType,type);
//
////        httpConnectonPost(workTypeUrl, json);
//
//        HttpConnectPost post = new HttpConnectPost();
//        post.params = json;
//        post.execute(workTypeUrl);
//    }
//
//    private String connectPullDB(){
//        String workTypeUrl = workurl+all_data;
//        String result = "";
//        HttpConnectPost post = new HttpConnectPost();
//        post.params = workingTimeService.getFirstNameAndIdData();//取得打到後端資料;
//        try {
//            return post.execute(workTypeUrl).get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return "No data";
//    }
//
//    private String connectStatus(){
//        String url = workurl + checkStatus;
//        String result = "";
//        HttpConnectPost post = new HttpConnectPost();
//        post.params = workingTimeService.getFirstNameAndIdData();
//        try {
//            result = post.execute(url).get();
//            return result;
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//        return result;
//    }
//
//    private boolean connectCuaAuth(String cua_eamil, String cua_password){
//        Map<String, String> json = new HashMap<>();
//        json.put("cuaemail", cua_eamil);
//        json.put("cuapassword",cua_password);
//        String url = workurl+cua;
//        HttpConnectPost post = new HttpConnectPost();
//        post.params = json;
//        try {
//            JSONObject jsonresult = new JSONObject(post.execute(url).get());
//            String jStatus = jsonresult.getString("status");
//            if (jStatus.equals("1")){
//                String jcua_id = jsonresult.getString("id");
//                String jName = jsonresult.getString("name");
//                String jResult = jsonresult.toString();
//                workingTimeService.insertFirstNameData(jcua_id, jName, jResult);
//                return true;
//            } else {
//                Toast.makeText(context, "提示:帳號或密碼錯誤. \nCode: "+jsonresult.optString("msg"), Toast.LENGTH_LONG).show();
//                return false;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//
//    private void showDoubleConfirmDialog(final String name, final String date, final String nowTime, final String worktype){
//        final Map<String, String> nameandid = workingTimeService.getFirstNameAndIdData();//取得打到後端資料
//        String timelockin = workingTimeService.getFindDate(date).get(workingTimeService.wkTimeClockIn);
////        Log.e("Main", "timelockin : " + timelockin + "\n" + "nowTime : " + nowTime );
//        String[] timesplit = timelockin.split(":");
//        String[] nowtsplit = nowTime.split(":");
//        int canTimeclockoutH = Integer.parseInt(timesplit[0]) + 9;
//        int canTimeclockoutM = Integer.parseInt(timesplit[1]) + 30;
//        if (canTimeclockoutM >= 60){
//            canTimeclockoutM = canTimeclockoutM - 60 ;
//            canTimeclockoutH = canTimeclockoutH + 1;
//        }
//        int nowTimeclockoutH = Integer.parseInt(nowtsplit[0]);
//        int nowIimeclockoutM = Integer.parseInt(nowtsplit[1]);
//        //20200615 如果超過以可以下班時間不用在提示
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        String msg = "您確定要下班了嗎？ \n您下班時間為 " + canTimeclockoutH + ":" + canTimeclockoutM + " 唷!";
//        if (canTimeclockoutH > 19 || canTimeclockoutH == 19 && canTimeclockoutM > 30){//如果超過七點半就不詢問了
//            msg = "您確定要下班了嗎？";
//        }
//        builder.setMessage(msg);
//        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Boolean ck = workingTimeService.updateData(name, date, "", nowTime, "0");
//                if (ck){
//                    type.setText("下班...");
//                    connectDB(nameandid, "0");//資料打到後端
//                }
//                popView(date, null);
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
////    private void popView(String date){
//    private void popView(String date, JSONObject jsonObject){
////        Log.e("Main",workingTimeService.getFindDate(date).toString());
//        Map<String, String> nameandid = workingTimeService.getFirstNameAndIdData();
//        Map<String, String> content = workingTimeService.getFindDate(date);
//        text_name.setText(nameandid.get(workingTimeService.fCuaName));
//        text_timeclockin.setText("");
//        text_timeclockout.setText("");
////        if (!getWebTime(date)) punchBtn.setEnabled(false);
////        Log.e(TAG, "popView: " + content.get(workingTimeService.wkType) + "\n123123: " + nameandid.get(workingTimeService.fCuaName) + "\n getName: " + text_name.getText());
//        if ("1".equals(content.get(workingTimeService.wkType)) && !"".equals(content.get(workingTimeService.wkTimeClockIn)) && "".equals(content.get(workingTimeService.wkTimeClockOut))){
//            showTitle(true);
//            text_timeclockin.setText(content.get(workingTimeService.wkTimeClockIn));
//            text_date.setText(content.get(workingTimeService.wkdate));
//            type.setText("上班...");
//            setShowBottomView(1);
////            Log.e(TAG, "popView: " +workingTimeService.getDbTableDetails() );
//
//        } else if ("0".equals(content.get(workingTimeService.wkType)) && !"".equals(content.get(workingTimeService.wkTimeClockIn)) && !"".equals(content.get(workingTimeService.wkTimeClockOut))){
//            showTitle(true);
//            text_timeclockin.setText(content.get(workingTimeService.wkTimeClockIn));
//            text_timeclockout.setText(content.get(workingTimeService.wkTimeClockOut));
//            text_date.setText(content.get(workingTimeService.wkdate));
//            type.setText("下班...");
//            setShowBottomView(2);
//        }else if ( null !=nameandid.get(workingTimeService.fCuaName) ){
//            text_date.setText(date);
//            showTitle(true);
//            type.setText("尚未打卡，\n要遲到摟～");
//            setShowBottomView(3);
//
//        } else if (null ==  nameandid.get(workingTimeService.fCuaName)){
//            showTitle(false);
//        }
//    }
//
//    private void showTitle(boolean b){
//        if (b){
//            showContent.setVisibility(View.VISIBLE);
//            title_textdate.setVisibility(View.VISIBLE);
//            title_texttimeclockout.setVisibility(View.VISIBLE);
//            title_texttimeclockin.setVisibility(View.VISIBLE);
//        } else {
//            showContent.setVisibility(View.GONE);
//            title_textdate.setVisibility(View.GONE);
//            title_texttimeclockout.setVisibility(View.GONE);
//            title_texttimeclockin.setVisibility(View.GONE);
//        }
//    }
//
//    private void setShowBottomView(int sw){
//        switch (sw){
//            case 1:
//                showBottomType.setImageDrawable(getResources().getDrawable(R.drawable.activity_bottom3));
//            break;
//            case 2:
//                showBottomType.setImageDrawable(getResources().getDrawable(R.drawable.activity_bottom7));
//                break;
//            default:
//                showBottomType.setImageDrawable(null);
//                break;
//        }
//    }
//
//    private boolean getWebTime(String date){//確認手機時間是否被修改
//        String url = workurl+"gettime";
//        HttpConnectPost h = new HttpConnectPost();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date1 = null, date2 = null;
//        try {
//            String s = h.execute(url).get();
//            date1 = sdf.parse(s);
//            date2 = sdf.parse(date);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (date1.getTime() == date2.getTime()){
//            return true;
//        }
//        return false;
//
//    }
//
//    private String getStrDate(){
//        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
////        TimeZone tz = TimeZone.getDefault();
//        sdfDate.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
//        String strDate = sdfDate.format(new Date());
//        Log.e(TAG, "Date: " + strDate);
//        return strDate;
//    }
//
//    private String getStrTime(){
//        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
//        TimeZone tz = TimeZone.getDefault();
//        sdfTime.setTimeZone(tz);
//        String strTime = sdfTime.format(new Date());
//
//        return strTime;
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        popView(getStrDate(), null);
//        rotateAnim(showContent);
//
//        if (null != historyDialog)
//            historyDialog.dismiss();
    }


}


