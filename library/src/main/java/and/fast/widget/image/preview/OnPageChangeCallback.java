package and.fast.widget.image.preview;

import android.os.Parcelable;

import java.io.Serializable;

import androidx.annotation.Px;

public interface OnPageChangeCallback extends Serializable {

    default void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels) {
    }

    void onPageSelected(int position);

}
