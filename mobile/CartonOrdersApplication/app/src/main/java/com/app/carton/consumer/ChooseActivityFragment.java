package com.app.carton.consumer;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.carton.orders.R;


/**
 * Created by aggarwal.swati on 12/26/18.
 */

public class ChooseActivityFragment extends Fragment implements View.OnClickListener {
    private static View view;
    private CardView openOrdersCardView, requiremntsCardView, productListCardView;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.openOrdersCardView){
            // Get FragmentManager and FragmentTransaction object.
           MainActivity.addActionFragment(new ConsumerOrderListFragment());

        }else if (view.getId() == R.id.productListCardView){
            MainActivity.addActionFragment(new ConsumerProductsListFragment());
        }else if (view.getId() == R.id.requiremntsCardView){
            MainActivity.addActionFragment(new ConsumerRequirementsListFragment());
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
        openOrdersCardView = (CardView) view.findViewById(R.id.openOrdersCardView);
        requiremntsCardView =(CardView) view.findViewById(R.id.requiremntsCardView);
        productListCardView = (CardView)view.findViewById(R.id.productListCardView);
        productListCardView.setOnClickListener(this);
        requiremntsCardView.setOnClickListener(this);
        openOrdersCardView.setOnClickListener(this);

    }
}
