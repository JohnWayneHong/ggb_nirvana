package com.ggb.nirvanaclub.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ggb.nirvanaclub.R;


public class HomeSelectTitle extends LinearLayout implements View.OnClickListener{

    private RelativeLayout llWalk,llCar;
    private TextView tvWalk,tvCar;
    private View vWalk,vCar;

    public HomeSelectTitle(Context context) {
        this(context,null);
    }

    public HomeSelectTitle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HomeSelectTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = View.inflate(context, R.layout.title_home_select, this);

        llWalk = view.findViewById(R.id.rl_walk);
        llCar = view.findViewById(R.id.rl_car);

        tvWalk = view.findViewById(R.id.tv_walk);
        tvCar = view.findViewById(R.id.tv_car);

        vWalk = view.findViewById(R.id.v_walk);
        vCar = view.findViewById(R.id.v_car);

        llWalk.setOnClickListener(this);
        llCar.setOnClickListener(this);

        initialize();
    }

    private void initialize(){
        tvWalk.setTextColor(getResources().getColor(R.color.main_color));
        tvCar.setTextColor(getResources().getColor(R.color.black));

        vWalk.setVisibility(View.VISIBLE);
        vCar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_walk:
                tvWalk.setTextColor(getResources().getColor(R.color.main_color));
                tvCar.setTextColor(getResources().getColor(R.color.title_main_color));
                vWalk.setVisibility(View.VISIBLE);
                vCar.setVisibility(View.GONE);
                mOnHomeTitleSelectListener.onHomeTitleSelect(0);
                break;
            case R.id.rl_car:
                tvWalk.setTextColor(getResources().getColor(R.color.title_main_color));
                tvCar.setTextColor(getResources().getColor(R.color.second_color));
                vWalk.setVisibility(View.GONE);
                vCar.setVisibility(View.VISIBLE);
                mOnHomeTitleSelectListener.onHomeTitleSelect(1);
                break;
        }
    }



    public interface OnHomeTitleSelectListener{
        void onHomeTitleSelect(int position);
    }

    private OnHomeTitleSelectListener mOnHomeTitleSelectListener;

    public void setOnSortSelectListener(OnHomeTitleSelectListener mOnHomeTitleSelectListener) {
        this.mOnHomeTitleSelectListener = mOnHomeTitleSelectListener;
    }
}
