package and.fast.widget.image.preview.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import and.fast.simple.library.R;
import and.fast.widget.image.preview.OnPageChangeCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class PreviewFragment extends Fragment {

    private PreviewFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.images_fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);

        Bundle arguments = getArguments();
        ArrayList<String> imageLinks = arguments.getStringArrayList("DATA");
        OnPageChangeCallback callback = (OnPageChangeCallback) arguments.getSerializable("CALLBACK");

        viewPager.setAdapter(new PreviewAdapter(imageLinks));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                if (callback != null){
                    callback.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (callback != null){
                    callback.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }
        });
    }


    public static class Builder {

        private ArrayList<String> mImageLinks; // 图片链接地址

        private OnPageChangeCallback mCallback; // 滑动回调

        public Builder setImages(ArrayList<String> images) {
            this.mImageLinks = images;
            return this;
        }

        public Builder setImage(String image){
            return setImages(new ArrayList<>(Collections.singleton(image)));
        }

        public Builder setOnPageChangeCallback(@NonNull OnPageChangeCallback callback) {
            this.mCallback = callback;
            return this;
        }

        public PreviewFragment build() {
            PreviewFragment previewFragment = new PreviewFragment();
            Bundle arguments = new Bundle();
            arguments.putStringArrayList("DATA", mImageLinks);
            arguments.putSerializable("CALLBACK", mCallback);
            previewFragment.setArguments(arguments);
            return previewFragment;
        }
    }
}
