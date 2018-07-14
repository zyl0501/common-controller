package com.ray.common.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * Created by zyl on 2016/4/8.
 */
public class ListDialog extends BaseDialog {
    private ListAdapter listAdapter;
    private OnItemClickListener mItemListener;

    public ListDialog(Context context) {
        super(context);
    }

    @Override
    protected View getContentView(ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.dialog_layout_dialog_list, parent, false);
    }

    @Override
    protected void bindView(View contentView) {
        setListAdapter(listAdapter);
        setOnItemClickListener(mItemListener);
    }

    public void setListAdapter(ListAdapter adapter) {
        this.listAdapter = adapter;
        if (contentView != null) {
            ((ListView) contentView).setAdapter(adapter);
        }
    }

    public void setOnItemClickListener(@NonNull final OnItemClickListener listener) {
        this.mItemListener = listener;
        if(contentView != null) {
            ListView listView = (ListView) contentView;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onItemClick(parent, view, position, id, ListDialog.this);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id, DialogInterface dialog);
    }

    public static class Builder extends AbsBuilder {
        ListAdapter listAdapter;
        OnItemClickListener itemClickListener;

        public Builder(Context context) {
            this.context = context;
            width = INVALID_SIZE;
            height = INVALID_SIZE;
        }

        public Builder setTitle(CharSequence title) {
            return (Builder) super.setTitle(title);
        }

        public Builder list(String[] items, OnItemClickListener itemClickListener) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1);
            adapter.addAll(items);
            listAdapter = adapter;
            this.itemClickListener = itemClickListener;
            return this;
        }

        public Builder setListAdapter(ListAdapter adapter) {
            this.listAdapter = adapter;
            return this;
        }

        public Builder setOnItemClickListener(OnItemClickListener listener) {
            this.itemClickListener = listener;
            return this;
        }

        public ListDialog show() {
            ListDialog dialog = create();
            dialog.show();
            return dialog;
        }

        @Override
        public ListDialog create() {
            ListDialog dialog = new ListDialog(context);
            dialog.setWidth(width);
            dialog.setHeight(height);
            dialog.setTitle(title);
            dialog.setPositiveButton(rightBtnRes, rightClick);
            dialog.setNegativeButton(leftBtnRes, leftClick);
            dialog.setListAdapter(listAdapter);
            dialog.setOnItemClickListener(itemClickListener);
            dialog.setCancelable(mCancelable);
            return dialog;
        }
    }
}
