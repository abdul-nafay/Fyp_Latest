package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.SPBidCounterModel;

import java.util.ArrayList;

/**
 * Created by abdul on 12/17/17.
 */

public class SPBidCounterTable extends Table {

    public static final String TABLE_NAME = "SP_BID_COUNTER";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS SP_BID_COUNTER (BID_ID TEXT,DATE TEXT,MESSAGE TEXT,AMOUNT TEXT)";

    //   private final String ID = "ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String BID_ID = "BID_ID";
    public static final String DATE = "DATE";
    public static final String AMOUNT  = "AMOUNT";


    public SPBidCounterTable(){

        super(TABLE_NAME);

    }


    @Override
    protected ContentValues buildContentValues(BaseModel data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE, ((SPBidCounterModel) data).getMessage());
        contentValues.put(BID_ID, ((SPBidCounterModel) data).getBidId());
        contentValues.put(DATE, ((SPBidCounterModel)data).getDate());
        contentValues.put(AMOUNT, ((SPBidCounterModel)data).getAmount());
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
                SPBidCounterModel bidModel = new SPBidCounterModel();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                bidModel.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                bidModel.setBidId(cursor.getString(cursor.getColumnIndex(BID_ID)));
                bidModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                bidModel.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
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
