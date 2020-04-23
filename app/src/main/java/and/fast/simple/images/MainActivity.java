package and.fast.simple.images;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.List;

import and.fast.widget.image.add.AddImageLayout;
import and.fast.widget.image.add.OnAddClickListener;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnAddClickListener {

    private AddImageLayout mAddImageView;

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

//            Transferee.getDefault(this)
//                    .apply(TransferConfig.build()
//                            .setImageLoader(GlideImageLoader.with(getApplicationContext()))
//                            .bindImageView(sourceIv, imageUrl)
//                    ).show();
        }
    }

    @Override
    public void preview(List<File> data, int position, View view) {
//        Transferee transferee = Transferee.getDefault(this);
//        TransferConfig config = TransferConfig.build()
//                .setSourceFileList(data) // 图片url集合
//                .setProgressIndicator(new ProgressPieIndicator()) // 图片加载进度指示器
//                .setIndexIndicator(new NumberIndexIndicator()) // 图片数量索引指示器
//                .setImageLoader(GlideImageLoader.with(getApplicationContext())) // 设置图片加载器
//                .setJustLoadHitImage(true) // 是否只加载当前显示在屏幕中的的图片
//                .enableDragClose(true) // 开启拖拽关闭
//                .bindRecyclerView((RecyclerView) mAddImageView.getChildAt(0), R.id.iv_image);
//        config.setNowThumbnailIndex(position);
//        transferee.apply(config).show();
    }

    public void onPreview(View view) {
        //startActivity(new Intent(this, PreviewActivity.class));
    }

    public void onAdd(View view) {
        add();
    }
}
