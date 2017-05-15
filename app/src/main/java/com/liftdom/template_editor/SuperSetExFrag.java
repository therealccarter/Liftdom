package com.liftdom.template_editor;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuperSetExFrag extends android.app.Fragment {


    public SuperSetExFrag() {
        // Required empty public constructor
    }

    String fragTag;
    boolean isEdit;
    int initialSchemeCount = 0;


    public interface removeFragCallback2{
        void removeFrag2(String fragTag);
    }

    private removeFragCallback2 removeFragCallback;

    // Butterknife
    @BindView(R.id.movementName) Button exerciseButton;
    @BindView(R.id.destroyFrag) ImageButton destroyFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_set_ex, container, false);

        ButterKnife.bind(this, view);

        removeFragCallback = (removeFragCallback2) getParentFragment();

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFragCallback.removeFrag2(fragTag);
            }
        });

        for(int i = 0; i < initialSchemeCount; i++){
            String fragString = "sss" + Integer.toString(i + 1);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            SetsLevelSSFrag superSetSetFrag = new SetsLevelSSFrag();
            fragmentTransaction.add(R.id.superSetSchemeHolder, superSetSetFrag, fragString);
            fragmentTransaction.commitAllowingStateLoss();
        }

        return view;
    }

    public void addSetScheme(String tag){

        String fragString = "sss" + tag;
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        SetsLevelSSFrag superSetSetFrag = new SetsLevelSSFrag();
        fragmentTransaction.add(R.id.superSetSchemeHolder, superSetSetFrag, fragString);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void removeSetScheme(String tag){
        String fragString = "sss" + tag;
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(fragString));
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onPause(){
        super.onPause();

    }

}
