package com.example.testapp.rest.response;

import com.example.testapp.model.DataModel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by User on 11/08/2020
 * Class Containing response data
 */
public class ResponseView {

    public static class DataResponseView {
        @SerializedName("Data")
        public List<DataModel> dataModelList;
    }

    public static class MarketDataResponseData {
        @SerializedName("Data")
        public List<MarketData> marketDataList;
    }

    public static class MarketData {
        @SerializedName("LastRate")
        private Double lastRate;

        public Double getLastRate() {
            return lastRate;
        }

        public void setLastRate(Double lastRate) {
            this.lastRate = lastRate;
        }
    }

}
