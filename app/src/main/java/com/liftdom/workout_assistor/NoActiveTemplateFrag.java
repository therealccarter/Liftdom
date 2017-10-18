package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.liftdom.template_housing.TemplateMenuFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoActiveTemplateFrag extends Fragment {


    public NoActiveTemplateFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.toTemplatesButton) Button toTemplatesButton;
    @BindView(R.id.newTemplateButton) Button fromScratch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_active_template, container, false);

        ButterKnife.bind(this, view);

        toTemplatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                fragmentTransaction.commit();
            }
        });

        fromScratch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "no";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("isEdit", isEdit );
                startActivity(intent);
            }
        });

        return view;
    }

}
