package org.xiaoheshan.hallo.boxing.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.GoodDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestRetCodeEnum;
import org.xiaoheshan.hallo.boxing.client.dal.GoodDAO;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;
import org.xiaoheshan.hallo.boxing.client.ui.activity.GoodActivity;
import org.xiaoheshan.hallo.boxing.client.ui.adapter.GoodListAdapter;
import org.xiaoheshan.hallo.boxing.client.ui.util.ViewInitUtils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment implements AMapLocationListener {

    @BindView(R.id.parent_layout)
    View parentLayout;

    @BindView(R.id.my_location_text_view)
    TextView locationTextView;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private GoodListAdapter adapter;

    private GoodDAO goodDAO = HttpClient.get().getDAO(GoodDAO.class);
    public AMapLocationClient locationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = GoodListAdapter.newInstance(getContext(), new ArrayList<GoodDO>(), GoodActivity.RENT_MODE);
        ViewInitUtils.initRecyclerView(getContext(), recyclerView, adapter);
        recyclerView.setNestedScrollingEnabled(false);
        initLocation();
        initData();
    }

    private void initLocation() {
        locationClient = new AMapLocationClient(getContext());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        locationClient.setLocationOption(option);
        locationClient.setLocationListener(this);
        locationClient.startLocation();
    }

    private void initData() {
        Call<RestResult<GoodDO>> call = goodDAO.getByCabinet(1);
        call.enqueue(new Callback<RestResult<GoodDO>>() {
            @Override
            public void onResponse(Call<RestResult<GoodDO>> call, Response<RestResult<GoodDO>> response) {
                RestResult<GoodDO> result = response.body();
                if (RestRetCodeEnum.SUCCESS.is(result.getCode())) {
                    if (result.getData() != null) {
                        adapter.refreshData(Arrays.asList(result.getData()));
                    }
                } else {
                    Snackbar.make(parentLayout, result.getMsg(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RestResult<GoodDO>> call, Throwable t) {
                Snackbar.make(parentLayout, "获取商品列表失败", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String location = aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();
                locationTextView.setText(location);
                return;
            }
            Log.e("test", aMapLocation.getErrorCode() + "");
            Log.e("test", aMapLocation.getErrorInfo());
        }
        Snackbar.make(parentLayout, "获取定位失败", Snackbar.LENGTH_LONG).show();
    }
}