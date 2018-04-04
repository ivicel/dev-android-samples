package info.ivicel.fileproviderdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static info.ivicel.fileproviderdemo.BuildConfig.DEBUG;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private ImageView mImageView;
    private Button mButton;
    private File mImageFile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        mButton = findViewById(R.id.capture_image);
        mImageView = findViewById(R.id.image_view);
        
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File filePath = new File(getExternalFilesDir(null), "images");
                mImageFile = new File(filePath, "image.jpg");
                Uri uri = FileProvider.getUriForFile(MainActivity.this,
                        "info.ivicel.fileprovider", mImageFile);
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    
                List<ResolveInfo> resolveActivities = getPackageManager().queryIntentActivities(
                        i, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : resolveActivities) {
                    grantUriPermission(activity.activityInfo.packageName, uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
    
                if (resolveActivities.size() <= 0) {
                    return;
                }
                
                startActivityForResult(i, 1);
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap bm = BitmapFactory.decodeFile(mImageFile.getPath());
                mImageView.setImageBitmap(bm);
            }
    
            Uri uri = FileProvider.getUriForFile(this, "info.ivicel.fileprovider",
                    mImageFile);
            revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }
}
