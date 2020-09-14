package com.example.testapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.testapp.repository.DataRepository;
import com.example.testapp.viewmodel.model.BaseApiResponse;

/**
 * Created by User on 13/09/2020
 */
public class DataViewModel extends AndroidViewModel {


    public DataViewModel(@NonNull Application application) {
        super(application);
    }
    private LiveData<BaseApiResponse.DataEvent> liveData;

    public LiveData<BaseApiResponse.DataEvent> getResponse() {
        if (liveData != null) {
            BaseApiResponse.DataEvent dataEvent = liveData.getValue();
            if (dataEvent != null && dataEvent.isSuccess()) {
                return liveData;
            } else {
                liveData = DataRepository.getInstance().getResponse();
                return liveData;
            }
        } else {
            liveData = DataRepository.getInstance().getResponse();
            return liveData;
        }
    }
}
