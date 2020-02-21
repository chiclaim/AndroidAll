package com.chiclaim.optimization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chiclaim.optimization.launchtime.ItemHolder;
import com.chiclaim.optimization.launchtime.TimeRecord;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ItemHolder> {

    private List<String> list;

    private boolean hasRecord;

    public ListAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {

        if (position == 0 && !hasRecord) {
            hasRecord = true;
            /*holder.itemView.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
                @Override
                public void onDraw() {
                    // java.lang.IllegalStateException: Cannot call removeOnDrawListener inside of onDraw
                    holder.itemView.getViewTreeObserver().removeOnDrawListener(this);
                    TimeRecord.stopRecord("FirstItemDraw");
                }
            });*/

            holder.textTitle.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    holder.itemView.getViewTreeObserver().removeOnPreDrawListener(this);
                    TimeRecord.stopRecord("FirstItemDraw");
                    return true;
                }
            });

        }

        String title = list.get(position);
        holder.textTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
