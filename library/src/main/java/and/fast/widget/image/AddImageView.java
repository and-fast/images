package and.fast.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import and.fast.simple.library.R;
import and.fast.widget.image.utils.GlideImageEngine;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class AddImageView extends FrameLayout {

    private boolean mDragEnable;
    private int     mMaxNumber, mSpanCount, mItemSpace;
    private int mImageLayoutRes, mAddImageLayoutRes, mImageId, mCloseId;

    private Adapter            mAdapter;
    private OnAddClickListener mOnAddClickListener;
    private ImageEngine        mImageEngine = new GlideImageEngine();

    public AddImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AddImageView);
        mImageLayoutRes = ta.getResourceId(R.styleable.AddImageView_image_layout_res, -1);
        mAddImageLayoutRes = ta.getResourceId(R.styleable.AddImageView_add_image_layout_res, -1);
        mImageId = ta.getResourceId(R.styleable.AddImageView_image_id, -1);
        mCloseId = ta.getResourceId(R.styleable.AddImageView_close_id, -1);
        mMaxNumber = ta.getInt(R.styleable.AddImageView_max_number, 9);
        mSpanCount = ta.getInt(R.styleable.AddImageView_span_count, 3);
        mDragEnable = ta.getBoolean(R.styleable.AddImageView_drag_enable, false);
        mItemSpace = ta.getDimensionPixelOffset(R.styleable.AddImageView_space, getResources().getDimensionPixelOffset(R.dimen.horizontal_space));
        ta.recycle();

        // 初始化列表
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        addView(recyclerView);

        mAdapter = new Adapter();
        recyclerView.setLayoutManager(new GridLayoutManager(context, mSpanCount));
        recyclerView.addItemDecoration(new ItemDecoration());
        post(() -> recyclerView.setAdapter(mAdapter));
        new ItemTouchHelper(new ItemTouchCallback()).attachToRecyclerView(recyclerView);
    }

    public void setImageEngine(ImageEngine engine) {
        this.mImageEngine = engine;
    }

    public void add(File data) {
        mAdapter.add(data);
    }

    public void addFile(List<File> files) {
        mAdapter.addAll(files);
    }

    public void addPath(List<String> paths){
        for (String path : paths) {
            mAdapter.getData().add(0, new File(path));
        }

        mAdapter.notifyDataSetChanged();
    }


    public List<File> obtainData() {
        ArrayList<File> files = new ArrayList<>();
        Collections.copy(files, mAdapter.getData());
        return files;
    }

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.mOnAddClickListener = onAddClickListener;
    }


    private class ItemDecoration extends RecyclerView.ItemDecoration {

        private int mItemSize;

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (mItemSize == 0) {
                int parentWidth = parent.getWidth() - (parent.getPaddingLeft() + parent.getPaddingRight());
                GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
                mItemSize = parentWidth / gridLayoutManager.getSpanCount();
            }

            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = lp.width = mItemSize - mItemSpace;
            view.setLayoutParams(lp);

            int position = parent.getChildLayoutPosition(view);
            if (position >= mSpanCount) {
                outRect.top = mItemSpace;
            }
        }
    }


    private class ItemTouchCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mAdapter.getData(), i, i + 1);
                }

            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mAdapter.getData(), i, i - 1);
                }
            }

            mAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
            int toPosition = target.getAdapterPosition();
            if (mAdapter.getData().size() != mMaxNumber) {
                if (toPosition == mAdapter.getItemCount() - 1) {
                    return false;
                }
            }

            return super.canDropOver(recyclerView, current, target);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return mDragEnable;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    }


    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<File> mData = new ArrayList<>();

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                return new AddImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(mAddImageLayoutRes, parent, false));
            }

            return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(mImageLayoutRes, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (isInEditMode()) {
                return;
            }

            if (getItemViewType(position) == 0) {
                holder.itemView.setOnClickListener(v -> mOnAddClickListener.add());

            } else if (getItemViewType(position) == 1) {

                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                mImageEngine.loadImage(holder.itemView.getContext(), imageViewHolder.mImageView, mData.get(position));

                // 查看，或添加图片
                imageViewHolder.mImageView.setOnClickListener(v -> mOnAddClickListener.preview(mData, position, v));

                // 删除图片
                if (imageViewHolder.mCloseImageView != null) {
                    imageViewHolder.mCloseImageView.setOnClickListener(v -> {
                        mData.remove(position);
                        notifyDataSetChanged();
                        mOnAddClickListener.delete(v);
                    });
                }
            }

        }

        @Override
        public int getItemCount() {
            if (mData.isEmpty()) {
                return 1;

            } else if (mData.size() < mMaxNumber) {
                return mData.size() + 1;
            }

            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mData.size() < mMaxNumber && position == mData.size()) { // 添加类型
                return 0;
            }

            return 1;
        }

        List<File> getData() {
            return mData;
        }

        void add(File data) {
            mData.add(0, data);
            notifyDataSetChanged();
        }

        void addAll(List<File> data) {
            mData.addAll(0, data);
            notifyDataSetChanged();
        }


        class ImageViewHolder extends RecyclerView.ViewHolder {

            private ImageView mImageView;
            private View      mCloseImageView;

            ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(mImageId);

                if (mCloseId != -1) {
                    mCloseImageView = itemView.findViewById(mCloseId);
                }
            }
        }


        class AddImageViewHolder extends RecyclerView.ViewHolder {

            AddImageViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }

}
