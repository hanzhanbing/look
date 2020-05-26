package cn.looksafe.client.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.looksafe.client.R;

public class ImagePagerAdapter extends CarouselPagerAdapter<CarouselViewPager> {

    ArrayList<String> uri;
    Context context;
    private Drawable drawable;
    BitmapFactory.Options options = new BitmapFactory.Options();
    protected BitmapUtils mBitmapUtils;

    public ImagePagerAdapter(Context context, CarouselViewPager viewPager, ArrayList<String> uri) {
        super(viewPager);
        mBitmapUtils = new BitmapUtils(context);
        this.uri = uri;
        this.context = context;
        options.inScaled = false;
        options.inSampleSize = 2;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }


    @SuppressLint("NewApi")
    @Override
    public Object instantiateRealItem(ViewGroup container, final int position) {
        View view = View.inflate(context, R.layout.n_vp_item, null);
        ImageView imageView = view.findViewById(R.id.nvi_img);
        mBitmapUtils.display(imageView, uri.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public int getRealDataCount() {
        return uri.size();
    }
}