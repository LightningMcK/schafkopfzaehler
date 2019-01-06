package project.schafkopfzaehler_2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import project.schafkopfzaehler_2.POJO.POJOClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardCaptureActivity extends AppCompatActivity {

    private Camera mCamera;
    private cameraPreview mPreview;
    Button capture; // Button init
    TextView captureLog; // TextView init
    public static final int MEDIA_TYPE_IMAGE = 1;

    // Add a listener to the Capture button
    private View.OnClickListener startCaptureListener = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            if (v == capture) {
                mCamera.takePicture(null, null, mPicture);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardcapture);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new cameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        capture = findViewById(R.id.capture);
        capture.setOnClickListener(startCaptureListener);

    }

    /* Get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    @Override
    protected void onPause() {

        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /** Picture button was pressed. Send picture to server for card recognition. */
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (pictureFile == null){
                Log.d("CAMERA", "Error creating media files");
                return;
            }

            // Compress image to lower site
            Bitmap bmp = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

            captureLog = findViewById(R.id.capture_log);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(bytes.toByteArray());
                fos.close();
                Log.d("CAMERA", "Saved...");
                captureLog.setText(captureLog.getText() + "\n" + "Saved...");
            } catch (FileNotFoundException e) {
                Log.d("CAMERA", "File not found: " + e.getMessage());
                captureLog.setText(captureLog.getText() + "\n" + "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("CAMERA", "Error accessing file: " + e.getMessage());
                captureLog.setText(captureLog.getText() + "\n" + "Error accessing file: " + e.getMessage());
            }

            // ------------------------ Send file to server -------------------------
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://192.168.0.195:8000/").addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            api_server client = retrofit.create(api_server.class);

            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", pictureFile.getName(), RequestBody.create(MediaType.parse("image/*"), pictureFile));

            Call<POJOClass> call = client.uploadAttachment(filePart);

            try {
                call.enqueue(new Callback<POJOClass>() {
                    @Override
                    public void onResponse(Call<POJOClass> call, Response<POJOClass> response) {
                        String card = response.body().getKarte();
                        captureLog.setText(captureLog.getText() + "\n" + card + " erkannt :)");
                    }

                    @Override
                    public void onFailure(Call<POJOClass> call, Throwable t) {
                        captureLog.setText(captureLog.getText() + "\n" + "Error..." + t.getMessage());
                    }
                });
            }

            catch (Exception e){
                Log.d("Schafkopfzaehler", "EXCEPTION: " + e.getMessage());
            }

            // ----------------------------------------------------------------------

            mCamera.startPreview();
        }
    };

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving the captured image */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Schafkopfzaehler");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Schafkopfzaehler", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "Karte.jpg");

        } else {
            return null;
        }

        return mediaFile;
    }

}



