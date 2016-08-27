package com.helpplusapp.amit.helpplus.widget;

/**
 * Created by amit on 8/9/2016.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.helpplusapp.amit.helpplus.R;
import com.helpplusapp.amit.helpplus.model.HomeSampleContent;

import java.util.ArrayList;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */
public class ListProvider implements RemoteViewsFactory {
    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private int appWidgetId;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Firebase.setAndroidContext(context);
    }

    private void populateListItem() {

        Firebase ref = new Firebase("https://helpplusapp-318b8.firebaseio.com/HomeContent");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("HomeContent");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            ListItem listItem;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItemList.clear();
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    HomeSampleContent post = postSnapshot.getValue(HomeSampleContent.class);

                    listItem = new ListItem();
                    listItem.setContent("");
                    listItem.setHeading(post.getHomeNotificationText());
                    listItemList.add(listItem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
        context.getPackageName(), R.layout.list_row);
        ListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.heading, listItem.getHeading());
//        remoteView.setTextViewText(R.id.content, listItem.getHeading());

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        populateListItem();
    }

    @Override
    public void onDestroy() {
    }
}
