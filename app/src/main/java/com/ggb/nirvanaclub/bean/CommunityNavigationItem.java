package com.ggb.nirvanaclub.bean;


import com.kunminx.linkage.bean.BaseGroupedItem;

/**
 * Create by KunMinX at 19/4/27
 */
public class CommunityNavigationItem extends BaseGroupedItem<CommunityNavigationItem.ItemInfo> {

  public CommunityNavigationItem(boolean isHeader, String header) {
    super(isHeader, header);
  }

  public CommunityNavigationItem(ItemInfo item) {
    super(item);
  }

  public static class ItemInfo extends BaseGroupedItem.ItemInfo {
    private String content;
    private String imgUrl;
    private String cost;

    public ItemInfo(String title, String group, String content, String imgUrl, String cost) {
      super(title, group);
      this.content = content;
      this.imgUrl = imgUrl;
      this.cost = cost;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getImgUrl() {
      return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
      this.imgUrl = imgUrl;
    }

    public String getCost() {
      return cost;
    }

    public void setCost(String cost) {
      this.cost = cost;
    }
  }
}
