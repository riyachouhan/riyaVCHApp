package com.vch.fragmets;


import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.utiles.AppConfig;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferNEarnFragment extends Fragment {
    TextView referCodeTV,copyTV;

    public ReferNEarnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_refer_nearn, container, false);

        referCodeTV = view.findViewById(R.id.refer_code_TV);
        referCodeTV.setText(HomeActivity.detail.getUserReferralCode());
        copyTV = view.findViewById(R.id.copy_TV);
        copyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                String getstring = referCodeTV.getText().toString();

                clipboard.setText(getstring);

                AppConfig.showToast("Copied");
            }
        });

        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AppConfig.showToast(HomeActivity.detail.getUserReferralCode());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Use this Referral code "+ HomeActivity.detail.getUserReferralCode()+" and Get 25 bonus point on first order worth Rs. 25 when you sign up - usable on first order");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Refer & Earn");
        HomeActivity.enableViews(true);
    }
}
