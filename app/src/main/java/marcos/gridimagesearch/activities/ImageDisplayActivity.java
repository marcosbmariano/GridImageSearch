package marcos.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import marcos.gridimagesearch.models.ImageResult;
import marcos.gridimagesearch.R;

public class ImageDisplayActivity extends ActionBarActivity {
    private SmartImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        Intent intent = getIntent();
        ImageResult imageResult = (ImageResult)intent.getSerializableExtra("result");

        mImage = (SmartImageView)findViewById(R.id.ivResult);
        mImage.setImageUrl(imageResult.getFullUrl());

    }

    //this is called when the image is clicked
    public void onShareItem(View v){

        // Get access to bitmap image from view
        SmartImageView smartImageView = (SmartImageView) findViewById(R.id.ivResult);

        Bitmap bmp = getImageBitmapFromImageView(smartImageView);

        Uri bmpUri = getLocalBitmapUri(bmp);

        shareImage(bmpUri);



    }

    public void shareImage(Uri bmpUri){

        if(bmpUri != null){
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");

            //Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));

        }else{
            Toast.makeText(this, "Image handling failed", Toast.LENGTH_LONG).show();
        }

    }


    public Uri getLocalBitmapUri(Bitmap bmp){

        // Store image to default external storage directory
        Uri bmpUri = null;
        try{
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS),
                    "share_image_" + System.currentTimeMillis() + ".png");

            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,90,out);
            out.close();

            bmpUri = Uri.fromFile(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;
    }


    // Extract Bitmap from ImageView drawable
    public Bitmap getImageBitmapFromImageView(ImageView imageView){

        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;

        if(drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }else{
            Toast.makeText(this, "Image is not a instance of Bitmap", Toast.LENGTH_LONG).show();
            return null;
        }
        return bmp;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
