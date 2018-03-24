package com.liftdom.charts_stats_tools.exercise_selector;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 8/9/2017.
 */

public class CustomExViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    View mView;
    private final LinearLayout mMainLinearLayout;
    private final TextView mExNameView;
    private final CheckBox mCheckBox;
    private final ImageButton mRemoveExercise;
    private String mExName;
    private String mRefKey;
    private boolean noCheckbox = false;
    private boolean isExclusive = false;
    private FragmentActivity fragActivity;

    public CustomExViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mMainLinearLayout = (LinearLayout) itemView.findViewById(R.id.mainLinearLayout);
        mExNameView = (TextView) itemView.findViewById(R.id.exNameView);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        mRemoveExercise = (ImageButton) itemView.findViewById(R.id.removeExercise);

        mView.setOnClickListener(this);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!ExSelectorSingleton.getInstance().customItems.contains(getExName())){
                        ExSelectorSingleton.getInstance().customItems.add(getExName());
                        Log.i("info", getExName());
                    }
                }else{
                    int index = ExSelectorSingleton.getInstance().customItems.indexOf(getExName());
                    try{
                        ExSelectorSingleton.getInstance().customItems.remove(index);
                    }catch (IndexOutOfBoundsException e){
                        Log.i("info", "out of bounds issue");
                    }
                }
            }
        });

        mRemoveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference exRef = FirebaseDatabase.getInstance().getReference().child("customExercises")
                        .child(uid).child(getRefKey());
                exRef.setValue(null);
            }
        });

    }

    @Override
    public void onClick(View view){
        if(isNoCheckbox()){
            Intent intent = new Intent();
            intent.putExtra("MESSAGE", getExName());
            fragActivity.setResult(2, intent);
            fragActivity.finish();
        }
    }

    public FragmentActivity getFragActivity() {
        return fragActivity;
    }

    public void setFragActivity(FragmentActivity fragActivity) {
        this.fragActivity = fragActivity;
    }

    public String getExName() {
        return mExName;
    }

    public void setExName(String mExName) {
        this.mExName = mExName;
        mExNameView.setText(mExName);
    }

    public void setIsChecked(boolean isChecked){
        if(isChecked){
            mCheckBox.setChecked(true);
        }else{
            mCheckBox.setChecked(false);
        }
    }

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }

    public boolean isNoCheckbox() {
        return noCheckbox;
    }

    public void setNoCheckbox(boolean noCheckbox) {
        this.noCheckbox = noCheckbox;
        if(noCheckbox){
            mCheckBox.setVisibility(View.GONE);
        }else{
            mCheckBox.setVisibility(View.VISIBLE);

        }
    }

    public boolean isExclusive() {
        return isExclusive;
    }

    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }
}
