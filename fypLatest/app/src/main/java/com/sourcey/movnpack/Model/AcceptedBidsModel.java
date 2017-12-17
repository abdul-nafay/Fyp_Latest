package com.sourcey.movnpack.Model;

/**
 * Created by abdul on 12/17/17.
 */

public class AcceptedBidsModel extends BaseModel {

    String bidId,spId,spName,date;

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
}
