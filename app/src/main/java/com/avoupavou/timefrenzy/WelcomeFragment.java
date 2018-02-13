package com.avoupavou.timefrenzy;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.avoupavou.timefrenzy.levels.LevelController;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnWelcomeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mLogoClicks;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AnimationDrawable logoAnimation;

    private OnWelcomeFragmentInteractionListener mListener;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WelcomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeFragment newInstance(String param1, String param2) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mLogoClicks =0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);


        Button levelsButton = (Button) view.findViewById(R.id.button_levels);
        levelsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLevelsButtonClicked();
                }
            }

        });


        Button competitiveButton = (Button) view.findViewById(R.id.button_competitive);
        competitiveButton .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onPlayButtonClicked();
                }
            }

        });

        ImageButton settingsButton = (ImageButton) view.findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onSettingsButtonClicked();
                }
            }

        });

        ImageView logoImage = (ImageView) view.findViewById(R.id.image_logo);
        //logoAnimation = (AnimationDrawable) logoImage.getDrawable();
        //logoAnimation.start();

        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogoClicks++;
                if(mLogoClicks > 10) {
                    cheatActivated();
                }
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mLogoClicks =0;
                    }
                }, 3000);
            }
        });
        return view;
    }

    private void cheatActivated(){
        LevelController.initLevels();
        LevelController.unlockAll();
        Toast.makeText(this.getActivity(),"Cheat activated",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWelcomeFragmentInteractionListener) {
            mListener = (OnWelcomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWelcomeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnWelcomeFragmentInteractionListener {
        void onPlayButtonClicked();
        void onLevelsButtonClicked();
        void onSettingsButtonClicked();
    }

}
