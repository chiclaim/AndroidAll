package com.chiclaim.optimization.launchtime;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chiclaim.optimization.R;

public class ItemHolder extends RecyclerView.ViewHolder {

    public TextView textTitle;


    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        textTitle = itemView.findViewById(R.id.text_title);
    }

}