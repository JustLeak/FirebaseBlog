package com.android.evgeniy.firebaseblog.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.evgeniy.firebaseblog.R;
import com.android.evgeniy.firebaseblog.fragments.CreateNoteFragment;
import com.android.evgeniy.firebaseblog.fragments.FriendsFragment;
import com.android.evgeniy.firebaseblog.fragments.MapFragment;
import com.android.evgeniy.firebaseblog.fragments.NotesFragment;
import com.android.evgeniy.firebaseblog.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

public class NotesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorTextWhite));
        toolbar.setSubtitle(getResources().getString(R.string.notes));


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                toolbar.setSubtitle(getResources().getString(R.string.profile));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_home:
                toolbar.setSubtitle(getResources().getString(R.string.notes));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent = new Intent(this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.nav_add:
                toolbar.setSubtitle(getResources().getString(R.string.add_note));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateNoteFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_map:
                toolbar.setSubtitle(getResources().getString(R.string.map));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_friends:
                toolbar.setSubtitle(getResources().getString(R.string.friends));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FriendsFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_communicate:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JustLeak/FirebaseBlog"));
                startActivity(browserIntent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(), "ssss", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
