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
    private Button getProductsButton, getOrderButton;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.getOrderButton){
            // Get FragmentManager and FragmentTransaction object.
           MainActivity.addActionFragment(new ProviderOrderListFragment());

        }else if (view.getId() == R.id.getProductsButton){
            MainActivity.addActionFragment(new ProviderProductsListFragment());
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
        getProductsButton = (Button)view.findViewById(R.id.getProductsButton);
        getOrderButton =(Button) view.findViewById(R.id.getOrderButton);
        getOrderButton.setOnClickListener(this);
        getProductsButton.setOnClickListener(this);

    }
}
