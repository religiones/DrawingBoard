package com.kronos.drawingboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.jar.Attributes;

public class PenStrockSelect extends RelativeLayout {
    private static final int CANCEL_BUTTON_ID = 0x0020;
    public interface StrockCallback{
        public void onStrockSelectCancel(PenStrockSelect sender);
        public void onStrockSelectChange(PenStrockSelect sender);
    }

    private int penPosition = 0;
    private int penPositionTemp = 0;

    private StrockCallback callback = null;

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case CANCEL_BUTTON_ID:
                    PenStrockSelect.this.setVisibility(View.GONE);
                    penPosition = penPositionTemp;
                    if (callback != null){
                        callback.onStrockSelectCancel(PenStrockSelect.this);
                    }
                    break;
                    default:
                        /*
                        * insert into pen size set
                        * */
                       if (callback != null){
                           callback.onStrockSelectChange(PenStrockSelect.this);
                       }
                       break;
            }
        }
    };

    public PenStrockSelect(Context context){
        this(context, null);
    }

    public PenStrockSelect(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public PenStrockSelect(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initialize(context);
    }

    public void initialize(Context context){

    }
}
