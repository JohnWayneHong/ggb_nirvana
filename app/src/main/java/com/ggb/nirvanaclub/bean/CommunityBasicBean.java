package com.ggb.nirvanaclub.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.jpush.im.android.api.model.Message;

public class CommunityBasicBean implements MultiItemEntity {

    public static final int BANNER = 1;
    public static final int VIDEO = 2;
    public static final int TEXTHEADER = 3;

    public int itemType=1;
    public CommunityDailyBean communityDailyBean;
    public CommunityDailyDataBean videoBean;


    public CommunityBasicBean(CommunityDailyBean communityDailyBean, int type,CommunityDailyDataBean videoBean){
        this.communityDailyBean=communityDailyBean;
        this.itemType=type;
        this.videoBean=videoBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
