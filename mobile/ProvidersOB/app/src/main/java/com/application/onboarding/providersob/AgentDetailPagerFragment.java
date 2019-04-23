package com.application.onboarding.providersob;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggarwal.swati on 2/12/19.
 */

public class AgentDetailPagerFragment extends Fragment {
    private View view;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String selectedId;
    String customerType = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.agent_pager_view, container, false);
        inflateViews();

        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            customerType = getArguments().containsKey("urlType") ? getArguments().getString("urlType") : "";
            selectedId = getArguments().containsKey("selectedId") ? getArguments().getString("selectedId") : "";

        }

    }
    private void inflateViews() {

        MainActivity main = (MainActivity)getActivity();

//        main.setSupportActionBar(toolbar);
//        main.getSupportActionBar().setTitle("Agents Details");


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AgentDetailsFragment(), "Details");
        adapter.addFragment(new AgentProductsListFragment(), "Products Added");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            if (mFragmentList.size() > position) {
//                Fragment f = mFragmentList.get(position);
//                if (f != null) {
//                    if (mCurTransaction == null) {
//                        mCurTransaction = mFragmentManager.beginTransaction();
//                    }
//
//                    mCurTransaction.detach(f);
//                    mCurTransaction.attach(f);
//
//                    return f;
//                }
//            }
//        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();

            bundle.putString("selectedId", selectedId);
            bundle.putString("urlType", customerType);
            Fragment fragment = mFragmentList.get(position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
