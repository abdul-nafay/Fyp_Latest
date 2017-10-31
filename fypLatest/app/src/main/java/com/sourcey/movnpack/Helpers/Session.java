package com.sourcey.movnpack.Helpers;

import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Model.User;

/**
 * Created by abdul on 10/22/17.
 */

public class Session {
    static Session sharedInstance;
    private User user;
    private ServiceProvider serviceProvider;

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private Session(){

    }

    public static Session getInstance()
    {
        if (sharedInstance == null) {
            sharedInstance = new Session();
        }
        return sharedInstance;
    }
}
