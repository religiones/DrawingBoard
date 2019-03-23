package com.kronos.drawingboard;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class Tools {
    final static int PEN = 0x1001;
    final static int ERASER = 0x1002;
    final static int UNDO= 0x1003;
    final static int SELECTCOLOR = 0x1004;
    final static int SAVE = 0x1005;
    final static int DELETE = 0x1006;
    final static int IMG= 0x1007;
    final static int SHARE= 0x1008;

    private FloatingActionButton pen;
    private FloatingActionButton eraser;
    private FloatingActionButton undo;
    private FloatingActionButton img;
    private FloatingActionButton share;
    private FloatingActionButton selectColor;
    private FloatingActionButton save;
    private FloatingActionButton delete;

    public Tools(Context context){
        pen = new FloatingActionButton(context);
        pen.setId(PEN);
        eraser = new FloatingActionButton(context);
        eraser.setId(ERASER);
        undo = new FloatingActionButton(context);
        undo.setId(UNDO);
        img = new FloatingActionButton(context);
        img.setId(IMG);
        share = new FloatingActionButton(context);
        share.setId(SHARE);
        selectColor = new FloatingActionButton(context);
        selectColor.setId(SELECTCOLOR);
        save = new FloatingActionButton(context);
        save.setId(SAVE);
        delete = new FloatingActionButton(context);
        delete.setId(DELETE);
    }

    public void setPen(FloatingActionButton pen) {
        this.pen = pen;
    }
    public void setEraser(FloatingActionButton eraser) {
        this.eraser = eraser;
    }
    public void setUndo(FloatingActionButton undo) {
        this.undo = undo;
    }
    public void setImg(FloatingActionButton img) {
        this.img = img;
    }
    public void setShare(FloatingActionButton share) {
        this.share = share;
    }
    public void setSelectColor(FloatingActionButton selectColor) {
        this.selectColor = selectColor;
    }
    public void setSave(FloatingActionButton save) {
        this.save = save;
    }
    public void setDelete(FloatingActionButton delete) {
        this.delete = delete;
    }
    public FloatingActionButton getPen() {
        return pen;
    }
    public FloatingActionButton getEraser() {
        return eraser;
    }
    public FloatingActionButton getUndo() {
        return undo;
    }
    public FloatingActionButton getImg() {
        return img;
    }
    public FloatingActionButton getShare() {
        return share;
    }
    public FloatingActionButton getSelectColor() {
        return selectColor;
    }
    public FloatingActionButton getSave() {
        return save;
    }
    public FloatingActionButton getDelete() {
        return delete;
    }

    public FloatingActionButton setToolStyle(FloatingActionButton btn, Drawable drawable){
        btn.setColorNormalResId(R.color.white);
        btn.setColorPressedResId(R.color.white_pressed);
        btn.setIconDrawable(drawable);
        btn.setSize(FloatingActionButton.SIZE_MINI);
        return btn;
    }
}
