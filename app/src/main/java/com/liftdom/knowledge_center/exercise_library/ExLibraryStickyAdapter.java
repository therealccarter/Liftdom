package com.liftdom.knowledge_center.exercise_library;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.liftdom.knowledge_center.ArticlesMainFrag;
import com.liftdom.liftdom.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static java.security.AccessController.getContext;

/**
 * Created by Brodin on 3/30/2017.
 */

public class ExLibraryStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    Context context;
    ViewGroup viewGroup;

    private String[] exercises;
    private LayoutInflater inflater;
    String mbodyString = "null";

    public ExLibraryStickyAdapter(Context mContext, String bodyString){

        context = mContext;

        inflater = LayoutInflater.from(mContext);

        mbodyString = bodyString;
        if(bodyString.equals("upper")){
            exercises = context.getResources().getStringArray(R.array.upperBodyLibList);
        }else if(bodyString.equals("lower")){
            exercises = context.getResources().getStringArray(R.array.lowerBodyLibList);
        }else if(bodyString.equals("other")){
            exercises = context.getResources().getStringArray(R.array.otherBodyLibList);
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
            convertView = inflater.inflate(R.layout.ex_lib_sticky_item_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //ExercisesDetailFrag exercisesDetailFrag = new ExercisesDetailFrag();
                //exercisesDetailFrag.exName = exercises[position];

                //fragmentTransaction.replace(R.id.upperBodyHolder, exercisesDetailFrag);
                //fragmentTransaction.commit();

                Intent intent = new Intent(context, ExerciseDetailActivity.class);

                intent.putExtra("exName", exercises[position]);

                context.startActivity(intent);
            }
        });

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
    }
}
