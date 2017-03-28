package com.liftdom.liftdom.utils.exercise_selector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.liftdom.liftdom.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Brodin on 3/24/2017.
 */

public class ExSelectorStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] exercises;
    private LayoutInflater inflater;
    String mbodyString = "null";

    public ExSelectorStickyAdapter(Context context, String bodyString){
        inflater = LayoutInflater.from(context);
        mbodyString = bodyString;
        if(bodyString.equals("upper")){
            exercises = context.getResources().getStringArray(R.array.upperBodyList);
        }else if(bodyString.equals("lower")){
            exercises = context.getResources().getStringArray(R.array.lowerBodyList);
        }else if(bodyString.equals("full")){
            exercises = context.getResources().getStringArray(R.array.fullBodyList);
        }
    }

    @Override
    public int getCount() {
        return exercises.length;
    }

    @Override
    public Object getItem(int position) {
        return exercises[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.ex_list_sticky_item_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.stickyCheckbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(mbodyString.equals("upper")){
                        ExSelectorSingleton.getInstance().upperBodyItems.add(exercises[position]);
                        Log.i("info", exercises[position]);
                    }else if(mbodyString.equals("lower")){
                        ExSelectorSingleton.getInstance().lowerBodyItems.add(exercises[position]);
                    Log.i("info", exercises[position]);
                    }else if(mbodyString.equals("full")){
                        ExSelectorSingleton.getInstance().fullBodyItems.add(exercises[position]);
                    Log.i("info", exercises[position]);
                    }
                } else{
                    int index;

                    if(mbodyString.equals("upper")){
                        index = ExSelectorSingleton.getInstance().upperBodyItems.indexOf(exercises[position]);
                        try {
                            ExSelectorSingleton.getInstance().upperBodyItems.remove(index);
                        } catch (IndexOutOfBoundsException e){
                            Log.i("info", "out of bounds issue..");
                        }

                    }else if(mbodyString.equals("lower")){
                        index = ExSelectorSingleton.getInstance().lowerBodyItems.indexOf(exercises[position]);
                        ExSelectorSingleton.getInstance().lowerBodyItems.remove(index);
                    }else if(mbodyString.equals("full")){
                        index = ExSelectorSingleton.getInstance().fullBodyItems.indexOf(exercises[position]);
                        ExSelectorSingleton.getInstance().fullBodyItems.remove(index);
                    }
                }

            }
        });

        if(ExSelectorSingleton.getInstance().upperBodyItems.contains(exercises[position])
                || ExSelectorSingleton.getInstance().lowerBodyItems.contains(exercises[position])
                || ExSelectorSingleton.getInstance().fullBodyItems.contains(exercises[position]))
            {
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }

        holder.text.setText(exercises[position]);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.sticky_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + exercises[position].subSequence(0, 1).charAt(0);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return exercises[position].subSequence(0, 1).charAt(0);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
        CheckBox checkBox;
    }
}
