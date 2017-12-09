package com.sourcey.movnpack.Model;

/**
 * Created by abdul on 11/5/17.
 */

public class SpProfileViewModel {

    private int errorCode;
    private String message;
    private ServiceProvider serviceProvider;

    public SpProfileViewModel(int errorCode, String message, ServiceProvider serviceProvider) {
        this.errorCode = errorCode;
        this.message = message;
        this.serviceProvider = serviceProvider;
    }


    public SpProfileViewModel(int errorCode, String message) {
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

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
