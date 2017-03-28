package com.example.shravan.asbdbsadbsabcxscsa;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Save{

    private final String TAG = "Arunachala";
    //the context who called this class (who the user is seeing currently
    private Context TheThis;
    private String NameOfFolder = "/Watershed_Images";
    private String NameOfFile = "Watershed_result";

    public String saveToInternalStorage(Bitmap bitmapImage){

        print("Creating cw");
        ContextWrapper cw = new ContextWrapper(TheThis.getApplicationContext());
        print("Creating dir");
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("ImagesDir", Context.MODE_PRIVATE);
        print("Created dir" + directory);
        // Create imageDir
        File mypath=new File(directory,"myImagesDGS.jpg");
        print("path is"+mypath);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CheckFile(mypath);
        return directory.getAbsolutePath();
    }

    public void saveImage(Context context, Bitmap ImageToSave){
        TheThis = context;
        String file_path= Environment.getExternalStorageDirectory().getAbsolutePath()+NameOfFolder;
        print("File path is" +file_path);
        String CurrentDateAndTime = getCurrentDateAndTime();
        print("Current date and time is: " + CurrentDateAndTime);
        //make a new file with the directory
        File dir = new File(file_path);
        //after setting up the directory we need to create it
        if(!dir.exists()){
            dir.mkdirs();
        }
        print("Dir created");
        File file = new File(dir, NameOfFile + CurrentDateAndTime + ".jpg");
        print("File created");
        try{
            print("entered try");
            FileOutputStream fout = new FileOutputStream(file);
            print("fout created");
            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fout);
            print("compressed");
            fout.flush();
            fout.close();
            CheckFile(file);
            AbleToSave();

        } catch (FileNotFoundException e) {
            print("FNF exception");
            UnableToSave();
        } catch (IOException e) {
            print("IO Excep");
            UnableToSave();
        }
    }



    private void print(String str){
        Log.d(TAG, str);
    }

    private String getCurrentDateAndTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }

    private void CheckFile(File file){
        print("checking...!!!");
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener(){
                    public void onScanCompleted(String path, Uri uri){
                        Log.e("ExternalStorage", "Scanned" + path + ":");
                        Log.e("ExternalStorage", "-> uri=" +uri);
                    }
                });
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "Picture cannot be saved", Toast.LENGTH_LONG).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Picture is saved", Toast.LENGTH_LONG).show();
    }
}
