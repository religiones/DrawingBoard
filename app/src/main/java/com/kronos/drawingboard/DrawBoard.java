package com.kronos.drawingboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.views.DrawView;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.ogaclejapan.arclayout.ArcLayout;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DrawBoard extends AppCompatActivity {
    private FloatingActionsMenu toolMenu = null;
    private Tools m_Tools = null;
    private ArcLayout arcLayout = null;
    private View menuLayout = null;
    private DrawView m_Draw = null;
    private int[] toolSize = {10,10};
    private File img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_board);
        init();
        m_Tools = new Tools(this);
        setTools(m_Tools);
        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.MOUNT_FORMAT_FILESYSTEMS"};
        if (ContextCompat.checkSelfPermission(DrawBoard.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DrawBoard.this, PERMISSIONS, 1);
        }
        setToolsAction(m_Tools);
    }

    protected void init() {
        m_Draw = (DrawView) findViewById(R.id.draw_view);
        menuLayout = findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
    }

    protected void setTools(Tools tools) {
        toolMenu = (FloatingActionsMenu) findViewById(R.id.tools);
        toolMenu.addButton(tools.setToolStyle(tools.getPen(), getDrawable(R.drawable.pen)));
        toolMenu.addButton(tools.setToolStyle(tools.getEraser(), getDrawable(R.drawable.eraser)));
        toolMenu.addButton(tools.setToolStyle(tools.getUndo(), getDrawable(R.drawable.undo)));
        toolMenu.addButton(tools.setToolStyle(tools.getImg(),getDrawable(R.drawable.img)));
        toolMenu.addButton(tools.setToolStyle(tools.getSelectColor(), getDrawable(R.drawable.color)));
        toolMenu.addButton(tools.setToolStyle(tools.getSave(), getDrawable(R.drawable.save)));
        toolMenu.addButton(tools.setToolStyle(tools.getDelete(), getDrawable(R.drawable.delete)));
    }

    protected void setToolsAction(final Tools tools) {
        tools.getPen().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use pen */
                m_Draw.setDrawingMode(DrawingMode.DRAW);
                m_Draw.setDrawWidth(toolSize[0]);
            }
        });
        tools.getPen().setOnLongClickListener(new View.OnLongClickListener() {
            /* set pen size */
            @Override
            public boolean onLongClick(View v) {
                setPenSize(v, m_Draw, DrawingMode.DRAW);
                return false;
            }
        });
        tools.getEraser().setOnClickListener(new View.OnClickListener() {
            /* use eraser */
            @Override
            public void onClick(View v) {
                m_Draw.setDrawingMode(DrawingMode.ERASER);
                m_Draw.setDrawWidth(toolSize[1]);
            }
        });
        tools.getEraser().setOnLongClickListener(new View.OnLongClickListener() {
            /* set eraser size */
            @Override
            public boolean onLongClick(View v) {
                setPenSize(v,m_Draw, DrawingMode.ERASER);
                return false;
            }
        });
        tools.getUndo().setOnClickListener(new View.OnClickListener() {
            /* use undo */
            @Override
            public void onClick(View v) {
                m_Draw.undo();
            }
        });
        tools.getImg().setOnClickListener(new View.OnClickListener() {
            /* get image */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
        tools.getSelectColor().setOnClickListener(new View.OnClickListener() {
            /* use selectColor */
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(DrawBoard.this)
                        .setTitle("Choose color")
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(6)
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                m_Draw.setDrawingMode(DrawingMode.DRAW).setDrawColor(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });
        tools.getSave().setOnClickListener(new View.OnClickListener() {
            /* use save */
            @Override
            public void onClick(View v) {
                try {
                    SavePhoto savePhoto = new SavePhoto();
                    m_Draw.setBackgroundColor(Color.WHITE);
                    savePhoto.SaveBitmapFromView(m_Draw,DrawBoard.this);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        tools.getDelete().setOnClickListener(new View.OnClickListener() {
            /* use delete */
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(DrawBoard.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this picture!")
                        .showCancelButton(true)
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("Deleted!")
                                        .setContentText("Your picture has been deleted!")
                                        .showCancelButton(false)
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                m_Draw.restartDrawing();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    protected void showPenSize(View v) {
        if (v.isSelected()) {
            /* hide */
            hidePenMenu();
        } else {
            /* show */
            showPenMenu();
        }
    }

    protected void setPenSize(View v, final DrawView drawView, final DrawingMode drawingMode) {
        /* set pen size */
        toolMenu.toggle();
        toolMenu.setVisibility(View.GONE);
        showPenSize(v);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    if (drawingMode.equals(DrawingMode.ERASER)){
                        drawView.setDrawingMode(DrawingMode.ERASER).setDrawWidth(Integer.valueOf(btn.getText().toString()));
                        toolSize[1] = Integer.valueOf(btn.getText().toString());
                    }else {
                        drawView.setDrawingMode(DrawingMode.DRAW).setDrawWidth(Integer.valueOf(btn.getText().toString()));
                        toolSize[0] = Integer.valueOf(btn.getText().toString());
                    }
                    hidePenMenu();
                    toolMenu.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    protected void hidePenMenu() {
        m_Draw.setEnabled(true);
        List<Animator> animatorList = new ArrayList<>();
        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animatorList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animatorList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();
    }

    protected void showPenMenu() {
        menuLayout.setVisibility(View.VISIBLE);
        m_Draw.setEnabled(false);
        List<Animator> animatorList = new ArrayList<>();
        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animatorList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animatorList);
        animSet.start();
    }

    protected Animator createShowItemAnimator(View item) {
        float dx = toolMenu.getX() - item.getX();
        float dy = toolMenu.getY() - item.getY();
        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );
        return anim;
    }

    protected Animator createHideItemAnimator(final View item) {
        float dx = toolMenu.getX() - item.getX();
        float dy = toolMenu.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        return anim;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    try {
                        final Uri imageUri = data.getData();
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        m_Draw.setBackgroundImage(bitmap,BackgroundType.BITMAP,BackgroundScale.CENTER_INSIDE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

}

