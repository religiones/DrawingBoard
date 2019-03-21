package com.kronos.drawingboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.ogaclejapan.arclayout.ArcLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

import static android.graphics.Bitmap.createBitmap;

public class DrawBoard extends AppCompatActivity {
    private FloatingActionsMenu toolMenu = null;
    private DrawableViewConfig configPen = null;
    private DrawableViewConfig configEra = null;
    private DrawableView m_Draw = null;
    private Tools m_Tools = null;
    private ArcLayout arcLayout = null;
    private View menuLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_board);
        init();
        m_Tools = new Tools(this);
        setTools(m_Tools);
        setToolsAction(m_Tools);
    }

    protected void init() {
        m_Draw = (DrawableView) findViewById(R.id.paintView);
        configPen = new DrawableViewConfig();
        configPen.setStrokeColor(getResources().getColor(android.R.color.black));
        configPen.setShowCanvasBounds(true);
        configPen.setStrokeWidth(10.0f);
        configPen.setMinZoom(1.0f);
        configPen.setMaxZoom(3.0f);
        configPen.setCanvasHeight(1920);
        configPen.setCanvasWidth(1080);
        configEra = new DrawableViewConfig();
        configEra.setStrokeColor(getResources().getColor(android.R.color.white));
        configEra.setShowCanvasBounds(true);
        configEra.setStrokeWidth(10.0f);
        configEra.setMinZoom(1.0f);
        configEra.setMaxZoom(3.0f);
        configEra.setCanvasHeight(1920);
        configEra.setCanvasWidth(1080);
        m_Draw.setConfig(configPen);
        menuLayout = findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
    }

    protected void setTools(Tools tools) {
        toolMenu = (FloatingActionsMenu) findViewById(R.id.tools);
        toolMenu.addButton(tools.setToolStyle(tools.getPen(), getDrawable(R.drawable.pen)));
        toolMenu.addButton(tools.setToolStyle(tools.getEraser(), getDrawable(R.drawable.eraser)));
        toolMenu.addButton(tools.setToolStyle(tools.getUndo(), getDrawable(R.drawable.undo)));
        toolMenu.addButton(tools.setToolStyle(tools.getSelectColor(), getDrawable(R.drawable.color)));
        toolMenu.addButton(tools.setToolStyle(tools.getSave(), getDrawable(R.drawable.save)));
        toolMenu.addButton(tools.setToolStyle(tools.getDelete(), getDrawable(R.drawable.delete)));
    }

    protected void setToolsAction(final Tools tools) {
        tools.getPen().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use pen */
                m_Draw.setConfig(configPen);
            }
        });
        tools.getPen().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPenSize(v, configPen);
                return false;
            }
        });
        tools.getEraser().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use eraser */
                m_Draw.setConfig(configEra);
            }
        });
        tools.getEraser().setOnLongClickListener(new View.OnLongClickListener() {
            /* set eraser size */
            @Override
            public boolean onLongClick(View v) {
                setPenSize(v, configEra);
                return false;
            }
        });
        tools.getUndo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use undo */
                m_Draw.undo();
            }
        });
        tools.getSelectColor().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use selectColor */
                ColorPickerDialogBuilder
                        .with(DrawBoard.this)
                        .setTitle("Choose color")
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(6)
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                configPen.setStrokeColor(selectedColor);
                                m_Draw.setConfig(configPen);
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
            @Override
            public void onClick(View v) {
                /* use save */
                String[] PERMISSIONS = {
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.MOUNT_FORMAT_FILESYSTEMS"};
                if (ContextCompat.checkSelfPermission(DrawBoard.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DrawBoard.this, PERMISSIONS, 1);
                }
                try {
                    SavePhoto savePhoto = new SavePhoto(DrawBoard.this);
                    m_Draw.setBackgroundColor(Color.WHITE);
                    savePhoto.SaveBitmapFromView(m_Draw,DrawBoard.this);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        tools.getDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* use delete */
                m_Draw.clear();
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

    protected void setPenSize(View v, final DrawableViewConfig config) {
        Toast.makeText(DrawBoard.this, "set pen size", Toast.LENGTH_SHORT).show();
        /* set pen size */
        toolMenu.toggle();
        showPenSize(v);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    config.setStrokeWidth(Float.valueOf(btn.getText().toString()));
                    m_Draw.setConfig(config);
                    hidePenMenu();
                    toolMenu.expand();
                }
            });
        }
    }

    protected void hidePenMenu() {
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
}

