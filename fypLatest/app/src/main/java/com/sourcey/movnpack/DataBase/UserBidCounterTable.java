package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.SPBidCounterModel;
import com.sourcey.movnpack.Model.UserBidCounterModel;

import java.util.ArrayList;

/**
 * Created by abdul on 12/17/17.
 */

public class UserBidCounterTable extends Table {

    public static final String TABLE_NAME = "USER_BID_COUNTER";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS USER_BID_COUNTER (BID_ID TEXT,SP_ID TEXT,SP_NAME TEXT,DATE TEXT,MESSAGE TEXT,AMOUNT TEXT)";

    //   private final String ID = "ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String BID_ID = "BID_ID";
    public static final String DATE = "DATE";
    public static final String AMOUNT  = "AMOUNT";
    public static final String SP_ID  = "SP_ID";
    public static final String SP_NAME  = "SP_NAME";



    public UserBidCounterTable(){

        super(TABLE_NAME);

    }


    @Override
    protected ContentValues buildContentValues(BaseModel data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE, ((UserBidCounterModel) data).getMessage());
        contentValues.put(BID_ID, ((UserBidCounterModel) data).getBidId());
        contentValues.put(DATE, ((UserBidCounterModel)data).getDate());
        contentValues.put(AMOUNT, ((UserBidCounterModel)data).getAmount());
        contentValues.put(SP_ID, ((UserBidCounterModel)data).getSpId());
        contentValues.put(SP_NAME, ((UserBidCounterModel)data).getSpName());

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
                UserBidCounterModel bidModel = new UserBidCounterModel();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                bidModel.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                bidModel.setBidId(cursor.getString(cursor.getColumnIndex(BID_ID)));
                bidModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                bidModel.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                bidModel.setSpId(cursor.getString(cursor.getColumnIndex(SP_ID)));
                bidModel.setSpName(cursor.getString(cursor.getColumnIndex(SP_NAME)));

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
    protected String whereClauseForData() {
        return null;
    }

    @Override
    protected String whereClauseForUpdate() {
        return null;
    }
}
