package com.sourcey.movnpack.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by abdul on 10/22/17.
 */

public class Utility {

    public static String getPathForServiceNamed(String service) {

        return AppConstants.SERVICE_PROVIDER + "/" + service;

    }

    public static SPCategory getCategoryFromInt(int value) {

        for (SPCategory sp : SPCategory.values())
        {
            if (sp.value == value)
            {
                return sp;
            }
        }
        return null;
    }


    public static SPCategory getCategoryForServiceProviderUsingString(String catName) {

        switch (catName)
        {
            case "CARGO":
               return SPCategory.CARGO;
            case "MANDI":
                return SPCategory.MANDI;
            case "LABOUR":
                return SPCategory.LABOUR;
            case "PICNIC":
                return SPCategory.PICNIC;
            case "PACKING" :
                return SPCategory.PACKING;
            case "ELECTRICIAN":
                return SPCategory.ELECTRICIAN;
            case "PLUMBER":
                return SPCategory.PLUMBER;
            default:
                return null;
        }

    }


    public static String getCategoryNameFromServiceCategory(SPCategory spCat)
    {
        switch (spCat) {
            case MANDI:
                return "Mandi";
            case CARGO:
                return "Cargo";
            case LABOUR:
                return "Labour";
            case PICNIC:
                return "Picnic";
            case PACKING:
                return "Packing";
            case ELECTRICIAN:
                return "Electrician";
            case PLUMBER:
                return "Plumber";
        }
        return null;
    }

    public static String getCurrentDateString() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String dateString = sdf2.format(new Date());
        return dateString;
    }

    public static Calendar getCalenderFromDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(dateString);
            Calendar clndr = format.getCalendar();

            System.out.println(date);
            return clndr;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar getCalendarFromTimeString(String timeString)
    {
        /*
        String[] splitted = timeString.split(":");
        int hour = Integer.parseInt(splitted[0]);
        int min = Integer.parseInt(splitted[1].split(" ")[0]);
        if (hour == 12) {
            hour -=12 ;
        } else if (hour < 12 ) {
            hour += 12;
        }
*/
        try{
            final SimpleDateFormat sdfm = new SimpleDateFormat("K:mm a");
            final Date dateObj = sdfm.parse(timeString);
            Calendar clndr = sdfm.getCalendar();
            return clndr;

        }
        catch (ParseException e ) {
            e.printStackTrace();
        }
        return null;
    }

}
