package com.example.testapp.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapp.R;
import com.example.testapp.model.DataModel;
import com.example.testapp.rest.request.RequestBody;
import com.example.testapp.rest.response.ResponseView;
import com.example.testapp.view.adapter.DataAdapter;
import com.example.testapp.viewmodel.DataViewModel;
import com.example.testapp.viewmodel.MarketDataViewModel;
import com.example.testapp.viewmodel.model.BaseApiResponse;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 13/09/2020
 */
public class DataValueFragment extends BaseFragment implements DataAdapter.OnItemClickListener{

    private  final long DATA_SYNC_TIME = 5000;
    @BindView(R.id.linear_layout_data_title) LinearLayout linearLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textview_no_data_found) TextView textViewNoDataFound;
    @BindView(R.id.text_view_progress) TextView textViewProgress;
    @BindView(R.id.text_view_retry) TextView textViewRetry;
    private DataViewModel dataViewModel;
    private MarketDataViewModel marketDataViewModel;
    private DataAdapter dataAdapter;
    private List<DataModel> dataModelList=new ArrayList<>();
    private DataFragment dataFragment;
    DataModel dataModelEven;
    DataModel dataModelOdd;
    private Handler handler;

    DataValueFragment(DataFragment dataFragment) {
        this.dataFragment = dataFragment;
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_data_value;
    }

    @Override
    String getTitle() {
        return getString(R.string.str_data_fragment_title);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
        initRecyclerView();
        observeData();
        setViewData();
    }

    void setViewData() {
        if (dataModelEven != null) {
            dataFragment.addMarketData(dataModelEven, true);
        }
        else {
            dataFragment.clearViewTwoData();
        }
        if (dataModelOdd != null) {
            dataFragment.addMarketData(dataModelOdd, false);
        }
        else {
            dataFragment.clearViewOneData();
        }
    }

    private void initViewModel() {
        dataViewModel = new ViewModelProvider(getParentActivity()).get(DataViewModel.class);
        marketDataViewModel = new ViewModelProvider(this).get(MarketDataViewModel.class);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParentActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataAdapter = new DataAdapter(dataModelList);
        dataAdapter.setListener(this);
        recyclerView.setAdapter(dataAdapter);
    }

    /**
     * Method to observe data changes from server
     */
    private void observeData() {
        if (isNetworkAvailable() && dataModelList.isEmpty()) {
            clearList();
            showErrorDataInfo(View.GONE, "");
            setRetryView(View.GONE, "");
            showProgressInfo(View.VISIBLE, getString(R.string.str_loading_data));
            dataViewModel.getResponse().observe(this, new Observer<BaseApiResponse.DataEvent>() {
                @Override
                public void onChanged(BaseApiResponse.DataEvent dataEvent) {
                    showProgressInfo(View.GONE, "");
                    if (dataEvent.isSuccess() && dataEvent.getResponseView() != null) {
                        if (!dataEvent.getResponseView().isEmpty()) {
                            linearLayout.setVisibility(View.VISIBLE);
                            dataModelList.clear();
                            dataModelList.addAll(dataEvent.getResponseView());
                            dataAdapter.notifyDataSetChanged();
                            observeMarketFeedApi();
                        } else {
                            showErrorDataInfo(View.VISIBLE, getString(R.string.no_data_found));
                        }
                    } else {
                        showShortToast(dataEvent.getStatusDescription());
                        showErrorDataInfo(View.VISIBLE, dataEvent.getStatusDescription());
                        setRetryView(View.VISIBLE, getString(R.string.str_retry));
                    }
                }
            });
        } else {
            showShortToast(getString(R.string.no_internet_available_text));
            showProgressInfo(View.GONE, "");
            if (dataModelList.isEmpty()) {
                showErrorDataInfo(View.VISIBLE, getString(R.string.no_internet_available_text));
                setRetryView(View.VISIBLE, getString(R.string.str_retry));
            }
        }
    }


    private void clearList() {
        dataModelList.clear();
        dataAdapter.notifyDataSetChanged();
    }


    private void showProgressInfo(int visibility,String text) {
        progressBar.setVisibility(visibility);
        textViewProgress.setVisibility(visibility);
        textViewProgress.setText(text);
    }

    private void setRetryView(int visibilty,String text) {
        textViewRetry.setVisibility(visibilty);
        textViewRetry.setText(text);
    }

    private void showErrorDataInfo(int visibility,String text) {
        textViewNoDataFound.setVisibility(visibility);
        textViewNoDataFound.setText(text);
    }

    @OnClick(R.id.text_view_retry)
    public void onRetryClick(View view) {
        observeData();
    }


    @Override
    public void onItemClick(DataModel dataModel) {
        int position=dataModelList.indexOf(dataModel)+1;
        if (position % 2 == 0) {
            this.dataModelEven=dataModel;
            dataFragment.addMarketData(dataModelEven,true);
        }
        else {
            this.dataModelOdd=dataModel;
            dataFragment.addMarketData(dataModelOdd,false);
        }
    }

    private void observeMarketFeedApi() {
        handler = new Handler();
        handler.postDelayed(runnable, DATA_SYNC_TIME);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isNetworkAvailable()) {
                marketDataViewModel.getResponse(getRequestBody()).observe(DataValueFragment.this, new Observer<BaseApiResponse.MarketDataDataEvent>() {
                    @Override
                    public void onChanged(BaseApiResponse.MarketDataDataEvent marketDataDataEvent) {
                        if (marketDataDataEvent.isSuccess() && marketDataDataEvent.getResponseView() != null) {
                            setResponseData(marketDataDataEvent.getResponseView().marketDataList);
                            if(DataValueFragment.this.dataModelEven!=null) {
                                dataFragment.addMarketData(DataValueFragment.this.dataModelEven,true);
                            }
                            handler.removeCallbacks(runnable);
                            observeMarketFeedApi();
                        } else {
                            handler.removeCallbacks(runnable);
                            observeMarketFeedApi();
                        }
                    }
                });
            } else {
                showShortToast(getString(R.string.no_internet_available_text));
            }
        }
    };

    private RequestBody.MarketRequestBody getRequestBody() {
        RequestBody.MarketRequestBody marketRequestBody = new RequestBody.MarketRequestBody();
        marketRequestBody.setClientLoginType(1);
        marketRequestBody.setCount(dataModelList.size());
        marketRequestBody.setLastRequestTime("/Date(0)/");
        List<RequestBody.MarketDataRequestBody> marketDataRequestBodyList = new ArrayList<>();
        marketDataRequestBodyList.clear();
        for (DataModel dataModel : dataModelList) {
            RequestBody.MarketDataRequestBody marketDataRequestBody = new RequestBody.MarketDataRequestBody();
            marketDataRequestBody.setClientLoginType(0);
            marketDataRequestBody.setExch("N");
            marketDataRequestBody.setExchType("C");
            marketDataRequestBody.setRequestType(0);
            marketDataRequestBody.setScripCode(dataModel.getScripCode());
            marketDataRequestBodyList.add(marketDataRequestBody);
        }
        marketRequestBody.setMarketDataRequestBodyList(marketDataRequestBodyList);
        return marketRequestBody;
    }

    private void setResponseData(List<ResponseView.MarketData> marketDataList) {
        for(int i=0;i<dataModelList.size();i++) {
            DataModel dataModel = dataModelList.get(i);
            ResponseView.MarketData marketData = marketDataList.get(i);
            dataModel.setChange(marketData.getLastRate());
            if (dataModelEven != null && !TextUtils.isEmpty(dataModelEven.getScripCode()) && dataModelEven.getScripCode().equalsIgnoreCase(dataModel.getScripCode())) {
                this.dataModelEven=dataModel;
            }
        }
        if(dataAdapter!=null) {
            dataAdapter.notifyDataSetChanged();
        }
    }
}
