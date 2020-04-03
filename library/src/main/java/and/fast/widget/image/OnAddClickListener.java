package and.fast.widget.image;

import android.view.View;

import java.util.List;

public interface OnAddClickListener {

    void add();

    default void preview(List<String> data, int position) {

    }

    void delete(View view);

}
