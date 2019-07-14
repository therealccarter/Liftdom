package com.liftdom.knowledge_center;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleCollapsedFrag extends Fragment {


    public ArticleCollapsedFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.articleTitleView) TextView articleTitle;
    @BindView(R.id.articleImage) ImageView articleImage;

    public String articleTitleString;
    public int articleImageInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_collapsed, container, false);

        ButterKnife.bind(this, view);

        articleTitle.setText(articleTitleString);

        // I think the best solution will be header as article key + an identifier (asdfdf - 123)

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // here we'd create a new Article Details Frag, and pass in the key.
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //add a fragment
                ArticlesDetailFrag articleDetail = new ArticlesDetailFrag();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.collapsedArticleHolder, articleDetail);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
