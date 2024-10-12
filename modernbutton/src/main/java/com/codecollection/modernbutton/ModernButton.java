package com.codecollection.modernbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class ModernButton extends FrameLayout {

    private ProgressBar progressBar;
    private ImageView tapImage;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int progressStatus = 0;
    private boolean isPressed = false;
    private OnProgressCompleteListener progressCompleteListener;

    public ModernButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context, AttributeSet attrs) {
        // Inflate the layout
        inflate(context, R.layout.view_circular_progress_button, this);

        progressBar = findViewById(R.id.progressBar);
        tapImage = findViewById(R.id.tapImage);

        // Get custom attributes from XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressButton, 0, 0);

        try {
            Drawable progressDrawable = a.getDrawable(R.styleable.CircularProgressButton_progressDrawable);
            Drawable imageBackground = a.getDrawable(R.styleable.CircularProgressButton_imageBackground);
            Drawable imageSrc = a.getDrawable(R.styleable.CircularProgressButton_imageSrc);

            if (progressDrawable != null) {
                progressBar.setProgressDrawable(progressDrawable);
            }

            if (imageBackground != null) {
                tapImage.setBackground(imageBackground);
            }

            if (imageSrc != null) {
                tapImage.setImageDrawable(imageSrc);
            }

        } finally {
            a.recycle();
        }

        // Set touch listener for the image
        tapImage.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isPressed = true;
                    startProgress();
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isPressed = false;
                    resetProgress();
                    return true;
            }
            return false;
        });
    }

    private void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
        tapImage.setVisibility(View.VISIBLE);
        tapImage.setBackground(null);

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isPressed) {
                    if (progressStatus < 100) {
                        progressStatus++;
                        progressBar.setProgress(progressStatus);
                        handler.postDelayed(this, 30);
                    } else {
                        if (progressCompleteListener != null) {
                            progressCompleteListener.onProgressComplete();
                        } else {
                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        }
                        resetProgress();
                    }
                }
            }
        });
    }

    private void resetProgress() {
        progressStatus = 0;
        progressBar.setProgress(0);
        progressBar.setVisibility(View.GONE);
        tapImage.setVisibility(View.VISIBLE);
        tapImage.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_background));

    }

    public void setOnProgressCompleteListener(OnProgressCompleteListener listener) {
        this.progressCompleteListener = listener;
    }

    // Interface for progress complete listener
    public interface OnProgressCompleteListener {
        void onProgressComplete();
    }
}
