# 插件描述
调用原生系统的一些工具插件，目前只支持android端！加入ios后会在此处进行说明！

## 如何使用
    添加依赖：在pubspec.yaml中添加依赖：
        dependencies:
          flutter_plugin_system: ^0.0.3//最新版本号
          
    获取依赖包：flutter packages get

    使用：
        import 'package:flutter_plugin_system/flutter_plugin_system.dart';
        //获取系统缓存：
        String cacheSize=await FlutterPluginSystem.getCacheSize();
        //判断是否使用vpn:
        bool isUsedVpn=await FlutterPluginSystem.isVpnUsed();
        //获取应用包名：
        String pakageName= await FlutterPluginSystem.getAppPakageName();
        //清除缓存
        bool isClear = await FlutterPluginSystem.clearCache();
