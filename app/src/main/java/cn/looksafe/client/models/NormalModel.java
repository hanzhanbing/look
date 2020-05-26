package cn.looksafe.client.models;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import cn.looksafe.client.BR;


public class NormalModel extends BaseObservable {
    private boolean showProgress;
    private String tip1;
    private String tip2;
    private String tip3;
    private String tip4;
    private boolean showRedPoint;

    @Bindable
    public boolean isShowRedPoint() {
        return showRedPoint;
    }

    public void setShowRedPoint(boolean showRedPoint) {
        this.showRedPoint = showRedPoint;
        notifyPropertyChanged(BR.showRedPoint);
    }

    @Bindable
    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        notifyPropertyChanged(BR.showProgress);

    }

    @Bindable
    public String getTip1() {
        return tip1;
    }

    public void setTip1(String tip1) {
        this.tip1 = tip1;
        notifyPropertyChanged(BR.tip1);
    }

    @Bindable
    public String getTip2() {
        return tip2;
    }

    public void setTip2(String tip2) {
        this.tip2 = tip2;
        notifyPropertyChanged(BR.tip2);
    }

    @Bindable
    public String getTip3() {
        return tip3;
    }

    public void setTip3(String tip3) {
        this.tip3 = tip3;
        notifyPropertyChanged(BR.tip3);
    }

    @Bindable
    public String getTip4() {
        return tip4;
    }

    public void setTip4(String tip4) {
        this.tip4 = tip4;
        notifyPropertyChanged(BR.tip4);
    }
}
