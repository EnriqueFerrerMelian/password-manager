package com.example.keymasterdegozer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.keymasterdegozer.R;
import com.google.android.material.imageview.ShapeableImageView;

public class IconSpinnerAdapter extends ArrayAdapter<Integer> {

    private final LayoutInflater inflater;
    private final Integer[] icons;

    public IconSpinnerAdapter(@NonNull Context context, Integer[] icons) {
        super(context, 0, icons);
        this.inflater = LayoutInflater.from(context);
        this.icons = icons;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return createIconView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return createIconView(position, parent);
    }

    private View createIconView(int position, ViewGroup parent) {
        ShapeableImageView imageView = (ShapeableImageView) inflater.inflate(R.layout.icon_selector, parent, false);
        imageView.setImageResource(icons[position]);
        return imageView;
    }
}
