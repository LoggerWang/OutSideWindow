package com.qh.outsidewindow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @desc:
 * @author: wanglezhi
 * @createTime: 2022/12/28 10:18 上午
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyService.class);
        context.startService(i);
    }
}
