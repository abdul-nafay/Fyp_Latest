package com.sourcey.movnpack.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Model.User;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by zafar on 10/22/17.
 */

public class DatabaseManager extends DatabaseHandler {


    private Semaphore semaphore = null;
    private SQLiteDatabase dbObj = null;
    private static DatabaseManager dbInstance;

    private DatabaseManager(Context context) {
        super(context);
        semaphore = new Semaphore(1);
        dbObj = null;
    }

    public static DatabaseManager getInstance(Context ctx){

        if (dbInstance == null){
            dbInstance = new DatabaseManager(ctx);
        }
        return dbInstance;
    }

    /**
     * This method return the database writable instance. And it is must to call the closeDb
     * so that other can use the db
     * @return SQLiteDatabase instance
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private SQLiteDatabase getDbForwrite() throws Exception
    {
        semaphore.acquire();
        dbObj = getWritableDatabase();
        return dbObj;
    }

    /**
     * This method return the database readable instance. And it is must to call the closeDb
     * so that other can use the db.
     * @return SQLiteDatabase instance
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private SQLiteDatabase getDbForRead() throws Exception
    {
        semaphore.acquire();
        dbObj = getReadableDatabase();
        return dbObj;
    }

    /**
     * This method closes the opened database and makes it available to others
     */
    @SuppressWarnings("unused")
    private void closeDb()
    {
        dbObj.close();
        semaphore.release();
    }


    public boolean addUser(User userModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            UserTable userTable = new UserTable();
            long row = userTable.insertData(db,userModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public User getUser(String email){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            UserTable userTable = new UserTable();
            data =userTable.getData(db, userTable.whereClause(), new String[]{email});
            return  data != null ? (User) (data.get(0)) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }


    public ArrayList<BaseModel> getUsers(){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            UserTable userTable = new UserTable();
            data =userTable.getData(db, null, null);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }

    /// For Service Providers

    public boolean addServiceProvider(ServiceProvider serviceProviderModel){
        SQLiteDatabase db = null;
        try {
            db = getDbForwrite();
            ServiceProviderTable serviceProviderTable = new ServiceProviderTable();
            long row = serviceProviderTable.insertData(db,serviceProviderModel);
            return  row >0  ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return false;

    }

    public ServiceProvider getServiceProvider(String spId){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            ServiceProviderTable serviceProviderTable = new ServiceProviderTable();
            data =serviceProviderTable.getData(db, serviceProviderTable.whereClause(), new String[]{spId});
            return  data != null ? (ServiceProvider) (data.get(0)) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }


    public ArrayList<BaseModel> getServiceProviders(){
        SQLiteDatabase db = null;
        ArrayList<BaseModel> data = null;
        try {
            db = getDbForRead();
            ServiceProviderTable serviceProviderTable = new ServiceProviderTable();
            data =serviceProviderTable.getData(db, null, null);
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return null;

    }


    /// End SP here

    public long updateUser(User userModel){

        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = getDbForwrite();
            UserTable userTable=new UserTable() ;
            String[] whereArgs = {String.valueOf(userModel.getEmail())};
            result = userTable.updateData(db, userModel, userTable.whereClauseForUpdate(), whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(db!= null)
                closeDb();
        }
        return result;
    }

    public boolean edituserprofile(User userModel){
        boolean result=false;
        if(userModel!=null) {
            long x = updateUser(userModel);
            result = x > 0;
        }
        else {
            result=false;
        }
        return result;

    }
}
