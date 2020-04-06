package and.fast.simple.images;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import and.fast.widget.image.AddImageView;
import and.fast.widget.image.OnAddClickListener;
import and.fast.widget.image.utils.GlideImageEngine;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnAddClickListener {

    private AddImageView mAddImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddImageView = findViewById(R.id.add_image_view);
        mAddImageView.setOnAddClickListener(this);
    }

    @Override
    public void add() {
        Matisse.from(MainActivity.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
                .showSingleMediaType(true)
                .gridExpectedSize(360)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showPreview(false) // Default is `true`
                .forResult(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mAddImageView.addPath(Matisse.obtainPathResult(data));
        }
    }

    @Override
    public void delete(View view) {

    }

    @Override
    public void preview(List<File> data, int position, View view) {
        StringBuffer sb = new StringBuffer();
        for (File file : mAddImageView.obtainData()) {
            sb.append(file.getAbsoluteFile()).append("\n");
        }

        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    public void onPreview(View view) {
        startActivity(new Intent(this, PreviewActivity.class));
    }
}
