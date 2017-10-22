package com.sourcey.movnpack.Model;

/**
 * Created by abdul on 10/22/17.
 */

public class ServiceProvider extends User {
    private String Address;
    private String CNIC;
    private String LicenseNumber;
    private int Category;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCNIC() {
        return CNIC;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
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
