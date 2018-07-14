package com.tomtaw.libs.test;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomtaw.widget.stick_framelayout.StickFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class StickFrameLayoutActivity extends AppCompatActivity {
    StickFrameLayout stickFrameLayout;
    boolean isTickText = false;
    boolean isMultiSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_frame);

        stickFrameLayout = (StickFrameLayout) findViewById(R.id.stick_frame_layout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MyAdapter(this));

        stickFrameLayout.setStickRes(R.layout.item_recycler_stick);
        stickFrameLayout.setItemStickId(R.id.item_layout);
        stickFrameLayout.setListener(new StickFrameLayout.StickListener() {
            @Override
            public void onStickChange(View stickView, int newPos) {
                TextView textView = (TextView) stickView.findViewById(R.id.text);
                textView.setText("item " + newPos);
            }
        });
    }

    public void clickStickItem(View view) {
        isTickText = !isTickText;
        if (isTickText) {
            stickFrameLayout.setStickRes(R.layout.item_stick_text);
            stickFrameLayout.setItemStickId(R.id.text);
        } else {
            stickFrameLayout.setStickRes(R.layout.item_recycler_stick);
            stickFrameLayout.setItemStickId(R.id.item_layout);
        }
    }

    public void clickMultiSelect(View view) {
        isMultiSelect = !isMultiSelect;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private Context context;
        private List<Integer> selPos = new ArrayList<>();

        public MyAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_recycler, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isMultiSelect) {
                        if (selPos.contains(position)) {
                            selPos.remove((Integer)position);
                            stickFrameLayout.setStickPositions(selPos);
                        } else {
                            selPos.add(position);
                            stickFrameLayout.addStickPosition(position);
                        }
                    } else {
                        if (selPos.contains(position)) {
                            selPos.clear();
                            stickFrameLayout.clearStick();
                        } else {
                            selPos.clear();
                            selPos.add(position);
                            stickFrameLayout.setStickPosition(position);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            holder.itemView.setBackgroundColor(selPos.contains(position) ? Color.parseColor("#dedede") : Color.WHITE);
            holder.textView.setText("item " + position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
