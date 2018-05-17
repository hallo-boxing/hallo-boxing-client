package org.xiaoheshan.hallo.boxing.client.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.OrderDO;
import org.xiaoheshan.hallo.boxing.client.bean.OrderVO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestPageResult;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;
import org.xiaoheshan.hallo.boxing.client.dal.CabinetDAO;
import org.xiaoheshan.hallo.boxing.client.dal.OrderDAO;
import org.xiaoheshan.hallo.boxing.client.state.SystemConstant;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;

import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 01-05-2018
 */
public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements  ReactiveAdapter<List<OrderVO>> {

    private List<OrderVO> orderVOS;
    private Context context;

    private CabinetDAO cabinetDAO = HttpClient.get().getDAO(CabinetDAO.class);
    private OrderDAO orderDAO = HttpClient.get().getDAO(OrderDAO.class);

    private static final int ITEM_TYPE_NORMAL = 0x464;
    private static final int ITEM_TYPE_END = 0x915;

    public static final int RENT_MODE = 0x943;
    public static final int LEASE_MODE = 0x419;

    private int mode;

    private OrderListAdapter() {
    }

    public static OrderListAdapter newInstance(Context context, List<OrderVO> orderDOS, int mode) {
        OrderListAdapter adapter = new OrderListAdapter();
        adapter.context = context;
        adapter.orderVOS = orderDOS;
        adapter.mode = mode;
        return adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (orderVOS == null || position >= orderVOS.size()) {
            return ITEM_TYPE_END;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_END) {
            return new SimpleViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_end, parent, false));
        }
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_order, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_NORMAL) {
            OrderVO orderVO = orderVOS.get(position);
            ((OrderViewHolder) holder).bindData(orderVO);
        }
    }

    @Override
    public int getItemCount() {
        return orderVOS == null ? 1 : orderVOS.size() + 1;
    }

    @Override
    public void refreshData(List<OrderVO> data) {
        this.orderVOS.clear();
        this.orderVOS.addAll(data);
        notifyDataSetChanged();
    }

    private void scan(final OrderVO orderVO) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("点击确认进行扫码开启柜门")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, CaptureActivity.class);
                        context.startActivity(intent);
                        dialogInterface.dismiss();
                        openDoor(1, orderVO);
                    }
                })
                .create();
        dialog.show();
    }

    private void openDoor(Integer cabinetId, final OrderVO orderVO) {
        Call<RestResult<Void>> call = cabinetDAO.openDoor(cabinetId);
        call.enqueue(new Callback<RestResult<Void>>() {
            @Override
            public void onResponse(Call<RestResult<Void>> call, Response<RestResult<Void>> response) {
                openDoor(orderVO);
            }

            @Override
            public void onFailure(Call<RestResult<Void>> call, Throwable t) {

            }
        });
    }

    private void openDoor(final OrderVO orderVO) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("请将物品放入柜中，关闭柜门，点击确认进行归还物品。")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeDoor(1, orderVO);
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void closeDoor(final Integer cabinetId, final OrderVO orderVO) {
        Call<RestResult<Void>> call = cabinetDAO.closeDoor(cabinetId);
        call.enqueue(new Callback<RestResult<Void>>() {
            @Override
            public void onResponse(Call<RestResult<Void>> call, Response<RestResult<Void>> response) {
                retur(orderVO);
            }

            @Override
            public void onFailure(Call<RestResult<Void>> call, Throwable t) {

            }
        });
    }

    private void retur(OrderVO orderVO) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("正在归还物品...")
                .setCancelable(false)
                .create();
        dialog.show();
        Call<RestPageResult<OrderDO>> call = orderDAO.retur(orderVO.getFromUserId(), orderVO.getId(), 1);
        call.enqueue(new Callback<RestPageResult<OrderDO>>() {
            @Override
            public void onResponse(Call<RestPageResult<OrderDO>> call, Response<RestPageResult<OrderDO>> response) {
                new AlertDialog.Builder(context)
                        .setMessage("归还物品成功，请刷新查看。")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<RestPageResult<OrderDO>> call, Throwable t) {

            }
        });
    }

    private void pay(final Integer orderId) {
        new AlertDialog.Builder(context)
                .setMessage("点击确认付款")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doPay(orderId);
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void doPay(Integer orderId) {
        Call<RestResult<Void>> call = orderDAO.pay(orderId);
        call.enqueue(new Callback<RestResult<Void>>() {
            @Override
            public void onResponse(Call<RestResult<Void>> call, Response<RestResult<Void>> response) {
                new AlertDialog.Builder(context)
                        .setMessage("付款成功，请刷新查看。")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
            }

            @Override
            public void onFailure(Call<RestResult<Void>> call, Throwable t) {

            }
        });
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        @BindView(R.id.good_image_view)
        ImageView goodImageView;
        @BindView(R.id.good_name_text_view)
        TextView goodNameTextView;
        @BindView(R.id.order_status_text_view)
        TextView orderStatusTextView;
        @BindView(R.id.rent_time_text_view)
        TextView rentTimeTextView;
        @BindView(R.id.good_price_text_view)
        TextView goodPriceTextView;
        @BindView(R.id.order_price_text_view)
        TextView orderPriceTextView;
        @BindView(R.id.order_control_button)
        Button orderControlButton;

        OrderViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        void bindData(final OrderVO orderVO) {
            goodNameTextView.setText(orderVO.getName());
            goodPriceTextView.setText("单价 ￥" + orderVO.getRentPrice());
            Long deliveryTime = Long.valueOf(orderVO.getDeliveryTime());
            Long receiveTime = 0L;
            long currentTimeMillis = System.currentTimeMillis();
            if (orderVO.getReceiveTime() != null) {
                receiveTime = Long.valueOf(orderVO.getReceiveTime());
            }
            if (orderVO.getImg() != null) {
                Picasso.get().load(SystemConstant.IMAGE_URL_PREFIX + orderVO.getImg()).into(goodImageView);
            }
            if (mode == LEASE_MODE) {
                orderControlButton.setVisibility(View.GONE);
            }
            else {
                orderControlButton.setVisibility(View.VISIBLE);
            }
            switch (orderVO.getStatus()) {
                case 0:
                    orderStatusTextView.setText("正在租借");
                    rentTimeTextView.setText(DateUtils.formatDateRange(context,
                            deliveryTime,
                            currentTimeMillis,
                            DateUtils.FORMAT_SHOW_TIME));
                    orderPriceTextView.setText("总价 ￥" + (currentTimeMillis - deliveryTime)/1000/60*orderVO.getRentPrice());
                    orderControlButton.setText("归还物品");
                    orderControlButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            scan(orderVO);
                        }
                    });
                    break;
                case 1:
                    orderStatusTextView.setText("已完成");
                    rentTimeTextView.setText(DateUtils.formatDateRange(context,
                            deliveryTime,
                            receiveTime,
                            DateUtils.FORMAT_SHOW_TIME));
                    orderPriceTextView.setText("总价 ￥" + (receiveTime - deliveryTime)/1000/60*orderVO.getRentPrice());
                    orderControlButton.setText("再来一单");
                    break;
                case 2:
                    orderStatusTextView.setText("已取消");
                    rentTimeTextView.setText("0");
                    orderPriceTextView.setText("总价 ￥" + (receiveTime - deliveryTime)/1000/60*orderVO.getRentPrice());
                    orderControlButton.setText("再来一单");
                    break;
                case 3:
                    orderStatusTextView.setText("待付款");
                    rentTimeTextView.setText(DateUtils.formatDateRange(context,
                            deliveryTime,
                            receiveTime,
                            DateUtils.FORMAT_SHOW_TIME));
                    orderPriceTextView.setText("总价 ￥" + (receiveTime - deliveryTime)/1000/60*orderVO.getRentPrice());
                    orderControlButton.setText("付款");
                    orderControlButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pay(orderVO.getId());
                        }
                    });
                    break;
            }
        }
    }
}
