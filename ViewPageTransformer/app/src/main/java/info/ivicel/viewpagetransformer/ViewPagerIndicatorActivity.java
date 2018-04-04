package info.ivicel.viewpagetransformer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ivicel on 16/10/2017.
 */

public class ViewPagerIndicatorActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_indicator);
    
        IndicatorView v = findViewById(R.id.indicator_view_pager);
        
        v.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return NumberFragment.newInstance(position + "");
            }
    
            @Override
            public int getCount() {
                return 5;
            }
        });
    }
    
    public static class NumberFragment extends Fragment {
        private static final String NUMBER = "number";
        
        
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_number, container, false);
            String number = getArguments().getString(NUMBER);
    
            TextView textView = v.findViewById(R.id.text_view);
            textView.setText(number);
            return v;
        }
        
        public static NumberFragment newInstance(String n) {
            Bundle args = new Bundle();
            args.putString(NUMBER, n);
            NumberFragment fragment = new NumberFragment();
            fragment.setArguments(args);
            return fragment;
        }
    }
}
