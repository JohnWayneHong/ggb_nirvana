package com.jgw.common_library.base.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.jgw.common_library.R;
import com.jgw.common_library.base.viewmodel.BaseViewModel;
import com.jgw.common_library.utils.ClassUtils;
import com.jgw.common_library.utils.click_utils.ClickUtils;
import com.jgw.common_library.utils.click_utils.listener.OnItemSingleClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnSingleClickListener;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by xsw on 2016/10/26.
 */
public abstract class BaseFragment<VM extends BaseViewModel, SV extends ViewDataBinding>
        extends Fragment implements OnSingleClickListener, OnItemSingleClickListener {

    /**
     * activity对象上下文
     */
    public Context context;
    private View mView;

    public VM mViewModel;
    public SV mBindingView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();

//        mBindingView = DataBindingUtil.inflate(getLayoutInflater(), initResId(), container, false);
        mBindingView = initViewBinding();

        mBindingView.setLifecycleOwner(this);
        mView = mBindingView.getRoot();
        initView();
        initViewModel();
        initLiveData();
        initFragmentData();
        initListener();
        return mView;
    }

    private SV initViewBinding() {
        Class<SV> clazz = ClassUtils.getViewBinding(this);
        SV viewBinding = null;
        //noinspection TryWithIdenticalCatches
        try {
            Method inflate = clazz.getMethod("inflate", LayoutInflater.class);
            viewBinding = (SV) inflate.invoke(null, getLayoutInflater());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return viewBinding;
    }

    /**
     * 初始化ViewModel
     */
    private void initViewModel() {
        Class<VM> viewModelClass = ClassUtils.getViewModel(this);
        if (viewModelClass != null) {
            ViewModelProvider viewModelProvider = new ViewModelProvider(this);
            this.mViewModel = viewModelProvider.get(viewModelClass);
        }

    }

    public <T extends View> T findViewById(int resId) {
        return mView.findViewById(resId);
    }

    @Deprecated
    public int initResId() {
        return 0;
    }

    protected abstract void initView();

    protected abstract void initFragmentData();

    public void initLiveData() {
    }

    protected void initListener() {
        View back = findViewById(R.id.iv_toolbar_back);
        if (back != null) {
            ClickUtils.register(this)
                    .addOnClickListener()
                    .addView(back)
                    .submit();
        }
        View tv_right = findViewById(R.id.tv_toolbar_right);
        if (tv_right != null) {
            ClickUtils.register(this)
                    .addOnClickListener()
                    .addView(tv_right)
                    .submit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onStop() {
        super.onStop();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemClick(View view, int groupPosition, int subPosition) {

    }

    @Override
    public void onItemClick(View view, int firstPosition, int secondPosition, int thirdPosition) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_toolbar_back) {
            onBackPressed();
        } else if (id == R.id.tv_toolbar_right) {
            clickRight();
        }
    }

    public void onBackPressed() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            //noinspection rawtypes
            ((BaseActivity) activity).onBackPressed();
        }
    }

    public void clickRight() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            //noinspection rawtypes
            ((BaseActivity) activity).clickRight();
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            window.setAttributes(lp);
        }
    }

    public void showLoadingDialog() {
        showLoadingDialog(-1);
    }

    public void showLoadingDialog(int progressType) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            //noinspection rawtypes
            ((BaseActivity) activity).showLoadingDialog(progressType);
        }
    }

    public void dismissLoadingDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            //noinspection rawtypes
            ((BaseActivity) activity).dismissLoadingDialog();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent != null) {
            super.startActivity(intent);
            if (getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.custom_slide_in_left, R.anim.custom_slide_out_left);
            }
        }
    }

    protected boolean isActivityNotFinished() {
        if (getActivity() == null) {
            return false;
        } else {
            return !getActivity().isFinishing() && !getActivity().isDestroyed();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }
}
