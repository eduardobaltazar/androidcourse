package com.unosquare.finalapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by admin on 19/10/2014.
 */
public class PrincipalFragment extends Fragment {
    public static String ARGS_SUBT = "ARGS_SUBT";
    private OnFragmentInteractionListener mListener;

    public static PrincipalFragment getInstance(String subtitle){
        PrincipalFragment fragment = new PrincipalFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARGS_SUBT, subtitle);
        fragment.setArguments(bundle);

        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout,container, false);

        String subtitle = getArguments().getString(ARGS_SUBT);
        RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.fragment_layout);
        mListener.onFragmentInteraction(subtitle);

        return rootView;
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
