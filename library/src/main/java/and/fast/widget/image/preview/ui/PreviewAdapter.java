package and.fast.widget.image.preview.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
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
import java.util.List;

import and.fast.simple.library.R;
import and.fast.widget.image.preview.SimpleCustomTarget;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ImageViewHolder> {

    private List<String> data;

    PreviewAdapter() {
        data = new ArrayList<>();
    }

    public PreviewAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PreviewAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_pager_item_preview_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ImageViewHolder holder, int position) {
        final RequestManager requestManager = Glide.with(holder.itemView.getContext());

        requestManager
                .asBitmap()
                .load(data.get(position))
                .into(new SimpleCustomTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        int h = bitmap.getHeight();
                        int w = bitmap.getWidth();
                        int MAX_SIZE = 4096, MAX_SCALE = 8;

                        if (h >= MAX_SIZE || h / w > MAX_SCALE) {
                            requestManager.asFile().load(data.get(position)).into(new LongPictureCustomTarget(holder));

                        } else {

                            holder.photoView.setImageBitmap(bitmap);
                            holder.photoView.setVisibility(View.VISIBLE);
                            holder.scaleImageView.setVisibility(View.GONE);
                        }
                    }

                });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    static class LongPictureCustomTarget extends CustomTarget<File> {

        private PreviewAdapter.ImageViewHolder holder;

        LongPictureCustomTarget(ImageViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
            holder.photoView.setVisibility(View.GONE);
            holder.scaleImageView.setVisibility(View.VISIBLE);

            float scale = getImageScale(holder.itemView.getContext(), resource.getAbsolutePath());
            ImageViewState imageViewState = new ImageViewState(scale, new PointF(0, 0), 0);
            holder.scaleImageView.setImage(ImageSource.uri(resource.getAbsolutePath()), imageViewState);
            holder.scaleImageView.setDoubleTapZoomScale(scale);
        }

        private float getImageScale(Context context, String imagePath) {
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

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {

        }
    }


    static class ImageViewHolder extends RecyclerView.ViewHolder {

        private PhotoView photoView;

        private SubsamplingScaleImageView scaleImageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photo_view);
            scaleImageView = itemView.findViewById(R.id.scale_image_view);
        }
    }

}
