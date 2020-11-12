package com.example.handlerthread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bitmap> {
    public static final String TAG = MainActivity.class.getSimpleName();
    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getSupportLoaderManager().initLoader(1, null, MainActivity.this);
                LoaderManager.getInstance(MainActivity.this).initLoader(1,null,MainActivity.this);
            }
        });

    }

    @NonNull
    @Override
    public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
        final Bitmap[] bitmap = new Bitmap[1];
        return new AsyncTaskLoader<Bitmap>(this) {
            @Nullable
            @Override
            public Bitmap loadInBackground() {

                try {
                    URL url = new URL("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/dog-puppy-on-garden-royalty-free-image-1586966191.jpg?crop=1.00xw:0.669xh;0,0.190xh&resize=1200:*");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(inputStream);
                    Log.e(TAG, "loadInBackground: **************"+Thread.currentThread().getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap[0];
            }

            @Override
            protected void onStartLoading() {
                Log.e(TAG, "onStartLoading: **************"+Thread.currentThread().getName());

                super.onStartLoading();
                forceLoad();
            }
        };


    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
        Log.e(TAG, "onLoadFinished: **************"+Thread.currentThread().getName());
        imageView.setImageBitmap(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bitmap> loader) {
        Log.e(TAG, "onLoadReset: **************"+Thread.currentThread().getName());

    }
}