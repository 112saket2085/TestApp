package com.example.testapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapp.R;
import com.example.testapp.app.App;
import com.example.testapp.rest.response.ResponseView;
import com.example.testapp.viewmodel.model.BaseApiResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 13/09/2020
 */
public  class DataRepository {

    private static DataRepository dataRepository;
    private MutableLiveData<BaseApiResponse.DataEvent> mutableLiveData;

    public static DataRepository getInstance() {
        if(dataRepository==null) {
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    private DataRepository() {}

    /**
     * Method to call api and get result
     * @return  Results
     */
    public LiveData<BaseApiResponse.DataEvent> getResponse() {
        mutableLiveData = new MutableLiveData<>();
        App.getInstance().getApiFactory().getShadiApi().getDataList().enqueue(new Callback<ResponseView.DataResponseView>() {
            @Override
            public void onResponse(@NotNull Call<ResponseView.DataResponseView> call, @NotNull Response<ResponseView.DataResponseView> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    mutableLiveData.setValue(new BaseApiResponse.DataEvent(true, "",response.body().dataModelList));
                } else {
                    mutableLiveData.setValue(new BaseApiResponse.DataEvent(false, App.getInstance().getString(R.string.str_server_error),null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseView.DataResponseView> call, @NotNull Throwable t) {
                mutableLiveData.setValue(new BaseApiResponse.DataEvent(false, App.getInstance().getString(R.string.str_server_error),null));
            }
        });
        return mutableLiveData;
    }
}
