package and.fast.simple.images;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.vansz.glideimageloader.GlideImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import and.fast.widget.image.preview.OnPageChangeCallback;
import and.fast.widget.image.preview.ui.PreviewFragment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PreviewActivity extends AppCompatActivity {

    private ArrayList<String> data = new ArrayList<>(Arrays.asList(
            "https://img3.doubanio.com/view/status/l/public/2927b09da6017a0.webp",
            "https://ww1.sinaimg.cn/bmiddle/6d33e6faly1gdise58zjbj20xc9mex6p.jpg",
            "https://gank.io/images/882afc997ad84f8ab2a313f6ce0f3522",
            "https://wx2.sinaimg.cn/large/006Bk55sly1g2rlx9wc9gg30a80i8e83.gif"
    ));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);


        TextView tvNumber = findViewById(R.id.tv_number);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, new PreviewFragment.Builder()
                .setImages(data)
                .setOnPageChangeCallback(new OnPageChangeCallback() {

                    @Override
                    public void onPageSelected(int position) {
                        tvNumber.setText(position + "/" + data.size());
                    }

                }).build())
                .commit();

        findViewById(R.id.layout).setOnClickListener(v -> finish());
    }

}
