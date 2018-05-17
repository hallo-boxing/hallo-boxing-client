package org.xiaoheshan.hallo.boxing.client.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.UserDO;
import org.xiaoheshan.hallo.boxing.client.state.StateHolder;
import org.xiaoheshan.hallo.boxing.client.ui.activity.EntryGoodActivity;
import org.xiaoheshan.hallo.boxing.client.ui.activity.GoodListActivity;
import org.xiaoheshan.hallo.boxing.client.ui.activity.LoginActivity;
import org.xiaoheshan.hallo.boxing.client.ui.activity.RentRecordActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {

    @BindView(R.id.parent_layout)
    View parentLayout;

    @BindView(R.id.login_button)
    Button loginButton;

    @BindView(R.id.user_info_layout)
    LinearLayout userInfoLayout;
    @BindView(R.id.user_avatar_image_view)
    ImageView userAvatarImageView;
    @BindView(R.id.user_name_text_view)
    TextView userNameTextView;
    @BindView(R.id.user_mobile_text_view)
    TextView userMobileTextView;

    @BindView(R.id.rent_text_view)
    TextView rentTextView;
    @BindView(R.id.rent_record_text_view)
    TextView rentRecordTextView;
    @BindView(R.id.my_good_text_view)
    TextView myGoodTextView;
    @BindView(R.id.collection_text_view)
    TextView collectionTextView;
    @BindView(R.id.custom_service_text_view)
    TextView customServiceTextView;

    @BindView(R.id.logout_container)
    CardView logoutContainer;
    @BindView(R.id.logout_text_view)
    TextView logoutTextView;

    private static final int CAPTURE_REQUEST_CODE = 0x424;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkedLogin();
        ZXingLibrary.initDisplayOpinion(getContext());

    }

    @OnClick(R.id.login_button)
    void onLoginButtonClick() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, LoginActivity.REQUEST_CODE);
    }

    @OnClick(R.id.rent_text_view)
    void onRentButtonClick() {
        if (StateHolder.isIsLogin()) {
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            startActivityForResult(intent, CAPTURE_REQUEST_CODE);
        }
    }

    @OnClick(R.id.rent_record_text_view)
    void onRentRecordButtonClick() {
        if (StateHolder.isIsLogin()) {
            Intent intent = new Intent(getContext(), RentRecordActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.my_good_text_view)
    void onMyGoodClick() {
        if (StateHolder.isIsLogin()) {
            Intent intent = new Intent(getContext(), GoodListActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.logout_text_view)
    void logout() {
        StateHolder.setIsLogin(false);
        StateHolder.setUserDO(null);
        checkedLogin();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LoginActivity.REQUEST_CODE:
                checkedLogin();
                break;
            case CAPTURE_REQUEST_CODE:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        startActivity(new Intent(getContext(), EntryGoodActivity.class));
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                    }
                }
                break;
        }
    }

    private void checkedLogin() {
        if (StateHolder.isIsLogin()) {
            loginButton.setVisibility(View.GONE);
            userInfoLayout.setVisibility(View.VISIBLE);
            UserDO userDO = StateHolder.getUserDO();
            userNameTextView.setText(userDO.getName());
            userMobileTextView.setText(userDO.getPhone());

            logoutContainer.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.VISIBLE);
            userInfoLayout.setVisibility(View.GONE);

            logoutContainer.setVisibility(View.GONE);
        }
    }
}
