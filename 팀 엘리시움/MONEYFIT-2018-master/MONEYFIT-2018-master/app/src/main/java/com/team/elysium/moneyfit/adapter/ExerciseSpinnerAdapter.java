package com.team.elysium.moneyfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team.elysium.moneyfit.R;

import java.util.List;

/**
 * Created by sh on 2018-07-19.
 */

public class ExerciseSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private LayoutInflater inflater;
    public ExerciseSpinnerAdapter(Context context, List<String> data) {

        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return (data != null) ? data.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_exercise_spinner_normal, parent, false);
        }

        if(data != null) {
            ((TextView)convertView.findViewById(R.id.start_spinner_normal_text_view)).setText(data.get(i));
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_exercise_spinner_dropdown, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.start_spinner_dropdown_text_view)).setText(data.get(position));

        return convertView;
    }
}
