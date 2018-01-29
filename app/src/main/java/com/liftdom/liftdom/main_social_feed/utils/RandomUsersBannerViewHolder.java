package com.liftdom.liftdom.main_social_feed.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;
import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by Brodin on 1/28/2018.
 */

public class RandomUsersBannerViewHolder extends RecyclerView.ViewHolder {

    private final TextView mUserNameView;
    private final TextView mPowerLevelView;
    private final ImageView mProfilePicView;
    private final Button mFollowUserButton;
    private final Button mUnFollowUserButton;
    private final AVLoadingIndicatorView loadingView;
    private String xUid;
    private String xUserName;
    Context mContext;

    public RandomUsersBannerViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mPowerLevelView = (TextView) itemView.findViewById(R.id.powerLevelView);
        mProfilePicView = (ImageView) itemView.findViewById(R.id.profilePicView);
        mFollowUserButton = (Button) itemView.findViewById(R.id.followUserButton);
        mUnFollowUserButton = (Button) itemView.findViewById(R.id.unFollowUserButton);
        loadingView = (AVLoadingIndicatorView) itemView.findViewById(R.id.loadingView);

        mFollowUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mUnFollowUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public String getxUid() {
        return xUid;
    }

    public void setxUid(String xUid) {
        this.xUid = xUid;

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                xUid + "/profilePic.png");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("glide", "success");
                Glide.with(mContext).load(uri).placeholder(R.drawable.usertest).crossFade().into(mProfilePicView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                mProfilePicView.setImageResource(R.drawable.usertest);
            }
        });
    }

    public String getxUserName() {
        return xUserName;
    }

    public void setxUserName(String xUserName) {
        this.xUserName = xUserName;
        mUserNameView.setText(xUserName);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
