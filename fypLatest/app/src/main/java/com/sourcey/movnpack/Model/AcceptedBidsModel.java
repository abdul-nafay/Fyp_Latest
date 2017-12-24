package com.sourcey.movnpack.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.sourcey.movnpack.DataBase.AcceptedBids;

/**
 * Created by abdul on 12/17/17.
 */

public class AcceptedBidsModel extends BaseModel implements Parcelable {

    String bidId,spId,spName,date,spToken;

    public AcceptedBidsModel() {

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

    public String getSpToken() {
        return spToken;
    }

    public void setSpToken(String spToken) {
        this.spToken = spToken;
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
    }

    public static final Parcelable.Creator<AcceptedBidsModel> CREATOR = new Parcelable.Creator<AcceptedBidsModel>() {

        @Override
        public AcceptedBidsModel createFromParcel(Parcel source) {
            return new AcceptedBidsModel(source);
        }

        @Override
        public AcceptedBidsModel[] newArray(int size) {
            return new AcceptedBidsModel[size];
        }
    };

    private AcceptedBidsModel(Parcel source) {
        bidId = source.readString();
        spId = source.readString();
        spName = source.readString();
        date = source.readString();
        spToken = source.readString();
    }
}
