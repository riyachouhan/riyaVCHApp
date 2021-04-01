package com.vch.fragmets;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.adapters.TiffinDetailsRVAdapter;
import com.vch.utiles.DataHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TifinDetailFragment extends Fragment {
    //int[] serviceList = {R.drawable.dessert,R.drawable.combo_meals,R.drawable.healthysnacks,R.drawable.mains};
   // private AViewFlipper vf;
    public TifinDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        TextView tiffinID = view.findViewById(R.id.tofon_order_id_TV);
        TextView orderAmount = view.findViewById(R.id.order_amount_TV);
        TextView orderDate = view.findViewById(R.id.order_date_TV);
        TextView lunchDinner = view.findViewById(R.id.lunch_dinner_TV);
        TextView address = view.findViewById(R.id.address_TV);
        TextView paymentStatus = view.findViewById(R.id.payment_status_TV);
        TextView totalTiffin = view.findViewById(R.id.total_tiffin_TV);
        TextView leftTiffin = view.findViewById(R.id.left_tiffin_TV);
        TextView deliveredTiffin = view.findViewById(R.id.delivered_tiffin_TV);
if (DataHolder.getTiffinOrder().getUserMealDetails().size()>0){
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    TiffinDetailsRVAdapter adapter = new TiffinDetailsRVAdapter(getActivity(),DataHolder.getTiffinOrder().getUserMealDetails());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
}

        tiffinID.setText(DataHolder.getTiffinOrder().getTiffinOrderId());
        orderAmount.setText(DataHolder.getTiffinOrder().getPlanTotalAmount());
        orderDate.setText(DataHolder.getTiffinOrder().getOrderTime());
        lunchDinner.setText(DataHolder.getTiffinOrder().getLunchDinner());
        address.setText(DataHolder.getTiffinOrder().getUserAddress());
        paymentStatus.setText(DataHolder.getTiffinOrder().getPaymentStatus());
        totalTiffin.setText(DataHolder.getTiffinOrder().getTotalMeal());
        leftTiffin.setText(DataHolder.getTiffinOrder().getBalanceMeal());
        deliveredTiffin.setText(DataHolder.getTiffinOrder().getDeliveredTiffin());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Tiffin Details");
        HomeActivity.enableViews(true);
    }
}
