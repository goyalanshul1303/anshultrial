package com.app.carton.provider;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
    RecyclerView recyclerView;
    @Override
    public void onClick(View view) {



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


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        ActionAdapter adapter = new ActionAdapter(4);
        recyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new ActionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if ((int)view.getTag() == 0){
                    // Get FragmentManager and FragmentTransaction object.
                    MainActivity.addActionFragment(new ProviderOngoingOrdersListFragment());

                }else if ((int)view.getTag() == 1){
                    MainActivity.addActionFragment(new CompletedOrderListFragment());
                }else if ((int)view.getTag() == 2){
                    MainActivity.addActionFragment(new PlacedOrderListFragment());
                }
                else if ((int)view.getTag() == 3){
                    MainActivity.addActionFragment(new ProductListOpenForPrice());
                }

            }
        });
    }
}
