package com.nablanet.aula31.dagger.databinding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.nablanet.aula31.core.ImageLoader;

public class ImageBindingAdapter {

    ImageLoader imageLoader;

    public ImageBindingAdapter(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @BindingAdapter(value = {"imageUrl", "error"})
    public void loadImage(ImageView view, String imageUrl, Drawable error) {
        imageLoader.loadImage(view, imageUrl);
    }

}
