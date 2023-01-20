package com.ggb.nirvanaclub.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ggb.nirvanaclub.R;


public class ImageLoaderUtil {

    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.logo))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(imageView);
    }


    public void displayHeadImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.logo))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(imageView);
    }

    public void displayStoreHeadImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.logo))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(imageView);
    }

    public void displayCommodityInfoImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.logo))

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if(imageView==null) {
                            return false;
                        }

                        ViewGroup.LayoutParams params =imageView.getLayoutParams();
                        int vw = ScreenUtils.getScreenWidth(context);
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.width = vw;
                        params.height = vh +imageView.getPaddingTop() +imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .into(imageView);
    }

}