package com.liftdom.user_profile.single_user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendDirectMessageFrag extends Fragment {


    public SendDirectMessageFrag() {
        // Required empty public constructor
    }

    String uidFromOutside;

    @BindView(R.id.userNameView) TextView userNameView;
    @BindView(R.id.messageHolder) LinearLayout messageHolder;
    @BindView(R.id.messageEditText) EditText messageEditText;
    @BindView(R.id.sendMessageView) ImageButton sendMessageButton;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.messageSentView) TextView messageSentView;
    @BindView(R.id.messageFailedView) TextView messageFailedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_direct_message, container, false);

        ButterKnife.bind(this, view);

        /**
         * So, first we need to check to see if there's already a chat with just you and the person you're messaging.
         * If there is, we will update that chat.
         * If not, we need to create a new chat group for both users and a new chat with the message typed here.
         * Got to make sure it's not empty.
         * We'll hide the edit text/initial views, show a loading view, and when the onComplete triggers,
         * we'll finish the dialog activity.
         */


        return view;
    }


}
