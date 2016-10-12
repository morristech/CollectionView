package com.example.collectionview.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.collectionview.R;
import com.example.collectionview.widget.CollectionView;
import com.example.collectionview.widget.async.AsyncExpandableCollectionView;
import com.example.collectionview.widget.async.AsyncExpandableCollectionViewCallbacks;
import com.example.collectionview.widget.async.AsyncHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AsyncActivity extends MainActivity implements AsyncExpandableCollectionViewCallbacks<String, News> {

    private AsyncExpandableCollectionView<String, News> mAsyncExpandableCollectionView;
    private CollectionView.Inventory<String, News> inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        mAsyncExpandableCollectionView = (AsyncExpandableCollectionView) findViewById(R.id.asyncExpandableCollectionView);
        mAsyncExpandableCollectionView.setCallbacks(this);

        inventory = new CollectionView.Inventory();

        CollectionView.InventoryGroup<String, News> group1 = inventory.newGroup(0); // groupOrdinal is the smallest, displayed first
        group1.setHeaderItem("Top Stories");


        CollectionView.InventoryGroup<String, News> group2 = inventory.newGroup(2);
        group2.setHeaderItem("World");


        CollectionView.InventoryGroup<String, News> group3 = inventory.newGroup(3); // 2 is smaller than 10, displayed second
        group3.setHeaderItem("Australia");

        CollectionView.InventoryGroup<String, News> group4 = inventory.newGroup(4); // 2 is smaller than 10, displayed second
        group4.setHeaderItem("International");

        CollectionView.InventoryGroup<String, News> group5 = inventory.newGroup(5); // 2 is smaller than 10, displayed second
        group5.setHeaderItem("Businesses");

        CollectionView.InventoryGroup<String, News> group6 = inventory.newGroup(6); // 2 is smaller than 10, displayed second
        group6.setHeaderItem("Technology");

        mAsyncExpandableCollectionView.updateInventory(inventory);
    }

    @Override
    public void onStartLoadingGroup(int groupOrdinal) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                List<News> items = new ArrayList<>();
                News news = new News();
                news.setNewsTitle("Lawyers meet voluntary pro bono target for first time since 2013");
                news.setNewsBody("A voluntary target for the amount of pro bono work done by Australian lawyers has been met for the first time since 2013. Key points: The Australian Pro Bono Centre's asks lawyers to do 35 hours of free community work a year; Pro bono services can help ...\n");
                items.add(news);

                news = new News();
                news.setNewsTitle("HSC 2016: 77000 students to sit first exams across NSW");
                news.setNewsBody("More than 77,000 NSW high school students will sit their first HSC exams this week as one of the final cohorts to sit the test before the NSW government enacts sweeping reforms across the state.");
                items.add(news);
                mAsyncExpandableCollectionView.onFinishLoadingGroup(items);
            }
        }.execute();
    }


    @Override
    public RecyclerView.ViewHolder newCollectionHeaderView(Context context, int groupOrdinal, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.header_row_item_async, parent, false);

        return new MyHeaderViewHolder(v, groupOrdinal, mAsyncExpandableCollectionView);
    }

    @Override
    public void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, String headerItem) {
        MyHeaderViewHolder myHeaderViewHolder = (MyHeaderViewHolder) holder;
        myHeaderViewHolder.getTextView().setText(headerItem);
    }

    public static class MyHeaderViewHolder extends AsyncHeaderViewHolder implements AsyncExpandableCollectionView.OnGroupStateChangeListener {

        private final TextView textView;
        private final ProgressBar mProgressBar;

        public MyHeaderViewHolder(View v, int groupOrdinal, AsyncExpandableCollectionView asyncExpandableCollectionView) {
            super(v, groupOrdinal, asyncExpandableCollectionView);
            textView = (TextView) v.findViewById(R.id.title);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }


        public TextView getTextView() {
            return textView;
        }


        @Override
        public void onGroupStartExpending() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onGroupExpanded() {
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onGroupCollapsed() {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
