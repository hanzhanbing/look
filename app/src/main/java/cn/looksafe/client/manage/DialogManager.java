package cn.looksafe.client.manage;


import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


import cn.looksafe.client.R;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;

/**
 * Created by huyg on 2020-03-16.
 */
public class DialogManager {

    private static final class DialogManagerHolder {
        private static final DialogManager INSTANCE = new DialogManager();
    }

    public static DialogManager getInstance() {
        return DialogManager.DialogManagerHolder.INSTANCE;
    }

    private Listener mListener;

    public DialogManager() {

    }


    public void showTipDialog(Activity activity, String title, String message, String positive, String negative,Listener listener) {
        mListener = listener;
        AnyLayer.dialog(activity)
                .contentView(R.layout.layout_dialog_tip)
                .gravity(Gravity.CENTER)
                .backgroundDimDefault()
                .cancelableOnTouchOutside(true)
                .cancelableOnClickKeyBack(true)
                .bindData(new Layer.DataBinder() {
                    @Override
                    public void bindData(Layer layer) {
                        ((TextView) layer.getView(R.id.dialog_message)).setText(message);
                        ((TextView) layer.getView(R.id.dialog_title)).setText(title);
                        ((TextView) layer.getView(R.id.dialog_positive)).setText(positive);
                        if (!TextUtils.isEmpty(negative)) {
                            ((TextView) layer.getView(R.id.dialog_negative)).setText(negative);
                            layer.getView(R.id.dialog_negative).setVisibility(View.VISIBLE);
                        } else {
                            layer.getView(R.id.dialog_negative).setVisibility(View.GONE);
                        }
                    }
                })
                .onClickToDismiss(R.id.dialog_negative)
                .onClick(new Layer.OnClickListener() {
                    @Override
                    public void onClick(Layer layer, View v) {
                        layer.dismiss();
                        mListener.onClickListener();
                    }
                }, R.id.dialog_positive)
                .show();
    }


    public interface Listener {

        void onClickListener();

    }

}
