package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;

import java.util.ArrayList;

/**
 * Created by abdul on 12/17/17.
 */

public class AcceptedBids extends Table {

    public static final String TABLE_NAME = "ACCEPTEDBIDS";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS ACCEPTEDBIDS (BID_ID TEXT,SP_ID TEXT,SP_NAME TEXT,DATE TEXT)";

    //   private final String ID = "ID";
    public static final String BID_ID = "BID_ID";
    public static final String SP_ID = "SP_ID";
    public static final String SP_NAME = "SP_NAME";
    public static final String DATE = "DATE";

    public AcceptedBids(){

        super(TABLE_NAME);

    }

    @Override
    protected ContentValues buildContentValues(BaseModel data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BID_ID, ((AcceptedBidsModel) data).getBidId());
        contentValues.put(SP_ID, ((AcceptedBidsModel) data).getSpId());
        contentValues.put(SP_NAME, ((AcceptedBidsModel) data).getSpName());
        contentValues.put(DATE, ((AcceptedBidsModel)data).getDate());


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
                AcceptedBidsModel bidModel = new AcceptedBidsModel();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                bidModel.setBidId(cursor.getString(cursor.getColumnIndex(BID_ID)));
                bidModel.setSpId(cursor.getString(cursor.getColumnIndex(SP_ID)));
                bidModel.setSpName(cursor.getString(cursor.getColumnIndex(SP_NAME)));

                bidModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));

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
        return null;
    }
}
