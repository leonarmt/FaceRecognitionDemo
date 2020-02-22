# FaceRecognitionDemo
ARMT Face Recognition SDK Demo

深圳市艾默特技术有限公司 人脸识别主板SDK使用说明
一、适用范围
1）本SDK Demo适用于AIS906，AIS908，AIS919主板。
2）本SDK Demo主要用于对主板端口状态的控制以及监控，不含有人脸识别功能。

二、GPIO口控制
    GPIO口有两种控制方式，消息方式或者调用函数。
1）光灯控制
    消息控制方式
                    补光灯                                          绿色指示灯                                        红色指示灯
 开        com.android.intent.OpenWhiteFlashlight      com.android.intent.OpenGreenFlashlight        com.android.intent.OpenRedFlashlight
 关        com.android.intent.CloseWhiteFlashlight     com.android.intent.CloseGreenFlashlight       com.android.intent.CloseRedFlashlight

     调用函数方式
                      补光灯                                          绿色指示灯                                        红色指示灯
         GPIOPort port = new GPIOPort(169);                    GPIOPort port = new GPIOPort(171);         GPIOPort port = new GPIOPort(162);
         port.enable_out();                                    port.enable_out();                         port.enable_out();
 开      port.write(1);                                        port.write(1);                             port.write(1);
 关      port.write(0);                                        port.write(0);                             port.write(0);

2）继电器(开门)控制
    消息控制方式
                    补光灯
       com.android.intent.OpenRelay  //开
       com.android.intent.CloseRelay //关

    调用函数方式
          GPIOPort port = new GPIOPort(65);
          port.enable_out();
          port.write(1);//打开
          port.write(0);//关闭

 3) 人体感应

     人体感应状态通过消息方式获取,参照类FaceReceiver
    com.android.intent.BodyIn  //检测到人体进入
    com.android.intent.BodyOut //检测到人体离开

 三、导航栏与状态栏
    通过消息方式控制：
  显示导航栏    com.android.intent.EnableNavigation
  关闭导航栏    com.android.intent.DisableStatusbar
  显示状态栏  com.android.intent.EnableStatusbar
  关闭状态栏  com.android.intent.DisableNavigation

    调用函数：
  显示导航栏    DeviceManager.showNavigationBar()
  关闭导航栏    DeviceManager.removeNavigationBar()
  显示状态栏    DeviceManager.showStatusBar()
  关闭状态栏    DeviceManager.showNavigationBar()

  四、韦根发送
  参见类WiegandActivity

  五、获取ADC值
  ADC接口可以用于接受模拟传感器数据利润环境光亮度传感器、温度传感器等，也可以接收ADC按键。
        AdcPort port = new AdcPort(adcid);
        adc_value = port.read();

  六、设置系统时间
  DeviceManager.setCurrentTimeMillis();

  七、设备重启
  DeviceManager.reboot();

  八、静默安装APK
  DeviceManager.installPackage(package_file, package_name);

  九、OTA升级
   DeviceManager.updateOtaPackage(ota_file);

  十、设置开机Launcher(启动界面)
  DeviceManager.setDefaultLauncher(package_name);