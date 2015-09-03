package com.made4food.jsonparser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GoogleAdapter extends ArrayAdapter<Google>{


    public GoogleAdapter(Context context, int row, ArrayList<Google> google) {
        super(context, R.layout.row ,google);

    }

    private static class ViewHolder {
        TextView result;
        TextView title ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Google google = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row, parent, false);
            viewHolder.result = (TextView) convertView.findViewById(R.id.res);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.result.setText(google.getResult());
        viewHolder.title.setText(google.getTitle());

        // Return the completed view to render on screen
        return convertView;

    }


}

