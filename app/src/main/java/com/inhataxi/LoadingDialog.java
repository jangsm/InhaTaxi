package com.inhataxi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;


public class LoadingDialog extends Dialog {

    ImageView mImageGif;
    Context mContext;

    public LoadingDialog(Context context) {
        super(context);
        mContext=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.dialog_loading);     //다이얼로그에서 사용할 레이아웃입니다.

        mImageGif=findViewById(R.id.iv_frame_loading);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(mImageGif);
        Glide.with(mContext).load(R.raw.loading2).into(imageViewTarget);

    }

}
