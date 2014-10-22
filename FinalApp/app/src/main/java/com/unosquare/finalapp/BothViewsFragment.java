package com.unosquare.finalapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by eduardo.baltazar on 20/10/2014.
 */
public class BothViewsFragment extends Fragment {
    public static String ARGS_COLOR = "ARGS_COLOR";
    public static String ARGS_SUBT = "ARGS_SUBT";

    public static BothViewsFragment getInstance(int color, String subtitle){
        BothViewsFragment fragment = new BothViewsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_COLOR, color);
        bundle.putString(ARGS_SUBT, subtitle);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_views_both, container, false);

        int color = getArguments().getInt(ARGS_COLOR);
        String subtitle = getArguments().getString(ARGS_SUBT);
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.bothviews_layout);
        layout.setBackgroundColor(getResources().getColor(color));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
