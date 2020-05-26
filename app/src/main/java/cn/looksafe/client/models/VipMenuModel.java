package cn.looksafe.client.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import cn.looksafe.client.BR;

public class VipMenuModel extends BaseObservable {
    private String vipdate;
    private String menu1;
    private String menu1price;
    private String menu2;
    private String menu2price;
    private String menu3;
    private String menu3price;
    private boolean showMenu1;
    private boolean showMenu2;
    private boolean showMenu3;
    private boolean showPaySelectUi;
    private String payMoney;
    private String payGoods;
    private boolean showProgress;

    @Bindable
    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        notifyPropertyChanged(BR.showProgress);
    }

    @Bindable
    public String getPayGoods() {
        return payGoods;
    }

    public void setPayGoods(String payGoods) {
        this.payGoods = payGoods;
        notifyPropertyChanged(BR.payGoods);
    }

    @Bindable
    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
        notifyPropertyChanged(BR.payMoney);
    }

    @Bindable
    public boolean isShowPaySelectUi() {
        return showPaySelectUi;
    }

    public void setShowPaySelectUi(boolean showPaySelectUi) {
        this.showPaySelectUi = showPaySelectUi;
        notifyPropertyChanged(BR.showPaySelectUi);
    }

    @Bindable
    public boolean isShowMenu1() {
        return showMenu1;
    }

    public void setShowMenu1(boolean showMenu1) {
        this.showMenu1 = showMenu1;
        notifyPropertyChanged(BR.showMenu1);
    }

    @Bindable
    public boolean isShowMenu2() {
        return showMenu2;
    }

    public void setShowMenu2(boolean showMenu2) {
        this.showMenu2 = showMenu2;
        notifyPropertyChanged(BR.showMenu2);
    }

    @Bindable
    public boolean isShowMenu3() {
        return showMenu3;
    }

    public void setShowMenu3(boolean showMenu3) {
        this.showMenu3 = showMenu3;
        notifyPropertyChanged(BR.showMenu3);
    }

    @Bindable
    public String getVipdate() {
        return vipdate;
    }

    public void setVipdate(String vipdate) {
        this.vipdate = vipdate;
        notifyPropertyChanged(BR.vipdate);
    }

    @Bindable
    public String getMenu1() {
        return menu1;
    }

    public void setMenu1(String menu1) {
        this.menu1 = menu1;
        notifyPropertyChanged(BR.menu1);
    }

    @Bindable
    public String getMenu1price() {
        return menu1price;
    }

    public void setMenu1price(String menu1price) {
        this.menu1price = menu1price;
        notifyPropertyChanged(BR.menu1price);
    }

    @Bindable
    public String getMenu2() {
        return menu2;
    }

    public void setMenu2(String menu2) {
        this.menu2 = menu2;
        notifyPropertyChanged(BR.menu2);
    }

    @Bindable
    public String getMenu2price() {
        return menu2price;
    }

    public void setMenu2price(String menu2price) {
        this.menu2price = menu2price;
        notifyPropertyChanged(BR.menu2price);
    }

    @Bindable
    public String getMenu3() {
        return menu3;
    }

    public void setMenu3(String menu3) {
        this.menu3 = menu3;
        notifyPropertyChanged(BR.menu3);
    }

    @Bindable
    public String getMenu3price() {
        return menu3price;
    }

    public void setMenu3price(String menu3price) {
        this.menu3price = menu3price;
        notifyPropertyChanged(BR.menu3price);
    }
}
