package com.vch.fragmets;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.bean.OrderHistory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackOrderFragment extends Fragment {


    public TrackOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_order, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Track Order");
        HomeActivity.enableViews(true);
    }

    public static Fragment newInstance(OrderHistory orderHistory) {
        TrackOrderFragment fragment = new TrackOrderFragment();

        Bundle arg = new Bundle();

        fragment.setArguments(arg);
        return fragment;
    }
}
