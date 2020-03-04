package com.nablanet.aula31.core;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public interface ImageLoader {

    void loadImage(ImageView imageView, String imageUrl);

    void loadImage(ImageView imageView, String imageUrl, Drawable error);

}
