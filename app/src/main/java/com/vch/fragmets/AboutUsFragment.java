package com.vch.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.vch.R;
import com.vch.activities.HomeActivity;

public class AboutUsFragment extends Fragment {

    private WebView webView;
    ImageView imageView;
    public AboutUsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);


    imageView = view.findViewById(R.id.imageview);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("About Us");
        HomeActivity.enableViews(true);
    }
}




