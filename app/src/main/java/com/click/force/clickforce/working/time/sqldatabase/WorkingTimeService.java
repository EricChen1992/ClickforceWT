package com.click.force.clickforce.working.time.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkingTimeService extends SQLiteOpenHelper {
    private static final String TAG = "WorkingTimeService";
    public static final String DB_NAME = "Work";
    public static final String TABLE_NAME = "PunchTime";//儲存User Type
    public static final String FIRST_NAME = "FirstName";//儲存User Info

    public String wResult = "result";
    public String wItem = "item";
    private String fId = "id";
    public String fCuaId = "cua_id";
    public String fCuaName = "cua_name";
    private String fCuaRespons = "cua_respons";
    private String wkId = "id";
    public String wkName = "name";
    public String wkdate = "date";
    public String wkTimeClockIn = "time_clock_in";
    public String wkTimeClockOut = "time_clock_out";
    public String wkType = "type";
    public String wkUpdate = "update_time";
    public String wkResponse = "response";

    public WorkingTimeService (Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("CREATE TABLE if NOT EXISTS " +
//                TABLE_NAME +
//                String.format("("+ "%s integer PRIMARY KEY AUTOINCREMENT, " +
//                                "%s text NOT NULL , " +
//                                "%s text NOT NULL , " +
//                                "%s text NOT NULL , " +
//                                "%s text NOT NULL , " +
//                                "%s text NOT NULL , " +
//                                "%s text NOT NULL , " +
//                                "%s text NOT NULL);"
//                                ,wkId
//                                ,wkName
//                                ,wkdate
//                                ,wkTimeClockIn
//                                ,wkTimeClockOut
//                                ,wkType
//                                ,wkUpdate
//                                ,wkResponse
//                ));
        sqLiteDatabase.execSQL("CREATE TABLE if NOT EXISTS " +
                TABLE_NAME +
                String.format("("+ "%s integer PRIMARY KEY AUTOINCREMENT, " +
                                "%s text NOT NULL , " +
                                "%s text NOT NULL , " +
                                "%s text NOT NULL , " +
                                "%s text NOT NULL , " +
                                "%s text NOT NULL , " +
                                "%s text NOT NULL);"
                        ,wkId
                        ,wkType
                        ,wkdate
                        ,wkTimeClockIn
                        ,wkTimeClockOut
                        ,wkUpdate
                        ,wkResponse
                ));
        sqLiteDatabase.execSQL("CREATE TABLE if NOT EXISTS " +
                FIRST_NAME +
                String.format("("+ "%s integer PRIMARY KEY AUTOINCREMENT, " +
                                "%s text NOT NULL , " +
                                "%s text NOT NULL , " +
                                "%s text NOT NULL);"
                        ,fId
                        ,fCuaId
                        ,fCuaName
                        ,fCuaRespons
                ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 檢查table是否存在
     * @return  存在:True 不存在:False
     * */
    public boolean getFTable(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM sqlite_master where type='table' and name='" + FIRST_NAME + "';",null);

        Boolean check = cursor.getCount() != 0;
        cursor.close();
        sqLiteDatabase.close();

        return check;
    }

    public boolean insertFirstNameData(String cuaid, String cuaname, String cuarespons){

        if (!"".equals(cuaid) && !"".equals(cuaname) && !"".equals(cuarespons)){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(fCuaId,cuaid);
            contentValues.put(fCuaName,cuaname);
            contentValues.put(fCuaRespons,cuarespons);
            long reponsedb = sqLiteDatabase.insert(FIRST_NAME, null, contentValues);
            sqLiteDatabase.close();
            return !(reponsedb == -1);
        }

        return false;
    }

    public Map<String,String> getFirstNameAndIdData(){
        Map<String, String> nameandid = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + FIRST_NAME + ";",null);
        cursor.moveToFirst();
        if (cursor.getCount()!= 0){
//            result = cursor.getString(2);
            nameandid.put(fCuaId, cursor.getString(1));
            nameandid.put(fCuaName, cursor.getString(2));
        }
        cursor.close();
        sqLiteDatabase.close();
        return nameandid;
//        return rawquery("SELECT * FROM " + FIRST_NAME + ";");
    }

//    public String getFirstNameData(){
//
//    }


    /**
     * 檢查table是否存在
     * @return  存在:True 不存在:False
     * */
    public boolean getTable(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM sqlite_master where type='table' and name='" + TABLE_NAME + "';",null);

        Boolean check = cursor.getCount() != 0;
        cursor.close();
        sqLiteDatabase.close();

        return check;
    }

    /**
     * 將資料插入DB
     * @param date
     * @param timeclockin
     * @param timeclockout
     * @param type
     * @return 成功:True 失敗:False
     * */
    public boolean insertData(String name, String date, String timeclockin,String timeclockout, String type, String respons){
        Format f = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String strTime = f.format(new Date());
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(wkdate, date);
        contentValues.put(wkTimeClockIn, timeclockin);
        contentValues.put(wkTimeClockOut, timeclockout);
        contentValues.put(wkType, type);
        contentValues.put(wkUpdate, strTime);
        contentValues.put(wkResponse, respons);

        long responsdb = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
//            Log.e(TAG,"responsedb = " + (responsdb == -1) );
        sqLiteDatabase.close();
        return !(responsdb == -1);
    }

    /**
     * 更新DB
     * @param name
     * @param date
     * @param timeclockin
     * @param timeclockout
     * @param type
     * @return 成功:True 失敗:False
     * */
    public boolean updateData(String name, String date, String timeclockin,String timeclockout, String type){
        Format f = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String strTime = f.format(new Date());
        if (!"".equals(name)){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(wkTimeClockOut, timeclockout);
            contentValues.put(wkType, type);
            contentValues.put(wkUpdate, strTime);
            String ckStr = wkName + " = \"" + name + "\"" + " and " + wkType + " = \"" + "1\"" + " and " + wkdate + " = \"" + date + "\"";
            long reponsecb = sqLiteDatabase.update(TABLE_NAME, contentValues, ckStr, null);
            sqLiteDatabase.close();
            return  !(reponsecb == -1);
        }
        return false;

    }

    /**
     * 檢查名字是否存在
     * @param selectType 搜尋型態 1.搜尋姓名 2.搜尋姓名及日期 3.搜尋姓名及日期跟狀態
     * @param name 姓名
     * @param date 日期
     * @param worktype 狀態
     * @return  存在:True 不存在:False
     * */
    public boolean getQuery(int selectType, String name, String date, String worktype){

        switch (selectType){
            case 1:
                return rawquery("SELECT * FROM " + TABLE_NAME + " WHERE " + wkName + " == \"" + name + "\"" + ";");

            case 2:
                return rawquery("SELECT * FROM " + TABLE_NAME + " WHERE " + wkName + " == \"" + name + "\"" + " and "
                                        + "\"" + wkdate + "\"" + "==" + "\"" + date + "\"" + ";");
            case 3:
                return rawquery("SELECT * FROM " + TABLE_NAME + " WHERE " + wkName + " == \"" + name + "\"" + " and "
                                        + "\"" + wkdate + "\"" + "==" + "\"" + date + "\"" + " and "
                                        + "\"" + wkType + "\"" + "==" + "\"" + worktype + "\"" + ";");
            default:
                return false;
        }

    }

    private boolean rawquery(String cmdLine){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(cmdLine,null);
//        Log.e(TAG,cursor.getCount() + "");
        return cursor.getCount()!= 0;//沒資料為零
    }

    /**
     * 取得PunchTimeTable所有的資料
     * @param date
     * @return 將每一筆資料用MAP包起來，存到ArrayList回傳
     * */
    public Map<String, String> getFindDate(String date){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Map<String, String> fDate = new HashMap<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT *FROM " + TABLE_NAME + ";",null);
        if (cursor.getCount() == 0) return fDate;
        cursor.moveToFirst();
        do{
            if (date.equals(cursor.getString(2))){
                fDate.put(wkName,cursor.getString(1));
                fDate.put(wkdate,cursor.getString(2));
                fDate.put(wkTimeClockIn,cursor.getString(3));
                fDate.put(wkTimeClockOut,cursor.getString(4));
                fDate.put(wkType,cursor.getString(5));
                fDate.put(wkUpdate,cursor.getString(6));
            }
        }while (cursor.moveToNext());
        cursor.close();
        sqLiteDatabase.close();
        return fDate;

    }
    /**
     * 取得PunchTimeTable所有的資料
     * @return 將每一筆資料用MAP包起來，存到ArrayList回傳
     * */
    public Map<String, String> getFindUsetStatus(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Map<String, String> fDate = new HashMap<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT *FROM " + TABLE_NAME + ";",null);
        if (cursor.getCount() == 0) return fDate;
        cursor.moveToFirst();
        do{
            for (String key : cursor.getColumnNames()) {
                fDate.put(key, cursor.getString(cursor.getColumnIndex(key)));
            }
//            fDate.put(wkName,cursor.getString(1));
//            fDate.put(wkdate,cursor.getString(2));
//            fDate.put(wkTimeClockIn,cursor.getString(3));
//            fDate.put(wkTimeClockOut,cursor.getString(4));
//            fDate.put(wkType,cursor.getString(5));
//            fDate.put(wkUpdate,cursor.getString(6));

        }while (cursor.moveToNext());
        cursor.close();
        sqLiteDatabase.close();
        return fDate;

    }

    public boolean delPunchTime(){
        Log.e(TAG, "delPunchTime");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_NAME, null, null);
        return result == 1;
    }

    /**
     * 取得TimeTable所有DB內的資料
     * @return 將每一筆資料用MAP包起來，存到ArrayList回傳
     * */
    public ArrayList<Map<String,String>> getAllData(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Map<String, String>> alldata = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT *FROM " + TABLE_NAME + ";",null);
        if (cursor.getCount() == 0) return alldata;
        cursor.moveToFirst();
        do {

            Map<String,String> data = new HashMap<>();
            data.put(wkName,cursor.getString(1));
            data.put(wkdate,cursor.getString(2));
            data.put(wkTimeClockIn,cursor.getString(3));
            data.put(wkTimeClockOut,cursor.getString(4));
            data.put(wkType,cursor.getString(5));
            data.put(wkUpdate,cursor.getString(6));
            alldata.add(data);
        }while (cursor.moveToNext());
        cursor.close();
        sqLiteDatabase.close();
        return alldata;
    }

    /**
     * Get all table Details from the sqlite_master table in Db.
     *
     * @return An ArrayList of table details.
     */
    public ArrayList<String[]> getDbTableDetails() {
        Cursor c = this.getReadableDatabase().rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String[]> result = new ArrayList<String[]>();
        int i = 0;
        result.add(c.getColumnNames());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            for (i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
            }
            result.add(temp);
        }

        return result;
    }
}
