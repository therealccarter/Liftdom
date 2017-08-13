package com.liftdom.template_housing;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.liftdom.template_editor.TemplateModelClass;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedTemplatesFrag extends Fragment {

    // declare_auth
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    int templateOptionsCheck = 0;

    public SavedTemplatesFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    bottomNavChanger navChangerCallback;

    public interface bottomNavChanger{
        void setBottomNavIndex(int navIndex);
    }

    private void navChanger(int navIndex){
        navChangerCallback.setBottomNavIndex(navIndex);
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private DatabaseReference mFeedRef = FirebaseDatabase.getInstance().getReference().child("templates")
            .child(uid);

    ArrayList<String> templateNamesList = new ArrayList<>();

    @BindView(R.id.noSavedTemplates) TextView noSavedTemplates;
    @BindView(R.id.button_new_template) LinearLayout linearLayout_new_template;
    @BindView(R.id.createTemplateLinearLayout) LinearLayout createTemplateLinearLayout;
    @BindView(R.id.new_template_image) ImageView new_template_image;
    @BindView(R.id.button_premade_templates) Button button_premade_templates;
    @BindView(R.id.button_from_scratch) Button button_from_scratch;
    @BindView(R.id.recycler_view_saved_templates) RecyclerView mRecyclerView;
    @BindView(R.id.loadingView2) AVLoadingIndicatorView loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_templates, container, false);

        ButterKnife.bind(this, view);

        navChanger(0);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        if(savedInstanceState == null) {
            DatabaseReference mDatabase  = FirebaseDatabase.getInstance().getReference();

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            final DatabaseReference mTemplateRef = mDatabase.child("templates").child(uid);

            mTemplateRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() == null) {
                                noSavedTemplates.setVisibility(View.VISIBLE);
                                linearLayout_new_template.setVisibility(View.VISIBLE);
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        linearLayout_new_template.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                if (templateOptionsCheck == 0) {
                    createTemplateLinearLayout.setVisibility(View.VISIBLE);

                    new_template_image.setImageResource(android.R.color.transparent);
                    new_template_image.setBackgroundResource(R.drawable.simple_up_arrow_small);
                    //newTemplateImage.setPadding(padding,0,padding,0);
                    linearLayout_new_template.setBackgroundColor(Color.parseColor("#FF131313"));


                    templateOptionsCheck = 1;
                }else if (templateOptionsCheck == 1) {
                    createTemplateLinearLayout.setVisibility(View.GONE);

                    new_template_image.setImageResource(android.R.color.transparent);
                    new_template_image.setBackgroundResource(R.drawable.simple_down_arrow_small);
                    //newTemplateImage.setPadding(padding,0,padding,0);
                    linearLayout_new_template.setBackgroundColor(Color.parseColor("#000000"));


                    templateOptionsCheck = 0;
                }
            }
        });


        button_from_scratch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "no";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("key1", isEdit );
                startActivity(intent);
            }
        });

        button_premade_templates.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new PremadeTemplatesFrag(),
                        "premadeTemplatesTag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        setUpFirebaseAdapter();

        return view;
    }

    private void setUpFirebaseAdapter(){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<TemplateModelClass, SavedTemplateViewHolder>
                (TemplateModelClass.class, R.layout.saved_template_list_item, SavedTemplateViewHolder.class, mFeedRef) {
            @Override
            protected void populateViewHolder(SavedTemplateViewHolder viewHolder, TemplateModelClass model, int position) {
                viewHolder.setTemplateNameView(model.getTemplateName());
                viewHolder.setTimeStampView(model.getDateCreated());
                viewHolder.setDaysView(model.getDays());
                viewHolder.setDescriptionView(model.getDescription());
                viewHolder.setActivity(getActivity());
                if(position == 0){
                    AVLoadingIndicatorView loadingView = (AVLoadingIndicatorView) getActivity().findViewById(R.id
                            .loadingView2);
                    if(loadingView != null){
                        loadingView.setVisibility(View.GONE);
                    }
                }
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        headerChanger("Saved Templates");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
            navChangerCallback = (bottomNavChanger) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString()
            //        + " must implement OnHeadlineSelectedListener");
        }
    }
}
