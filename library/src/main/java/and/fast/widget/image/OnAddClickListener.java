package and.fast.widget.image;

import android.view.View;

import java.io.File;
import java.util.List;

public interface OnAddClickListener {

    void add();

    default void preview(List<File> path, int position, View view) {

    }

    void delete(View view);

}
