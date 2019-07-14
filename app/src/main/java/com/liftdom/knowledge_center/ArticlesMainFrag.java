package com.liftdom.knowledge_center;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesMainFrag extends Fragment {


    public ArticlesMainFrag() {
        // Required empty public constructor
    }

    headerChangeToFrag mCallback;

    public interface headerChangeToFrag{
        public void changeHeader(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeader(title);
    }

    @BindView(R.id.collapsedArticleHolder) LinearLayout collapsedArticleHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articles_main, container, false);

        ButterKnife.bind(this, view);

        headerChanger("Articles");

        //TODO: Here we'd fetch article headers and maybe identifiers? Then add them to LL.
        // Also, you'll be able to see views and directly rep the author

        // Just a test article
        // get an instance of FragmentTransaction from your Activity
        if(savedInstanceState == null){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //add a fragment
            ArticleCollapsedFrag articleTest = new ArticleCollapsedFrag();
            articleTest.articleTitleString = "Lorem Ipsum, yada yada yada?";
            fragmentTransaction.add(R.id.collapsedArticleHolder, articleTest);

            fragmentTransaction.commit();
        }

        //if(savedInstanceState != null){
        //    setSchemeString = savedInstanceState.getString("set_scheme_string");
        //    setSchemesView.setText(setSchemeString);
        //}

        //TODO: I think we'l add them to an arraylist and then save that in the bundle

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeToFrag) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    //@Override
    //public void onSaveInstanceState(Bundle savedInstanceState) {
//
    //    savedInstanceState.putString("set_scheme_string", setSchemeString);
//
    //    super.onSaveInstanceState(savedInstanceState);
    //}

}
