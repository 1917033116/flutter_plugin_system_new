import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPluginSystem {
  static const MethodChannel _channel =
      const MethodChannel('flutter_plugin_system');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  ///判断是否使用vpn
  static Future<bool> isVpnUsed() async {
    return await _channel.invokeMethod("isVpnUsed");
  }

  ///获取应用包名
  static Future<String> getAppPakageName() async {
    return await _channel.invokeMethod("getAppPakageName");
  }

  static Future<String> getCacheSize() async {
    return await _channel.invokeMethod("getCacheSize");
  }

  static Future<bool> clearCache() async {
    return await _channel.invokeMethod("clearCache");
  }

  static void lunchWebView(String url, String webViewLocation) async {
    List<String> arguments = [url, webViewLocation];
    await _channel.invokeMethod("lunchWebView", arguments);
  }
  static Future<bool> isHaveNetWork()async{
    return await _channel.invokeMethod("isHaveNetWork");
  }
}
