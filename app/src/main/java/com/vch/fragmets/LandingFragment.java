package com.vch.fragmets;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import android.net.Uri;
import android.widget.RelativeLayout.LayoutParams;
import com.vch.R;
import android.media.MediaPlayer;
import com.vch.activities.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LandingFragment extends Fragment {


    public LandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        VideoView videoview = view.findViewById(R.id.videoview);

         android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
         getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
             LayoutParams params = (LayoutParams) videoview.getLayoutParams();
             params.width =  metrics.widthPixels;
             params.height = metrics.heightPixels;
             params.leftMargin = 0;
             videoview.setLayoutParams(params);

        Uri uri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.test);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }
});
        view.findViewById(R.id.sign_in_TV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.addFragment(new LoginFragment(),true);
            }
        });

        return view;
    }

}
