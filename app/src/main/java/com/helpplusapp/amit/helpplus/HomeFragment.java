package com.helpplusapp.amit.helpplus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.helpplusapp.amit.helpplus.model.HomeSampleContent;
import com.helpplusapp.amit.helpplus.model.Tags;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public ProgressBar mProgressBar;
    private TextView mEmptyTextMsg;
    private RecyclerView mHomeNotifRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    FirebaseListAdapter<HomeSampleContent> adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Firebase.setAndroidContext(getContext());
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        SwipeMenuListView homelist = (SwipeMenuListView)view.findViewById(R.id.home_listview);
        mFirebaseDatabaseReference.child("HomeContent");
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("HomeContent");
        adapter = new FirebaseListAdapter<HomeSampleContent>(getActivity(), HomeSampleContent.class, R.layout.item_home_message, mDatabaseReference)
        {

            @Override
            protected void populateView(View v, HomeSampleContent model, int position) {
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    ((TextView) v.findViewById(R.id.notifTextView)).setText(model.getHomeNotificationText());

            }

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }
        };
        homelist.setAdapter(adapter);

        //Swipe to action code
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(190, 190,
                        190)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("Take action");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                //share item menu
                SwipeMenuItem shareItem = new SwipeMenuItem(
                        getContext());
                shareItem.setBackground(new ColorDrawable(Color.rgb(30,144,255)));
                shareItem.setWidth(dp2px(90));
                shareItem.setIcon(R.drawable.ic_share_white_24dp);
                menu.addMenuItem(shareItem);


            }
        };

// set creator
        homelist.setMenuCreator(creator);

        homelist.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0: {
                        // Decide which action to take
                        HomeSampleContent homeSampleContent = adapter.getItem(position);
                        if (homeSampleContent.getActionType().equals("addScreenName")) {
//                            String UId = mFirebaseUser.getUid();
                            HashMap<String, Object> timestampCreated = new HashMap<>();
                            timestampCreated.put("timestamp", ServerValue.TIMESTAMP);
                            Tags tags = new Tags(homeSampleContent.getHomeContent(), timestampCreated);

                            mFirebaseDatabaseReference.child("users").child(mFirebaseUser.getUid()).child("tags").push().setValue(tags);
                            Toast.makeText(getContext(), homeSampleContent.getHomeContent() + " added to your tags", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 1:
                    {
                        HomeSampleContent homeSampleContent = adapter.getItem(position);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, homeSampleContent.getHomeNotificationText());
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.empty_tag_text_msg)));
                        break;
                    }
                    default:
                        Toast.makeText(getContext(),"Action cannot be completed!",Toast.LENGTH_SHORT).show();
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // set SwipeListener
        homelist.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        homelist.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sample_home_content, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if(item.getItemId() == R.id.action_add_home_content)
//        {
//            new MaterialDialog.Builder(getContext())
//                    .title(R.string.add_home_content_title_text)
//                    .content("Home Content")
//                    .inputType(InputType.TYPE_CLASS_TEXT )
//                    .inputRangeRes(5, 160, R.color.tw__composer_red)
//                    .input(R.string.home_content_input_hint, R.string.home_content_input_prefill, new MaterialDialog.InputCallback() {
//                        @Override
//                        public void onInput(MaterialDialog dialog, CharSequence input) {
////                            String UId = mFirebaseUser.getUid();
//                            HashMap<String, Object> timestampCreated = new HashMap<>();
//                            timestampCreated.put("timestamp", ServerValue.TIMESTAMP);
////                            String time = ServerValue.TIMESTAMP;
//                            HomeSampleContent homeContent = new HomeSampleContent(input.toString(),input.toString(),"addScreenName",timestampCreated);
//                            mFirebaseDatabaseReference.child("HomeContent").push().setValue(homeContent);
//                        }
//                    }).show();
//        }
        return  true;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
