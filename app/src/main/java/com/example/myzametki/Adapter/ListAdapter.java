package com.example.myzametki.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myzametki.Models.Zametki;
import com.example.myzametki.R;
import com.example.myzametki.ZametkiClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListAdapter extends RecyclerView.Adapter<ZametkiViewHolder>{
Context context;
List<Zametki> list;
ZametkiClickListener listener;

    public ListAdapter(Context context, ZametkiClickListener listener, List<Zametki> list) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public ZametkiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ZametkiViewHolder(LayoutInflater.from(context).inflate(R.layout.zametki_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ZametkiViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);
        holder.textView_zametka.setText(list.get(position).getZametki());
        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);

        if (list.get(position).isPinned()) {
            holder.imageView_pin.setImageResource(R.drawable.skrepka);
        } else {
            holder.imageView_pin.setImageResource(0);
        }
        int colorCode = getRandomColor();
        holder.list_container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));

        holder.list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.list_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.list_container);
                return true;
            }
        });
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.ListBackground1);
        colorCode.add(R.color.ListBackground2);
        colorCode.add(R.color.ListBackground3);
        colorCode.add(R.color.ListBackground4);
        colorCode.add(R.color.ListBackground5);
        colorCode.add(R.color.ListBackground6);

        Random random = new Random();
        int randomColor = random.nextInt(colorCode.size());
        return  colorCode.get(randomColor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void filterList (List<Zametki> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
}

class ZametkiViewHolder extends RecyclerView.ViewHolder{
    CardView list_container;
    TextView textView_title;
    ImageView imageView_pin;
    TextView textView_zametka;
    TextView textView_date;
    public ZametkiViewHolder(@NonNull View itemView) {
        super(itemView);
        list_container = itemView.findViewById((R.id.list_container));
        textView_title = itemView.findViewById((R.id.textView_title));
        imageView_pin = itemView.findViewById((R.id.imageView_pin));
        textView_zametka = itemView.findViewById((R.id.textView_zametka));
        textView_date = itemView.findViewById((R.id.textView_date));
    }
}