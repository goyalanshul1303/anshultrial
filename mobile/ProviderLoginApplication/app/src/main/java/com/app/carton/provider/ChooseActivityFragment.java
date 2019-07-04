package com.app.carton.provider;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by aggarwal.swati on 12/26/18.
 */

public class ChooseActivityFragment extends Fragment implements View.OnClickListener {
    private static View view;
    private ProgressBar progressBar;
    private int inprogressCount, productPricingCount, quotationOrderCount;
    ActionAdapter adapter;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SlidingImageAdapter sliderPagerAdapter;
    ArrayList<Integer> slider_image_list;
    private TextView[] dots;
    int page_position = 0;

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(SharedPreferences.getString(getActivity(), "companyName"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choose_action, container, false);
        inflateViews();
        init(view);
        return view;
    }

    private void inflateViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new ActionAdapter(4);
        recyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new ActionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if ((int) view.getTag() == 0) {
                    // Get FragmentManager and FragmentTransaction object.
                    MainActivity.addActionFragment(new ProviderOngoingOrdersListFragment());

                } else if ((int) view.getTag() == 1) {
                    MainActivity.addActionFragment(new CompletedOrderListFragment());
                } else if ((int) view.getTag() == 2) {
                    MainActivity.addActionFragment(new PlacedOrderListFragment());
                } else if ((int) view.getTag() == 3) {
                    MainActivity.addActionFragment(new ProductListOpenForPrice());
                }

            }
        });
        new FetchProviderDashboard().execute();
    }

    public class FetchProviderDashboard extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_DASHBOARD_DETAILS);
                URL url = new URL(string.toString());


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", SharedPreferences.getString(getActivity(), SharedPreferences.KEY_AUTHTOKEN));

                InputStream inputStream;

                if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp, response = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }

                return response.toString();


            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject object = null;
            progressBar.setVisibility(View.GONE);

            if (null != result) {
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != object) {
                    if (!object.optString("status").isEmpty()) {
                        if ((Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST)
                                || (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)
                                || ((Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_FORBIDDEN))) {
                            Toast.makeText(getActivity(), "Something went wrong please try again",
                                    Toast.LENGTH_LONG).show();
                            MainActivity.replaceLoginFragment(new ProviderLoginFragment());

                        } else {
                            Toast.makeText(getActivity(), "Something went wrong please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {

//                        new OrderDetailFragment.FetchDetailsTask().execute();
                        parseOrderListingData(object);
                        adapter.notifyDataSetChanged();


                    }
                }
            } else {
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

    private void parseOrderListingData(JSONObject object) {
        inprogressCount = object.optInt("inProgressCount");
        quotationOrderCount = object.optInt("quotationOrderCount");
        productPricingCount = object.optInt("productPricingCount");
        adapter.setCardCount(quotationOrderCount, productPricingCount, inprogressCount);
    }

    private void init(View view) {

        vp_slider = (ViewPager) view.findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) view.findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();
        Integer[] IMAGES = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};

//Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
// here i am adding few sample image links, you can add your own

        for (int i = 0; i < IMAGES.length; i++)
            slider_image_list.add(IMAGES[i]);

        sliderPagerAdapter = new SlidingImageAdapter(getActivity(), slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (null != getActivity())
                    addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (null != getActivity())
            addBottomDots(0);

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 3000);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }
}
