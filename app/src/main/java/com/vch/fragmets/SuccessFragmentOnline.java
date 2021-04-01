package com.vch.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.vch.R;
import com.vch.activities.HomeActivity;

public class SuccessFragmentOnline extends Fragment {

    private WebView webView;
    ImageView imageView;
    public SuccessFragmentOnline(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.paytmseccess, container, false);
        final TextView btnVerify =view.findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HomeActivity.clearFragmentStack();
                HomeActivity.addFragment(new HomeFragment(),false);

                // HomeActivity.clearFragmentStack();
                //HomeActivity.addFragment(new HomeFragment(),true);

                //Fragment fragment = new HomeFragment();
                //HomeActivity.addFragment(fragment, true);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Success Payment");
        HomeActivity.enableViews(true);
    }
}




