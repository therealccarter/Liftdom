package com.liftdom.knowledge_center.hall_of_fame;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.liftdom.liftdom.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Brodin on 3/31/2017.
 */

public class HoFStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    Context context;
    ViewGroup viewGroup;

    private String[] peopleHoF;
    private LayoutInflater inflater;
    String mDiscipline = "null";

    public HoFStickyAdapter(Context mContext, String discipline){

        context = mContext;

        inflater = LayoutInflater.from(mContext);

        mDiscipline = discipline;
        if(discipline.equals("Bodybuilding")){
            peopleHoF = context.getResources().getStringArray(R.array.bodybuildingHoF);
        }else if(discipline.equals("Powerlifting")){
            peopleHoF = context.getResources().getStringArray(R.array.powerliftingHoF);
        }else if(discipline.equals("Strongman")){
            peopleHoF = context.getResources().getStringArray(R.array.strongmanHoF);
        }else if(discipline.equals("Other")){
            peopleHoF = context.getResources().getStringArray(R.array.generalFitnessHoF);
        }
    }

    @Override
    public int getCount() {
        return peopleHoF.length;
    }

    @Override
    public Object getItem(int position) {
        return peopleHoF[position];
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

                //peopleHoFDetailFrag peopleHoFDetailFrag = new peopleHoFDetailFrag();
                //peopleHoFDetailFrag.exName = peopleHoF[position];

                //fragmentTransaction.replace(R.id.upperBodyHolder, peopleHoFDetailFrag);
                //fragmentTransaction.commit();

                Intent intent = new Intent(context, HallOfFameDetailActivity.class);

                intent.putExtra("person", peopleHoF[position]);

                context.startActivity(intent);
            }
        });

        holder.text.setText(peopleHoF[position]);

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
        String headerText = "" + peopleHoF[position].subSequence(0, 1).charAt(0);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return peopleHoF[position].subSequence(0, 1).charAt(0);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }
}
