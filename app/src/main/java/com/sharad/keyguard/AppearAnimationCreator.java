package com.sharad.keyguard;

import android.view.animation.Interpolator;

/**
 * Created by Sharad on 08-Oct-15.
 */

/**
 * An interface which can create animations when starting an appear animation with
 * {@link com.sharad.keyguard.AppearAnimationUtils}
 */
public interface AppearAnimationCreator<T> {
    void createAnimation(T animatedObject, long delay, long duration,
                         float translationY, boolean appearing, Interpolator interpolator,
                         Runnable finishListener);
}
