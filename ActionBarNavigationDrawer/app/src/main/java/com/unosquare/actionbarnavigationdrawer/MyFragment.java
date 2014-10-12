package com.unosquare.actionbarnavigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.app.Activity;

public class MyFragment extends Fragment {

    public static String ARGS_COLOR = "ARGS_COLOR";
    public static String ARGS_SUBT = "ARGS_SUBT";

    private OnFragmentInteractionListener mListener;

    public static MyFragment getInstance(int color, String subtitle){
        MyFragment fragment = new MyFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_COLOR, color);
        bundle.putString(ARGS_SUBT, subtitle);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout,container, false);

        int color = getArguments().getInt(ARGS_COLOR);
        String subtitle = getArguments().getString(ARGS_SUBT);
        RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.layout);
        layout.setBackgroundColor(getResources().getColor(color));
        //getActivity().getActionBar().setSubtitle(subtitle);
        mListener.onFragmentInteraction(subtitle);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String texto);
    }
}