package cn.looksafe.client.my;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.look.core.manager.SpManager;
import com.look.core.ui.BaseActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;

import cn.looksafe.client.R;
import cn.looksafe.client.beans.VipMenuHttp;
import cn.looksafe.client.beans.WxpayInitBean;
import cn.looksafe.client.databinding.ActivityVipBinding;
import cn.looksafe.client.models.VipMenuModel;
import cn.looksafe.client.tools.AppConfig;
import cn.looksafe.client.tools.Contents;
import cn.looksafe.client.tools.HttpTools;
import cn.looksafe.client.tools.Tools;
import cn.looksafe.client.utils.HttpCallBack;
import cn.looksafe.client.utils.HttpStatus;

@Route(path = "/user/vip")
public class VipActivity extends BaseActivity<ActivityVipBinding> implements HttpCallBack, View.OnClickListener {
    private int selectType = -1;
    private VipMenuModel vipMenuModel = new VipMenuModel();
    private ArrayList<VipMenuHttp.Menu> priceList = new ArrayList<>();
    private IWXAPI api;
    public static Handler mHandler;
    public static final int HD_PAY_OK = 0;//支付成功
    public static final int HD_PAY_CANCLE = 1;//支付取消
    public static final int HD_PAY_FAILURE = 2;//支付失败


    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void init() {
        mBinding.setMenuModel(vipMenuModel);
        mBinding.avSub.setOnClickListener(this);
        mBinding.avMenu1Rel.setOnClickListener(this);
        mBinding.avMenu2Rel.setOnClickListener(this);
        mBinding.avMenu3Rel.setOnClickListener(this);
        mBinding.avPro.setOnClickListener(null);
        mBinding.avpSubPay.setOnClickListener(this);
        vipMenuModel.setVipdate("会员有效期至:" + SpManager.getInstance(mContext).getSP("date"));
        api = WXAPIFactory.createWXAPI(mContext, null);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HD_PAY_OK:
                        finish();
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_LONG).show();
                        break;
                    case HD_PAY_FAILURE:
                        finish();
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_LONG).show();
                        break;
                    case HD_PAY_CANCLE:
                        finish();
                        Toast.makeText(mContext, "支付取消", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        HttpTools.getVipDate(mContext, VipActivity.this);
    }


    @Override
    public void requestSuccess(int code, Object object, Object object2) {
        switch (code) {
            case HttpStatus.HTTP_GET_VIP_MENU_SUC:
                VipMenuHttp vipMenuHttp = (VipMenuHttp) object;
                if (vipMenuHttp != null && vipMenuHttp.list != null && vipMenuHttp.list.size() > 0) {
                    for (int i = 0; i < vipMenuHttp.list.size(); i++) {
                        priceList.add(vipMenuHttp.list.get(i));
                        switch (i) {
                            case 0:
                                vipMenuModel.setShowMenu1(true);
                                vipMenuModel.setMenu1(vipMenuHttp.list.get(0).pname);
                                vipMenuModel.setMenu1price("¥" + (vipMenuHttp.list.get(0).realamount / 100.00));
                                break;
                            case 1:
                                vipMenuModel.setShowMenu2(true);
                                vipMenuModel.setMenu2(vipMenuHttp.list.get(1).pname);
                                vipMenuModel.setMenu2price("¥" + (vipMenuHttp.list.get(1).realamount / 100.00));
                                break;
                            case 2:
                                vipMenuModel.setShowMenu3(true);
                                vipMenuModel.setMenu3(vipMenuHttp.list.get(2).pname);
                                vipMenuModel.setMenu3price("¥" + (vipMenuHttp.list.get(2).realamount / 100.00));
                                break;
                        }
                    }
                }
                break;
            case HttpStatus.HTTP_PRE_PAY_SUC://获取预支付成功
                WxpayInitBean wxpayInitBean = (WxpayInitBean) object;
                api.registerApp(wxpayInitBean.appid);
                PayReq req = new PayReq();
                req.appId = AppConfig.wx_appid;
                req.partnerId = wxpayInitBean.partnerid;
                req.prepayId = wxpayInitBean.prepay_id;
                req.nonceStr = Tools.getRandomString(32);
                req.timeStamp = wxpayInitBean.timestamp;
                req.packageValue = "Sign=WXPay";
                String md = "appid=" + req.appId + "&noncestr=" + req.nonceStr + "&package=" + req.packageValue + "&partnerid=" + req.partnerId + "&prepayid=" + req.prepayId + "&timestamp=" + req.timeStamp + "&key=" + AppConfig.wx_key;
                req.sign = Tools.getWxMD5(md, 32);
                api.sendReq(req);

                break;
            case HttpStatus.HTTP_GET_VIP_DATE_SUC:
                vipMenuModel.setVipdate("会员有效期至" + SpManager.getInstance(mContext).getSP("date"));
                break;
        }
    }

    @Override
    public void requestFailure(int code, Object object) {
        //toast("支付失败");
        vipMenuModel.setShowProgress(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpTools.getViplist(mContext, VipActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.av_menu1_rel:
                selectMenu(1);
                break;
            case R.id.av_menu2_rel:
                selectMenu(2);
                break;
            case R.id.av_menu3_rel:
                selectMenu(3);
                break;
            case R.id.av_sub:
                if (selectType < 1) {
                    Toast.makeText(mContext, "请选择套餐", Toast.LENGTH_SHORT).show();
                    return;
                }
                showPayDetail();
                break;
//            case R.id.avp_back:
//                vipMenuModel.setShowPaySelectUi(false);
//                break;
            case R.id.avp_sub_pay:
                if (!mBinding.avpCkWx.isChecked()) {
                    Toast.makeText(mContext, "请选择支付方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                toPay();
                break;
        }
    }

    private void toPay() {
        vipMenuModel.setShowProgress(true);
        //进行请求预支付
        HttpTools.prePay(mContext, VipActivity.this, priceList.get(selectType - 1).realamount, priceList.get(selectType - 1).id);
    }


    private void showPayDetail() {
        //订单信息
        vipMenuModel.setShowPaySelectUi(true);
        vipMenuModel.setPayGoods("订单详情:" + priceList.get(selectType - 1).pname);
        vipMenuModel.setPayMoney("¥" + (priceList.get(selectType - 1).realamount / 100.00));
    }


    private void selectMenu(int type) {
        selectType = type;
        mBinding.avMenu1Rel.setSelected(false);
        mBinding.avMenu2Rel.setSelected(false);
        mBinding.avMenu3Rel.setSelected(false);
        switch (type) {
            case 1:
                mBinding.avMenu1Rel.setSelected(true);
                break;
            case 2:
                mBinding.avMenu2Rel.setSelected(true);
                break;
            case 3:
                mBinding.avMenu3Rel.setSelected(true);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void setActionBar() {
        super.setActionBar();
        mTitle.setText("会员中心");
    }
}
