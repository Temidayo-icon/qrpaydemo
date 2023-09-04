package com.example.qrpaydemo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable{
    private String userId;
    private String qrCodeData;
    private double amount;

    public User(String userId, String qrCodeData) {
        this.userId = userId;
        this.qrCodeData = qrCodeData;
        this.amount = amount;
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
        userId = in.readString();
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

        dest.writeString(userId);
        dest.writeString(qrCodeData);

    }
}