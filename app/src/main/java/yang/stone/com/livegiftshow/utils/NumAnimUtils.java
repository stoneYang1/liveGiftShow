package yang.stone.com.livegiftshow.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by ameng on 2017/2/24.
 * 数字放大动画
 */

public class NumAnimUtils {
    private Animator lastAnimator = null;
    
    public void start(View view) {
        if (lastAnimator != null) {
            lastAnimator.removeAllListeners();
            lastAnimator.end();
            lastAnimator.cancel();
        }
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",1.0f,1.3f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",1.0f,1.3f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        lastAnimator = animSet;
        animSet.setDuration(300);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }
}
