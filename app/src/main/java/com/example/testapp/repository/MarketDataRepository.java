package com.example.testapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapp.R;
import com.example.testapp.app.App;
import com.example.testapp.rest.request.RequestBody;
import com.example.testapp.rest.response.ResponseView;
import com.example.testapp.viewmodel.model.BaseApiResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 13/09/2020
 */
public class MarketDataRepository {
    private static MarketDataRepository marketRepository;
    private MutableLiveData<BaseApiResponse.MarketDataDataEvent> mutableLiveData;

    public static MarketDataRepository getInstance() {
        if(marketRepository==null) {
            marketRepository = new MarketDataRepository();
        }
        return marketRepository;
    }

    private MarketDataRepository() {}

    /**
     * Method to call api and get result
     * @param marketRequestBody MarketRequestBody
     * @return  Results
     */
    public LiveData<BaseApiResponse.MarketDataDataEvent> getResponse(RequestBody.MarketRequestBody marketRequestBody) {
        mutableLiveData = new MutableLiveData<>();
        App.getInstance().getApiFactory().getShadiApi().getMarketFeedList(marketRequestBody).enqueue(new Callback<ResponseView.MarketDataResponseData>() {
            @Override
            public void onResponse(@NotNull Call<ResponseView.MarketDataResponseData> call, @NotNull Response<ResponseView.MarketDataResponseData> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(new BaseApiResponse.MarketDataDataEvent(true, "",response.body()));
                } else {
                    mutableLiveData.setValue(new BaseApiResponse.MarketDataDataEvent(false, App.getInstance().getString(R.string.str_server_error),null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseView.MarketDataResponseData> call, @NotNull Throwable t) {
                mutableLiveData.setValue(new BaseApiResponse.MarketDataDataEvent(false, App.getInstance().getString(R.string.str_server_error),null));
            }
        });
        return mutableLiveData;
    }
}
