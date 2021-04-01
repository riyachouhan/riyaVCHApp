package com.vch.utiles;

import java.util.List;

public interface PhotoSelectedListener {
    public void onSelected(List<String> imageUrl);
    public void onError();
}
