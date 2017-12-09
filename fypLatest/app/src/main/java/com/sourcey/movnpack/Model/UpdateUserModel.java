package com.sourcey.movnpack.Model;

/**
 * Created by abdul on 11/2/17.
 */

public class UpdateUserModel {

    private int errorCode ;
    private String message ;

    public UpdateUserModel(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
