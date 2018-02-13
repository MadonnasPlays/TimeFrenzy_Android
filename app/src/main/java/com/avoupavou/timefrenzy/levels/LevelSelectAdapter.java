package com.avoupavou.timefrenzy.levels;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avoupavou.timefrenzy.R;

/**
 * Created by Pantazis on 13-Feb-18.
 */

public class LevelSelectAdapter extends RecyclerView.Adapter<LevelSelectAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ConstraintLayout mConstraintLayout;
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mConstraintLayout = (ConstraintLayout) v;
            mTextView = v.findViewById(R.id.text_levelTitle);
        }
    }


    @Override
    public LevelSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_level, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(LevelSelectAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(new StringBuilder().append("Level ").append(position+1).toString())  ;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

}
