package com.example.testapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.testapp.repository.MarketDataRepository;
import com.example.testapp.rest.request.RequestBody;
import com.example.testapp.viewmodel.model.BaseApiResponse;

/**
 * Created by User on 14/09/2020
 */
public class SingleMarketDataViewModel extends AndroidViewModel {

    public SingleMarketDataViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseApiResponse.MarketDataDataEvent> getResponse(RequestBody.MarketRequestBody marketDataRequestBody) {
        return MarketDataRepository.getInstance().getResponse(marketDataRequestBody);
    }

}
