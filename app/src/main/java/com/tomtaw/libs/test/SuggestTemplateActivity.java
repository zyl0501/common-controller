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

import com.tomtaw.widget.suggest_template_layout.SuggestTemplateLayout;

import java.util.ArrayList;
import java.util.List;

public class SuggestTemplateActivity extends AppCompatActivity {
    SuggestTemplateLayout templateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_template);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MyAdapter(this));

        templateLayout = (SuggestTemplateLayout) findViewById(R.id.suggest_layout);
        templateLayout.setCallBack(new SuggestTemplateLayout.CallBack() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onChange(float offset) {

            }
        });
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
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyAdapter.ViewHolder(mInflater.inflate(R.layout.item_recycler_2, parent, false));
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    templateLayout.close(true);
                    selPos.clear();
                    selPos.add(position);
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
