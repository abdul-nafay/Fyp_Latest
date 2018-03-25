package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;

import java.util.ArrayList;

/**
 * Created by Abdul Nafay Waseem on 12/24/2017.
 */

public class Confirmed_Bids extends Table {
        public static final String TABLE_NAME = "Confirmed_Bids";
        public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS Confirmed_Bids (ID TEXT, MESSAGE TEXT, BID_ID TEXT,DATE TEXT,USER_ID TEXT,AMOUNT TEXT,SP_ID TEXT,SP_TOKEN TEXT,LAT TEXT,LONG TEXT,TIME TEXT,IS_DELETED TEXT)";

        public static final String ID = "ID";
        public static final String MESSAGE = "MESSAGE";
        public static final String BID_ID = "BID_ID";
        public static final String DATE = "DATE";
        public static final String USER_ID  = "USER_ID";
        public static final String AMOUNT  = "AMOUNT";
        public static final String SP_ID  = "SP_ID";
        public static final String SP_TOKEN  = "SP_TOKEN";
        public static final String LAT = "LAT";
        public static final String LONG = "LONG";
        public static final String TIME = "TIME";
        public static final String ISDELETED = "IS_DELETED";
        public Confirmed_Bids(){

            super(TABLE_NAME);

        }

        @Override
        protected ContentValues buildContentValues(BaseModel data) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, ((ConfirmBidModel) data).getID());
            contentValues.put(MESSAGE, ((ConfirmBidModel) data).getMessage());
            contentValues.put(BID_ID, ((ConfirmBidModel) data).getBidId());
            contentValues.put(DATE, ((ConfirmBidModel)data).getDate());
            contentValues.put(SP_TOKEN, ((ConfirmBidModel)data).getSpToken());
            contentValues.put(USER_ID, ((ConfirmBidModel)data).getUserId());
            contentValues.put(SP_ID, ((ConfirmBidModel)data).getSpId());
            contentValues.put(AMOUNT, ((ConfirmBidModel)data).getAmount());
            contentValues.put(LAT, ((ConfirmBidModel)data).getLat());
            contentValues.put(LONG, ((ConfirmBidModel)data).getLongi());
            contentValues.put(TIME, ((ConfirmBidModel)data).getTime());
            contentValues.put(ISDELETED, ((ConfirmBidModel)data).getIsDeleted());
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
                    ConfirmBidModel bidModel = new ConfirmBidModel();
                    bidModel.setID(cursor.getString(cursor.getColumnIndex(ID)));
                    bidModel.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                    bidModel.setBidId(cursor.getString(cursor.getColumnIndex(BID_ID)));
                    bidModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                    bidModel.setSpToken(cursor.getString(cursor.getColumnIndex(SP_TOKEN)));
                    bidModel.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                    bidModel.setSpId(cursor.getString(cursor.getColumnIndex(SP_ID)));
                    bidModel.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                    bidModel.setLat(cursor.getString(cursor.getColumnIndex(LAT)));
                    bidModel.setLongi(cursor.getString(cursor.getColumnIndex(LONG)));
                    bidModel.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                    bidModel.setIsDeleted(cursor.getString(cursor.getColumnIndex(ISDELETED)));
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
            return SP_TOKEN + "=?";
        }

        @Override
        protected String whereClauseForData() {
            return " WHERE "+ BID_ID + "=?" + " AND " + ISDELETED + "=?";
        }
        protected String whereClauseForUpdateID() {
            return ID + "=?";
        }
         protected String whereClauseID() {
            return " WHERE "+ ID + "=?";
        }

    protected String whereClauseForNonDeleted() { return " WHERE "+ ISDELETED + "!=?";}
}
