package com.click.force.clickforce.working.time;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class HistoryListView extends BaseAdapter {
    private ArrayList<View> views;

    public HistoryListView(){
        super();
        views = new ArrayList<View>();
    }

    public void addView(View view){
        views.add(view);
    }

    public Object getViewObject(int pos){
        return views.get(pos);
    }

    public void clear(){
        views.clear();
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object getItem(int position) {
        return views.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = views.get(position);
        return convertView;
    }

}
