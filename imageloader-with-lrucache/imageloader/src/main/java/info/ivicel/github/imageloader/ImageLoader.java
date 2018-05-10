package info.ivicel.github.imageloader;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private static final int DISK_MAX_SIZE = 100 * 1024 * 1024;
    private static final int IO_BUFFER_SIZE = 8192;
    volatile private static ImageLoader loader;
    private DiskLruCache diskCache;
    private LruCache<String, Bitmap> memCache;
    private int width;
    private int height;

    private ExecutorService executor = new ThreadPoolExecutor(1, 5, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    public static ImageLoader get(Context context) {
        if (loader == null) {
            synchronized (ImageLoader.class) {
                if (loader == null) {
                    loader = new ImageLoader(context);
                }
            }
        }
        return loader;
    }

    private class LayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private View view;
        private String url;

        LayoutListener(String url, final View view) {
            this.url = url;
            this.view = view;
        }

        @Override
        public void onGlobalLayout() {
            width = view.getWidth();
            height = view.getHeight();
            Log.d(TAG, "loadSpecifyBitmap: " + url);

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String hashKey = getHashKey(url);
                        Bitmap bitmap = memCache.get(hashKey);
                        if (bitmap == null) {
                            DiskLruCache.Snapshot snapshot = diskCache.get(hashKey);
                            if (snapshot != null) {
                                FileInputStream in = (FileInputStream) snapshot.getInputStream(0);
                                bitmap = loadSpecifyBitmap(in, width, height);
                            }

                            if (bitmap == null) {
                                bitmap = loadBitmapFromWeb(url);
                            }

                        }
                        final Bitmap b = bitmap;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (b != null) {
                                    ((ImageView)view).setImageBitmap(b);
                                }
                            }
                        });
                    } catch (IOException e) {

                    }
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener.this);
                }
            });
        }
    }


    public void load(@NonNull String url, @NonNull final ImageView view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new LayoutListener(url, view));
    }


    private class LoadImageCallable implements Callable<Bitmap> {
        private String url;

        LoadImageCallable(String url) {
            this.url = url;
        }

        @Override
        public Bitmap call() throws IOException {
            final String hashKey = getHashKey(url);
            Bitmap bitmap = memCache.get(hashKey);
            if (bitmap == null) {
                DiskLruCache.Snapshot snapshot = diskCache.get(hashKey);
                if (snapshot != null) {
                    FileInputStream in = (FileInputStream) snapshot.getInputStream(0);
                    bitmap = loadSpecifyBitmap(in, width, height);
                }

                if (bitmap == null) {
                    bitmap = loadBitmapFromWeb(url);
                }

            }
            return bitmap;
        }
    }

    private ImageLoader(Context context) {
        initCache(context);
    }

    private void initCache(Context context) {
        File cacheFile = getDiskCacheFile(context);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        try {
            diskCache = DiskLruCache.open(cacheFile, getAppVersion(context),
                    1, DISK_MAX_SIZE);
        } catch (IOException e) {
            Log.d(TAG, "can not init cache: " + e.getMessage());
        }
        memCache = new LruCache<>(getMemoryCacheSize());
    }

    private int getMemoryCacheSize() {
        return (int) (Runtime.getRuntime().maxMemory() / 8);
    }

    private int getAppVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 1;
        }
    }

    private File getDiskCacheFile(Context context) {
        File file;
        if (!Environment.isExternalStorageRemovable() ||
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = context.getExternalCacheDir();
        } else {
            file = context.getCacheDir();
        }

        return new File(file, TAG);
    }

    private Bitmap loadSpecifyBitmap(InputStream in, int destWidth, int destHeight) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[IO_BUFFER_SIZE];
        int c;
        while ((c = in.read(buffer, 0, IO_BUFFER_SIZE)) != -1) {
            out.write(buffer, 0, c);
        }

        return decodeBitmapByteArray(out.toByteArray(), destWidth, destHeight);
    }

    private Bitmap decodeBitmapByteArray(byte[] data, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        final int halfWidth = options.outWidth / 2;
        final int halfHeight = options.outHeight / 2;
        int inSampleSize = 1;
        while ((halfWidth / inSampleSize >= destWidth) && (halfHeight / inSampleSize >= destHeight)) {
            inSampleSize *= 2;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        Log.d(TAG, "loadSpecifyBitmap: " + inSampleSize);
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    private String getHashKey(@NonNull String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(url.getBytes());
            return bytesToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(url.hashCode());
        }
    }

    private String bytesToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String s = Integer.toHexString(data[i]);
            if (s.length() < 2) {
                sb.append('0');
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private Bitmap loadBitmapFromWeb(String url) {
        if (url == null) {
            return null;
        }

        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
//            conn.setConnectTimeout(120);
            InputStream in = new BufferedInputStream(conn.getInputStream());
            Log.d(TAG, "loadBitmapFromWeb: " + url);
            Bitmap bitmap = loadSpecifyBitmap(in, width, height);
            writeToCache(url, bitmap);
            return bitmap;
        } catch (MalformedURLException e) {
            Log.e(TAG, "not a valid " + url);
        } catch (IOException e) {
            Log.e(TAG, "read web content error: " + e.getMessage(), e);
        }
        return null;
    }

    private void writeToCache(String url, Bitmap bitmap) {
        final String hashKey = getHashKey(url);
        memCache.put(hashKey, bitmap);

        DiskLruCache.Editor editor;
        OutputStream out = null;
        try {
            editor = diskCache.edit(hashKey);
            if (editor != null) {
                out = editor.newOutputStream(0);
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "save cache file error: " + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
