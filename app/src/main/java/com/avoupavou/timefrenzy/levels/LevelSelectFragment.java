package com.avoupavou.timefrenzy.levels;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avoupavou.timefrenzy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLevelSelectFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LevelSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelSelectFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnLevelSelectFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private LevelSelectAdapter mLevelSelectAdapter;
    private Level[] mLevels;

    public LevelSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LevelSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelSelectFragment newInstance(String param1, String param2) {
        LevelSelectFragment fragment = new LevelSelectFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level_select, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_level_select);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mLevels = new Level[5];
        for(int i =0 ; i < mLevels.length ; i ++) mLevels[i] = new Level(i);
        mLevelSelectAdapter = new LevelSelectAdapter(mLevels,this);
        mRecyclerView.setAdapter(mLevelSelectAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLevelSelectFragmentInteractionListener) {
            mListener = (OnLevelSelectFragmentInteractionListener) context;
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

    /*
    * onClickListener method implementation
    * This triggers when
    */
    @Override
    public void onClick(View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        Level level = mLevels[itemPosition];
        mListener.onLevelSelect(level);
    }


    public interface OnLevelSelectFragmentInteractionListener {
        //TODO add level select argument
        void onLevelSelect(Level level);
    }
}
