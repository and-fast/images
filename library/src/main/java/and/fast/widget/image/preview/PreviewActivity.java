package and.fast.widget.image.preview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import and.fast.simple.library.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class PreviewActivity extends AppCompatActivity {

    public static void newIntent(List<String> list, View v, int p) {
        Intent intent = new Intent(v.getContext(), PreviewActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((Activity) v.getContext()), v, "img");
        intent.putStringArrayListExtra("model", new ArrayList<>(list));
        intent.putExtra("p", p);
        v.getContext().startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        ArrayList<String> model = getIntent().getStringArrayListExtra("model");
        //getIntent().getSerializableExtra()

        if (model != null) {
//            int p = getIntent().getIntExtra("p", 0);
            recyclerView.setAdapter(new ImageAdapter(model));
            recyclerView.scrollToPosition(getIntent().getIntExtra("p", 0));

        } else {
            recyclerView.setAdapter(new ImageAdapter());
        }

    }


    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

        private List<String> data = new ArrayList<>(Arrays.asList(
                "https://img3.doubanio.com/view/status/l/public/2927b09da6017a0.webp",
                "https://ww1.sinaimg.cn/bmiddle/6d33e6faly1gdise58zjbj20xc9mex6p.jpg",
                "https://gank.io/images/882afc997ad84f8ab2a313f6ce0f3522",
                "https://wx2.sinaimg.cn/large/006Bk55sly1g2rlx9wc9gg30a80i8e83.gif"
        ));

        public ImageAdapter() {
        }

        public ImageAdapter(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item_preview_image, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            final RequestManager requestManager = Glide.with(holder.itemView.getContext());

            // 单击退出
            // holder.photoView.setOnPhotoTapListener((view, x, y) -> finish());

            requestManager
                    .asBitmap()
                    .load(data.get(position))
                    .into(new CustomTarget<Bitmap>() {

                        @SuppressLint("CheckResult")
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            int h = bitmap.getHeight();
                            int w = bitmap.getWidth();
                            int MAX_SIZE = 4096, MAX_SCALE = 8;
                            if (h >= MAX_SIZE || h / w > MAX_SCALE) {

                                requestManager
                                        .asFile()
                                        .load(data.get(position))
                                        .into(new CustomTarget<File>() {

                                            @Override
                                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                                holder.photoView.setVisibility(View.GONE);
                                                holder.scaleImageView.setVisibility(View.VISIBLE);

                                                float scale = getImageScale(PreviewActivity.this, resource.getAbsolutePath());
                                                ImageViewState imageViewState = new ImageViewState(scale, new PointF(0, 0), 0);
                                                holder.scaleImageView.setImage(ImageSource.uri(resource.getAbsolutePath()), imageViewState);
                                                holder.scaleImageView.setDoubleTapZoomScale(scale);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }

                                        });


                            } else {

                                holder.photoView.setImageBitmap(bitmap);
                                holder.photoView.setVisibility(View.VISIBLE);
                                holder.scaleImageView.setVisibility(View.GONE);
//                                requestManager.asGif().load(data.get(position)).into(holder.photoView);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                    });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ImageViewHolder extends RecyclerView.ViewHolder {
            private PhotoView                 photoView;
            private SubsamplingScaleImageView scaleImageView;

            ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                photoView = itemView.findViewById(R.id.photo_view);
                scaleImageView = itemView.findViewById(R.id.scale_image_view);
            }
        }

    }


    public static float getImageScale(Context context, String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return 2.0f;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        if (bitmap == null) {
            return 2.0f;
        }

        // 拿到图片的宽和高
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();

        WindowManager wm = ((Activity) context).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        float scale = 1.0f;
        // 图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }

        // 图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;
        }

        // 图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }

        // 图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }

        bitmap.recycle();
        return scale;
    }


    float getDoubleTapScale(int width, int height, int viewWidth, int viewHeight) {
        if (viewWidth <= 0 || viewHeight <= 0) {
            return 1;
        }

        float sx = viewWidth * 1.f / width;
        float sy = viewHeight * 1.f / height;

        float scale = Math.max(sx, sy);
        if (Math.abs(sx - sy) < Float.MIN_NORMAL) {
            scale = 2 * sx;
        }

        return scale;
    }

}
