package com.example.baekhyun.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baekhyun.R;
import com.example.baekhyun.View.ISepcialCallback;
import com.example.baekhyun.base.BaseApplication;
import com.example.baekhyun.base.BaseFragment;
import com.example.baekhyun.model.Api;
import com.example.baekhyun.model.domain.SpecialContent;
import com.example.baekhyun.model.domain.TicketParams;
import com.example.baekhyun.model.domain.TicketResult;
import com.example.baekhyun.presenter.ISepicialPresenter;
import com.example.baekhyun.ui.adapter.SpecialAdapter;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.PresenterManager;
import com.example.baekhyun.utils.RetrofitManager;
import com.example.baekhyun.utils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RedFragment extends BaseFragment implements ISepcialCallback, SpecialAdapter.onSpecialContent {

    private ISepicialPresenter mSpecialPresenter;
    private RecyclerView mRecyclerView;
    private SpecialAdapter mSpecialAdapter;
    private TwinklingRefreshLayout tk;
    private TicketParams mTicketParams=null;
    private TicketResult mBody;
    private LinearLayout mLinearLayout;

    @Override
    public int getRootResId() {
        return R.layout.fragment_red;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_other_title,container,false);
    }

    @Override
    protected void initView(View view) {
        mRecyclerView=view.findViewById(R.id.special_content);
        tk=view.findViewById(R.id.special_reflash);
        tk.setEnableRefresh(false);
        tk.setEnableLoadmore(true);
        mSpecialAdapter = new SpecialAdapter();
        GridLayoutManager grid=new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(grid);
        mRecyclerView.setAdapter(mSpecialAdapter);
        mLinearLayout=view.findViewById(R.id.network_error);
        //LogUtils.d(this,mRecyclerView.toString());
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top=8;
                outRect.bottom=8;
                outRect.left=8;
                outRect.right=8 ;
            }
        });
       // setUpState(State.SUCCESS);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tk.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (mSpecialPresenter != null) {
                    mSpecialPresenter.loadMore();
                }
            }
        });
        mSpecialAdapter.setSpecialListener(this);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpecialPresenter.reLoad();
            }
        });
    }

    @Override
    protected void initPresenter() {
        mSpecialPresenter = PresenterManager.getInstance().getSpecialPresenter();
        mSpecialPresenter.registerViewCallback(this);
        mSpecialPresenter.onSellContent();
    }

    @Override
    public void SpecialContentLoad(SpecialContent content) {
        setUpState(State.SUCCESS);
        mSpecialAdapter.setDate(content);
    }

    @Override
    public void LoadMore(SpecialContent content) {
        tk.finishLoadmore();
        mSpecialAdapter.onMoreLoaded(content);
    }

    @Override
    public void onLoadMoreerror() {
        tk.finishLoadmore();
        ToastUtil.getToast("网络异常，请稍后重试~");
    }

    @Override
    public void onMoreEmpty() {
        tk.finishLoadmore();
        ToastUtil.getToast("没有更多内容啦~");
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    protected void release() {
        super.release();
        if(mSpecialPresenter!=null)
        mSpecialPresenter.unregisterViewCallback(this);
    }

    @Override
    public void Specialclick(SpecialContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean) {
        String url="https:"+mapDataBean.getCoupon_click_url();
        //String detailurl="https:"+mapDataBean.getClick_url();
        Retrofit retrofit= RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        mTicketParams = new TicketParams(url, mapDataBean.getTitle());
        Call<TicketResult> task = api.getTicket(mTicketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code=response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    mBody = response.body();
                    TiacketLoaded(mBody);
                }else{
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

    public void TiacketLoaded(TicketResult result) {
        ClipboardManager cm = (ClipboardManager) BaseApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", result.getData().getTbk_tpwd_create_response().getData().getModel());
        //LogUtils.d(this,url);
        cm.setPrimaryClip(clipData);
        Intent taobao=new Intent();
        ComponentName componentName=new ComponentName("com.taobao.taobao","com.taobao.tao.TBMainActivity");
        taobao.setComponent(componentName);
        startActivity(taobao);
    }
}
