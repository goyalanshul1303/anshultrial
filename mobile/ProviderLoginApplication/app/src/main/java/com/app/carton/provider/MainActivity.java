package com.app.carton.provider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            if (null == SharedPreferences.getString(this, SharedPreferences.KEY_AUTHTOKEN) || SharedPreferences.getString(this, SharedPreferences.KEY_AUTHTOKEN).isEmpty()) {
                replaceLoginFragment(new ProviderLoginFragment());
            }else{
                replaceLoginFragment(new ChooseActivityFragment());
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
               // do logout
                SharedPreferences.logout(this);
                FragmentManager fm = getSupportFragmentManager();
                int count = fm.getBackStackEntryCount();
                for(int i = 0; i < count; ++i) {
                    fm.popBackStackImmediate();
                }
               replaceLoginFragment(new ProviderLoginFragment());
                return true;
            case R.id.completed_list:

                addActionFragment(new CompletedOrderListFragment());
                return true;
            case R.id.ongoingOrders:

                addActionFragment(new ProviderOngoingOrdersListFragment());
                return true;
            case R.id.placed_order:
                addActionFragment(new PlacedOrderListFragment());
                    return  true;
            case R.id.productForOffers:
                addActionFragment(new ProductListOpenForPrice());
                return  true;
            case android.R.id.home:
//                onBackPressed();
                fragmentManager.popBackStackImmediate();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Replace Login Fragment with animation
    protected static void replaceLoginFragment(Fragment fragment) {
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
