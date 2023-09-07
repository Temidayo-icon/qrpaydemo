package com.example.qrpaydemo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable{

    public User(int pin) {
        this.pin = pin;
    }

    private int pin;

    public User(String userId, double amount) {
        this.userId = String.valueOf(userId);
        this.amount = amount;

    }



    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    public User(String userId) {
        this.userId = String.valueOf(Integer.parseInt(String.valueOf(userId)));
    }

    public void setUserId(String userId) {
        this.userId = String.valueOf(Integer.parseInt(userId));
    }

    private String userId;

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public User(double amount) {
        this.amount = amount;
    }

    private String qrCodeData;

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private double amount;

    public User(String userId, String username, double amount, int pin, String qrCodeData) {
        this.userId = String.valueOf(userId);
        this.username = username;
        this.qrCodeData = qrCodeData;
        this.amount = amount;
        this.pin = pin;
    }

    public String getUserId() {
        return userId;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }
    public double getAmount() {
        return amount;
    }


    // Parcelable implementation methods

    protected User(Parcel in) {
        userId = String.valueOf(in.readString());
        qrCodeData = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };






    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(String.valueOf(userId));
        dest.writeString(qrCodeData);

    }
}