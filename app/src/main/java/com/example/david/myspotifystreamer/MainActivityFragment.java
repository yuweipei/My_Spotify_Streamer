package com.example.david.myspotifystreamer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
public class MainActivityFragment extends Fragment {

    private ImageAdapter imageAdapter;

    private GridView gridView;

    private ArrayList<MovieInfo> movieInfos;

    public MainActivityFragment() {
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
        inflater.inflate(R.menu.movieview,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle refresh here
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchMovieTask fetchMovieTask = new FetchMovieTask();
            fetchMovieTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //setContentView(R.layout.activity_main);

        // Use ImageAdapter to create GridView

        imageAdapter = new ImageAdapter(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.movieGrid);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                /*
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
                */
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, (MovieInfo) movieInfos.get(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, ArrayList<MovieInfo>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private ArrayList<MovieInfo> getMovieDataFromJson(String JsonStr)
                throws JSONException {

            // These are names of the json object that need to be extracted
            final String OWM_RESULTS = "results";
            final String OWM_POSTER = "poster_path";
            final String OWM_TITLE = "original_title";
            final String OWM_OVERVIEW = "overview";
            final String OWM_VOTE = "vote_average";

            JSONObject movieJson = new JSONObject(JsonStr);

            JSONArray resultArray = movieJson.getJSONArray(OWM_RESULTS);

            ArrayList<MovieInfo> arrayList = new ArrayList<MovieInfo>();
            for (int i = 0; i < resultArray.length(); i++) {

                // Get the JSON Object of each movie
                JSONObject eachMovie = resultArray.getJSONObject(i);

                //
                String postalURL = eachMovie.getString(OWM_POSTER);
                String actualURL = "http://image.tmdb.org/t/p/w185" + postalURL;
                String title = eachMovie.getString(OWM_TITLE);
                String overview = eachMovie.getString(OWM_OVERVIEW);
                String vote = eachMovie.getString(OWM_VOTE);
                Log.v(LOG_TAG, "Postal_url:" + actualURL);
                arrayList.add(i, new MovieInfo(actualURL,title,overview,vote));
            }

            return arrayList;
         }



        @Override
        protected ArrayList<MovieInfo> doInBackground(Void... params) {

            if (params == null) {
                return null;
            }

            // Declare out side try/catch
            // so that they can be closed in the finally block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string
            String jsonStr = null;

            //
            String searchArea = "movie";
            String[] sortParamList = {
                    "popularity.desc",
                    "vote_average.desc",
                    "vote_count.desc"
            };
            String myID = "d3fa940065011fb83cc29169d05e8019";
            try {
                // Construct the URL for the
                final String REQUEST_BASE_URL = "http://api.themoviedb.org/3/discover";
                final String QUERY_PARAM = "";
                final String APP_KEY = "api_key";
                final String SORT_PARAM = "sort_by";

                Uri builtUri = Uri.parse(REQUEST_BASE_URL).buildUpon()
                        .appendPath(searchArea)
                        .appendQueryParameter(SORT_PARAM, sortParamList[0])
                        .appendQueryParameter(APP_KEY, myID)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI: " + builtUri.toString());

                // Create the request to themoviedb
                urlConnection = (HttpURLConnection) url.openConnection();
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
                //Log.v(LOG_TAG, "JSON String: " + jsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } finally {
                if (urlConnection != null) {
                    // still connected
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, e.getMessage(), e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> result) {
            if (result != null) {
                movieInfos = result;
                imageAdapter.clear();
                imageAdapter.addAll(result);
            }
        }

    }
}
