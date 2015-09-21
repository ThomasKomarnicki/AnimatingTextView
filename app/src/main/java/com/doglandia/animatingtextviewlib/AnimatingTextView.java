package com.doglandia.animatingtextviewlib;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Thomas on 9/20/2015.
 */
public class AnimatingTextView extends TextView {

    private String textToAnimate;
    private long totalDuration = 0;
    private long perCharDuration = 0;
    private long duration = 0;

    private ObjectAnimator animator;
    private String currentlyDisplayingText;

    public AnimatingTextView(Context context) {
        super(context);
    }

    public AnimatingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public void setTextToAnimate(String text){
        this.textToAnimate = text;
    }

    public void setDurationPerCharacter(long milliseconds){
        this.perCharDuration = milliseconds;
        totalDuration = 0;
    }

    public void setTotalDuration(long milliseconds){
        this.totalDuration = milliseconds;
        perCharDuration = 0;
    }


    /* animation api */

    public void start(){
        if(textToAnimate == null){
            RuntimeException exception = new RuntimeException("Text to animate must be set.");
            throw exception;
        }else if(totalDuration == 0 || perCharDuration == 0){
            RuntimeException exception = new RuntimeException("Total Duration or Duration per Charcter must be set");
            throw exception;
        }
        measureText(this);
        resolveDuration();

        if(animator == null){
            animator = ObjectAnimator.ofInt(this,"remainingDuration",0,(int)duration);
        }

        animator.end();
        animator.start();
    }

    public void stop(){

    }

    private void setRemainingDuration(int remainingDuration){

    }

    private void resolveDuration(){
        if(totalDuration != 0){
            duration = totalDuration;
        }else{
            totalDuration = perCharDuration * textToAnimate.length();
        }
    }

    private void measureText(TextView tv){
        TextPaint textPaint = tv.getPaint();

        final String[] words = textToAnimate.split(" ");
        ArrayList<String> resolvedLines = new ArrayList<>();

        StringBuilder currentLine = new StringBuilder();

        for(int i = 0; i < words.length; i++){
            String word = words[i] + " ";
            currentLine.append(word);
            int viewWidth = getWidth();
            if(textPaint.measureText(currentLine.toString()) > viewWidth){
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
            newMessage.append(currentLine.toString());
        }

        textToAnimate = newMessage.toString();

    }

}
