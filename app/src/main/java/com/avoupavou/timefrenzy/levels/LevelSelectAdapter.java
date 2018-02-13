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

    private View.OnClickListener mOnClickListener;
    private Level[] mLevels;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ConstraintLayout mConstraintLayout;
        public TextView mTextTitle;
        public TextView mTextSpeed;
        public ViewHolder(View v) {
            super(v);
            mConstraintLayout = (ConstraintLayout) v;
            mTextTitle = v.findViewById(R.id.text_level_title);
            mTextSpeed= v.findViewById(R.id.text_level_speed);
        }
    }

    public LevelSelectAdapter(Level[] levels, View.OnClickListener clickListener) {
        this.mLevels = levels;
        mOnClickListener = clickListener;
    }

    @Override
    public LevelSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_level, parent, false);
        v.setOnClickListener(mOnClickListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(LevelSelectAdapter.ViewHolder holder, int position) {
        Level level = mLevels[position];
        holder.mTextTitle.setText(level.getName());
        holder.mTextSpeed.setText(level.getSpeedString());
        if(level.isLocked()) holder.mConstraintLayout.setAlpha(0.5f);
        holder.mConstraintLayout.setClickable(!level.isLocked());
    }

    @Override
    public int getItemCount() {
        return mLevels.length;
    }

}
