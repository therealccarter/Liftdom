package com.liftdom.knowledge_center;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //add a fragment
        ArticleCollapsedFrag articleTest = new ArticleCollapsedFrag();
        articleTest.articleTitleString = "Lorem Ipsum, yada yada yada?";
        fragmentTransaction.add(R.id.collapsedArticleHolder, articleTest);

        fragmentTransaction.commit();

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


}
