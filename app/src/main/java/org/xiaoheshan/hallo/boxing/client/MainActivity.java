package org.xiaoheshan.hallo.boxing.client;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.xiaoheshan.hallo.boxing.client.dal.CabinetDAO;
import org.xiaoheshan.hallo.boxing.client.dal.GoodDAO;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;
import org.xiaoheshan.hallo.boxing.client.ui.fragment.ExploreFragment;
import org.xiaoheshan.hallo.boxing.client.ui.fragment.MyFragment;
import org.xiaoheshan.hallo.boxing.client.ui.fragment.OrderFragment;
import org.xiaoheshan.hallo.boxing.client.ui.util.DrawableUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.nav_explore)
    TextView navFindButton;
    @BindView(R.id.nav_order)
    TextView navOrderButton;
    @BindView(R.id.nav_my)
    TextView navMyButton;

    private Fragment exploreFragment;
    private Fragment orderFragment;
    private Fragment myFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragments();
        resetMyButton();
    }

    @OnClick(R.id.nav_explore)
    void onExploreButtonClick() {
        replaceFragment(exploreFragment);
        resetButtons();
        navFindButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        navFindButton.setCompoundDrawables(null, DrawableUtils.getDrawable(this, R.drawable.ic_pets_blue_200_24dp), null, null);
    }

    @OnClick(R.id.nav_order)
    void onOrderButtonClick() {
        replaceFragment(orderFragment);
        resetButtons();
        navOrderButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        navOrderButton.setCompoundDrawables(null, DrawableUtils.getDrawable(this, R.drawable.ic_assignment_blue_200_24dp), null, null);
    }

    @OnClick(R.id.nav_my)
    void onMyButtonClick() {
        replaceFragment(myFragment);
        resetButtons();
        navMyButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        navMyButton.setCompoundDrawables(null, DrawableUtils.getDrawable(this, R.drawable.ic_account_circle_blue_200_24dp), null, null);
    }

    private void resetButtons() {
        resetFindButton();
        resetOrderButton();
        resetMyButton();
    }

    private void resetFindButton() {
        navFindButton.setTextColor(Color.GRAY);
        navFindButton.setCompoundDrawables(null, DrawableUtils.getDrawable(this, R.drawable.ic_pets_grey_500_24dp), null, null);
    }

    private void resetOrderButton() {

        navOrderButton.setTextColor(Color.GRAY);
        navOrderButton.setCompoundDrawables(null, DrawableUtils.getDrawable(this, R.drawable.ic_assignment_grey_500_24dp), null, null);
    }

    private void resetMyButton() {
        navMyButton.setTextColor(Color.GRAY);
        navMyButton.setCompoundDrawables(null, DrawableUtils.getDrawable(this, R.drawable.ic_account_circle_grey_500_24dp), null, null);
    }

    private void initFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        exploreFragment = new ExploreFragment();
        orderFragment = new OrderFragment();
        myFragment = new MyFragment();
        currentFragment = exploreFragment;
        transaction.add(R.id.nav_page_frame_layout, exploreFragment);
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(currentFragment);
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.nav_page_frame_layout, fragment);
        }
        currentFragment = fragment;
        transaction.commit();
    }
}
