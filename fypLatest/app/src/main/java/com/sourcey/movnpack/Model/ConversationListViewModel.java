package com.sourcey.movnpack.Model;

import static com.sourcey.movnpack.R.id.amount;
import static com.sourcey.movnpack.R.style.AppTheme;

/**
 * Created by Abdul Nafay Waseem on 12/17/2017.
 */

public class ConversationListViewModel {

    String name;
    String message;
    String date;
    String status;
    String spToken;
    String bidID;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String amount;
    public ConversationListViewModel(String name, String message, String date, String status, String spToken, String amount) {
        this.name = name;
        this.message = message;
        this.date = date;
        this.status = status;
        this.spToken = spToken;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpToken() {
        return spToken;
    }

    public void setSpToken(String spToken) {
        this.spToken = spToken;
    }
}