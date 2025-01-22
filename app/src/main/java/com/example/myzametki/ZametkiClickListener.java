package com.example.myzametki;

import androidx.cardview.widget.CardView;

import com.example.myzametki.Models.Zametki;

public interface ZametkiClickListener {
    void onClick(Zametki zametki);
    void onLongClick(Zametki zametki, CardView cardView);

}
