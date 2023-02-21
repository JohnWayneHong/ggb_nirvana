package com.ggb.nirvanaclub.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.GLES10;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ggb.nirvanaclub.R;
import com.ggb.nirvanaclub.bean.ChatImageBean;

import java.io.ByteArrayOutputStream;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;


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


    public void displayHugeImage(Context context, Object path, ImageView imageView) {
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

                        //获取图片的宽高
                        int height = resource.getIntrinsicHeight();
                        int width = resource.getIntrinsicWidth();
                        //与限制值进行判断，重新设置宽高
                        if (height > getOpenglRenderLimitValue()) {
                            width = width * getOpenglRenderLimitValue() / height;
                            height = getOpenglRenderLimitValue();
                            //图片转bitmap
                            Bitmap bitmap = drawable2Bitmap(resource);
                            //根据新的宽高比进行图片压缩
                            Bitmap result = mixCompress(context,bitmap, null,height,width);
                            if (result != null) {
                                imageView.setImageBitmap(result);

                            }
                        }else {
                            //小于限制值，则直接显示
                            imageView.setImageDrawable(resource);

                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 聊天页面的加载图片用法
     * @param mActivity
     * @param url
     * @param placeholder
     * @param imageView
     */
    public void loadImage(Activity mActivity, String url, int placeholder, ImageView imageView) {
        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            return;
        }

        RequestOptions glideoptions = new RequestOptions()
                .centerCrop()
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(mActivity).load(url).apply(glideoptions).into(imageView);
    }




    public final static int IMAGE_MAX_SIZE_DP = 150;
    /**
     * 聊天图片的在页面的压缩 缩略图
     * @param context 上下文
     * @param largeWidth   图片原解析的宽度
     * @param largeHeight  图片原解析的高度
//     * @param thumbnailImageHeight 压缩后的高度
//     * @param thumbnailImageWidth 压缩后的宽度
     */
    public static ChatImageBean calculaThumhSize(Context context, int largeWidth, int largeHeight){

//        if (thumbnailImageHeight > 0){
//            return new ChatImageBean(0f,0f);
//        }

        int IMAGE_MAX_SIZE = (int)ViewUtil.Dp2px(context,IMAGE_MAX_SIZE_DP);

        float thumhWidth;
        float thumhHeight;

        if (largeHeight < largeWidth) {
            // 图片很扁
            if (largeWidth < IMAGE_MAX_SIZE){
                // 图片很扁，但是宽度依然小于150dp
                thumhWidth = largeWidth;
                thumhHeight = largeHeight;
            } else {
                thumhWidth = IMAGE_MAX_SIZE;
                thumhHeight = largeHeight * IMAGE_MAX_SIZE / largeWidth;
            }
        } else {
            // 图片很长
            if (largeHeight < IMAGE_MAX_SIZE){
                // 图片很扁，但是宽度依然小于150dp
                thumhWidth = largeWidth;
                thumhHeight = largeHeight;
            } else {
                thumhHeight = IMAGE_MAX_SIZE;
                thumhWidth = largeWidth * IMAGE_MAX_SIZE / largeHeight;
            }
        }

        return new ChatImageBean(thumhHeight,thumhWidth);
//        this.thumbnailImageHeight = thumhHeight;
//        this.thumbnailImageWidth = thumhWidth;
    }



    // Drawable转换成Bitmap
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 混合终极方法（尺寸、质量、JNI压缩）
     *
     * @param image    bitmap对象
     * @param filePath 要保存的指定目录
     * @Description: 通过JNI图片压缩把Bitmap保存到指定目录
     */
    public static Bitmap mixCompress(Context context,Bitmap image, String filePath,int Height,int Width ) {
        // 最大图片大小 1000KB
        int maxSize = 1000;
        // 获取尺寸压缩倍数
        int ratio = getRatioSize(context,Width, Height);
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(Width / ratio,  Height / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, Width/ ratio,  Height / ratio);
        canvas.drawBitmap(image, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        // 循环判断如果压缩后图片是否大于最大值,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            quality -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        return result;
    }

    /**
     * 计算缩放比
     *
     * @param bitWidth  当前图片宽度
     * @param bitHeight 当前图片高度
     * @return
     * @Description:函数描述
     */
    public static int getRatioSize(Context context,int bitWidth, int bitHeight) {
        // 图片最大分辨率
        int imageHeight =getOpenglRenderLimitValue();
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageHeight) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageHeight;
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    /**
     * 获取手机显示最长图片的限制值
     * @return
     */
    public static int getOpenglRenderLimitValue() {
        int maxsize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maxsize = getOpenglRenderLimitEqualAboveLollipop();
        } else {
            maxsize = getOpenglRenderLimitBelowLollipop();
        }
        return maxsize == 0 ? 4096 : maxsize;
    }

    private static int getOpenglRenderLimitBelowLollipop() {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private static int getOpenglRenderLimitEqualAboveLollipop() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers = new int[2];
        egl.eglInitialize(dpy, vers);
        int[] configAttr = {
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
        if (numConfig[0] == 0) {// TROUBLE! No config found.
        }
        EGLConfig config = configs[0];
        int[] surfAttr = {
                EGL10.EGL_WIDTH, 64,
                EGL10.EGL_HEIGHT, 64,
                EGL10.EGL_NONE
        };
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;// missing in EGL10
        int[] ctxAttrib = {
                EGL_CONTEXT_CLIENT_VERSION, 1,
                EGL10.EGL_NONE
        };
        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }

}