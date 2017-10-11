package com.liftdom.misc_activities.followers_following;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 10/11/2017.
 */

public class FollowersFollowingViewHolder extends RecyclerView.ViewHolder{

    private String xUid;
    private String uid;
    private String userName;
    private final ImageView mProfilePicView;
    private final TextView mUserNameView;
    private final Button mFollowUserButton;
    private final Button mUnFollowUserButton;

    public FollowersFollowingViewHolder(View itemView){
        super(itemView);
        mProfilePicView = (ImageView) itemView.findViewById(R.id.profilePicView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mFollowUserButton = (Button) itemView.findViewById(R.id.followUserButton);
        mUnFollowUserButton = (Button) itemView.findViewById(R.id.unFollowUserButton);



    }

    public String getxUid() {
        return xUid;
    }

    public void setxUid(String xUid) {
        this.xUid = xUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
