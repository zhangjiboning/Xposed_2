package com.example.zhangji.xposed_2;

/**
 * Created by zhangji on 2016-9-20.
 */
import android.os.Build;
import android.view.View;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class  Test implements IXposedHookLoadPackage{
    @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {



        //XposedBridge.log("loaded: " + loadPackageParam.packageName);
        android.util.Log.d("--- TAG ---",loadPackageParam.packageName);
        XposedHelpers.setStaticObjectField(Build.class, "MODEL", "你的手机型号是苹果9");
        XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", "你的手机厂商是苹果");


        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                android.util.Log.d("--- TAG ---", "IMEI：" + param.getResult());
                param.setResult("11111111");//修改IMEI
                super.afterHookedMethod(param);
            }
        });

        if( loadPackageParam.packageName.equals("com.example.target.myapplication")){

            //XposedHelpers.setStaticObjectField(Build.class,"MODEL","你的手机型号是苹果9");
            //XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", "你的手机厂商是苹果");　　
            /**

            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getDeviceId", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    android.util.Log.d("--- TAG ---","IMEI：" + param.getResult());
                    param.setResult("11111111");
                    super.afterHookedMethod(param);
                }
            });

            */
            XposedHelpers.findAndHookMethod("com.example.target.myapplication.MainActivity", loadPackageParam.classLoader, "testFunction", int.class, int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                   // param.setResult( 20 ); //设计结果是 20
                    super.afterHookedMethod(param);
                }

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    android.util.Log.d("--- TAG ---","参数1：" + String.valueOf((int)param.args[0]));//拦截第一个第二个参数
                    android.util.Log.d("--- TAG ---","参数2：" + String.valueOf((int)param.args[1]));
                    //param.args[0]=7;//改变第一个第二个参数值
                    //param.args[1]=8;

                    super.beforeHookedMethod(param);
                }
            });
            XposedHelpers.findAndHookMethod("com.example.target.myapplication.MainActivity", loadPackageParam.classLoader, "Button_Onclick_Test", View.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    int xxx=XposedHelpers.getIntField(param.thisObject,"xxx");
                    android.util.Log.d("--- TAG ---", "参数xxx:：" + String.valueOf(xxx));
                    XposedHelpers.setIntField(param.thisObject,"xxx",100);

                    super.beforeHookedMethod(param);
                }
            });


        }
    }
}
