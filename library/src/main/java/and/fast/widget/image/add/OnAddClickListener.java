package and.fast.widget.image.add;

import android.view.View;

import java.io.File;
import java.util.List;

public interface OnAddClickListener {

    void add();

    default void preview(List<File> files, int position, View view) {

    }

    void delete(int position, View view);

}
