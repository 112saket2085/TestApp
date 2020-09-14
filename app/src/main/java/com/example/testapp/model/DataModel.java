package com.example.testapp.model;

import com.google.gson.annotations.SerializedName;
import java.text.DecimalFormat;

/**
 * Created by User on 13/09/2020
 */
public class DataModel {

    @SerializedName("ShortName")
    private String shortName;
    @SerializedName("LastTradePrice")
    private Double lastTradePrice;
    @SerializedName("ScripCode")
    private String scripCode;
    private String change="0";

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(Double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public String getScripCode() {
        return scripCode;
    }

    public void setScripCode(String scripCode) {
        this.scripCode = scripCode;
    }

    public String getChange() {
        return change;
    }

    public void setChange(Double lastRate) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double value = ((lastRate - lastTradePrice) / lastTradePrice) * 100;
        this.change=decimalFormat.format(value);
    }
}
