package com.zendeka.guildwars2fieldmanual.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.zendeka.guildwars2fieldmanual.R;
import com.zendeka.gw2apiandroid.gw2api.Continent;

import java.util.List;

/**
 * Created by lawrence on 3/20/15.
 */
public class ContinentsAdapter extends ArrayAdapter<Continent> implements SpinnerAdapter {
    public ContinentsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ContinentsAdapter(Context context, int resource, List<Continent> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView textView = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        if (view != null) {
            textView = (TextView) view.findViewById(android.R.id.text1);
            int color = getContext().getResources().getColor(android.R.color.primary_text_dark);
            textView.setTextColor(color);
        }

        if (textView != null) {
            Continent continent = getItem(position);
            textView.setText(continent.getName());
        }

        return view;
    }

    @Override
    public View getDropDownView (int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView textView = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_continent, parent, false);
        }

        if (view != null) {
            textView = (TextView) view.findViewById(R.id.continent_row_text);
        }

        if (textView != null) {
            Continent continent = getItem(position);
            textView.setText(continent.getName());
        }

        return view;
    }
}
