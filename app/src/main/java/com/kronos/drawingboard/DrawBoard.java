package com.kronos.drawingboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class DrawBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_board);
        Tools m_Tools = new Tools(this);
        setTools(m_Tools);
        setToolsAction(m_Tools);
    }

    protected void setTools(Tools tools){
        FloatingActionsMenu toolMenu = (FloatingActionsMenu) findViewById(R.id.tools);
        toolMenu.addButton(tools.setToolStyle(tools.getPen(),getDrawable(R.drawable.pen)));
        toolMenu.addButton(tools.setToolStyle(tools.getEraser(),getDrawable(R.drawable.eraser)));
        toolMenu.addButton(tools.setToolStyle(tools.getUndo(),getDrawable(R.drawable.undo)));
        toolMenu.addButton(tools.setToolStyle(tools.getSelectColor(),getDrawable(R.drawable.color)));
        toolMenu.addButton(tools.setToolStyle(tools.getSave(),getDrawable(R.drawable.save)));
        toolMenu.addButton(tools.setToolStyle(tools.getDelete(),getDrawable(R.drawable.delete)));
    }

    protected void setToolsAction(Tools tools){
        tools.getPen().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawBoard.this,"pen",Toast.LENGTH_SHORT).show();
                /* use pen */
            }
        });
        tools.getEraser().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawBoard.this,"eraser",Toast.LENGTH_SHORT).show();
                /* use eraser */
            }
        });
        tools.getUndo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawBoard.this,"undo",Toast.LENGTH_SHORT).show();
                /* use undo */
            }
        });
        tools.getSelectColor().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawBoard.this,"selectColor",Toast.LENGTH_SHORT).show();
                /* use selectColor */
            }
        });
        tools.getSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawBoard.this,"save",Toast.LENGTH_SHORT).show();
                /* use save */
            }
        });
        tools.getDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawBoard.this,"delete",Toast.LENGTH_SHORT).show();
                /* use delete */
            }
        });
    }
}
