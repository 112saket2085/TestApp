package com.example.testapp.rest.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 13/09/2020
 */
public class RequestBody {



    public static class MarketRequestBody {
        @SerializedName("Count")
        private int count;
        @SerializedName("ClientLoginType")
        private int clientLoginType;
        @SerializedName("LastRequestTime")
        private String lastRequestTime;
        @SerializedName("MarketFeedData")
        private List<MarketDataRequestBody> marketDataRequestBodyList;

        public void setCount(int count) {
            this.count = count;
        }

        public void setClientLoginType(int clientLoginType) {
            this.clientLoginType = clientLoginType;
        }

        public void setLastRequestTime(String lastRequestTime) {
            this.lastRequestTime = lastRequestTime;
        }

        public void setMarketDataRequestBodyList(List<MarketDataRequestBody> marketDataRequestBodyList) {
            this.marketDataRequestBodyList = marketDataRequestBodyList;
        }
    }

    public static class MarketDataRequestBody {
        @SerializedName("Exch")
        private String exch;
        @SerializedName("ExchType")
        private String exchType;
        @SerializedName("ScripCode")
        private String scripCode;
        @SerializedName("ClientLoginType")
        private int clientLoginType;
        @SerializedName("RequestType")
        private int requestType;

        public void setExch(String exch) {
            this.exch = exch;
        }

        public void setExchType(String exchType) {
            this.exchType = exchType;
        }

        public void setScripCode(String scripCode) {
            this.scripCode = scripCode;
        }

        public void setClientLoginType(int clientLoginType) {
            this.clientLoginType = clientLoginType;
        }

        public void setRequestType(int requestType) {
            this.requestType = requestType;
        }
    }
}
