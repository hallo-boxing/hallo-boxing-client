package org.xiaoheshan.hallo.boxing.client.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.GoodDO;
import org.xiaoheshan.hallo.boxing.client.bean.OrderDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestRetCodeEnum;
import org.xiaoheshan.hallo.boxing.client.dal.CabinetDAO;
import org.xiaoheshan.hallo.boxing.client.dal.GoodDAO;
import org.xiaoheshan.hallo.boxing.client.dal.OrderDAO;
import org.xiaoheshan.hallo.boxing.client.state.StateHolder;
import org.xiaoheshan.hallo.boxing.client.state.SystemConstant;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodActivity extends AppCompatActivity {

    @BindView(R.id.parent_layout)
    View parentLayout;

    @BindView(R.id.good_image_banner)
    Banner banner;
    @BindView(R.id.name_text_view)
    TextView nameTextView;
    @BindView(R.id.price_text_view)
    TextView priceTextView;
    @BindView(R.id.rent_info_text_view)
    TextView rentInfoTextView;
    @BindView(R.id.desc_text_view)
    TextView descTextView;
    @BindView(R.id.good_control_button)
    Button goodControlButton;

    ImageView remoteImageView;

    public static final String MODE_NAME = "mode";
    public static final String GOOD_ID_NAME = "good-id";

    public static final int RENT_MODE = 0x943;
    public static final int LEASE_MODE = 0x419;

    private GoodDAO goodDAO = HttpClient.get().getDAO(GoodDAO.class);
    private OrderDAO orderDAO = HttpClient.get().getDAO(OrderDAO.class);
    private CabinetDAO cabinetDAO = HttpClient.get().getDAO(CabinetDAO.class);

    private int mode;
    private int goodId;
    private GoodDO goodDO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mode = intent.getIntExtra(MODE_NAME, RENT_MODE);
        goodId = intent.getIntExtra(GOOD_ID_NAME, -1);
        remoteImageView = new ImageView(this);
        remoteImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        remoteImageView.setPadding(240, 240, 240, 240);
        switch (mode) {
            case RENT_MODE:
                initRentMode();
                break;
            case LEASE_MODE:
                initLeaseMode();
                break;
        }
        initView();
    }

    private void initView() {
        banner.setImageLoader(new GoodImageLoader());
        if (goodId == -1) {
            return;
        }
        Call<RestResult<GoodDO>> call = goodDAO.getById(goodId);
        call.enqueue(new Callback<RestResult<GoodDO>>() {
            @Override
            public void onResponse(Call<RestResult<GoodDO>> call, Response<RestResult<GoodDO>> response) {
                RestResult<GoodDO> result = response.body();
                if (RestRetCodeEnum.SUCCESS.is(result.getCode())) {
                    goodDO = result.getData();
                    nameTextView.setText(goodDO.getName());
                    priceTextView.setText("出租 ￥" + goodDO.getRentPrice() + "  |  " + "市场 ￥" + goodDO.getMarketPrice());
                    rentInfoTextView.setText("已出租 " +goodDO.getRentNum() + " 次");
                    descTextView.setText(goodDO.getDesc());
                    String[] images = goodDO.getGallery().split(";");
                    banner.setImages(Arrays.asList(images));
                    banner.start();
                } else {
                    Snackbar.make(parentLayout, result.getMsg(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RestResult<GoodDO>> call, Throwable t) {
                Snackbar.make(parentLayout, "获取商品详情失败", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void initRentMode() {
        goodControlButton.setText("租借");
        goodControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(GoodActivity.this)
                        .setMessage("正在租借...")
                        .setCancelable(false)
                        .create();
                dialog.show();
                OrderDO orderDO = new OrderDO();
                orderDO.setFromUserId(StateHolder.getUserDO().getId());
                orderDO.setGoodId(goodId);
                orderDO.setToUserId(goodDO.getUserId());
                orderDO.setDeliveryCabinetId(1);
                Call<RestResult<OrderDO>> call = orderDAO.create(orderDO);
                call.enqueue(new Callback<RestResult<OrderDO>>() {
                    @Override
                    public void onResponse(Call<RestResult<OrderDO>> call, Response<RestResult<OrderDO>> response) {
                        dialog.dismiss();
                        openDoor(1);
                    }

                    @Override
                    public void onFailure(Call<RestResult<OrderDO>> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void openDoor(final Integer cabinetId) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("正在开门...")
                .setCancelable(false)
                .create();
        dialog.show();
        Call<RestResult<Void>> call = cabinetDAO.openDoor(cabinetId);
        call.enqueue(new Callback<RestResult<Void>>() {
            @Override
            public void onResponse(Call<RestResult<Void>> call, Response<RestResult<Void>> response) {
                dialog.dismiss();
                new AlertDialog.Builder(GoodActivity.this)
                        .setMessage("请取走商品后, 点击确认然后关闭柜门.")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                closeDoor(cabinetId);
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

    private void closeDoor(final Integer cabinetId) {
        Call<RestResult<Void>> call = cabinetDAO.closeDoor(cabinetId);
        call.enqueue(new Callback<RestResult<Void>>() {
            @Override
            public void onResponse(Call<RestResult<Void>> call, Response<RestResult<Void>> response) {
            }

            @Override
            public void onFailure(Call<RestResult<Void>> call, Throwable t) {
            }
        });
    }

    private void initLeaseMode() {
        goodControlButton.setText("查看照片");
        goodControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(GoodActivity.this)
                        .setMessage("正在执行远程拍照命令...")
                        .setCancelable(false)
                        .create();
                dialog.show();
                Call<RestResult<Integer>> call = cabinetDAO.takePhoto(1, StateHolder.getUserDO().getId());
                call.enqueue(new Callback<RestResult<Integer>>() {
                    @Override
                    public void onResponse(Call<RestResult<Integer>> call, Response<RestResult<Integer>> response) {
                        dialog.dismiss();
                        RestResult<Integer> result = response.body();
                        if (RestRetCodeEnum.SUCCESS.is(result.getCode())) {
                            Picasso.get().load(SystemConstant.IMAGE_URL_PREFIX + result.getData()).into(remoteImageView);
                            new AlertDialog.Builder(GoodActivity.this)
                                    .setView(remoteImageView)
                                    .create()
                                    .show();
                        } else {
                            Snackbar.make(parentLayout, result.getMsg(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RestResult<Integer>> call, Throwable t) {
                        dialog.dismiss();
                        Snackbar.make(parentLayout, "获取照片失败", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private static class GoodImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object o, ImageView imageView) {
            if (o instanceof String) {
                Picasso.get().load(SystemConstant.IMAGE_URL_PREFIX + o)
                        .into(imageView);
            }
        }
    }
}
