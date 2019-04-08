package com.app.carton.provider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by aggarwal.swati on 12/26/18.
 */

public class ChooseActivityFragment extends Fragment implements View.OnClickListener {
    private static View view;
    private Button getAwardedOrders, getOrderButton;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.getOrderButton){
            // Get FragmentManager and FragmentTransaction object.
           MainActivity.addActionFragment(new PlacedOrderListFragment());

        }else if (view.getId() == R.id.getAwardedOrders){
            MainActivity.addActionFragment(new AwardedOrdersListFragment());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choose_action, container, false);
        inflateViews();
        return view;
    }

    private void inflateViews() {
        getAwardedOrders = (Button)view.findViewById(R.id.getAwardedOrders);
        getOrderButton =(Button) view.findViewById(R.id.getOrderButton);
        getOrderButton.setOnClickListener(this);
        getAwardedOrders.setOnClickListener(this);
        // see if any ongoing orders present else take to list of requirements. placed orders screen

    }
}
