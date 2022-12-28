package com.qh.outsidewindow;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.util.Assert;


public abstract class BaseDialog extends Dialog {

    protected final Context mContext;
    protected View mContentView;
    protected boolean mCancelable = true;

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        initView();

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    protected abstract void initView();

    public void show() {
//        Assert.notNull(mContentView);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelable)
                    dismiss();
            }
        });

        setContentView(mContentView);
        super.show();
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable = flag;
    }
}
