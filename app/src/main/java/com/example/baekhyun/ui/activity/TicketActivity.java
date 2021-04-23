package com.example.baekhyun.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baekhyun.R;
import com.example.baekhyun.View.ITicketCallback;
import com.example.baekhyun.model.domain.TicketResult;
import com.example.baekhyun.presenter.ITicketPresenter;
import com.example.baekhyun.ui.LoadingView;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.PresenterManager;
import com.example.baekhyun.utils.ToastUtil;

public class TicketActivity extends AppCompatActivity implements ITicketCallback {
    private ITicketPresenter mITicketPresenter;
    private static boolean HaveTb = false;
    private ImageView mImageView;
    private EditText mEditText;
    private TextView mTextView;
    private ImageView mback;
    private LoadingView mLoadingView;
    private TextView mretry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        initPresenter();
        initView();
        initEvent();
    }

    private void initView() {
        mImageView = findViewById(R.id.ticket_cover);
        mEditText = findViewById(R.id.ticket_code);
        mTextView = findViewById(R.id.ticket_copy_or_open_btn);
        mback = findViewById(R.id.ticket_back_press);
        mLoadingView = findViewById(R.id.ticket_cover_loading);
        mretry = findViewById(R.id.ticket_load_retry);
        mTextView.setText(HaveTb ? "领券吧" : "复制口令");
    }

    private void initEvent() {
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString().trim();
                LogUtils.d(this, text);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", text);
                cm.setPrimaryClip(clipData);
                if (HaveTb) {
                    Intent taobao = new Intent();
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobao.setComponent(componentName);
                    startActivity(taobao);
                } else {
                    ToastUtil.getToast("口令复制成功（๑>\u0602<๑）");
                }
            }
        });
    }

    public void initPresenter() {
        mITicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mITicketPresenter != null) {
            mITicketPresenter.registerViewCallback(this);
            PackageManager pm = getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
                HaveTb = packageInfo != null;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                HaveTb = false;
            }
        }
    }

    @Override
    public void TiacketLoaded(String cover, TicketResult result) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        LogUtils.d(TicketActivity.class, cover);
        if (mImageView != null && !TextUtils.isEmpty(cover)) {
            Glide.with(this).load(cover).into(mImageView);
        }
        if (mEditText != null && result.getData().getTbk_tpwd_create_response().getData().getModel() != null) {
            LogUtils.d(TicketActivity.this, result.getData().getTbk_tpwd_create_response().getData().getModel());
            mEditText.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());

        }
    }

    @Override
    public void onNetworkError() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        if (mretry != null) {
            mretry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (mretry != null) {
            mretry.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmpty() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mITicketPresenter != null) {
            mITicketPresenter.unregisterViewCallback(this);
        }
    }
}
