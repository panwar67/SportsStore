package com.lions.app.sportsstore.structs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by Panwar on 27/01/18.
 */

public class NewImageSlider extends BaseSliderView {




    public NewImageSlider(Context context) {
        super(context);
        setScaleType(ScaleType.CenterInside);
        //        setScaleType(ScaleType.Fit);
    }



    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(com.daimajia.slider.library.R.layout.render_type_default,null);
        ImageView target = (ImageView)v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
        target.setScaleType(ImageView.ScaleType.FIT_CENTER);

        bindEventAndShow(v, target);
        return v;
    }
}