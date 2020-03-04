package com.nablanet.aula31;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nablanet.aula31.core.ImageLoader;

public class ImageLoaderGlide implements ImageLoader {

    @Override
    public void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
    }

    @Override
    public void loadImage(ImageView imageView, String imageUrl, Drawable error) {
        loadImage(imageView, imageUrl);
    }

}
