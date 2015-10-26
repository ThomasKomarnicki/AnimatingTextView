package com.doglandia.animatingtextviewlib;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Thomas on 9/20/2015.
 */
public class AnimatingTextView extends TextView {

    private static final String TAG = "AnimatingTextView";
    private String textToAnimate;
    private String targetText;
    private long totalDuration = 0;
    private long perCharDuration = 0;
    private long duration = 0;

    private boolean startOnLayout = false;
    private boolean laidOut = false;

    private ObjectAnimator animator;
    private String currentlyDisplayingText;

    // used to restore a playing animation after instance restore
    private long restoredPlayTime = -1;

    private Animator.AnimatorListener animatorListener;

    public AnimatingTextView(Context context) {
        super(context);
    }

    public AnimatingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     *
     * @param text The text to be animated
     */
    public void setTextToAnimate(String text){
        this.targetText = text;
        invalidate();
    }

    public void setDurationPerCharacter(long milliseconds){
        this.perCharDuration = milliseconds;
        totalDuration = 0;
    }

    public void setTotalDuration(long milliseconds){
        this.totalDuration = milliseconds;
        perCharDuration = 0;
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    /* animation api */

    public void start(){
        post(new Runnable() {
            @Override
            public void run() {
                startAnimating();
            }
        });


    }

    private void startAnimating(){
        if (!laidOut) {
            startOnLayout = true;
        }
        if (targetText == null) {
            RuntimeException exception = new RuntimeException("Text to animate must be set.");
            throw exception;
        } else if (totalDuration == 0 && perCharDuration == 0) {
            RuntimeException exception = new RuntimeException("Total Duration or Duration per Charcter must be set");
            throw exception;
        }
        measureText(AnimatingTextView.this);
        resolveDuration();

        if (animator == null) {
            animator = ObjectAnimator.ofInt(this, "Delta", 0, (int) totalDuration);
            animator.setDuration(totalDuration);
            if(animatorListener != null){
                animator.addListener(animatorListener);
            }
        } else {
            animator.end();
        }

        animator.start();
        if (restoredPlayTime >= 0) {
            animator.setCurrentPlayTime(restoredPlayTime);
            restoredPlayTime = -1; // reset to avoid any problems
        }
    }

    public void stop(){
        if(animator != null){
            animator.cancel();
        }
    }

    public boolean isAnimating(){
        if(restoredPlayTime >=0){
            return true;
        }
        if(animator != null){
            return animator.isRunning();
        }
        return false;
    }

    private void setDelta(int delta){
//        Log.d(TAG, "delta/totalDuration == " + delta + "/" + totalDuration + " == " + ((double) delta / (double) totalDuration));
        int subStringIndex = (int)(((((double)delta/(double)totalDuration))) * (double)textToAnimate.length());
//        Log.d(TAG,"delta = "+delta);
//        Log.d(TAG,"subStringIndex = "+subStringIndex);
        currentlyDisplayingText = textToAnimate.substring(0, subStringIndex);

        setText(currentlyDisplayingText);
    }


    /**
     * text to animate must be resolved by measure text at this point
     */
    private void resolveDuration(){
        if(totalDuration != 0){
            duration = totalDuration;
        }else{
            totalDuration = perCharDuration * textToAnimate.length();
        }
    }

    private void measureText(TextView tv){
//        if(getWidth() == 0){
//            measure(lastWidthMeasureSpec,lastHeightMeasureSpec);
//        }
        TextPaint textPaint = tv.getPaint();

        final String[] words = targetText.split(" ");
        ArrayList<String> resolvedLines = new ArrayList<>();

        StringBuilder currentLine = new StringBuilder();

        for(int i = 0; i < words.length; i++){
            String word = words[i] + " ";
            currentLine.append(word);
            int viewWidth = getWidth() - (getPaddingLeft() + getPaddingRight());
            if(textPaint.measureText(currentLine.toString().trim()) > viewWidth){
                currentLine.delete(currentLine.length()-word.length()-1,currentLine.length()); // -1 is for the last space
                resolvedLines.add(currentLine.toString());
                currentLine = new StringBuilder();
                i--; // will cause stack overflow if one word is longer than width
            }
        }

        StringBuilder newMessage = new StringBuilder();
        for(String string : resolvedLines){
            newMessage.append(string);
            newMessage.append("\n");
        }

        if(currentLine.length() > 0){
            newMessage.append(currentLine.toString().trim());
        }

        resolvedLines.add(currentLine.toString());

        textToAnimate = newMessage.toString();

        desiredWidth = (int) textPaint.measureText(textToAnimate);

        desiredHeight = (int) ((textPaint.getFontSpacing())*resolvedLines.size());
        desiredHeight += getPaddingBottom() + getPaddingTop();


    }

    int desiredWidth;
    int desiredHeight;

    int lastWidthMeasureSpec;
    int lastHeightMeasureSpec;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.lastWidthMeasureSpec = widthMeasureSpec;
        this.lastHeightMeasureSpec = heightMeasureSpec;
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if(desiredHeight == 0 || desiredWidth == 0){
//            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//            return;
//        }

        if(targetText != null){
            desiredWidth = (int) getPaint().measureText(targetText) + getTotalPaddingLeft() + getTotalPaddingRight();
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = Math.min(desiredWidth, widthSize);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        laidOut = true;
        if(startOnLayout){
            start();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        if(animator != null && animator.isRunning()) {
            SavedState ss = new SavedState(superState);
            ss.elapsedAnimationTime = animator.getCurrentPlayTime();
            ss.totalDuration = totalDuration;
            ss.targetText = targetText;
            animator.cancel();
            return ss;
        }
        return superState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.targetText = ss.targetText;
        this.totalDuration = ss.totalDuration;
        this.restoredPlayTime = ss.elapsedAnimationTime;
        start();
    }

    static class SavedState extends BaseSavedState {
        long elapsedAnimationTime;
        long totalDuration;
        String targetText;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.elapsedAnimationTime = in.readLong();
            this.totalDuration = in.readLong();
            this.targetText = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(this.elapsedAnimationTime);
            out.writeLong(this.totalDuration);
            out.writeString(this.targetText);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
