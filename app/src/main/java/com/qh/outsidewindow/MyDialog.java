package com.qh.outsidewindow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * @desc:
 * @author: wanglezhi
 * @createTime: 2022/12/28 10:51 上午
 */
public class MyDialog extends BaseDialog{
    TextView textview;
    public MyDialog(Context context) {
        super(context, R.style.Base_DialogTheme);
    }



    @Override
    protected void initView() {
        mContentView = View.inflate(mContext, R.layout.dialog_layout, null);
        getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        textview = mContentView.findViewById(R.id.tv);
        mContentView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
//                    Uri uri = Uri.parse("app://guangya/path?from=1");
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(uri);
//                    Intent intent = new Intent(getContext(),MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getContext().startActivity(intent);

                    MyAppliction.isRunningForegroundToApp1(getContext(), MainActivity.class);
                    dismiss();
                } catch (Exception e) {
                    Log.d("legend", "====Exception===" + e.toString());
                    e.printStackTrace();
                }
            }
        });
    }
    public void updateTv(int type){
        textview.setText(String.valueOf(type));
    }
}
