package com.liftdom.liftdom.main_social_feed.user_search;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.liftdom.liftdom.PremiumFeaturesActivity;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.other_profile.OtherUserProfileFrag;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSearchResultFrag extends Fragment {


    public UserSearchResultFrag() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public String userName;
    public String xUid;

    @BindView(R.id.userName) TextView userNameTextView;
    @BindView(R.id.userLevel) TextView userLevelTextView;
    @BindView(R.id.profilePic) ImageView profilePicImageView;
    @BindView(R.id.userInfoHolder) LinearLayout userInfoHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_search_result, container, false);

        ButterKnife.bind(this, view);

        userNameTextView.setText(userName);

        userInfoHolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(uid.equals(xUid)){
                    Intent intent = new Intent(getContext(), CurrentUserProfile.class);
                    startActivity(intent);
                } else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    OtherUserProfileFrag otherUserProfileFrag = new OtherUserProfileFrag();
                    otherUserProfileFrag.userName = userName;
                    otherUserProfileFrag.xUid = xUid;

                    fragmentTransaction.replace(R.id.mainFragHolder, otherUserProfileFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }

}
