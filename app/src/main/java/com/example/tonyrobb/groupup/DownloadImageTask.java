package com.example.tonyrobb.groupup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageViewProfile;

    public DownloadImageTask(ImageView bmImage) {
        this.imageViewProfile = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap scaledImageBitmap = null;

        int scaledWidth = imageViewProfile.getDrawable().getIntrinsicWidth() + 50;
        int scaledHeight = imageViewProfile.getDrawable().getIntrinsicHeight() + 50;

        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            scaledImageBitmap = getResizedBitmap(BitmapFactory.decodeStream(in), scaledWidth, scaledHeight);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            scaledImageBitmap = null;
        }
        return scaledImageBitmap;
    }

    protected void onPostExecute(Bitmap result) {
        imageViewProfile.setImageBitmap(result);
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}