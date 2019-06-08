package com.app.carton.provider;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * Created by aggarwal.swati on 12/26/18.
 */

public class ChooseActivityFragment extends Fragment implements View.OnClickListener {
    private static View view;
    private LinearLayout completedCardView, waitingCardView, progressCardView,pricingCardView;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.progressCardView){
            // Get FragmentManager and FragmentTransaction object.
           MainActivity.addActionFragment(new ProviderOngoingOrdersListFragment());

        }else if (view.getId() == R.id.completedCardView){
            MainActivity.addActionFragment(new CompletedOrderListFragment());
        }else if (view.getId() == R.id.waitingCardView){
            MainActivity.addActionFragment(new PlacedOrderListFragment());
        }
        else if (view.getId() == R.id.pricingCardView){
            MainActivity.addActionFragment(new ProductListOpenForPrice());
        }


    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Welcome");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choose_action, container, false);
        inflateViews();
        return view;
    }

    private void inflateViews() {
        completedCardView = (LinearLayout) view.findViewById(R.id.completedCardView);
        waitingCardView =(LinearLayout) view.findViewById(R.id.waitingCardView);
        progressCardView = (LinearLayout) view.findViewById(R.id.progressCardView);
        pricingCardView =(LinearLayout)view.findViewById(R.id.pricingCardView);
        pricingCardView.setOnClickListener(this);
        progressCardView.setOnClickListener(this);
        waitingCardView.setOnClickListener(this);
        completedCardView.setOnClickListener(this);

    }
}
