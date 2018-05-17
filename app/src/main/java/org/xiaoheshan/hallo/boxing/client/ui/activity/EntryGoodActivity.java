package org.xiaoheshan.hallo.boxing.client.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.xiaoheshan.hallo.boxing.client.R;
import org.xiaoheshan.hallo.boxing.client.bean.GoodDO;
import org.xiaoheshan.hallo.boxing.client.common.rest.RestResult;
import org.xiaoheshan.hallo.boxing.client.dal.CabinetDAO;
import org.xiaoheshan.hallo.boxing.client.dal.ImageDAO;
import org.xiaoheshan.hallo.boxing.client.state.StateHolder;
import org.xiaoheshan.hallo.boxing.client.support.http.HttpClient;
import org.xiaoheshan.hallo.boxing.client.ui.adapter.ImageAdapter;
import org.xiaoheshan.hallo.boxing.client.ui.util.CameraUtils;
import org.xiaoheshan.hallo.boxing.client.ui.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryGoodActivity extends AppCompatActivity {

    @BindView(R.id.entry_text_view)
    TextView entryTextView;

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_image_button)
    FloatingActionButton addImageButton;

    @BindView(R.id.good_name_edit_text)
    AppCompatEditText goodNameEditText;
    @BindView(R.id.good_cat1_spinner)
    AppCompatSpinner goodCat1Spinner;
    @BindView(R.id.good_cat2_spinner)
    AppCompatSpinner goodCat2Spinner;
    @BindView(R.id.good_cat3_spinner)
    AppCompatSpinner goodCat3Spinner;

    @BindView(R.id.rent_price_edit_text)
    AppCompatEditText rentPriceEditText;
    @BindView(R.id.market_price_edit_text)
    AppCompatEditText marketPriceEditText;
    @BindView(R.id.good_desc_edit_text)
    AppCompatEditText goodDescEditText;

    private ImageAdapter adapter;

    private CabinetDAO cabinetDAO = HttpClient.get().getDAO(CabinetDAO.class);
    private ImageDAO imageDAO = HttpClient.get().getDAO(ImageDAO.class);

    private final static int REQUEST_CODE = 0x846;

    private Uri imageUriHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_good);
        ButterKnife.bind(this);
        adapter = ImageAdapter.newInstance(this, new ArrayList<Uri>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.add_image_button)
    void addImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriHolder = Uri.fromFile(CameraUtils.generateImageFile()));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.entry_text_view)
    void entry() {
        openDoor(1);
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
                new AlertDialog.Builder(EntryGoodActivity.this)
                        .setMessage("将商品放入柜后，关闭柜门，点击确认进行录入。")
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
                getNfcCode(cabinetId);
            }

            @Override
            public void onFailure(Call<RestResult<Void>> call, Throwable t) {

            }
        });
    }

    private void getNfcCode(final Integer cabinetId) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("正在获取唯一标识码...")
                .setCancelable(false)
                .create();
        dialog.show();
        Call<RestResult<String>> call = cabinetDAO.getNfcCode(cabinetId);
        call.enqueue(new Callback<RestResult<String>>() {
            @Override
            public void onResponse(Call<RestResult<String>> call, Response<RestResult<String>> response) {
                dialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(EntryGoodActivity.this)
                        .setMessage("正在录入商品，请等待...")
                        .setCancelable(false)
                        .create();
                alertDialog.show();
                new EntryGoodAsyncTask(cabinetId, response.body().getData(), alertDialog).execute();
            }

            @Override
            public void onFailure(Call<RestResult<String>> call, Throwable t) {

            }
        });
    }

    private Integer uploadImage(Uri uri) throws IOException {
        File file = FileUtils.fromUri(uri, getContentResolver());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody);
        Call<RestResult<Integer>> call = imageDAO.upload(builder.build().parts());
        Response<RestResult<Integer>> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getData();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE:
                if (imageUriHolder != null) {
                    adapter.refreshData(imageUriHolder);
                    imageUriHolder = null;
                }
                break;
        }
    }

    private class EntryGoodAsyncTask extends AsyncTask<Void, Void, RestResult> {

        private Integer cabinetId;
        private String nfcCode;
        private AlertDialog dialog;

        public EntryGoodAsyncTask(Integer cabinetId, String nfcCode, AlertDialog dialog) {
            this.cabinetId = cabinetId;
            this.nfcCode = nfcCode;
            this.dialog = dialog;
        }

        @Override
        protected RestResult doInBackground(Void... voids) {
            List<Uri> data = adapter.getData();
            StringBuilder builder = new StringBuilder();
            List<Integer> imageIds = new ArrayList<>();
            for (Uri uri : data) {
                Integer imageId = null;
                try {
                    imageId = uploadImage(uri);
                } catch (IOException e) {
                    Log.e("录入商品发生错误", e.getMessage());
                }
                imageIds.add(imageId);
                builder.append(imageId).append(';');
            }
            GoodDO goodDO = new GoodDO();
            goodDO.setUserId(StateHolder.getUserDO().getId());
            goodDO.setDesc(goodDescEditText.getText().toString());
            goodDO.setImg(imageIds.get(0).toString());
            goodDO.setGallery(builder.toString());
            goodDO.setName(goodNameEditText.getText().toString());
            goodDO.setMarketPrice(Integer.parseInt(marketPriceEditText.getText().toString()));
            goodDO.setRentPrice(Integer.parseInt(rentPriceEditText.getText().toString()));

            Call<RestResult<Void>> call = cabinetDAO.entryGood(cabinetId, nfcCode, goodDO);
            Response<RestResult<Void>> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
            }
            return response.body();
        }

        @Override
        protected void onPostExecute(RestResult result) {
            super.onPostExecute(result);
            if (result != null) {
                EntryGoodActivity.this.finish();
            }
            dialog.dismiss();
        }
    }
}
