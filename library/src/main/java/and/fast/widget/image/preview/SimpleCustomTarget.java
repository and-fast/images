package and.fast.widget.image.preview;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SimpleCustomTarget<T> extends CustomTarget<T> {

    @Override
    public void onResourceReady(@NonNull T resource, @Nullable Transition<? super T> transition) {

    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {

    }

}
