package info.ivicel.github.imageloaderdemo;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.ivicel.github.imageloader.ImageLoader;

public class MainActivity extends AppCompatActivity {

    private String[] urls = {"https://pixabay.com/get/ea37b70b28f0093ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b1072ef0043ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b1072df1063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea34b1092af5023ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea37b60d2df7073ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10c2cf1023ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea34b00c2df5023ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea35b50f2ffd063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea35b80621fc073ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10c2cf3013ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10c2cf2083ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea37b8072af5063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/eb3cb5072cf4053ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10a29f6033ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b00e2df7063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b00e2ef7013ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10728f3003ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b1082cf4063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/eb3cb30c29fc073ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10c2cf1013ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg", "https://pixabay.com/get/ea37b70b28f0093ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b1072ef0043ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b1072df1063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea34b1092af5023ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea37b60d2df7073ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10c2cf1023ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea34b00c2df5023ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea35b50f2ffd063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea35b80621fc073ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10c2cf3013ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10c2cf2083ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea37b8072af5063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/eb3cb5072cf4053ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10a29f6033ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b00e2df7063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b00e2ef7013ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10728f3003ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b1082cf4063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/eb3cb30c29fc073ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10c2cf1013ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg",
            "https://pixabay.com/get/ea37b70b28f0093ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b1072ef0043ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b1072df1063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea34b1092af5023ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea37b60d2df7073ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10c2cf1023ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea34b00c2df5023ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea35b50f2ffd063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea35b80621fc073ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10c2cf3013ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10c2cf2083ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea37b8072af5063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/eb3cb5072cf4053ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10a29f6033ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b00e2df7063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b00e2ef7013ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10728f3003ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b1082cf4063ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/eb3cb30c29fc073ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg",
            "https://pixabay.com/get/ea36b10c2cf1013ed1584d05fb1d4e90e570e3d71dac104497f3c479a1eeb1b8_1280.jpg", "https://pixabay.com/get/ea37b70b28f0093ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b1072ef0043ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b1072df1063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea34b1092af5023ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea37b60d2df7073ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10c2cf1023ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea34b00c2df5023ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea35b50f2ffd063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea35b80621fc073ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10c2cf3013ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10c2cf2083ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea37b8072af5063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/eb3cb5072cf4053ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10a29f6033ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b00e2df7063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b00e2ef7013ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10728f3003ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b1082cf4063ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/eb3cb30c29fc073ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"
            , "https://pixabay.com/get/ea36b10c2cf1013ed1584d05fb1d4e90e570e3d71dac104497f3c479a2eab2b1_1280.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new ImageAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

        @NonNull
        @Override
        public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.image_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
            holder.bindViewHolder(urls[position]);
        }

        @Override
        public int getItemCount() {
            return urls.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }

            public void bindViewHolder(String url) {
                ImageLoader.get(MainActivity.this).load(url, (ImageView) itemView);
            }
        }
    }
}
