package com.jgw.common_library.base.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.jgw.common_library.R;
import com.jgw.common_library.base.viewmodel.BaseViewModel;
import com.jgw.common_library.utils.ClassUtils;
import com.jgw.common_library.utils.LogUtils;
import com.jgw.common_library.utils.click_utils.ClickUtils;
import com.jgw.common_library.utils.click_utils.listener.OnItemSingleClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnSingleClickListener;
import com.jgw.common_library.widget.loadingDialog.CircularProgressDialog;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by XiongShaoWu
 * on 2019/9/10
 */
@SuppressWarnings({"rawtypes", "UnnecessaryLocalVariable"})
public abstract class BaseActivity<VM extends BaseViewModel, SV extends ViewDataBinding>
        extends AppCompatActivity implements OnSingleClickListener, OnItemSingleClickListener {
    // ViewModel
    protected VM mViewModel;
    protected SV mBindingView;

    public FragmentManager fm;
    private static float xMultiple;
    private CircularProgressDialog mDialog;
    private boolean isShowing;
    public static int mPhoneWidth;
    public static int mPhoneHeight;
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            getSaveInstanceState(savedInstanceState);
        }
        context=this;
        fm = getSupportFragmentManager();
//        mBindingView = DataBindingUtil.inflate(getLayoutInflater(), initResId(), null, false);
        mBindingView = initViewBinding();
        setContentView(mBindingView.getRoot());
        mBindingView.setLifecycleOwner(this);
        initView();
        initViewModel();
        //需要提前观察数据才能在设置数据时正确触发
        initLiveData();
        initData();
        initListener();

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        setXMultiple(point.x / 360f);
        mPhoneWidth = point.x;
        mPhoneHeight = point.y;
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        MobclickAgent.onPageEnd(this.getClass().getName());
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

    public void getSaveInstanceState(@NonNull Bundle savedInstanceState) {
    }

    protected abstract void initView();

    protected abstract void initData();

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

    public static Context getContext() {
        return context;
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            TextView textView = getTitleView();
            if (textView != null) {
                textView.setText(title);
            }
        }
    }

    @Nullable
    public TextView getTitleView() {
        TextView editText = findViewById(R.id.tv_toolbar_title);
        return editText;
    }

    public void setRightVisibility(int visibility) {
        TextView tv_right = findViewById(R.id.tv_toolbar_right);
        if (tv_right != null) {
            tv_right.setVisibility(visibility);
        }
    }

    public void setRight(String right) {
        if (!TextUtils.isEmpty(right)) {
            TextView tv_right = findViewById(R.id.tv_toolbar_right);
            if (tv_right != null) {
                tv_right.setText(right);
            }
        }
    }

    @Nullable
    public EditText getSearchView() {
        EditText editText = findViewById(R.id.et_search);
        return editText;
    }

    //点击别处隐藏软件盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (v != null && getCurrentFocus().getWindowToken() != null) {
                    if (isShouldHideInput(v, ev)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }

                return super.dispatchTouchEvent(ev);
            }
        } catch (Exception e) {
            LogUtils.xswShowLog(e.toString());
        }

        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);

    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        onBackStack();
    }

    public void onBackStack() {
        if (fm.getBackStackEntryCount() <= 1) {
            finish();
            overridePendingTransition(R.anim.custom_slide_in_right, R.anim.custom_slide_out_right);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void addTransparentBackground() {
        backgroundAlpha(0.5f);
    }

    public void removeTransparentBackground() {
        backgroundAlpha(1.0f);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void showLoadingDialog() {
        showLoadingDialog(-1);
    }

    public CircularProgressDialog showLoadingDialog(int progressType) {
        if (mDialog != null && progressType != mDialog.getProgressType()) {
            dismissLoadingDialog();
        }
        if (mDialog == null) {
            mDialog = CircularProgressDialog.newInstance(this, progressType);
            mDialog.setCancelable(false);
            mDialog.setOnDismissListener(dialog -> dismissLoadingDialog());
            mDialog.setLoadProgressFinishListener(this::dismissLoadingDialog);
        }
        if (!mDialog.isShowing()) {
            isShowing = true;
            mDialog.show();
        }
        return mDialog;
    }

    public void dismissLoadingDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        isShowing = false;
        mDialog = null;
    }

    @Override
    public void startActivity(@Nullable Intent intent) {
        if (intent != null) {
            super.startActivity(intent);
            overridePendingTransition(R.anim.custom_slide_in_left, R.anim.custom_slide_out_left);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent != null) {
            super.startActivityForResult(intent, requestCode);
            overridePendingTransition(R.anim.custom_slide_in_left, R.anim.custom_slide_out_left);
        }
    }

    public boolean isActivityNotFinished() {
        return !isFinishing() && !isDestroyed();
    }

    public static boolean isActivityNotFinished(Context context) {
        if (context == null) {
            return false;
        }
        boolean b = context instanceof BaseActivity;
        return b && ((BaseActivity) context).isActivityNotFinished();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_toolbar_back) {
            onBackPressed();
        } else if (view.getId() == R.id.tv_toolbar_right) {
            clickRight();
        }
    }

    protected void clickRight() {
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

    public static float getXMultiple() {
        return xMultiple;
    }

    public static void setXMultiple(float xMultiple) {
        BaseActivity.xMultiple = xMultiple;
    }
}
