package com.liftdom.template_housing;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.PPL.PPLHolderActivity;
import com.liftdom.workout_programs.Smolov.SmolovHolderActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PremadeTemplatesFrag extends Fragment {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.smolovButton) Button smolovButton;
    @BindView(R.id.pplButton) Button pplButton;
    @BindView(R.id.comingSoonView) TextView comingSoonView;

    public PremadeTemplatesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premade_templates, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        comingSoonView.setTypeface(lobster);

        smolovButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent;
            intent = new Intent(getContext(), SmolovHolderActivity.class);
            startActivity(intent);
            }
        });

        pplButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), PPLHolderActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
