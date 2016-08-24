package com.helpplusapp.amit.helpplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.helpplusapp.amit.helpplus.model.Constants;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class MyPostsFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private SwipeRefreshLayout swipeLayout;

    public MyPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPostsFragment newInstance(String param1, String param2) {
        MyPostsFragment fragment = new MyPostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();



//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
// Can also use Twitter directly: Twitter.getApiClient()
//        SearchService searchService = twitterApiClient.getSearchService();
//        searchService.tweets("@helpplusapp", null, null, null, null, 100, null, null,
//                null, true, new Callback<Search>() {
//                    @Override
//                    public void success(Result<Search> searchResult) {
//                        final List<Tweet> tweets = searchResult.data.tweets;
//                        // Use tweets as you like
//                        for (Tweet t:tweets
//                             ) {
//                            Log.d("search results:",t.text);
//                        }
//
//                    }
//
//                    @Override
//                    public void failure(TwitterException error) {
//                        Log.d("search failed:","Unsuccessful");
//                    }
//                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Log.d("User data",mFirebaseUser.getProviderData().toString());
//        Log.d("User data",mFirebaseUser.getProviderData().get(0).toString());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
//        List proData = mFirebaseUser.getProviderData();
//        for (Object ui:proData
//             ) {
//            Log.d("Twitter Data:",ui.toString());
//        }

//        if (mFirebaseUser != null) {
//            for (UserInfo profile : mFirebaseUser.getProviderData()) {
//                // Id of the provider (ex: google.com)
//                String providerId = profile.getProviderId();
//
//                // UID specific to the provider
//                String uid = profile.getUid();
//
//                // Name, email address, and profile photo Url
//                String name = profile.getDisplayName();
//                String email = profile.getEmail();
//                Uri photoUrl = profile.getPhotoUrl();
//                Log.d("Twitter details:",name+email+profile.getProviderId());
//            }
//        }

//        TwitterSession session = Twitter.getSessionManager().getActiveSession(getActivity());
//        Log.d("Twitter details:",session.getUserName());

        String screenname="";
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                Constants.TWITTER_USERNAME_PREF, Context.MODE_PRIVATE);
        sharedPref.getString(Constants.TWITTER_USERNAME,screenname);

        Log.d("Screename",screenname);
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(screenname)
                .includeReplies(true)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Toast or some other action
                    }
                });
            }
        });
        return view;

    }

}
