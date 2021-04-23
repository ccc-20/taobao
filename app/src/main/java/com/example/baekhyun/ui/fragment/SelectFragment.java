package com.example.baekhyun.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baekhyun.R;
import com.example.baekhyun.View.ISelelctCallback;
import com.example.baekhyun.View.ITicketCallback;
import com.example.baekhyun.base.BaseApplication;
import com.example.baekhyun.base.BaseFragment;
import com.example.baekhyun.model.Api;
import com.example.baekhyun.model.domain.SelectCategory;
import com.example.baekhyun.model.domain.Selectid;
import com.example.baekhyun.model.domain.TicketParams;
import com.example.baekhyun.model.domain.TicketResult;
import com.example.baekhyun.presenter.ISelectPagePresenter;
import com.example.baekhyun.presenter.Impl.TicketPresenterImpl;
import com.example.baekhyun.ui.adapter.LeftAdapter;
import com.example.baekhyun.ui.adapter.RightAdapter;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.PresenterManager;
import com.example.baekhyun.utils.RetrofitManager;
import com.example.baekhyun.utils.ToastUtil;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectFragment extends BaseFragment implements ISelelctCallback, LeftAdapter.onLeftListener, RightAdapter.onItemClick, ITicketCallback {

    private ISelectPagePresenter mSelectPresenter;
    private RecyclerView mleft;
    private RecyclerView mright;
    private LeftAdapter mMleftAdapter;
    private RightAdapter mRightAdapter;
    private TicketResult mBody;
    private TicketParams mTicketParams;
    private LinearLayout mLinearLayout;
    private TextView title;

    @Override
    public int getRootResId() {
        return R.layout.fragment_select;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_other_title, container, false);
    }

    @Override
    protected void initView(View view) {
        title = view.findViewById(R.id.text_title);
        title.setText("精选宝贝");
        mleft = view.findViewById(R.id.left_category_list);
        mright = view.findViewById(R.id.right_content_list);
        mleft.setLayoutManager(new LinearLayoutManager(getContext()));
        mMleftAdapter = new LeftAdapter();
        mleft.setAdapter(mMleftAdapter);
        mright.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new RightAdapter();
        mright.setAdapter(mRightAdapter);
        mright.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 10;
                outRect.bottom = 10;
                outRect.left = 10;
                outRect.right = 5;
            }
        });
        mLinearLayout = view.findViewById(R.id.network_error);
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mMleftAdapter.onLeftClick(this);
        mRightAdapter.setListener(this);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectPresenter = PresenterManager.getInstance().getSelectPresenter();
        mSelectPresenter.registerViewCallback(this);
        mSelectPresenter.getCategories();
    }

    @Override
    protected void release() {
        super.release();
        mSelectPresenter.unregisterViewCallback(this);
    }

    @Override
    public void onCategoriesLoaded(Selectid selectid) {
        setUpState(State.SUCCESS);
        mMleftAdapter.getDate(selectid);
    }

    @Override
    public void onContentByidLoaded(SelectCategory content) {
        mRightAdapter.setDate(content);
        mright.scrollToPosition(0);
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPresenter != null) {
                    mSelectPresenter.getCategories();
                }
            }
        });
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onleftClick(Selectid.DataBean item) {
        mSelectPresenter.getContentByid(item);
    }

    @Override
    public void onTicketClick(SelectCategory.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean) {
        String url = "https:" + mapDataBean.getCoupon_click_url();
        //String detailurl="https:"+mapDataBean.getClick_url();
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        mTicketParams = new TicketParams(url, mapDataBean.getTitle());
        Call<TicketResult> task = api.getTicket(mTicketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    mBody = response.body();
                    TiacketLoaded("", mBody);
                } else {
                    //ToastUtil.getToast("哎呀 这个没有优惠券哦");
                    onNetworkError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                onNetworkError();
            }
        });
    }


    @Override
    public void TiacketLoaded(String cover, TicketResult result) {
        ClipboardManager cm = (ClipboardManager) BaseApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", result.getData().getTbk_tpwd_create_response().getData().getModel());
        //LogUtils.d(this,url);
        cm.setPrimaryClip(clipData);
        Intent taobao = new Intent();
        ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
        taobao.setComponent(componentName);
        startActivity(taobao);
    }
}
