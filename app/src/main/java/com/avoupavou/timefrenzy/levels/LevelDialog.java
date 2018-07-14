package com.avoupavou.timefrenzy.levels;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.avoupavou.timefrenzy.R;

/**
 * Created by Pantazis on 01-Mar-18.
 */

public class LevelDialog extends DialogFragment{

    private TextView mTitleTextView;
    private TextView mScoreTextView;

    private Level mLevel;
    private int mScore;

    private OnLevelDialogInteractionListener mListener;

    public LevelDialog() {
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.fragment_level_popup,null);
        builder.setView(dialogView);

        mTitleTextView = dialogView.findViewById(R.id.text_dialog_title);
        mScoreTextView = dialogView.findViewById(R.id.text_dialog_score);

        mTitleTextView.setText(mLevel.getName()+" Passed!!");
        mScoreTextView.setText("Score: "+mScore);

        final Dialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        dialogView.findViewById(R.id.dialog_next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.nextButtonDialogPressed(mLevel.getId() +1);
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.dialog_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backButtonDialogPressed();
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.dialog_playagain_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mListener.playAgainButtonDialogPressed(mLevel);
                dialog.dismiss();
            }
        });

        return dialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnLevelDialogInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setLevel(Level mLevel) {
        this.mLevel = mLevel;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }

    public interface OnLevelDialogInteractionListener{
        public void backButtonDialogPressed();
        public void playAgainButtonDialogPressed(Level level);
        public void nextButtonDialogPressed(int nextLevelId);
    }
}
