package and.fast.widget.image.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import and.fast.widget.image.ImageEngine;

public class GlideImageEngine implements ImageEngine {

    @Override
    public void loadImage(Context context, ImageView imageView, Uri uri) {
        Glide.with(imageView).load(uri).into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, File file) {
        Glide.with(imageView).load(file).into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return false;
    }

}
