package info.ivicel.viewpagetransformer;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CardFlipAnimationActivity extends AppCompatActivity {
    private boolean isFront = true;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toggle_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toogle:
                toggleFragment();
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,
                new CardFrontFragment()).commit();
    }
    
    public static class CardFrontFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.card_flip_image, container, false);
            
            return v;
        }
    }
    
    public static class CardBackFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_card_flip_back, container, false);
            
            
            return v;
        }
    }
    
    private void toggleFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        if (isFront) {
            fragment = new CardBackFragment();
            
        } else {
            fragment = new CardFrontFragment();
        }
        isFront = !isFront;
        fm.beginTransaction()
                .setCustomAnimations(R.animator.card_flip_left_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out)
                .replace(R.id.activity_fragment, fragment).commit();
    }
}
