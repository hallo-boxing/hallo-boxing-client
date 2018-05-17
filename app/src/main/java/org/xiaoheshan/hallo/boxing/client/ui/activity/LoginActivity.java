package org.xiaoheshan.hallo.boxing.client.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.UserDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestRetCodeEnum;
import org.xiaoheshan.hallo.boxing.client.dal.UserDAO;
import org.xiaoheshan.hallo.boxing.client.state.StateHolder;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;
import org.xiaoheshan.hallo.boxing.client.ui.util.ViewInitUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.parent_layout)
    View parentLayout;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.username_edit_text)
    AppCompatEditText usernameEditText;
    @BindView(R.id.password_edit_text)
    AppCompatEditText passwordEditText;
    @BindView(R.id.login_button)
    Button loginButton;

    public static final int REQUEST_CODE = 0x567;

    private UserDAO userDAO = HttpClient.get().getDAO(UserDAO.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ViewInitUtils.initToolBar(this, toolbar);
    }

    @OnClick(R.id.login_button)
    void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Call<RestResult<UserDO>> call = userDAO.login(username, password);
        call.enqueue(new Callback<RestResult<UserDO>>() {
            @Override
            public void onResponse(Call<RestResult<UserDO>> call, Response<RestResult<UserDO>> response) {
                RestResult<UserDO> result = response.body();
                if (RestRetCodeEnum.SUCCESS.is(result.getCode())) {
                    StateHolder.setIsLogin(true);
                    StateHolder.setUserDO(result.getData());
                    LoginActivity.this.finish();
                } else {
                    Snackbar.make(parentLayout, result.getMsg(), Snackbar.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<RestResult<UserDO>> call, Throwable t) {
                Snackbar.make(parentLayout, "登录失败", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}

