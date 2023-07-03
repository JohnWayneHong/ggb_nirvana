package com.jgw.common_library.widget.selectDialog;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.jgw.common_library.R;
import com.jgw.common_library.base.adapter.CustomRecyclerAdapter;
import com.jgw.common_library.databinding.ItemBottomDialogBinding;
import com.jgw.common_library.utils.click_utils.ClickUtils;

public class SelectDialogAdapter extends CustomRecyclerAdapter<String> {

    @Override
    public ContentViewHolder<? extends ViewDataBinding> onCreateCustomViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT1) {
            return new ContentType1ViewHolder(DataBindingUtil.inflate(mLayoutInflater
                    , R.layout.item_bottom_dialog, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindCustomViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentType1ViewHolder) {
            String content = mList.get(position);
            ((ContentType1ViewHolder) holder).mBindingView.tvDialogContent.setText(content);
        }
    }

    private class ContentType1ViewHolder extends ContentViewHolder<ItemBottomDialogBinding> {

        public ContentType1ViewHolder(ItemBottomDialogBinding binding) {
            super(binding);
            ClickUtils.register(this)
                    .addItemClickListener()
                    .addView(binding.getRoot())
                    .submit();
        }


        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            if (mListener != null) {
                mListener.onItemClick(view, position);
            }
        }
    }
}
