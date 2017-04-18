package com.liftdom.charts_stats_tools.exercise_selector;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Brodin on 3/24/2017.
 */

public class ExSelectorStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] exercises;
    private LayoutInflater inflater;
    String mbodyString = "null";
    boolean mNoCheckbox = false;
    FragmentActivity fragActivity;

    public ExSelectorStickyAdapter(Context context, FragmentActivity activity,  String bodyString, boolean noCheckbox){
        fragActivity = activity;

        inflater = LayoutInflater.from(context);

        mbodyString = bodyString;

        mNoCheckbox = noCheckbox;

        ArrayList<String> completedUpperList = ExSelectorSingleton.getInstance().completedExercises;

        if(bodyString.equals("upper")){

            String[] upperArray = context.getResources().getStringArray(R.array.upperBodyList);
            ArrayList<String> upperList = new ArrayList<>(Arrays.asList(upperArray));
            ArrayList<String> newList = new ArrayList<>();
            for(String string : completedUpperList){
                if(upperList.contains(string)){
                    newList.add(string);
                }
            }

            Collections.sort(newList, String.CASE_INSENSITIVE_ORDER);
            exercises = newList.toArray(new String[0]);

        }else if(bodyString.equals("lower")){

            String[] lowerArray = context.getResources().getStringArray(R.array.lowerBodyList);
            ArrayList<String> lowerList = new ArrayList<>(Arrays.asList(lowerArray));
            ArrayList<String> newList = new ArrayList<>();
            for(String string : completedUpperList){
                if(lowerList.contains(string)){
                    newList.add(string);
                }
            }

            Collections.sort(newList, String.CASE_INSENSITIVE_ORDER);
            exercises = newList.toArray(new String[0]);

        }else if(bodyString.equals("other")){

            String[] otherArray = context.getResources().getStringArray(R.array.upperBodyList);
            ArrayList<String> otherList = new ArrayList<>(Arrays.asList(otherArray));
            ArrayList<String> newList = new ArrayList<>();
            for(String string : completedUpperList){
                if(otherList.contains(string)){
                    newList.add(string);
                }
            }

            Collections.sort(newList, String.CASE_INSENSITIVE_ORDER);
            exercises = newList.toArray(new String[0]);
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
            if(mNoCheckbox){
                convertView = inflater.inflate(R.layout.ex_list_sticky_item2, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.textFull);
            }else{
                convertView = inflater.inflate(R.layout.ex_list_sticky_item_layout, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.stickyCheckbox);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(mNoCheckbox){
            holder.text.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //ExercisePickerController.getInstance().exName = exercises[position];
                    //String message = ExercisePickerController.getInstance().exName;
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", exercises[position]);
                    fragActivity.setResult(2, intent);
                    //ExercisePickerController.getInstance().exName = null;
                    fragActivity.finish();
                }
            });
        }else{
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(mbodyString.equals("upper")){
                            if(!ExSelectorSingleton.getInstance().upperBodyItems.contains(exercises[position])){
                                ExSelectorSingleton.getInstance().upperBodyItems.add(exercises[position]);
                                Log.i("info", exercises[position]);
                            }
                        }else if(mbodyString.equals("lower")){
                            if(!ExSelectorSingleton.getInstance().lowerBodyItems.contains(exercises[position])){
                                ExSelectorSingleton.getInstance().lowerBodyItems.add(exercises[position]);
                                Log.i("info", exercises[position]);
                            }
                        }else if(mbodyString.equals("other")){
                            if(!ExSelectorSingleton.getInstance().otherItems.contains(exercises[position])){
                                ExSelectorSingleton.getInstance().otherItems.add(exercises[position]);
                                Log.i("info", exercises[position]);
                            }
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
                            try {
                                ExSelectorSingleton.getInstance().lowerBodyItems.remove(index);
                            } catch (IndexOutOfBoundsException e){
                                Log.i("info", "out of bounds issue..");
                            }
                        }else if(mbodyString.equals("other")){
                            index = ExSelectorSingleton.getInstance().otherItems.indexOf(exercises[position]);
                            try {
                                ExSelectorSingleton.getInstance().otherItems.remove(index);
                            } catch (IndexOutOfBoundsException e){
                                Log.i("info", "out of bounds issue..");
                            }
                        }
                    }

                }
            });

            if(ExSelectorSingleton.getInstance().upperBodyItems.contains(exercises[position])
                    || ExSelectorSingleton.getInstance().lowerBodyItems.contains(exercises[position])
                    || ExSelectorSingleton.getInstance().otherItems.contains(exercises[position]))
            {
                holder.checkBox.setChecked(true);
            }else{
                holder.checkBox.setChecked(false);
            }
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
