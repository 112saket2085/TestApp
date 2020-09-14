package com.example.testapp.rest;

import com.example.testapp.rest.request.RequestBody;
import com.example.testapp.rest.response.ResponseView;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 11/08/2020
 */
public interface TestApi {

    @GET("ChartService/GetData")
    Call<ResponseView.DataResponseView> getDataList();

    @POST("ChartService/MarketFeed")
    Call<ResponseView.MarketDataResponseData> getMarketFeedList(@Body RequestBody.MarketRequestBody marketRequestBody);
}
