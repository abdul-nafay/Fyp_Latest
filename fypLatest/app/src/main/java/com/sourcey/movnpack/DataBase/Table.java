package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.BaseModel;

import java.util.ArrayList;

/**
 * Created by zafar on 10/22/17.
 */

public abstract class Table {
    private String mTableName = "";
    protected Table(String tableName)
    {
        mTableName = tableName;
    }

    /**
     * This method insert the data into the table
     * @param db
     * @param data
     * @return returns the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertData(SQLiteDatabase db, BaseModel data)
    {
        long rowId = -1;
        if(data != null)
        {
            ContentValues contentValues = buildContentValues(data);
            //rowId =db.insert(mTableName, null, contentValues);
            rowId=db.insertWithOnConflict(mTableName, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }
        return rowId;
    }


    /**
     * This method updates the data in table
     * @param db
     * @param data
     * @param whereClause
     * @param whereArgs
     */
    public int updateData(SQLiteDatabase db, BaseModel data,String whereClause, String[] whereArgs)
    {
        if(data != null)
        {
            ContentValues contentValues = buildContentValues(data);
            return db.update(mTableName, contentValues, whereClause, whereArgs);
        }
        return -1;
    }


    /**
     * Deletes the row from table
     * @param db
     * @param whereClause
     * @param whereArgs
     */
    public int deleteData(SQLiteDatabase db,String whereClause, String[] whereArgs)
    {
        return db.delete(mTableName, whereClause, whereArgs);
    }


    /**
     * This method fetches the information from table
     * @param db
     * @param whereClause
     * @param whereArgs
     * @returns
     */
    public ArrayList<BaseModel> getData(SQLiteDatabase db, String whereClause, String[] whereArgs)
    {
        String query = "";
        if(cutomQuery() != null && cutomQuery().length() > 0)
            query = cutomQuery();
        else
            query = "SELECT * FROM " + mTableName;

        if (whereClause != null && whereClause.length() > 0) {
            query = query + whereClause;
        }

        ArrayList<BaseModel> model = null;

        Cursor cursor = db.rawQuery(query, whereArgs);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            model = buildModel(db, cursor);
        }
        cursor.close();

        return model;
    }


    protected abstract ContentValues buildContentValues(BaseModel data);
    protected abstract ArrayList<BaseModel> buildModel(SQLiteDatabase db, Cursor cursor);
    protected abstract String cutomQuery();
    protected abstract String whereClause();
    protected abstract String whereClauseForData();
    protected abstract String whereClauseForUpdate();

}
