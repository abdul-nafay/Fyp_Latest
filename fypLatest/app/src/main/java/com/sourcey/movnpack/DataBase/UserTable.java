package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.User;

import java.util.ArrayList;

/**
 * Created by zafar on 10/22/17.
 */

public class UserTable extends Table {

    public static final String TABLE_NAME = "USER";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS USER (NAME TEXT, EMAIL TEXT,PHONENUMBER TEXT,PASSWORD TEXT)";

 //   private final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String PHONE_NUMBER  = "PHONENUMBER";


    public UserTable(){

        super(TABLE_NAME);

    }

    @Override
    protected ContentValues buildContentValues(BaseModel data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, ((User) data).getName());
        contentValues.put(EMAIL, ((User) data).getEmail());
        contentValues.put(PASSWORD, ((User)data).getPassword());
        contentValues.put(PHONE_NUMBER, ((User)data).getPhoneNumber());


        return contentValues;

    }

    @Override
    protected ArrayList<BaseModel> buildModel(SQLiteDatabase db, Cursor cursor) {
        if(cursor != null)
        {
            int count = cursor.getCount();
            ArrayList<BaseModel> userArray = new ArrayList<BaseModel>(count);
            int i = 0;

            while(!cursor.isAfterLast())
            {
                User user = new User();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
                userArray.add(user);
                cursor.moveToNext();
                i++;
            }

            return userArray;
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
        return " WHERE "+ EMAIL + "=?";
    }

    @Override
    protected String whereClauseForUpdate() {
        return EMAIL + "=?";
    }

    @Override
    protected String whereClauseForData() {
        return null;
    }
}
