package com.sourcey.movnpack.Utility;

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
}
