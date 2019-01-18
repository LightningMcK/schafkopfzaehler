package project.schafkopfzaehler_2;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    // Static base variables
    private static final String serverURL = "http://192.168.1.6:8000/";
    public static final int MEDIA_TYPE_IMAGE = 1;


    // Camera variables
    private Camera mCamera;
    private cameraPreview mPreview;

    // Variables from / for intent
    int points;
    String playerNumber;

    Button capture, confirm; // Button init
    TextView captureLog; // TextView init

    // Add a listener to the capture / confirm button
    private View.OnClickListener startClickListener = new View.OnClickListener() {

        @Override
        public void onClick (View v) {

            // Capture button was pressed --> Take picture
            if (v == capture) {
                mCamera.takePicture(null, null, mPicture);
            }

            // Confirm button was pressed --> Send data to previous activity
            if ( v == confirm ) {
                // Send points and player number to the previous
                // activity and then finishes the current activity
                Intent data = new Intent();
                data.putExtra("points", points);
                data.putExtra("playerNo", playerNumber);
                setResult(RESULT_OK, data);
                finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardcapture);

        TextView captureLog = findViewById(R.id.capture_log);
        captureLog.setMovementMethod(new ScrollingMovementMethod());

        // Get player names from the activity that started this one
        Intent intent = getIntent();
        playerNumber = intent.getStringExtra("playerName");

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create preview view and set it as the content of our activity.
        mPreview = new cameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        // Start click listener
        confirm = findViewById(R.id.capture_done);
        capture = findViewById(R.id.capture);
        confirm.setOnClickListener(startClickListener);
        capture.setOnClickListener(startClickListener);

    }

    // Get an instance of the Camera object
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // Attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // Returns null if camera is unavailable
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

    // Picture button was pressed. Send picture to server for card recognition
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            final File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (pictureFile == null){
                Log.d("CAMERA", "Error creating media files");
                return;
            }

            captureLog = findViewById(R.id.capture_log);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                // Show successful save to the user
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.pictureSaved), Toast.LENGTH_SHORT);
                toast.show();

                Log.d("CAMERA", "Saved...");
            } catch (FileNotFoundException e) {
                Log.d("CAMERA", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("CAMERA", "Error accessing file: " + e.getMessage());
            }

            sendFileToServer(pictureFile);

            mCamera.startPreview();
        }
    };

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    // Create a file Uri for saving an image or video
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    // Create a File for saving the captured image
    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Schafkopfzaehler");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Schafkopfzaehler", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "Karte" + timeStamp + ".jpg");

        } else {
            return null;
        }

        return mediaFile;
    }

    // Returns the amount of points the player receives for the card
    private int getCardPoints (String cardName) {

        if (cardName.contains("Ass")) {
            return 11;
        }
        if (cardName.contains("Koenig")) {
            return 4;
        }
        if (cardName.contains("Ober")) {
            return 3;
        }
        if (cardName.contains("Unter")) {
            return 2;
        }
        if (cardName.contains("10")) {
            return 10;
        }

        return 0;

    }

    private void sendFileToServer (final File picture) {

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(serverURL).addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        api_server client = retrofit.create(api_server.class);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", picture.getName(), RequestBody.create(MediaType.parse("image/*"), picture));

        Call<POJOClass> call = client.uploadAttachment(filePart);

        call.enqueue(new Callback<POJOClass>() {
            @Override
            public void onResponse(Call<POJOClass> call, Response<POJOClass> response) {
                String card = response.body().getKarte();
                captureLog.setText(captureLog.getText() + "\n" + card + " erkannt!");
                points += getCardPoints(card);
                // Delete picture to minimize memory usage
                if ( picture.delete() );

                // Show current points to user
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.points) + points , Toast.LENGTH_SHORT);
                toast.show();

            }

            @Override
            public void onFailure(Call<POJOClass> call, Throwable t) {
                captureLog.setText(captureLog.getText() + "\n" + "Error..." + t.getMessage());
                // Delete picture to minimize memory usage
                if ( picture.delete() );
            }
        });



    }

}



