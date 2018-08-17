package flavor.tech.com.iptfwebrtc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import flavor.tech.com.iptfwebrtc.R;
import flavor.tech.com.iptfwebrtc.fragment.CoBrowsingFragment;
import flavor.tech.com.iptfwebrtc.fragment.DashboardFragment;
import flavor.tech.com.iptfwebrtc.fragment.LiveCameraFragment;
import flavor.tech.com.iptfwebrtc.fragment.ScreenSharingFragment;
import flavor.tech.com.iptfwebrtc.fragment.ShareSessionFragment;
import flavor.tech.com.iptfwebrtc.fragment.ToolsFragment;
import flavor.tech.com.iptfwebrtc.services.GetServerData;
import flavor.tech.com.iptfwebrtc.util.Constant;
import flavor.tech.com.iptfwebrtc.util.ExternalHttpClient;
import flavor.tech.com.iptfwebrtc.util.SharedObject;
import flavor.tech.com.iptfwebrtc.util.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedObject sharedObject = null;
    FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedObject = (SharedObject) getApplicationContext();
        sharedObject.setData("This is testing of safe shared object");

        sharedObject.setActivity(this);
        sharedObject.setBundle(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        sharedObject.setFragmentManager(fragmentManager);

        //Checking server URL is available or not.
        if(Utility.getSharedPreferencesByValue(MainActivity.class.getCanonicalName(), MainActivity.this, "SERVER_URL") == null ){
            Log.i(Constant.TAG, "Server URL not found!");
            getServerURLInputDialog();
        }else{
            Log.i(Constant.TAG, "Server URL found!");
            sharedObject.setServerURL(Utility.getSharedPreferencesByValue(MainActivity.class.getCanonicalName(), MainActivity.this, "SERVER_URL"));
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
       // ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).addToBackStack(null).commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.i(Constant.TAG, "onDoubleTap");
                    getServerURLInputDialog();
                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    Log.i(Constant.TAG, "onSingleTap");
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).addToBackStack(null).commit();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }


        });

        if(sharedObject.getCallStarted() ) {
            Log.i(Constant.TAG, "Call started");
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new LiveCameraFragment()).addToBackStack(null).commit();
        } else {
            Log.i(Constant.TAG, "Call not started");
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).addToBackStack(null).commit();
            }
        }
        //sharedObject.setServerURL(Constant.WebRTCRoomSErviceURL);
    }


    public void getServerURLInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Input server URL");


// Set up the input
        final EditText input = new EditText(MainActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT );//| InputType.TYPE_TEXT_VARIATION_PASSWORD);
        if(sharedObject.getServerURL() != null ){
            input.setText(sharedObject.getServerURL());
        }
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString() != null) {
                    sharedObject.setServerURL(input.getText().toString());
                    // Saving server url to get the room link
                    HashMap<String, String> data = new HashMap<>();
                    data.put("SERVER_URL", sharedObject.getServerURL());
                    Utility.setSharedPreferences(MainActivity.class.getCanonicalName(), data, MainActivity.this, false);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_live_camera) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LiveCameraFragment()).addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_co_browsing) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CoBrowsingFragment()).addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_screen_sharing) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScreenSharingFragment()).addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_tool) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToolsFragment()).addToBackStack(null)
                    .commit();
//        } else if (id == R.id.nav_call_support) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CallSuportFragment()).commit();
        } else if (id == R.id.nav_share_session) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareSessionFragment()).addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        Log.i(Constant.TAG,"djjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
        if(sharedObject.getCallStarted() ) {
            Log.i(Constant.TAG, "Call started");
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new LiveCameraFragment()).addToBackStack(null).commit();
        }
    }
}
