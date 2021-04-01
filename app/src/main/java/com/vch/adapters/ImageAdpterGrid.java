package com.vch.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vch.R;
import com.bumptech.glide.Glide;
import com.vch.utiles.SquareImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by technorizen on 15/11/17.
 */

public class ImageAdpterGrid extends BaseAdapter {
    private List<String> mImagesList;
    private Context mContext;
    private SparseBooleanArray mSparseBooleanArray_new;
    private LayoutInflater inflater;

    public ImageAdpterGrid(Context context, List<String> imageList) {
        mContext = context;
        mSparseBooleanArray_new = new SparseBooleanArray();
        mImagesList = new ArrayList<>();
        this.mImagesList = imageList;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public List<String> getCheckedItems() {
        List<String> mTempArry = new ArrayList<>();

        for (int i = 0; i < mImagesList.size(); i++) {
            if (mSparseBooleanArray_new.get(i)) {
                mTempArry.add(mImagesList.get(i));
            }
        }

        return mTempArry;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        return mImagesList == null ? 0 : mImagesList.size();

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView;
        final SquareImageView imageView;
        final ImageView selectedview;

        rowView = inflater.inflate(R.layout.item_multiphoto, null);


        imageView = (SquareImageView) rowView.findViewById(R.id.imageView1);
        selectedview = (ImageView) rowView.findViewById(R.id.selectedview);
        if (mSparseBooleanArray_new.get(position)) {
            //blur_back.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.last_trans));
            selectedview.setVisibility(View.VISIBLE);

        } else {

           // blur_back.setBackgroundColor(mContext.getResources().getColor(R.color.trans));
            selectedview.setVisibility(View.GONE);

        }
        String imageUrl = mImagesList.get(position);
        Log.e("Come Bind", "");
        Glide.with(mContext)
                .load("file://" + imageUrl)
                .into(imageView);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSparseBooleanArray_new.get(position)) {
                    mSparseBooleanArray_new.put(position, false);
                    //blur_back.setBackgroundColor(mContext.getResources().getColor(R.color.trans));
                    selectedview.setVisibility(View.GONE);
                } else {
                    mSparseBooleanArray_new.put(position, true);
                    //blur_back.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.last_trans));
                    selectedview.setVisibility(View.VISIBLE);

                }
            }
        });
        return rowView;
    }

}
