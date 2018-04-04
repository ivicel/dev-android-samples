package info.ivicel.viewpagetransformer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ivicel on 10/10/2017.
 */

public class ColorFragment extends Fragment {
    private static final String COLOR = "background_color";
    
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_depth_page, container, false);
        int background = getArguments().getInt(COLOR);
        v.setBackgroundColor(background);
        return v;
    }
    
    public static ColorFragment newInstance(int background) {
        Bundle args = new Bundle();
        args.putInt(COLOR, background);
        ColorFragment fragment = new ColorFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
