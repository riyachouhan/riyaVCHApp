package com.vch.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.vch.R;
import com.vch.activities.HomeActivity;

public class FragmentTermsConditions extends Fragment {

    ImageView imageView;
    public FragmentTermsConditions(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_termsconditions, container, false);


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


