package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.ServiceProvider;

import java.util.ArrayList;

/**
 * Created by zafar on 10/22/17.
 */

public class ServiceProviderTable extends Table {

    public static final String TABLE_NAME = "SERVICE_PROVIDER";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS SERVICE_PROVIDER (NAME TEXT, EMAIL TEXT,PHONENUMBER TEXT,PASSWORD TEXT,ADDRESS TEXT, CNIC TEXT, LICENSENUMBER TEXT, CATEGORY INT)";

    //   private final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String PHONE_NUMBER  = "PHONENUMBER";
    public static final String ADDRESS  = "ADDRESS";
    public static final String CNIC  = "CNIC";
    public static final String LICENSE_NUMBER  = "LICENSENUMBER";
    public static final String CATEGORY  = "CATEGORY";



    public ServiceProviderTable(){

        super(TABLE_NAME);
    }

    @Override
    protected ContentValues buildContentValues(BaseModel data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, ((ServiceProvider) data).getName());
        contentValues.put(EMAIL, ((ServiceProvider) data).getEmail());
        contentValues.put(PASSWORD, ((ServiceProvider)data).getPassword());
        contentValues.put(PHONE_NUMBER, ((ServiceProvider)data).getPhoneNumber());
        contentValues.put(ADDRESS,((ServiceProvider) data).getAddress());
        contentValues.put(CNIC,((ServiceProvider) data).getCNIC());
        contentValues.put(LICENSE_NUMBER,((ServiceProvider) data).getLicenseNumber());
        contentValues.put(CATEGORY,((ServiceProvider) data).getCategory());


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
                ServiceProvider serviceProvider = new ServiceProvider();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                serviceProvider.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                serviceProvider.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                serviceProvider.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
                serviceProvider.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
                serviceProvider.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                serviceProvider.setCNIC(cursor.getString(cursor.getColumnIndex(CNIC)));
                serviceProvider.setLicenseNumber(cursor.getString(cursor.getColumnIndex(LICENSE_NUMBER)));
                serviceProvider.setCategory(cursor.getInt(cursor.getColumnIndex(CATEGORY)));
                userArray.add(serviceProvider);
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
        return null;
    }

    @Override
    protected String whereClauseForData() {
        return null;
    }
}
