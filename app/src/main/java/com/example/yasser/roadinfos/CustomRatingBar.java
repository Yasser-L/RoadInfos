package com.example.yasser.roadinfos;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by poliveira on 07/08/2014.
 */

/**
 * regular rating bar. it wraps the stars making its size fit the parent
 */
public class CustomRatingBar extends LinearLayout {
    public IRatingBarCallbacks getOnScoreChanged() {
        return onScoreChanged;
    }

    public void setOnScoreChanged(IRatingBarCallbacks onScoreChanged) {
        this.onScoreChanged = onScoreChanged;
    }

    public interface IRatingBarCallbacks {
        void scoreChanged(float score);
    }

    private int mMaxStars = 5;
    private float mCurrentScore = 2.5f;
    private int mStarOnResource = R.drawable.star_on;
    private int mStarOffResource = R.drawable.star_off;
    private int mStarHalfResource = R.drawable.star_half;
    private ImageView[] mStarsViews;
    private float mStarPadding;
    private IRatingBarCallbacks onScoreChanged;
    private int mLastStarId;
    private boolean mOnlyForDisplay;
    private double mLastX;
    private boolean mHalfStars = true;

    public CustomRatingBar(Context context) {
        super(context);
        init();
    }

    public float getScore() {
        return mCurrentScore;
    }

    public void setScore(float score) {
        score = Math.round(score * 2) / 2.0f;
        if (!mHalfStars)
            score = Math.round(score);
        mCurrentScore = score;
        refreshStars();
    }

    public void setScrollToSelect(boolean enabled) {
        mOnlyForDisplay = !enabled;
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttributes(attrs, context);
        init();
    }

    private void initializeAttributes(AttributeSet attrs, Context context) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomRatingBar);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomRatingBar_maxStars)
                mMaxStars = a.getInt(attr, 5);
            else if (attr == R.styleable.CustomRatingBar_stars)
                mCurrentScore = a.getFloat(attr, 2.5f);
            else if (attr == R.styleable.CustomRatingBar_starHalf)
                mStarHalfResource = a.getResourceId(attr, android.R.drawable.star_on);
            else if (attr == R.styleable.CustomRatingBar_starOn)
                mStarOnResource = a.getResourceId(attr, android.R.drawable.star_on);
            else if (attr == R.styleable.CustomRatingBar_starOff)
                mStarOffResource = a.getResourceId(attr, android.R.drawable.star_off);
            else if (attr == R.styleable.CustomRatingBar_starPadding)
                mStarPadding = a.getDimension(attr, 0);
            else if (attr == R.styleable.CustomRatingBar_onlyForDisplay)
                mOnlyForDisplay = a.getBoolean(attr, false);
            else if (attr == R.styleable.CustomRatingBar_halfStars)
                mHalfStars = a.getBoolean(attr, true);
        }
        a.recycle();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeAttributes(attrs, context);
        init();
    }

    void init() {
        mStarsViews = new ImageView[mMaxStars];
        for (int i = 0; i < mMaxStars; i++) {
            ImageView v = createStar();
            addView(v);
            mStarsViews[i] = v;
        }
        refreshStars();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * hardcore math over here
     *
     * @param x
     * @return
     */
    private float getScoreForPosition(float x) {
        if (mHalfStars)
            return (float) Math.round(((x / ((float) getWidth() / (mMaxStars * 3f))) / 3f) * 2f) / 2;
        float value = (float) Math.round((x / ((float) getWidth() / (mMaxStars))));
        return value < 0 ? 1 : value;
    }

    private int getImageForScore(float score) {
        if (score > 0)
            return Math.round(score) - 1;
        else return -1;
    }

    private void refreshStars() {
        boolean flagHalf = (mCurrentScore != 0 && (mCurrentScore % 0.5 == 0)) && mHalfStars;
        for (int i = 1; i <= mMaxStars; i++) {

            if (i <= mCurrentScore)
                mStarsViews[i - 1].setImageResource(mStarOnResource);
            else {
                if (flagHalf && i - 0.5 <= mCurrentScore)
                    mStarsViews[i - 1].setImageResource(mStarHalfResource);
                else
                    mStarsViews[i - 1].setImageResource(mStarOffResource);
            }
        }
    }

    private ImageView createStar() {
        ImageView v = new ImageView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        v.setPadding((int) mStarPadding, 0, (int) mStarPadding, 0);
        v.setAdjustViewBounds(true);
        v.setScaleType(ImageView.ScaleType.FIT_CENTER);
        v.setLayoutParams(params);
        v.setImageResource(mStarOffResource);
        return v;
    }

    private ImageView getImageView(int position) {
        try {
            return mStarsViews[position];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mOnlyForDisplay)
            return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                animateStarRelease(getImageView(mLastStarId));
                mLastStarId = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - mLastX) > 50)
                    requestDisallowInterceptTouchEvent(true);
                float lastscore = mCurrentScore;
                mCurrentScore = getScoreForPosition(event.getX());
                if (lastscore != mCurrentScore) {
                    animateStarRelease(getImageView(mLastStarId));
                    animateStarPressed(getImageView(getImageForScore(mCurrentScore)));
                    mLastStarId = getImageForScore(mCurrentScore);
                    refreshStars();
                    if (onScoreChanged != null)
                        onScoreChanged.scoreChanged(mCurrentScore);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                lastscore = mCurrentScore;
                mCurrentScore = getScoreForPosition(event.getX());
                animateStarPressed(getImageView(getImageForScore(mCurrentScore)));
                mLastStarId = getImageForScore(mCurrentScore);
                if (lastscore != mCurrentScore) {
                    refreshStars();
                    if (onScoreChanged != null)
                        onScoreChanged.scoreChanged(mCurrentScore);
                }
        }
        return true;
    }

    private void animateStarPressed(ImageView star) {
        if (star != null)
            ViewCompat.animate(star).scaleX(1.2f).scaleY(1.2f).setDuration(100).start();
    }

    private void animateStarRelease(ImageView star) {
        if (star != null)
            ViewCompat.animate(star).scaleX(1f).scaleY(1f).setDuration(100).start();
    }

    public boolean isHalfStars() {
        return mHalfStars;
    }

    public void setHalfStars(boolean halfStars) {
        mHalfStars = halfStars;
    }
}
