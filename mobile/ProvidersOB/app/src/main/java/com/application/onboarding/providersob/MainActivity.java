package com.application.onboarding.providersob;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            if (null == SharedPreferences.getString(this, SharedPreferences.KEY_AUTHTOKEN) || SharedPreferences.getString(this, SharedPreferences.KEY_AUTHTOKEN).isEmpty()) {
                replaceLoginFragment(new UserAdminLoginFragment());
            }else{
                replaceLoginFragment(new ChooseListActivityFragment());
            }

        }

//        if (savedInstanceState == null ) {
//            fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.frameContainer, new UserAdminLoginFragment(),
//                            Utils.Login_Fragment).commit();
//        }


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
                replaceLoginFragment(new UserAdminLoginFragment());
                return true;
            case R.id.offerPrice:
                addActionFragment(new OfferPriceProductList());
                return  true;
            case R.id.allOrders:
                addActionFragment(new AllOrderListFragment());
                return true;
            case android.R.id.home:
//                onBackPressed();
                fragmentManager.popBackStackImmediate();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Replace Login Fragment with animation
    public static void replaceLoginFragment(Fragment fragmentName) {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer,fragmentName,
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item=menu.findItem(R.id.action_add_agent);
        if(item!=null)
            item.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

}


