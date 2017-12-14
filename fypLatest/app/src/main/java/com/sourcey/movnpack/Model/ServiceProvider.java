package com.sourcey.movnpack.Model;

import com.sourcey.movnpack.Utility.SPCategory;
import com.sourcey.movnpack.Utility.Utility;

/**
 * Created by abdul on 10/22/17.
 */

public class ServiceProvider extends BaseModel {
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

    private String Address;
    private String CNIC;
    private String LicenseNumber;
    private SPCategory Category;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCNIC() {
        return CNIC;
    }

    public SPCategory getCategory() {
        return Category;
    }

    public void setCategory(SPCategory category) {
        Category = category;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getLicenseNumber() {
        return LicenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        LicenseNumber = licenseNumber;
    }

    public String getCategoryName() {

        return Utility.getCategoryNameFromServiceCategory(this.getCategory());
    }


}
