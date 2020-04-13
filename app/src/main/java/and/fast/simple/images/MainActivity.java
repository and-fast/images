package and.fast.simple.images;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.vansz.glideimageloader.GlideImageLoader;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import and.fast.widget.image.add.AddImageLayout;
import and.fast.widget.image.add.OnAddClickListener;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
        }
    }

    @Override
    public void preview(List<File> data, int position, View view) {
        ArrayList<String> urls = new ArrayList<>(Arrays.asList(
                "https://img3.doubanio.com/view/status/l/public/2927b09da6017a0.webp",
                "https://ww1.sinaimg.cn/bmiddle/6d33e6faly1gdise58zjbj20xc9mex6p.jpg",
                "https://gank.io/images/882afc997ad84f8ab2a313f6ce0f3522",
                "https://wx2.sinaimg.cn/large/006Bk55sly1g2rlx9wc9gg30a80i8e83.gif"
        ));

        Transferee transferee = Transferee.getDefault(this);
        TransferConfig config = TransferConfig.build()
                .setSourceImageList(urls) // 图片url集合
//                .setMissPlaceHolder(R.mipmap.ic_launcher) // 图片加载前的占位图
//                .setErrorPlaceHolder(R.mipmap.ic_launcher) // 图片加载错误后的占位图
                .setProgressIndicator(new ProgressPieIndicator()) // 图片加载进度指示器
                .setIndexIndicator(new NumberIndexIndicator()) // 图片数量索引指示器
                .setImageLoader(GlideImageLoader.with(getApplicationContext())) // 设置图片加载器
                .setJustLoadHitImage(true) // 是否只加载当前显示在屏幕中的的图片
                .enableDragClose(true) // 开启拖拽关闭

//                .setOnLongClickListener(new Transferee.OnTransfereeLongClickListener() {
////                    @Override
////                    public void onLongClick(ImageView imageView, String imageUri, int pos) {
////                        //saveImageFile(imageUri); // 使用 transferee.getFile(imageUri) 获取缓存文件保存
////                    }
////                })
                .bindRecyclerView((RecyclerView) mAddImageView.getChildAt(0), R.id.iv_image);
        //config.setNowThumbnailIndex(0);
        transferee.apply(config).show();

        Toast.makeText(this, mAddImageView.obtainData().toString(), Toast.LENGTH_SHORT).show();
    }

    public void onPreview(View view) {
        startActivity(new Intent(this, PreviewActivity.class));
    }

}
