package com.app.carton.consumer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.carton.orders.R;

public class MainActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            if (null == SharedPreferences.getString(this, SharedPreferences.KEY_AUTHTOKEN) || SharedPreferences.getString(this, SharedPreferences.KEY_AUTHTOKEN).isEmpty()) {
                    replaceLoginFragment(new ConsumerLoginFragment());
                }else{
                    replaceLoginFragment(new ConsumerOrderListFragment());
            }
            }


    }

    // Replace Login Fragment with animation
    public static void replaceLoginFragment(Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, fragment,
                        "choose_action").commit();
    }

    // Replace Login Fragment with animation
    protected static void addActionFragment(Fragment fragment) {

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, fragment)
                .addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed () {

        super.onBackPressed();
    }
}
