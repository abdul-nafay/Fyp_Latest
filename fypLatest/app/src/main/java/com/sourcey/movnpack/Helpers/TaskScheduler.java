package com.sourcey.movnpack.Helpers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sourcey.movnpack.AppFragments.HomeMapFragment;
import com.sourcey.movnpack.AppFragments.SPScheduledTaskFragment;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.MainActivity;
import com.sourcey.movnpack.Model.AlarmRequestCodeModel;
import com.sourcey.movnpack.Model.AssignedTasksModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;
import com.sourcey.movnpack.Utility.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by ali.haider on 2/13/2018.
 */

public class TaskScheduler {

    static private TaskScheduler instance;
    private TaskScheduler() {

    }
    public static TaskScheduler getInstance() {
        if (instance == null) {
            instance = new TaskScheduler();
        }
        return instance;
    }
    public void scheduleTaskForUser(ConfirmBidModel cbm , Context c) {

        int requestCode = cbm.getID().hashCode();
        Intent myIntent =  new Intent(ApplicationContextProvider.getContext(), AlarmReceiver.class);
        myIntent.putExtra("taskID",cbm.getID());
        myIntent.putExtra("bidID",cbm.getBidId());
       // PendingIntent pendingIntent = PendingIntent.getService(c, requestCode, myIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ApplicationContextProvider.getContext(),requestCode,myIntent,0);
        AlarmManager alarmManager =  (AlarmManager) ApplicationContextProvider.getContext().getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        ScheduledCalenderDay clndrData = parseDateAndTime(cbm.getDate(),cbm.getTime());
        calendar.set(clndrData.year,clndrData.month,clndrData.date,clndrData.hour,clndrData.minute);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Add To DB
        AlarmRequestCodeModel model = new AlarmRequestCodeModel();
        model.setID(requestCode +"");
        model.setTaskID(cbm.getID());
        DatabaseManager.getInstance(ApplicationContextProvider.getContext()).addAlarmRequestCode(model);
    }

    public void deleteAlarmforTask(ConfirmBidModel cbm , AlarmRequestCodeModel alarmRequestCodeModel) {
        int requestCode = Integer.parseInt(alarmRequestCodeModel.getID());
        Intent myIntent =  new Intent(ApplicationContextProvider.getContext(), AlarmReceiver.class);
        myIntent.putExtra("taskID",cbm.getID());
        myIntent.putExtra("bidID",cbm.getBidId());
        // PendingIntent pendingIntent = PendingIntent.getService(c, requestCode, myIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ApplicationContextProvider.getContext(),requestCode,myIntent,0);
        AlarmManager alarmManager =  (AlarmManager) ApplicationContextProvider.getContext().getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);


    }


    void scheduleTaskForSP(String date , String time , AssignedTasksModel asgModel) {

    }

    //HELPER Function
    private ScheduledCalenderDay parseDateAndTime(String dateString, String timeString) {

        Calendar dateClndr = Utility.getCalenderFromDateString(dateString);
        int year = dateClndr.get(Calendar.YEAR);
        int month = dateClndr.get(Calendar.MONTH);
        int date = dateClndr.get(Calendar.DATE);
        Calendar timeClndr = Utility.getCalendarFromTimeString(timeString);
        int hour = timeClndr.get(Calendar.HOUR_OF_DAY);
        int minute = timeClndr.get(Calendar.MINUTE);

        return new ScheduledCalenderDay(year,month,date,hour,minute);
    }


}

class ScheduledCalenderDay {
int year,month,date,hour,minute;

    public ScheduledCalenderDay(int year, int month, int date, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }
}

