package com.app.carton.consumer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.carton.orders.R;

public class MainActivity extends AppCompatActivity {
    public static final int RESULT_PG_SUCCESS = 200;
    public static final int RESULT_FAILURE = 400;
    public static final int REQUEST_CODE = 100;
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
                    replaceLoginFragment(new ConsumerLoginFragment());
                }else{
                    replaceLoginFragment(new ChooseActivityFragment());
            }
            }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_create_order);
        if(item!=null)
            item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
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
                replaceLoginFragment(new ConsumerLoginFragment());
                return true;
            case R.id.open_orders_list:
                addActionFragment(new ConsumerOrderListFragment());
                return  true;
            case R.id.requirements_list:
                addActionFragment(new ConsumerRequirementsListFragment());
                return  true;
                case R.id.product_list:
                addActionFragment(new ConsumerProductsListFragment());
                    return true;
            case android.R.id.home:
//                onBackPressed();
                fragmentManager.popBackStackImmediate();
            default:
                return super.onOptionsItemSelected(item);
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
    @Override
    public void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
        fragmentManager = null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_FAILURE) {
                if (null!=this){
                    Toast.makeText(this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == RESULT_PG_SUCCESS ) {
                android.support.v4.app.Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.frameContainer);
                if(f instanceof CreateOrderFragment)
                    // do something with f
                    ((CreateOrderFragment) f).createOrderTask();
               else if(f instanceof QuotationListingFragment)
                    // do something with f
                    ((QuotationListingFragment) f).awardQuoteMethod();

        }
    }
}
