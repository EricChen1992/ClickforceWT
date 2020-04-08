package com.click.force.clickforce.working.time;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class RotateAnim extends Animation {

    int centerX, centerY;

    Camera camera = new Camera();

    public final int X = 0;

    public final int Y = 1;

    public int direction = Y;

    public boolean isZhengfangxiang = true;

    public Context context;

    @Override

    public void initialize(int width, int height, int parentWidth, int parentHeight) {

        super.initialize(width, height, parentWidth, parentHeight);

        //中心點座標
        centerX = width / 2;

        centerY = height / 2;


    }

    @Override

    protected void applyTransformation(float interpolatedTime, Transformation t) {

        super.applyTransformation(interpolatedTime, t);

        final Matrix matrix = t.getMatrix();

        camera.save();

        //中心是繞Ｙ軸旋轉 這裡可以自行設定 X、Y、Z
        if (direction == X) {

            if (isZhengfangxiang) {

                camera.rotateX(360 * interpolatedTime);

            } else {

                camera.rotateX(360 - 360 * interpolatedTime);

            }

        } else {

            if (isZhengfangxiang) {

                camera.rotateY(360 * interpolatedTime);

            } else {

                camera.rotateY(360 - 360 * interpolatedTime);

            }

        }

        //把鏡頭加在變換陣列上
        camera.getMatrix(matrix);

        //設定翻轉中心點
        matrix.preTranslate(-centerX, -centerY);

        matrix.postTranslate(centerX, centerY);

//        scaleCurrentDuration(0);

        camera.restore();//Restores the saved state, if any.
    }
}
