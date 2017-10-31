package com.sourcey.movnpack.Model;


/**
 * Created by abdul on 9/20/17.
 */

public class SignupModel{

    private int errorCode ;
    private String message ;
    private ServiceProvider serviceProvider;

    public SignupModel(int errorCode, String message) {

        this.errorCode = errorCode;
        this.message = message;

    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
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
