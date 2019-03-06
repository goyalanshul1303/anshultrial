package com.app.carton.consumer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.app.carton.orders.R;

/**
 * Created by aggarwal.swati on 1/4/19.
 */

public class NoOrderFragment extends Fragment implements View.OnClickListener{
    private  View view;
    private Button createOrderBtn,repeatOrderBtn;
    private LinearLayout lastOrderLL, noOrderLL;

    public NoOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.no_order, container, false);
        initViews();
        return view;
    }

    // Initiate Views
    private void initViews() {
        createOrderBtn = (Button) view.findViewById(R.id.createOrderBtn);
        repeatOrderBtn = (Button)view.findViewById(R.id.repeatOrderBtn);
        noOrderLL = (LinearLayout)view.findViewById(R.id.noOrderLL);
        lastOrderLL = (LinearLayout)view.findViewById(R.id.lastOrderLL);
        createOrderBtn.setOnClickListener(this);
        repeatOrderBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

    }
}
