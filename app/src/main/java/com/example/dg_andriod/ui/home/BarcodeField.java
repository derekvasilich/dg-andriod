package com.example.dg_andriod.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class BarcodeField implements Parcelable {

    public String label;
    public String value;

    protected BarcodeField(Parcel in) {
        label = in.readString();
        value = in.readString();
    }

    public BarcodeField(String label, String value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BarcodeField> CREATOR = new Creator<BarcodeField>() {
        @Override
        public BarcodeField createFromParcel(Parcel in) {
            return new BarcodeField(in);
        }

        @Override
        public BarcodeField[] newArray(int size) {
            return new BarcodeField[size];
        }
    };
}
