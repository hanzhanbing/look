package com.look.core.manager;

import android.app.Activity;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;


/**
 * Created by huyg on 2020-02-13.
 */
public class PermissionManager {


    public PermissionManager(){

    }


    public void initPermission(Activity activity, PermissionListener listener) {
        XXPermissions.with(activity)
                //.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Permission.ACCESS_COARSE_LOCATION,
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE
                        )
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            listener.hasPermission(true);
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        listener.hasPermission(false);
                    }
                });
    }


    public void reqPermission(Activity activity, String permission, PermissionListener listener) {
        XXPermissions.with(activity)
                .permission(permission)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            listener.hasPermission(true);
                        }

                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        listener.hasPermission(false);
                    }
                });
    }


    public interface PermissionListener {
        void hasPermission(boolean isOk);
    }

}
