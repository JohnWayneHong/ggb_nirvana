package com.ggb.nirvanaclub.modules.message;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ggb.nirvanaclub.R;
import com.ggb.nirvanaclub.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageTextDialog extends Dialog {

	public TextView title_tv;

	public MessageTextDialog(Context context ) {
		super(context, R.style.LoadingDialog);
		// TODO Auto-generated constructor stub
	}

	public void initTextView(final Context context ,final String title) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_menu_title, null);
		TextView title_tv = layout.findViewById(R.id.title_tv);
		title_tv.setText(title);

		View titleline = new View(context);
		titleline.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
		layout.addView(titleline,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.Dp2px(context,0.5f)));

		List<String> menuList = new ArrayList<>();
		menuList.add("复制");

		for (int i = 0 ; i < menuList.size(); i++){
			final int position = i;
			TextView tv = new TextView(context);
			tv.setText(menuList.get(i));
			tv.setTextSize(17);
			tv.setPadding(0, ViewUtil.Dp2px(context,14), 0, ViewUtil.Dp2px(context,14));
			tv.setGravity(Gravity.CENTER);
			tv.setClickable(true);
			tv.setTextColor(context.getResources().getColor(R.color.text_main_color));
			tv.setBackgroundResource(R.drawable.bg_white_full_5);
			layout.addView(tv,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dismiss();
					if (position == 0){
						ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setText(title);
						Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
					}
				}
			});

			if (i != menuList.size()-1){
				//加上横线
				View line = new View(context);
				line.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
				layout.addView(line,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtil.Dp2px(context,0.5f)));
			}
		}

		setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));

	}

	public void initPhoneView(final Context context ,final String title) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_menu_title, null);
		TextView title_tv = layout.findViewById(R.id.title_tv);
		title_tv.setText(title);

		View titleline = new View(context);
		titleline.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
		layout.addView(titleline,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtil.Dp2px(context,0.5f)));

		List<String> menuList = new ArrayList<>();
		menuList.add("复制");
		menuList.add("呼叫");

		for (int i = 0 ; i < menuList.size(); i++){
			final int position = i;
			TextView tv = new TextView(context);
			tv.setText(menuList.get(i));
			tv.setTextSize(17);
			tv.setPadding(0, ViewUtil.Dp2px(context,14), 0, ViewUtil.Dp2px(context,14));
			tv.setGravity(Gravity.CENTER);
			tv.setClickable(true);
			tv.setTextColor(context.getResources().getColor(R.color.text_main_color));
			tv.setBackgroundResource(R.drawable.bg_white_full_5);
			layout.addView(tv,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dismiss();
					if (position == 0){
						ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setText(title);
						Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
					} else {
						Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + title));
						context.startActivity(dialIntent);
					}
				}
			});

			if (i != menuList.size()-1){
				//加上横线
				View line = new View(context);
				line.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
				layout.addView(line,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtil.Dp2px(context,0.5f)));
			}
		}

		setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
	
	}


	public void initWebView(final Context context ,final String title) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_menu_title, null);
		TextView title_tv = layout.findViewById(R.id.title_tv);
		title_tv.setText(title);

		View titleline = new View(context);
		titleline.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
		layout.addView(titleline,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtil.Dp2px(context,0.5f)));

		List<String> menuList = new ArrayList<>();
		menuList.add("复制");
		menuList.add("打开网页");

		for (int i = 0 ; i < menuList.size(); i++){
			final int position = i;
			TextView tv = new TextView(context);
			tv.setText(menuList.get(i));
			tv.setTextSize(17);
			tv.setPadding(0, ViewUtil.Dp2px(context,14), 0, ViewUtil.Dp2px(context,14));
			tv.setGravity(Gravity.CENTER);
			tv.setClickable(true);
			tv.setTextColor(context.getResources().getColor(R.color.text_main_color));
			tv.setBackgroundResource(R.drawable.bg_white_full_5);
			layout.addView(tv,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dismiss();
					if (position == 0){
						ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setText(title);
						Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
					} else {
						Intent i = new Intent(context, MessageWebViewActivity.class);
						i.putExtra("url", title);
						context.startActivity(i);
					}
				}
			});

			if (i != menuList.size()-1){
				//加上横线
				View line = new View(context);
				line.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
				layout.addView(line,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtil.Dp2px(context,0.5f)));
			}
		}

		setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));

	}


	public void initEmailView(final Context context ,final String title) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_menu_title, null);
		TextView title_tv = layout.findViewById(R.id.title_tv);
		title_tv.setText(title);

		View titleline = new View(context);
		titleline.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
		layout.addView(titleline,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtil.Dp2px(context,0.5f)));

		List<String> menuList = new ArrayList<>();
		menuList.add("复制");
		menuList.add("发邮件");

		for (int i = 0 ; i < menuList.size(); i++){
			final int position = i;
			TextView tv = new TextView(context);
			tv.setText(menuList.get(i));
			tv.setTextSize(17);
			tv.setPadding(0, ViewUtil.Dp2px(context,14), 0, ViewUtil.Dp2px(context,14));
			tv.setGravity(Gravity.CENTER);
			tv.setClickable(true);
			tv.setTextColor(context.getResources().getColor(R.color.text_main_color));
			tv.setBackgroundResource(R.drawable.bg_white_full_5);
			layout.addView(tv,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dismiss();
					if (position == 0){
						ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setText(title);
						Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
					} else {
						Intent dialIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + title));
						context.startActivity(dialIntent);
					}
				}
			});

			if (i != menuList.size()-1){
				//加上横线
				View line = new View(context);
				line.setBackgroundColor(context.getResources().getColor(R.color.divider_color));
				layout.addView(line,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewUtil.Dp2px(context,0.5f)));
			}
		}

		setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));

	}
}
