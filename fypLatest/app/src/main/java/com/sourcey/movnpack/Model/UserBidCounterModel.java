package com.sourcey.movnpack.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abdul on 12/17/17.
 */

public class UserBidCounterModel extends BaseModel implements Parcelable {

    String bidId;
    String spId;
    String spName;
    String date;
    String spToken;
    String message;
    String amount;

    public UserBidCounterModel() {
    }

    public String getSpToken() {
        return spToken;
    }

    public void setSpToken(String spToken) {
        this.spToken = spToken;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(bidId);
        dest.writeString(spId);
        dest.writeString(spName);
        dest.writeString(date);
        dest.writeString(spToken);
        dest.writeString(message);
        dest.writeString(amount);
    }

    public static final Parcelable.Creator<UserBidCounterModel> CREATOR = new Parcelable.Creator<UserBidCounterModel>() {

        @Override
        public UserBidCounterModel createFromParcel(Parcel source) {
            return new UserBidCounterModel(source);
        }

        @Override
        public UserBidCounterModel[] newArray(int size) {
            return new UserBidCounterModel[size];
        }
    };
    private UserBidCounterModel(Parcel source) {
        bidId = source.readString();
        spId = source.readString();
        spName = source.readString();
        date = source.readString();
        spToken = source.readString();
        message = source.readString();
        amount = source.readString();
    }
}
