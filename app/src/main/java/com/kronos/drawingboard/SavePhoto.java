package com.kronos.drawingboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

public class SavePhoto {

    public SavePhoto() {
        }

    public void SaveBitmapFromView(View view, Context context) throws ParseException {
        int w = view.getWidth();
        int h = view.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        view.layout(0, 0, w, h);
        view.draw(c);
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f,0.5f);
        bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
        String path = saveBitmap(bmp,System.currentTimeMillis() + ".JPEG");
        refreshFile(path, context);
    }

    public String saveBitmap(Bitmap bitmap, String bitName){
        String fileName ;
        File file, appDir;
        appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"DrawBoard");
        if (!appDir.exists()){
                appDir.mkdir();
        }
        fileName = appDir.getPath()+"/"+bitName;
        file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        return file.getAbsolutePath();
    }

    public void refreshFile(String filePath, Context context) {
        File file = new File(filePath);
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        MediaScannerConnection.scanFile(context, new String[] { file.toString() }, new String[] { mtm.getMimeTypeFromExtension(file.toString().substring(file.toString().lastIndexOf(".")+1)) },
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(final String path, final Uri uri) {
                    }
                });
        Toast.makeText(context,"The picture has been saved ",Toast.LENGTH_SHORT).show();
    }
}

