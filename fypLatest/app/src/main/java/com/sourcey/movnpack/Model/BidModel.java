package com.sourcey.movnpack.Model;

import android.app.Application;

import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Helpers.ApplicationContextProvider;

/**
 * Created by abdul on 12/16/17.
 */

public class BidModel extends BaseModel {

    String message,bidId,date,userToken,userId,userName,amount,categoryName ,Subject;

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isConfirmed() {
        ConfirmBidModel confirmBidModel = (ConfirmBidModel) DatabaseManager.getInstance(ApplicationContextProvider.getContext()).getConfirmedBid(this.getBidId());
        if (confirmBidModel!= null) {
            return true;
        }
        else {
            return false;
        }
    }

    public ConfirmBidModel getConfirmedBidModel() {
        ConfirmBidModel confirmBidModel = (ConfirmBidModel) DatabaseManager.getInstance(ApplicationContextProvider.getContext()).getConfirmedBid(this.getBidId());
        return confirmBidModel;
    }

}
