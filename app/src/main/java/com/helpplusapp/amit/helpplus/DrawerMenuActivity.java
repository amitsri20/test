package com.helpplusapp.amit.helpplus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

public class DrawerMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,PostsFragment.OnFragmentInteractionListener
         {

    private FirebaseAuth mAuth;
    TextView header_primary_text;
    ImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Firebase persistance
        if (!(Firebase.getDefaultConfig().isPersistenceEnabled()))
        {
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
            Firebase.setAndroidContext(this);
        }
        setContentView(R.layout.activity_drawer_menu);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig),new TwitterCore(authConfig), new TweetComposer());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                TweetComposer.Builder builder = new TweetComposer.Builder(DrawerMenuActivity.this)
                        .text("#heplplus");

                builder.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
//        Toast.makeText(this,user.getDisplayName(),Toast.LENGTH_SHORT).show();

        View header=navigationView.getHeaderView(0);
        header_primary_text=(TextView)header.findViewById(R.id.nav_header_text_primary);
        headerImage=(ImageView) header.findViewById(R.id.nav_header_image);
        header_primary_text.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl().toString()).into(headerImage);
//        Log.d("Image url",user.getPhotoUrl().toString());
//        HomeFragment homeFragment =  ((HomeFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.fragment_home));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new HomeFragment());
            ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

       if (id == R.id.nav_home) {

            ft.replace(R.id.fragment_container, new HomeFragment());
            ft.commit();
        } else if (id == R.id.nav_posts) {

           ft.replace(R.id.fragment_container, new PostsFragment());
           ft.commit();
        } else if (id == R.id.nav_myposts) {

           ft.replace(R.id.fragment_container, new MyPostsFragment());
           ft.commit();
        } else if (id == R.id.nav_manage) {
           ft.replace(R.id.fragment_container, new TagsFragment());
           ft.commit();
        } else if (id == R.id.nav_signout) {
            signOut();
           startActivity(new Intent(this,TwitterLoginActivity.class));
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void signOut() {
        mAuth.signOut();
        Twitter.logOut();
    }
}
