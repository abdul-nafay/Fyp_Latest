package com.sourcey.movnpack.Helpers;

/**
 * Created by Abdul Nafay Waseem on 12/24/2017.
 */

public enum API_TYPE {
    confirmBidBroadcast(0), confirmBidSIngle(1) , unLockBid(2) , spCancelBid(3), userCancelBid(4);

    public int value;
    API_TYPE(int value){
        this.value=value;
    }
}
