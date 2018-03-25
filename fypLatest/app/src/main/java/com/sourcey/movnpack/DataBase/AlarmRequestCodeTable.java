package com.sourcey.movnpack.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sourcey.movnpack.Model.AlarmRequestCodeModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.SPBidCounterModel;

import java.util.ArrayList;

/**
 * Created by ali.haider on 2/13/2018.
 */

public class AlarmRequestCodeTable extends  Table {

    public static final String TABLE_NAME = "Alarm_Request_Code";
    public static final String SCHEMA = "CREATE TABLE IF NOT EXISTS Alarm_Request_Code (ID TEXT,TASK_ID TEXT)";

    //   private final String ID = "ID";

    public static final String ID = "ID";
    public static final String TASK_ID = "TASK_ID";



    public AlarmRequestCodeTable(){

        super(TABLE_NAME);

    }


    @Override
    protected ContentValues buildContentValues(BaseModel data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, ((AlarmRequestCodeModel) data).getID());
        contentValues.put(TASK_ID, ((AlarmRequestCodeModel) data).getTaskID());
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
                AlarmRequestCodeModel taskModel = new AlarmRequestCodeModel();
                //user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                taskModel.setID(cursor.getString(cursor.getColumnIndex(ID)));
                taskModel.setTaskID(cursor.getString(cursor.getColumnIndex(TASK_ID)));
                bidArray.add(taskModel);
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
        return ""+ ID + "=?";
    }

    @Override
    protected String whereClauseForData() {
        return " WHERE "+ TASK_ID + "=?";
    }

    @Override
    protected String whereClauseForUpdate() {
        return null;
    }
}
