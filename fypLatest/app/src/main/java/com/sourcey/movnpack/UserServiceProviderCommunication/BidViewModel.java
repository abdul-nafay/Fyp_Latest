package com.sourcey.movnpack.UserServiceProviderCommunication;

/**
 * Created by Abdul Nafay Waseem on 12/16/2017.
 */

public class BidViewModel {

    static String catTitle;
    static String messageInitials;
    static String date;

    public BidViewModel(String catTitle, String messageInitials, String date) {
        this.catTitle = catTitle;
        this.messageInitials = messageInitials;
        this.date = date;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public String getMessageInitials() {
        return messageInitials;
    }

    public String getDate() {
        return date;
    }
}
