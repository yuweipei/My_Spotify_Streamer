package com.example.david.myspotifystreamer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {


    //private
    private MovieInfo mMovieInfo;
    private class BiString{
        String mFirst;
        String mSecond;
        BiString(String first, String second) {
            this.mFirst = first;
            this.mSecond = second;
        }
    };

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //fetchDetailTask.execute("reviews");
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
            mMovieInfo = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detailTitleText))
                    .setText(mMovieInfo.getTitleName());

            ((TextView) rootView.findViewById(R.id.userRate))
                    .setText(mMovieInfo.getVoteAverage());

            ((TextView) rootView.findViewById(R.id.releaseDate))
                    .setText(mMovieInfo.getReleaseDate());

            ((TextView) rootView.findViewById(R.id.overview))
                    .setText(mMovieInfo.getOverview());



            ImageView imageView = (ImageView) rootView.findViewById(R.id.detailPostalView);
            Picasso.with(getContext())
                    .load(mMovieInfo.getPosterPath())
                    .noFade()
                    .into(imageView);


            new FetchDetailTask().execute("videos");
            //
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new FetchDetailTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "reviews");
            } else {
                new FetchDetailTask().execute("reviews");
            }

        }

        return rootView;
    };

    public class FetchDetailTask extends AsyncTask<String, Void, ArrayList<BiString>> {

        private final String LOG_TAG = FetchDetailTask.class.getSimpleName();

        private String mType="";

        private ArrayList<BiString> getMovieDataFromJson(String JsonStr)
                throws JSONException {

            // These are names of the js    on object that need to be extracted
            // For videos
            final String OWM_RESULTS = "results";
            final String OWM_VIDEO_KEY = "key";
            final String OWM_SITE = "site";
            final String OWM_AUTHOR = "author";
            final String OWM_CONTENT = "content";
            //final String OWM_VOTE = "vote_average";
            //final String OWM_RELEASE = "release_date";

            JSONObject movieJson = new JSONObject(JsonStr);

            JSONArray resultArray = movieJson.getJSONArray(OWM_RESULTS);

            ArrayList<BiString> arrayList = new ArrayList<BiString>();
            for (int i = 0; i < resultArray.length(); i++) {

                // Get the JSON Object of each movie
                JSONObject eachObject = resultArray.getJSONObject(i);

                if ( mType == "videos" ) {
                    String fetchURL = eachObject.getString(OWM_VIDEO_KEY);
                    String videoUrl="";
                    //if (eachObject.getString(OWM_SITE) == "YouTube") {
                        videoUrl = "www.youtube.com/watch?v=" + fetchURL;
                    //}
                    Log.v(LOG_TAG, "video: "+i +  videoUrl + "\n");
                    arrayList.add(new BiString(videoUrl, ""));
                } else if (mType == "reviews"){
                    String author = eachObject.getString(OWM_AUTHOR);
                    String content = eachObject.getString(OWM_CONTENT);
                    arrayList.add(new BiString(author,content));
                    Log.v(LOG_TAG, "review: "+ i + author + " " + content + "\n");
                } else {
                    Log.v(LOG_TAG, "wrong controlString");
                    throw new RuntimeException();
                }
                //
                //Log.v(LOG_TAG, "Postal_url:" + actualURL);
                //arrayList.add(i, new MovieInfo(actualURL,title,overview,vote,releaseDate));
            }

            return arrayList;
        }


        @Override
        protected ArrayList<BiString> doInBackground(String... params) {
            // Save the string of what type the string is
            mType = params[0];
            // Declare out side try/catch
            // so that they can be closed in the finally block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string
            String jsonStr = null;

            // Construct the URL for video and reviews
            final String REQUEST_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String APP_KEY = "api_key";
            //String searchVideoArea = "videos";
            //String searchReviewArea = "reviews";
            String myID = "d3fa940065011fb83cc29169d05e8019";
            try {
                // Create URL for movie video
                Uri builtUri = Uri.parse(REQUEST_BASE_URL).buildUpon()
                        .appendPath(mMovieInfo.getMovieId())
                        .appendPath(mType)
                        .appendQueryParameter(APP_KEY, myID)
                        .build();

                URL videoUrl = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built Video URI: " + builtUri.toString());

                // Create the request to themoviedb
                urlConnection = (HttpURLConnection) videoUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if ( inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                if (stringBuffer.length() == 0) {
                    // Stream was empty
                    return null;
                }
                jsonStr = stringBuffer.toString();
                Log.v(LOG_TAG, "JSON String: " + params[0] + jsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage() + " JSON RETRIEVE ERROR: ", e);
            }

            try {
                return getMovieDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage() + " JSON WRAPPER ERROR: " + params[0], e);
                e.printStackTrace();
            }

            return null;
        }


    }

}
