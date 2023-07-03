package com.jgw.common_library.event;

import com.jgw.common_library.utils.LogUtils;

/**
 * Created by XiongShaoWu
 * on 2019/11/5
 */
public class CommonEvent {
    //二维码或条形码
    public static class ScanQRCodeEvent {
        public String mCode;

        public ScanQRCodeEvent(String code) {
            mCode = code;
            LogUtils.xswShowLog("scanCode:"+code);
        }
    }
    //RFID
    public static class ScanRFIDEvent {
        public String mCode;

        public ScanRFIDEvent(String code) {
            mCode = code;
        }
    }
}
