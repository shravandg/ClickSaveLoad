package com.example.shravan.asbdbsadbsabcxscsa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//*************android.app.AlertDialog

public class MainActivity extends AppCompatActivity {
    //private static String path;
    Uri imageUri;

    private File output = null;
    private static final String FILENAME = "CameraContentDemo";
    private static final String TAG = "abc";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView iv;
    private AlertDialog dialog;
    String path;
    String abc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.myIV);
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void MyCapture(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            print("in onActivityResult");
            imageUri = data.getData();
            print("got data");
            iv.setImageURI(imageUri);
            iv.setRotation(90);
            print("image set to ImageView");
        }
    }

    public void MySave1(View v) {
        BitmapDrawable draw = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        FileOutputStream outStream = null;

        // Write to SD Card
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/camtest");
            dir.mkdirs();
            abc = getCurrentDateAndTime();
            String fileName = "Arunachala"+abc+".jpg";
            File outFile = new File(dir, fileName);

            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

            Log.d(TAG, "onPictureTaken - wrote to " + outFile.getAbsolutePath());
            path = outFile.getAbsolutePath();

            refreshGallery(outFile);
            //iv.setImageBitmap(null);
        } catch (FileNotFoundException e) {
            print("FNF");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        iv.setImageBitmap(null);

    }

    public void loadImage(View v){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/camtest", "Arunachala"+abc+".jpg");
        print(file.getAbsolutePath());
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        iv.setImageBitmap(bitmap);
        iv.setRotation(90);
    }


    public void MyLoad(View v){
        loadImage1(path);
    }

    public void MySave(View v) {
        //BitmapDrawable draw = (BitmapDrawable) iv.getDrawable();
        //Bitmap bitmap = draw.getBitmap();
        FileOutputStream fos = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()) {
            //Toast.makeText(this, "Can't create directory to store image", Toast.LENGTH_LONG).show();
            //return;
            print("file not created");
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "Arunachala" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);
        print("new_file created");
        try {
            fos = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(iv, iv.getWidth(), iv.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(this, "Save success Arunachala", Toast.LENGTH_LONG).show();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            print("FNF");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshGallery(new_file);
    }

    private void loadImage1(String name) {
        print("name is " + path);
        try {
            Context context = this;
            File f = new File(name, "Arunachala.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            iv.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            print("FNF");
            e.printStackTrace();
        }
    }

    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }


    private File getDisc() {
        String t = getCurrentDateAndTime();
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "ImageDemo");
    }

    public void print(String s) {
        Log.d(TAG, s);
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }
}

    /*
//****************not working************************************
public void myImagesave(View v) {
        print("clicked save");
        //Context context =this;
        BitmapDrawable draw = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        File albumF;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            albumF = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "MyDir");
        } else {
            print("in else");
            albumF = new File(getApplicationContext().getFilesDir(), "MyDir");

        }
        if (!albumF.exists()) {
            print("creating album");
            albumF.mkdirs();
            print("created");
        }
        String CurrentDateAndTime = getCurrentDateAndTime();
        print("creating file...");
        File file = new File(albumF, FILENAME + CurrentDateAndTime + ".jpg");
        print("File created");
        try {
            FileOutputStream fout = new FileOutputStream(file);
            print("fout created");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            print("FNF");
            e.printStackTrace();
        } catch (IOException e) {
            print("IO");
            e.printStackTrace();
        }
        print("path is" + output.getAbsolutePath());
    }

    //****************not working************************
    //******************saving at random memory**************
    public void saveI(View v){
        Context context =this;
        BitmapDrawable ddrawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = ddrawable.getBitmap();
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File dir = cw.getDir("myImageDir", Context.MODE_PRIVATE);
        File mypath = new File(dir, "abc.jpg");
        FileOutputStream out = null;
            try {
                out = new FileOutputStream(mypath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }print(dir.getAbsolutePath());

    }

    //******************saving at random memory**************
    public void savee(View v){
        Context context =this;
        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File mydir = context.getDir("mydir", Context.MODE_WORLD_READABLE); //Creating an internal dir;
        File fileWithinMyDir = new File(mydir, "myfile"); //Getting a file within the dir.
        try {
            FileOutputStream fos = new FileOutputStream(fileWithinMyDir);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            print("path is"+fileWithinMyDir.getAbsolutePath());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //*******************not working******************8
    ////******************saving at random memory**************
    public void abcd(View v){
        BitmapDrawable draw = (BitmapDrawable)iv.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        path= abc(bitmap);

    }
    public String abc(Bitmap bitmap){
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fout = null;
        File file = new File (path, "/DCIM/Signatures/Arunachala.jpg");
        try {
            fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName() );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        print("saved at"+file.getAbsolutePath());
        iv.setImageBitmap(null);
        return file.getAbsolutePath();
    }

    ////*******************not working******************8
    ////******************saving at random memory**************
    public void saveFile(View v){
        Context context =this;
        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        if(drawable.getBitmap()!=null){ print("got drawable"); }
        Bitmap bitmap = drawable.getBitmap();
            if(bitmap!=null){ print("got bitmap"); }
        File mydir = context.getFilesDir();
        String filename = "Arunachala/Shravan/Images/MyImage.jpg";
        print("Created file");
        File file = new File(mydir, filename); //Getting a file within the dir.
        print("Created file within dir");
        file.mkdirs();
        print("directory created");
        print("path is"+file.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            print("path is"+file.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //nopt working
    //these two are for using ImageSaver class
    public void ab(View v){
        Context context = this;
        BitmapDrawable draw = (BitmapDrawable)iv.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        new ImageSaver(context).
                setFileName("myImage.png").
                setDirectoryName("images").
                save(bitmap);
        iv.setImageBitmap(null);
    }
    public void b(View v){
        Context context = this;
        Bitmap bitmap = new ImageSaver(context).
                setFileName("myImage.png").
                setDirectoryName("images").
                load();
        iv.setImageBitmap(bitmap);
    }
}
*/