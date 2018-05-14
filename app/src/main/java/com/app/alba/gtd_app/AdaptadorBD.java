package com.app.alba.gtd_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdaptadorBD extends SQLiteOpenHelper {

    public static final String TABLE_ID = "_idtask";
    public static final String TITLE = "title";
    public static final String DATE_I = "date_i";
    public static final String DATE_F = "date_f";
    public static final String TIME_I = "time_i";
    public static final String TIME_F = "time_f";
    public static final String CONTENT ="content";

    public static final String DATABASE = "Task";
    public static final String TABLE = "tasks";

    public AdaptadorBD(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT," + DATE_I + " TEXT," + TIME_I + "TEXT," + DATE_F + " TEXT," + TIME_F + "" +
                " TEXT," + CONTENT + " TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addTask(String title, String date_i, String time_i, String date_f, String time_f, String content) {
        ContentValues valores = new ContentValues();
        valores.put(TITLE, title);
        valores.put(DATE_I, date_i);
        valores.put(TIME_I, time_i);
        valores.put(DATE_F, date_f);
        valores.put(TIME_F, time_f);
        valores.put(CONTENT, content);
        this.getWritableDatabase().insert(TABLE,null,valores);
    }

    public Cursor getTask(String condition) {
        String columnas[] = {TABLE_ID,TITLE,CONTENT};
        String[] args = new String[] {condition};

        Cursor c = this.getReadableDatabase().query(TABLE,columnas,TITLE+"=?",args,null,null,null);

        return c;
    }

    public void deleteTask(String condition) {
        String args[] = {condition};
        this.getWritableDatabase().delete(TABLE, TITLE + "=?",args);
    }

    public void UpdateTask(String title, String date_i, String time_i, String date_f, String time_f, String content, String condition) {
        String args[] = {condition};
        ContentValues valores = new ContentValues();
        valores.put(TITLE, title);
        valores.put(DATE_I, date_i);
        valores.put(TIME_I, time_i);
        valores.put(DATE_F, date_f);
        valores.put(TIME_F, time_f);
        valores.put(CONTENT, content);
        this.getWritableDatabase().update(TABLE, valores, TITLE + "=?",args);
    }

    public Cursor getTasks() {
        String columnas[] = {TABLE_ID, TITLE, DATE_I, TIME_I, DATE_F, TIME_F, CONTENT};
        Cursor c = this.getReadableDatabase().query(TABLE, columnas, null, null, null, null, null);
        return c;
    }

    public void deleteTasks() {
        this.getWritableDatabase().delete(TABLE, null, null);
    }
}
