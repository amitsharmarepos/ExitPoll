package com.samyotech.exitpoll.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {

    String name = "";
    String dob = "";
    String mobileno = "";
    String image = "";
    String door = "";
    String pincode = "";

    public UserDTO() {
    }


    public UserDTO(String name, String dob, String mobileno, String image, String door, String pincode) {
        this.name = name;
        this.dob = dob;
        this.mobileno = mobileno;
        this.image = image;
        this.door = door;
        this.pincode = pincode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
