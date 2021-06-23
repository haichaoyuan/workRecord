package com.example.module_homepage2.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * luke
 * 基础数据适配器
 */
public abstract class DataAdapter<T> extends BaseAdapter {

    protected ArrayList<T> dataArray;
    protected Context context;

    public DataAdapter(Context ctx, List<T> objects) {
        context = ctx;
        // super(context, id);
        if (objects instanceof ArrayList) {
            dataArray = (ArrayList<T>) objects;
        } else {
            dataArray = new ArrayList<T>();
            for (T item : objects) {
                dataArray.add(item);
            }
        }

        // here is the tricky stuff

    }

    public DataAdapter(Context ctx) {
        context = ctx;
        dataArray = new ArrayList<T>();
    }

    public ArrayList<T> getData() {
        return dataArray;
    }

    public abstract View getTheView(int position, View convertView,
                                    ViewGroup parent);

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ArrayAdapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getTheView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public T getItem(int position) {
        return dataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(T itemView) {
        if (!dataArray.contains(itemView))
            dataArray.add(itemView);
        this.notifyDataSetChanged();

    }

    public void setItems(List<T> items) {
        dataArray.clear();
        dataArray.addAll(items);
        this.notifyDataSetChanged();
    }

    public void append(List<T> items) {
        dataArray.addAll(items);
        this.notifyDataSetChanged();
    }

    public void setItem(int index, T itemView) {
        if (index < dataArray.size()) {
            dataArray.set(index, itemView);
            this.notifyDataSetChanged();
        }
    }

    public void clear() {
        dataArray.clear();
        this.notifyDataSetChanged();

    }

    public void remove(T itemView) {
        if (dataArray.contains(itemView))
            dataArray.remove(itemView);
        this.notifyDataSetChanged();
    }

    public void remove(int index) {
        if (index < getCount())
            dataArray.remove(index);
        this.notifyDataSetChanged();
    }

}
