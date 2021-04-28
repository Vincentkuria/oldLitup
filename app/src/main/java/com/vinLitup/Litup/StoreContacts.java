package com.vinLitup.Litup;

import java.io.Serializable;

public class StoreContacts implements Comparable<StoreContacts> ,Serializable {
    private String name;
    private String phoneNo;


    public StoreContacts(String name , String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }


    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }


    @Override
    public int compareTo(StoreContacts other) {
        int compareInt=name.compareTo(other.name);
        if (compareInt<0)return -1;
        if (compareInt>0)return  1;
        return 0;
    }
}
