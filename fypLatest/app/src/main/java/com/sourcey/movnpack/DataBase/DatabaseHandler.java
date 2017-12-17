package com.sourcey.movnpack.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zafar on 10/22/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "MovNPack";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UserTable.SCHEMA);
        db.execSQL(ServiceProviderTable.SCHEMA);
        db.execSQL(Bid.SCHEMA);
        db.execSQL(BidRecievedTable.SCHEMA);
        db.execSQL(AcceptedBids.SCHEMA);
        db.execSQL(SPBidCounterTable.SCHEMA);
        db.execSQL(UserBidCounterTable.SCHEMA);

        //UserTable userTable = new UserTable();
        //userTable.insertData(db,new User());
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ServiceProviderTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Bid.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BidRecievedTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AcceptedBids.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SPBidCounterTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserBidCounterTable.TABLE_NAME);
    }
}
