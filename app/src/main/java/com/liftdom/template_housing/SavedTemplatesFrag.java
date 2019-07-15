package com.liftdom.template_housing;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
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
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.liftdom.template_editor.TemplateModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import me.toptas.fancyshowcase.FancyShowCaseView;

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

    public boolean isFromSendProgram;
    public String uidFromOutside;

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
    @BindView(R.id.selectProgramTitleView) TextView selectProgramTitleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_templates, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        if(isFromSendProgram){
            selectProgramTitleView.setVisibility(View.VISIBLE);
            setMargins(mRecyclerView, 0, 0, 0, 0);
        }

        if(savedInstanceState == null) {
            if(!isFromSendProgram){
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
                                }else{
                                    loadingView.setVisibility(View.GONE);
                                    setUpFirebaseAdapter();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }else{
                loadingView.setVisibility(View.GONE);
                setUpFirebaseAdapter();
            }
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



        return view;
    }

    private void setUpFirebaseAdapter(){

        Query query = mFeedRef.orderByChild("dateUpdated");

        FirebaseRecyclerOptions<TemplateModelClass> options = new FirebaseRecyclerOptions
                .Builder<TemplateModelClass>()
                .setQuery(query, TemplateModelClass.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<TemplateModelClass, SavedTemplateViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SavedTemplateViewHolder holder, int position, @NonNull TemplateModelClass model) {
                if(isFromSendProgram){
                    holder.setFromSendProgram(true);
                    holder.setUidFromOutside(uidFromOutside);
                }else{
                    holder.setFromSendProgram(false);
                }
                holder.setTemplateNameView(model.getTemplateName());
                holder.setTimeStampView(model.getDateUpdated());
                holder.setDaysView(model.getDays());
                holder.setDescriptionView(model.getDescription());
                holder.setActivity(getActivity());
            }

            @Override
            public SavedTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.saved_template_list_item,
                        parent, false);

                return new SavedTemplateViewHolder(view);
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mFirebaseAdapter.startListening();
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(mFirebaseAdapter != null && mFirebaseAdapter.getItemCount() == 0){
            mFirebaseAdapter.startListening();
        }
        //final DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child
        //        ("firstTime").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child
        //        ("isSavedProgFirstTime");
        //firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(DataSnapshot dataSnapshot) {
        //        if(dataSnapshot.exists()){
        //            new FancyShowCaseView.Builder(getActivity())
        //                    .title("All of your saved programs will be here. \n Your Active Template will have a " +
        //                    "gold" +
        //                            " title.")
        //                    .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
        //                    .build().show();
//
        //            firstTimeRef.setValue(null);
//
        //        }
        //    }
//
        //    @Override
        //    public void onCancelled(DatabaseError databaseError) {
//
        //    }
        //});
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
        }
    }

}
