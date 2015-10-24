package com.example.david.myspotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Use its own movieview to inflate the view
        // use xml view to create a menu option.
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();


        if ( intent != null ) {
            MovieInfo movieInfo = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detailTitleText))
                    .setText(movieInfo.getTitleName());

            ImageView imageView = (ImageView) rootView.findViewById(R.id.detailPostalView);
            Picasso.with(getContext())
                    .load(movieInfo.getPosterPath())
                    .noFade()
                    .into(imageView);

            ((TextView) rootView.findViewById(R.id.detailOtherText))
                    .setText(movieInfo.getOverview());
        }

        return rootView;
    };
}
