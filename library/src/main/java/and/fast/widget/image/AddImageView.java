package and.fast.widget.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class AddImageView extends ViewGroup {

    public AddImageView(Context context) {
        this(context, null);
    }

    public AddImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

}
