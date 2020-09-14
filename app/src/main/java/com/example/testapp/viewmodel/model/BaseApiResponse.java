package com.example.testapp.viewmodel.model;

import com.example.testapp.model.DataModel;
import com.example.testapp.rest.response.ResponseView;

import java.util.List;

/**
 * Created by User on 11/08/2020
 * Model class for Mutable Livedata object
 */
public class BaseApiResponse {

    public static class DataEvent {
        private boolean isSuccess;
        private String statusDescription;
        private List<DataModel> dataModelList;

        public DataEvent(boolean isSuccess, String statusDescription, List<DataModel> dataModelList) {
            this.isSuccess = isSuccess;
            this.statusDescription = statusDescription;
            this.dataModelList = dataModelList;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public String getStatusDescription() {
            return statusDescription;
        }

        public List<DataModel> getResponseView() {
            return dataModelList;
        }
    }

    public static class MarketDataDataEvent {
        private boolean isSuccess;
        private String statusDescription;
        private ResponseView.MarketDataResponseData marketDataResponseData;

        public MarketDataDataEvent(boolean isSuccess, String statusDescription, ResponseView.MarketDataResponseData marketDataResponseData) {
            this.isSuccess = isSuccess;
            this.statusDescription = statusDescription;
            this.marketDataResponseData = marketDataResponseData;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public String getStatusDescription() {
            return statusDescription;
        }

        public ResponseView.MarketDataResponseData getResponseView() {
            return marketDataResponseData;
        }
    }

}
