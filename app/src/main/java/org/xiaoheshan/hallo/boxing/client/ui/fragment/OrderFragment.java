package org.xiaoheshan.hallo.boxing.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.OrderVO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestRetCodeEnum;
import org.xiaoheshan.hallo.boxing.client.dal.OrderDAO;
import org.xiaoheshan.hallo.boxing.client.state.StateHolder;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;
import org.xiaoheshan.hallo.boxing.client.ui.adapter.OrderListAdapter;
import org.xiaoheshan.hallo.boxing.client.ui.util.ViewInitUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.parent_layout)
    View parentLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private OrderListAdapter adapter;
    private boolean isRefreshing = false;

    private OrderDAO orderDAO = HttpClient.get().getDAO(OrderDAO.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewInitUtils.initSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = OrderListAdapter.newInstance(getContext(), new ArrayList<OrderVO>(), OrderListAdapter.RENT_MODE);
        ViewInitUtils.initRecyclerView(getContext(), recyclerView, adapter);

        refreshData();
    }

    private void refreshData() {
        if (StateHolder.isIsLogin()) {
            Call<RestResult<List<OrderVO>>> call = orderDAO.listByFromUserId(StateHolder.getUserDO().getId());
            call.enqueue(new Callback<RestResult<List<OrderVO>>>() {
                @Override
                public void onResponse(Call<RestResult<List<OrderVO>>> call, Response<RestResult<List<OrderVO>>> response) {
                    RestResult<List<OrderVO>> result = response.body();
                    if (RestRetCodeEnum.SUCCESS.is(result.getCode())) {
                        adapter.refreshData(result.getData());
                    } else {
                        Snackbar.make(parentLayout, result.getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                    refreshOver();
                }

                @Override
                public void onFailure(Call<RestResult<List<OrderVO>>> call, Throwable t) {
                    Snackbar.make(parentLayout, "获取租借列表失败", Snackbar.LENGTH_LONG).show();
                    refreshOver();
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            isRefreshing = true;
            refreshData();
        }
    }

    private void refreshOver() {
        isRefreshing = false;
        swipeRefreshLayout.setRefreshing(false);
    }
}
