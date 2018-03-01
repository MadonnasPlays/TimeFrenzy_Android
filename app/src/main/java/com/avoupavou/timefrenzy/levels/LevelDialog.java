package com.avoupavou.timefrenzy.levels;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.avoupavou.timefrenzy.R;

/**
 * Created by Pantazis on 01-Mar-18.
 */

public class LevelDialog extends DialogFragment {

    private TextView mTitleTextView;
    private TextView mScoreTextView;

    private String mLevel;
    private int mScore;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.fragment_level_popup,null);
        builder.setView(dialogView);

        mTitleTextView = dialogView.findViewById(R.id.text_dialog_title);
        mScoreTextView = dialogView.findViewById(R.id.text_dialog_score);

        mTitleTextView.setText(mLevel+" Passed!!");
        mScoreTextView.setText("Score: "+mScore);

        Dialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        return dialog;
    }

    public void setLevel(String mLevel) {
        this.mLevel = mLevel;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }
}
