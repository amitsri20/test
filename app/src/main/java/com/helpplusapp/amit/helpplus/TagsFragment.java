package com.helpplusapp.amit.helpplus;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.helpplusapp.amit.helpplus.model.Tags;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.HashMap;

import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TagsFragment extends Fragment  {



    public TagsFragment() {
        // Required empty public constructor
//        initFire();
    }
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public ProgressBar mProgressBar;
    private RecyclerView mTagsRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Tags, TagsViewHolder> mFirebaseAdapter;



    public static class TagsViewHolder extends RecyclerView.ViewHolder{
        public TextView tagTextView;

        public TagsViewHolder(View v) {
            super(v);
            tagTextView = (TextView) itemView.findViewById(R.id.tagTextView);
//            tagTextView.setOnClickListener(this);
//            deleteTagButton.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            if(v.getId() == R.id.delete_tag_btn)
//            {
//                Log.d("delete butto pressed at",Integer.toString(getAdapterPosition()));
//            }
//            else
////                        if (v.getId() == R.id.tagTextView)
//            {
//                new TagsFragment().openTweetWindow(getAdapterPosition(),v.getContext());
//            }
//        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getContext());

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        mTagsRecyclerView = (RecyclerView) view.findViewById(R.id.tagsRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Tags, TagsViewHolder>(
                Tags.class,
                R.layout.item_message,
                TagsViewHolder.class,
                mFirebaseDatabaseReference.child("users").child(mFirebaseUser.getUid()).child("tags")

        ) {

            @Override
            protected void populateViewHolder(TagsViewHolder viewHolder, Tags tags, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
//                if((tags.getUserid()).equals(mFirebaseUser.getUid()))
                viewHolder.tagTextView.setText(tags.getTagname());
            }
        };


        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });

        mTagsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mTagsRecyclerView.setAdapter(mFirebaseAdapter);

        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                mTagsRecyclerView,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        int id = mTagsRecyclerView.getChildPosition(view);
                        mFirebaseAdapter.getRef(id).removeValue();
                        mFirebaseAdapter.notifyDataSetChanged();
                        Snackbar snackbar = Snackbar
                                .make(view,String.format("%s deleted",mFirebaseAdapter.getItem(id).getTagname()),Snackbar.LENGTH_SHORT);

                        snackbar.show();
                    }
                })
                .setIsVertical(false)
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        // Do what you want when item be clicked
                        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                                    .getActiveSession();
                            TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
                                    .text(mFirebaseAdapter.getItem(position).getTagname());
                            builder.show();
                            Log.d("Item clicked at",Integer.toString(position));
                         }
                    })
                .create();

        mTagsRecyclerView.setOnTouchListener(listener);


//        mTagsRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(),
//                new RecyclerViewItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View v, int position) {
//                        //TODO:
//                        // do something on item click
//                        if(v.getId() == R.id.delete_tag_btn)
//                        {
//                            Log.d("delete butto pressed at",Integer.toString(position));
//                        }
//                        else
////                        if (v.getId() == R.id.tagTextView)
//                        {
//                            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
//                                    .getActiveSession();
//                            TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
//                                    .text(mFirebaseAdapter.getItem(position).getTagname());
//                            builder.show();
//                            Log.d("Item clicked at",Integer.toString(position));
//                        }
//
//                    }
//                }));


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tags_fragment, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add_tag)
        {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.addtag_title_text)
                    .content("Hashtag or Screen text")
                    .inputType(InputType.TYPE_CLASS_TEXT )
                    .inputRangeRes(2, 20, R.color.tw__composer_red)
                    .input(R.string.tags_input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            String UId = mFirebaseUser.getUid();
                            HashMap<String, Object> timestampCreated = new HashMap<>();
                            timestampCreated.put("timestamp", ServerValue.TIMESTAMP);
                            Tags tags = new Tags(input.toString(),timestampCreated);
                            mFirebaseDatabaseReference.child("users").child(mFirebaseUser.getUid()).child("tags").push().setValue(tags);
                        }
                    }).show();
        }
        return  true;
    }

    private void openTweetWindow(int position, Context context) {
        initFire();
        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                .getActiveSession();
        TweetComposer.Builder builder = new TweetComposer.Builder(context)
                .text(mFirebaseAdapter.getItem(position).getTagname());
//                .text("njio");
        builder.show();
        Log.d("Item clicked at",Integer.toString(position));
    }

    private void initFire() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Tags, TagsViewHolder>(
                Tags.class,
                R.layout.item_message,
                TagsViewHolder.class,
                mFirebaseDatabaseReference.child("tags")) {

            @Override
            protected void populateViewHolder(TagsViewHolder viewHolder, Tags tags, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.tagTextView.setText(tags.getTagname());
            }
        };
    }
}
