package com.nablanet.aula31;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nablanet.aula31.core.ImageLoader;

public class ImageLoaderGlide implements ImageLoader {

    @Override
    public void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
    }

    @Override
    public void loadImage(ImageView imageView, String imageUrl, Drawable error) {
        RequestOptions requestOptions = new RequestOptions().error(error);
        Glide.with(imageView.getContext())
                .applyDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(imageView);
    }

    @Override
    public void loadImage(
            ImageView imageView, String imageUrl, Drawable error, Drawable placeholder
    ) {
        RequestOptions requestOptions = new RequestOptions().error(error).placeholder(placeholder);
        Glide.with(imageView.getContext())
                //.applyDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(imageView);
    }

}
