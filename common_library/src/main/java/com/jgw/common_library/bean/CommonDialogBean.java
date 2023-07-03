package com.jgw.common_library.bean;

import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.jgw.common_library.BR;
import com.jgw.common_library.widget.commonDialog.CommonDialog;

public class CommonDialogBean extends BaseObservable {
    private String title;
    private Spanned titleSpanned;
    private String details;
    private Spanned detailsSpanned;
    private String left;
    private String right;
    private String input;
    private String inputHint;

    private int type;

    public String getTitle() {
        return title;
    }

    public Spanned getTitleSpanned() {
        return titleSpanned;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.titleText);
    }

    public void setTitleSpanned(Spanned titleSpanned) {
        this.titleSpanned = titleSpanned;
        notifyPropertyChanged(BR.titleText);
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
        notifyPropertyChanged(BR.detailsText);
    }

    public Spanned getDetailsSpanned() {
        return detailsSpanned;
    }

    public void setDetailsSpanned(Spanned detailsSpanned) {
        this.detailsSpanned = detailsSpanned;
        notifyPropertyChanged(BR.detailsText);
    }

    @Bindable
    public CharSequence getTitleText() {
        if (getTitleSpanned() != null) {
            return getTitleSpanned();
        }
        return getTitle();
    }

    @Bindable
    public CharSequence getDetailsText() {
        if (getDetailsSpanned() != null) {
            return getDetailsSpanned();
        }
        return getDetails();
    }

    @Bindable
    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
        notifyPropertyChanged(BR.left);
    }

    @Bindable
    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
        notifyPropertyChanged(BR.right);
    }

    @Bindable
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
        notifyPropertyChanged(BR.input);
    }

    @Bindable
    public String getInputHint() {
        return inputHint;
    }

    public void setInputHint(String inputHint) {
        this.inputHint = inputHint;
        notifyPropertyChanged(BR.inputHint);
    }

    @Bindable
    public int getDetailsVisible() {
        boolean emptyContent = getDetailsSpanned() == null && TextUtils.isEmpty(getDetails());
        boolean isInput = getType() == CommonDialog.TYPE_INPUT_DIALOG;
        return isInput || emptyContent ? View.GONE : View.VISIBLE;
    }

    @Bindable
    public int getLeftVisible() {
        boolean isConfirm = getType() == CommonDialog.TYPE_CONFIRM_DIALOG;
        return isConfirm ? View.GONE : View.VISIBLE;
    }

    @Bindable
    public int getInputVisible() {
        boolean isInput = getType() == CommonDialog.TYPE_INPUT_DIALOG;
        return isInput ? View.VISIBLE : View.GONE;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.inputVisible);
        notifyPropertyChanged(BR.detailsVisible);
        notifyPropertyChanged(BR.leftVisible);
    }
}
