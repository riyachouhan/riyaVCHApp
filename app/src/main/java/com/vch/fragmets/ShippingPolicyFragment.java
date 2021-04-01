package com.vch.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.vch.R;
import com.vch.activities.HomeActivity;

public class ShippingPolicyFragment extends Fragment {

    ImageView imageView;
    public ShippingPolicyFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shipping_policy, container, false);


        imageView = view.findViewById(R.id.imageview);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Privacy Policy");
        HomeActivity.enableViews(true);
    }
}
