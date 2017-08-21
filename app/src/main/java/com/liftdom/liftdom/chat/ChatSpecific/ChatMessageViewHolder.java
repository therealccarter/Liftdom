package com.liftdom.liftdom.chat.ChatSpecific;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 5/4/2017.
 */

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    View mView;
    Context mContext;
    private final TextView mTextMessageView;
    private final TextView mUserNameView;
    private final TextView mTimeStampView;
    private final Button mRepCountView;
    private final LinearLayout mMessageLayout;
    private final ImageView mProfilePicView;
    private String userId;

    public ChatMessageViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        mTextMessageView = (TextView) itemView.findViewById(R.id.textMessageView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mTimeStampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mRepCountView = (Button) itemView.findViewById(R.id.repCountView);
        mMessageLayout = (LinearLayout) itemView.findViewById(R.id.chatMessageLinearLayout);
        mProfilePicView = (ImageView) itemView.findViewById(R.id.profilePic);
    }

    public void setMessage(String message){
        mTextMessageView.setText(message);
    }

    public void setUserName(String userName){
        mUserNameView.setText(userName);
    }

    public void setRepCount(int repCount){
        mRepCountView.setText(String.valueOf(repCount));
    }

    public void setTimeStamp(String timeStamp){
        mTimeStampView.setText(timeStamp);
    }

    public void setBackground(){
        mMessageLayout.setBackgroundColor(Color.parseColor("#cccccc"));
        mUserNameView.setTextColor(Color.parseColor("#000000"));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                userId + "/profilePic.png");

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
}
