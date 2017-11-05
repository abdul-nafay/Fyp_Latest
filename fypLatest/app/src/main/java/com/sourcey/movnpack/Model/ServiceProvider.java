package com.sourcey.movnpack.Model;

import com.sourcey.movnpack.Utility.SPCategory;

/**
 * Created by abdul on 10/22/17.
 */

public class ServiceProvider extends User {
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
}
