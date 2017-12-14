package com.sourcey.movnpack.Model;

/**
 * Created by Abdul Nafay Waseem on 9/24/2017.
 */

public class User extends BaseModel {

    private String name;
    private String email;
    private String phoneNumber;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

/*    public boolean isServiceProvider()
    {
        return this instanceof ServiceProvider;
    }
*/
}
