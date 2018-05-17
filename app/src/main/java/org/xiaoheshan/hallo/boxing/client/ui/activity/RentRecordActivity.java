package org.xiaoheshan.hallo.boxing.client.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

public class RentRecordActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private OrderListAdapter adapter;

    private OrderDAO orderDAO = HttpClient.get().getDAO(OrderDAO.class);

    @BindView(R.id.parent_layout)
    View parentLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private boolean isRefreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        ViewInitUtils.initSwipeRefreshLayout(refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        adapter = OrderListAdapter.newInstance(this, new ArrayList<OrderVO>(), OrderListAdapter.LEASE_MODE);
        ViewInitUtils.initRecyclerView(this, recyclerView, adapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        refreshLayout.setRefreshing(true);
        if (StateHolder.isIsLogin()) {
            Call<RestResult<List<OrderVO>>> call = orderDAO.listByToUserId(StateHolder.getUserDO().getId());
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

    private void refreshOver() {
        isRefreshing = false;
        refreshLayout.setRefreshing(false);
    }
}
