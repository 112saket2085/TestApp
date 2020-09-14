package com.example.testapp.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.testapp.R;
import com.example.testapp.model.DataModel;
import com.example.testapp.rest.request.RequestBody;
import com.example.testapp.rest.response.ResponseView;
import com.example.testapp.view.adapter.ViewPagerAdapter;
import com.example.testapp.viewmodel.SingleMarketDataViewModel;
import com.example.testapp.viewmodel.model.BaseApiResponse;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import static com.example.testapp.app.Constants.DATA_SYNC_TIME;

/**
 * Created by User on 13/09/2020
 */
public class DataFragment extends BaseFragment {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager2 viewPager;
    @BindView(R.id.textview_view_name_view_one) TextView textViewNameViewOne;
    @BindView(R.id.textview_view_value_view_one) TextView textViewValueViewOne;
    @BindView(R.id.textview_view_name_view_two) TextView textViewNameViewTwo;
    @BindView(R.id.textview_view_value_view_two) TextView textViewValueViewTwo;
    private TabLayout.Tab selectTab;
    private DataModel dataModel;
    private SingleMarketDataViewModel singleMarketDataViewModel;
    private Handler handler;
    private List<Fragment> fragmentList = new ArrayList<>();
    private DataValueFragment dataValueFragment;


    @Override
    int getLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    String getTitle() {
        return getString(R.string.str_data_fragment_title);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPager();
        initViewModel();
    }

    private void initViewModel() {
        singleMarketDataViewModel = new ViewModelProvider(this).get(SingleMarketDataViewModel.class);
    }


    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,getFragmentList());
        viewPager.setAdapter(viewPagerAdapter);
        addTab(getString(R.string.tab_one));
        addTab(getString(R.string.tab_two));
        addTab(getString(R.string.tab_three));
        if (selectTab!=null) {
            tabLayout.selectTab(selectTab);
        }
        dataValueFragment = (DataValueFragment) fragmentList.get(viewPager.getCurrentItem());
        dataValueFragment.setViewData();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab=tab;
                viewPager.setCurrentItem(tab.getPosition());
                dataValueFragment = (DataValueFragment) fragmentList.get(viewPager.getCurrentItem());
                dataValueFragment.setViewData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectTab = tabLayout.getTabAt(position);
                tabLayout.selectTab(selectTab);
            }
        });
    }

    private void addTab(String text) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(text);
        tabLayout.addTab(tab);
    }


    private List<Fragment> getFragmentList() {
        fragmentList.clear();
        fragmentList.add(new DataValueFragment(this));
        fragmentList.add(new DataValueFragment(this));
        fragmentList.add(new DataValueFragment(this));
        return fragmentList;
    }

    void clearViewOneData() {
        this.dataModel=null;
        textViewNameViewOne.setText("");
        textViewValueViewOne.setText("");
    }

    void clearViewTwoData() {
        textViewNameViewTwo.setText("");
        textViewValueViewTwo.setText("");
    }

    void addMarketData(DataModel dataModel, boolean isEvenDataClick) {
        if (isEvenDataClick && dataValueFragment.dataModelEven!=null) {
            textViewNameViewTwo.setText(dataModel.getShortName());
            String changeValue = TextUtils.isEmpty(dataModel.getChange()) ? "" : "(" + dataModel.getChange() + ")";
            textViewValueViewTwo.setText(getString(R.string.str_data_value, dataModel.getLastTradePrice(), changeValue));
        } else {
            this.dataModel = dataModel;
            textViewNameViewOne.setText(dataModel.getShortName());
            String changeValue = TextUtils.isEmpty(dataModel.getChange()) ? "" : "(" + dataModel.getChange() + ")";
            textViewValueViewOne.setText(getString(R.string.str_data_value, dataModel.getLastTradePrice(), changeValue));
            observeMarketFeedApi();
        }
    }

    private void observeMarketFeedApi() {
        if (dataModel != null) {
            handler = new Handler();
            handler.postDelayed(runnable,DATA_SYNC_TIME);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isNetworkAvailable()) {
                RequestBody.MarketRequestBody marketRequestBody = getRequestBody();
                if(marketRequestBody!=null) {
                    singleMarketDataViewModel.getResponse(marketRequestBody).observe(getViewLifecycleOwner(), new Observer<BaseApiResponse.MarketDataDataEvent>() {
                        @Override
                        public void onChanged(BaseApiResponse.MarketDataDataEvent marketDataDataEvent) {
                            if (marketDataDataEvent.isSuccess() && marketDataDataEvent.getResponseView() != null) {
                                setResponseData(marketDataDataEvent.getResponseView().marketDataList);
                                handler.removeCallbacks(runnable);
                                observeMarketFeedApi();
                            } else {
                                showShortToast(marketDataDataEvent.getStatusDescription());
                                handler.removeCallbacks(runnable);
                                observeMarketFeedApi();
                            }
                        }
                    });
                }
            } else {
                showShortToast(getString(R.string.no_internet_available_text));
            }
        }
    };

    private RequestBody.MarketRequestBody getRequestBody() {
        if (dataModel != null) {
            RequestBody.MarketRequestBody marketRequestBody = new RequestBody.MarketRequestBody();
            marketRequestBody.setClientLoginType(1);
            marketRequestBody.setCount(1);
            marketRequestBody.setLastRequestTime("/Date(0)/");
            List<RequestBody.MarketDataRequestBody> marketDataRequestBodyList = new ArrayList<>();
            RequestBody.MarketDataRequestBody marketDataRequestBody = new RequestBody.MarketDataRequestBody();
            marketDataRequestBody.setClientLoginType(0);
            marketDataRequestBody.setExch("N");
            marketDataRequestBody.setExchType("C");
            marketDataRequestBody.setRequestType(0);
            if (dataModel != null) {
                marketDataRequestBody.setScripCode(dataModel.getScripCode());
            }
            marketDataRequestBodyList.add(marketDataRequestBody);
            marketRequestBody.setMarketDataRequestBodyList(marketDataRequestBodyList);
            return marketRequestBody;
        }
        return null;
    }

    private void setResponseData(List<ResponseView.MarketData> marketDataList) {
        ResponseView.MarketData marketData = marketDataList.get(0);
        if (dataModel != null) {
            dataModel.setChange(marketData.getLastRate());
            addMarketData(dataModel, false);
        }
    }

}
