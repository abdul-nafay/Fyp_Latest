package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.AssignedTasksModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;

import java.util.ArrayList;

/**
 * Created by Abdul Nafay Waseem on 1/5/2018.
 */

public class Assigned_Tasks extends Table {
    public static final String TABLE_NAME = "Assigned_Tasks";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS Assigned_Tasks (ID TEXT,MESSAGE TEXT, BID_ID TEXT,DATE TEXT,USER_ID TEXT,USER_TOKEN,AMOUNT TEXT,SP_ID TEXT,LAT TEXT,LONG TEXT,TIME TEXT)";

    private final String ID = "ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String BID_ID = "BID_ID";
    public static final String DATE = "DATE";
    public static final String USER_ID  = "USER_ID";
    public static final String USER_TOKEN  = "USER_TOKEN";
    public static final String AMOUNT  = "AMOUNT";
    public static final String SP_ID  = "SP_ID";
    public static final String LAT = "LAT";
    public static final String LONG = "LONG";
    public static final String TIME = "TIME";
    public Assigned_Tasks(){

        super(TABLE_NAME);

    }

    @Override
    protected ContentValues buildContentValues(BaseModel data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, ((AssignedTasksModel) data).getID());
        contentValues.put(MESSAGE, ((AssignedTasksModel) data).getMessage());
        contentValues.put(BID_ID, ((AssignedTasksModel) data).getBidId());
        contentValues.put(DATE, ((AssignedTasksModel)data).getDate());
        contentValues.put(USER_ID, ((AssignedTasksModel)data).getUserId());
        contentValues.put(USER_TOKEN, ((AssignedTasksModel)data).getUserToken());
        contentValues.put(SP_ID, ((AssignedTasksModel)data).getSpId());
        contentValues.put(AMOUNT, ((AssignedTasksModel)data).getAmount());
        contentValues.put(LAT, ((AssignedTasksModel)data).getLat());
        contentValues.put(LONG, ((AssignedTasksModel)data).getLongi());
        contentValues.put(TIME, ((AssignedTasksModel)data).getTime());
        return contentValues;

    }

    @Override
    protected ArrayList<BaseModel> buildModel(SQLiteDatabase db, Cursor cursor) {
        if(cursor != null)
        {
            int count = cursor.getCount();
            ArrayList<BaseModel> bidArray = new ArrayList<BaseModel>(count);
            int i = 0;

            while(!cursor.isAfterLast())
            {
                AssignedTasksModel bidModel = new AssignedTasksModel();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                bidModel.setID(cursor.getString(cursor.getColumnIndex(ID)));
                bidModel.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                bidModel.setBidId(cursor.getString(cursor.getColumnIndex(BID_ID)));
                bidModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                bidModel.setUserToken(cursor.getString(cursor.getColumnIndex(USER_TOKEN)));
                bidModel.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                bidModel.setSpId(cursor.getString(cursor.getColumnIndex(SP_ID)));
                bidModel.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                bidModel.setLat(cursor.getString(cursor.getColumnIndex(LAT)));
                bidModel.setLongi(cursor.getString(cursor.getColumnIndex(LONG)));
                bidModel.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                bidArray.add(bidModel);
                cursor.moveToNext();
                i++;
            }

            return bidArray;
        }
        else
            return null;

    }

    @Override
    protected String cutomQuery() {
        return null;
    }

    @Override
    protected String whereClause() {
        return " WHERE "+ USER_ID + "=?";
    }

    @Override
    protected String whereClauseForUpdate() {
        return USER_TOKEN + "=?";
    }

    @Override
    protected String whereClauseForData() {
        return " WHERE "+ BID_ID + "=?";
    }
}
