package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.BidRecievedModel;

import java.util.ArrayList;

/**
 * Created by abdul on 12/17/17.
 */

public class BidRecievedTable extends Table {

    public static final String TABLE_NAME = "BID_RECIEVED";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS BID_RECIEVED (MESSAGE TEXT, BID_ID TEXT,DATE TEXT,USER_TOKEN TEXT,USER_ID TEXT,USER_NAME TEXT,AMOUNT TEXT,SP_ID TEXt,STATUS TEXT , SUBJECT TEXT , LOCK INTEGER)";

    //   private final String ID = "ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String BID_ID = "BID_ID";
    public static final String DATE = "DATE";
    public static final String USER_TOKEN  = "USER_TOKEN";
    public static final String USER_ID  = "USER_ID";
    public static final String USER_NAME  = "USER_NAME";
    public static final String AMOUNT  = "AMOUNT";
    public static final String SP_ID  = "SP_ID";
    public static final String STATUS  = "STATUS";
    public static final String SUBJECT  = "SUBJECT";
    public static  final String LOCK = "LOCK";
    public BidRecievedTable(){

        super(TABLE_NAME);

    }

    @Override
    protected ContentValues buildContentValues(BaseModel data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE, ((BidRecievedModel) data).getMessage());
        contentValues.put(BID_ID, ((BidRecievedModel) data).getBidId());
        contentValues.put(DATE, ((BidRecievedModel)data).getDate());
        contentValues.put(USER_TOKEN, ((BidRecievedModel)data).getUserToken());
        contentValues.put(USER_ID, ((BidRecievedModel)data).getUserId());
        contentValues.put(USER_NAME, ((BidRecievedModel)data).getUserName());
        contentValues.put(AMOUNT, ((BidRecievedModel)data).getAmount());
        contentValues.put(SP_ID, ((BidRecievedModel)data).getSpId());
        contentValues.put(STATUS, ((BidRecievedModel)data).getStatus());
        contentValues.put(SUBJECT, ((BidRecievedModel)data).getSubject());
        contentValues.put(LOCK, ((BidRecievedModel)data).getLock());

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
                BidRecievedModel bidModel = new BidRecievedModel();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                bidModel.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                bidModel.setBidId(cursor.getString(cursor.getColumnIndex(BID_ID)));
                bidModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                bidModel.setUserToken(cursor.getString(cursor.getColumnIndex(USER_TOKEN)));
                bidModel.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                bidModel.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                bidModel.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                bidModel.setSpId(cursor.getString(cursor.getColumnIndex(SP_ID)));
                bidModel.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                bidModel.setSubject(cursor.getString(cursor.getColumnIndex(SUBJECT)));
                bidModel.setLock(cursor.getInt(cursor.getColumnIndex(LOCK)));
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
        return " WHERE "+ BID_ID + "=?";
    }

    @Override
    protected String whereClauseForUpdate() {
        return BID_ID + "=?";
    }

    @Override
    protected String whereClauseForData() {
        return " WHERE "+ BID_ID + "=?";
    }
}
