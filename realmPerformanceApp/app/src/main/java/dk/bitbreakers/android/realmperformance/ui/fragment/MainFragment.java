package dk.bitbreakers.android.realmperformance.ui.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.bitbreakers.android.realmperformance.R;
import dk.bitbreakers.android.realmperformance.util.Typefaces;


public class MainFragment extends Fragment {

    private TextView                mHeadline;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeadline = (TextView) view.findViewById(R.id.textHeadline);
        Typeface type = Typefaces.get(getActivity(), "font/SourceSansPro-Black.ttf");
        mHeadline.setTypeface(type);
    }
}
