package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.User;

import java.util.ArrayList;

/**
 * Created by abdul on 12/16/17.
 */

public class Bid  extends Table {

    public static final String TABLE_NAME = "BID";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS BID (MESSAGE TEXT, BID_ID TEXT,DATE TEXT,USER_TOKEN TEXT,USER_ID TEXT,USER_NAME TEXT,AMOUNT TEXT,SUBJECT TEXT,CATEGORY_NAME TEXT)";

    //   private final String ID = "ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String BID_ID = "BID_ID";
    public static final String DATE = "DATE";
    public static final String USER_TOKEN  = "USER_TOKEN";
    public static final String USER_ID  = "USER_ID";
    public static final String USER_NAME  = "USER_NAME";
    public static final String AMOUNT  = "AMOUNT";
    public static final String SUBJECT  = "SUBJECT";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";

    public Bid(){

        super(TABLE_NAME);

    }

    @Override
    protected ContentValues buildContentValues(BaseModel data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE, ((BidModel) data).getMessage());
        contentValues.put(BID_ID, ((BidModel) data).getBidId());
        contentValues.put(DATE, ((BidModel)data).getDate());
        contentValues.put(USER_TOKEN, ((BidModel)data).getUserToken());
        contentValues.put(USER_ID, ((BidModel)data).getUserId());
        contentValues.put(USER_NAME, ((BidModel)data).getUserName());
        contentValues.put(AMOUNT, ((BidModel)data).getAmount());
        contentValues.put(SUBJECT, ((BidModel)data).getSubject());
        contentValues.put(CATEGORY_NAME,((BidModel) data).getCategoryName());
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
                BidModel bidModel = new BidModel();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                bidModel.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                bidModel.setBidId(cursor.getString(cursor.getColumnIndex(BID_ID)));
                bidModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                bidModel.setUserToken(cursor.getString(cursor.getColumnIndex(USER_TOKEN)));
                bidModel.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                bidModel.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                bidModel.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                bidModel.setSubject(cursor.getString(cursor.getColumnIndex(SUBJECT)));
                bidModel.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)));
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
