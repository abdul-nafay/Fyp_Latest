package com.sourcey.movnpack.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abdul on 12/17/17.
 */

public class BidRecievedModel extends BaseModel implements Parcelable {

    String message,bidId,date,userToken,userId,userName,amount,categoryName,status ,spId, subject;
    int lock ;

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public BidRecievedModel(){
        //super();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(message);
        dest.writeString(bidId);
        dest.writeString(date);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userToken);
        dest.writeString(amount);
        dest.writeString(categoryName);
        dest.writeString(status);
        dest.writeString(spId);
        dest.writeString(subject);
        dest.writeInt(lock);
    }

    public static final Parcelable.Creator<BidRecievedModel> CREATOR = new Parcelable.Creator<BidRecievedModel>() {

        @Override
        public BidRecievedModel createFromParcel(Parcel source) {
            return new BidRecievedModel(source);
        }

        @Override
        public BidRecievedModel[] newArray(int size) {
            return new BidRecievedModel[size];
        }
    };

    private BidRecievedModel(Parcel source) {
        message = source.readString();
        bidId = source.readString();
        date = source.readString();
        userId = source.readString();
        userName = source.readString();
        userToken = source.readString();
        amount = source.readString();
        categoryName = source.readString();
        status = source.readString();
        spId = source.readString();
        subject = source.readString();
        lock = source.readInt();
    }
}
