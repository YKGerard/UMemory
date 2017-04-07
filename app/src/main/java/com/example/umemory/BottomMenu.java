package com.example.umemory;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup.LayoutParams;
/**
 * Created by ${HYK} on 2017/4/7.
 */

public class BottomMenu implements View.OnClickListener,View.OnTouchListener{
    private PopupWindow popupWindow;
    private Button findByEmail, findByContact, btn_Cancel;
    private View mMenuView;
    private Activity mContext;
    private View.OnClickListener clickListener;

    public BottomMenu(Activity context,View.OnClickListener clickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.clickListener=clickListener;
        mContext=context;
        mMenuView = inflater.inflate(R.layout.bottom_menu, null);
        findByEmail = (Button) mMenuView.findViewById(R.id.findByEmail);
        findByContact = (Button) mMenuView.findViewById(R.id.findByContact);
        btn_Cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        btn_Cancel.setOnClickListener(this);
        findByEmail.setOnClickListener(this);
        findByContact.setOnClickListener(this);
        popupWindow=new PopupWindow(mMenuView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.colorBottomMenu));
        popupWindow.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(this);
    }

    /**
     * 显示菜单
     */
    public void show(){
        //得到当前activity的rootView
        View rootView=((ViewGroup)mContext.findViewById(android.R.id.content)).getChildAt(0);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onClick(View v) {
        popupWindow.dismiss();
        switch (v.getId()) {
            case R.id.btn_cancel:
                break;
            default:
                clickListener.onClick(v);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int height = mMenuView.findViewById(R.id.bottom_menu_layout).getTop();
        int y=(int) event.getY();
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(y<height){
                popupWindow. dismiss();
            }
        }
        return true;
    }
}
