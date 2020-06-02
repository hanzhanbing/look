package cn.looksafe.client.utils;

import android.content.Context;
import android.widget.ImageView;

import com.look.core.manager.GlideManager;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by huyg on 2018/8/6.
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //  imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test));

        GlideManager.getInstance().displayNetImage(context, String.valueOf(path), imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }
}
