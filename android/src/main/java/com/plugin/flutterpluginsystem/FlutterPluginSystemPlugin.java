package com.plugin.flutterpluginsystem;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterPluginSystemPlugin
 */
public class FlutterPluginSystemPlugin implements MethodCallHandler {
    private Activity mActivity;

    public FlutterPluginSystemPlugin(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_plugin_system");
        channel.setMethodCallHandler(new FlutterPluginSystemPlugin(registrar.activity()));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        switch (call.method) {
            case "isVpnUsed":
                isVpnUsed(result);
                break;
            case "getAppPakageName":
                getAppPakageName(result);
                break;
            case "getCacheSize":
                try {
                    result.success(DataCleanManager.getTotalCacheSize(mActivity));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "clearCache":
                DataCleanManager.clearAllCache(mActivity);
                result.success(true);
                break;
            case "lunchWebView":
                lunchWebView((List<String>) call.arguments);
                break;
            case "isHaveNetWork":
                isHaveNetWork(result);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void isHaveNetWork(MethodChannel.Result result) {
        boolean isHaveNetWork = false;
        ConnectivityManager manager = (ConnectivityManager) mActivity.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            isHaveNetWork = false;
        } else {
            NetworkInfo info = manager.getActiveNetworkInfo();
            isHaveNetWork = !(info == null || info.isAvailable());
        }
        result.success(isHaveNetWork);
    }

    private void lunchWebView(List<String> arguments) {
//        Toast.makeText(mActivity, "${arguments[0]}----${arguments[1]}", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        intent.setComponent(new ComponentName(mActivity,arguments.get(1)));
        intent.putExtra("extra_url",arguments.get(0));
        mActivity.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void getAppPakageName(MethodChannel.Result result) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)
                //得到当前应用
                result.success(info.processName); //返回包名
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void isVpnUsed(MethodChannel.Result result) {
        boolean isUsedVpn = false;
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0" == intf.getName() || "ppp0" == intf.getName()) {
                        isUsedVpn = true; // The VPN is up
                    }
                }

            }
            result.success(isUsedVpn);
        } catch (Throwable e) {
            result.error("检查vpn发生异常", e.getMessage(), isUsedVpn);
            e.printStackTrace();
        }
    }
}
