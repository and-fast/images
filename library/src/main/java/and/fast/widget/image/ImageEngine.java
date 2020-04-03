package and.fast.widget.image;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

public interface ImageEngine {

    void loadImage(Context context, ImageView imageView, Uri uri);

    void loadImage(Context context, ImageView imageView, File file);

    boolean supportAnimatedGif();

}
