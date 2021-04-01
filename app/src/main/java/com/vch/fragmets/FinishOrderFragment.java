package com.vch.fragmets;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vch.R;
import com.vch.activities.HomeActivity;

public class FinishOrderFragment extends Fragment {
    public FinishOrderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finish_order, container, false);
        if (getArguments()!=null){
            TextView orderTV = view.findViewById(R.id.order_id);


            orderTV.setText(Html.fromHtml(getArguments().getString("order_id")));

            Button contibnueBt = view.findViewById(R.id.continue_BT);
            contibnueBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity.clearFragmentStack();
                    HomeActivity.addFragment(new HomeFragment(),false);
                }
            });
        }
        return view;
    }

    public static FinishOrderFragment newInstance(String orderId){
        FinishOrderFragment fragment = new FinishOrderFragment();

        Bundle bundle = new Bundle();
        bundle.putString("order_id",orderId);

        fragment.setArguments(bundle);
        return fragment;
    }
}
