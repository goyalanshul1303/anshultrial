package com.application.onboarding.providersob;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by aggarwal.swati on 12/26/18.
 */

public class ChooseActivityFragment extends Fragment implements View.OnClickListener {
    private static View view;
    private Button consumerButton, providerButton;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.providerButton){
            // Get FragmentManager and FragmentTransaction object.
           MainActivity.addActionFragment(new ProviderFirstDetailsFragment());

        }else if (view.getId() == R.id.consumerButton){
            MainActivity.addActionFragment(new ConsumerDetailsFragment());
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
        consumerButton = (Button)view.findViewById(R.id.consumerButton);
        providerButton =(Button) view.findViewById(R.id.providerButton);
        providerButton.setOnClickListener(this);
        consumerButton.setOnClickListener(this);

    }
}
