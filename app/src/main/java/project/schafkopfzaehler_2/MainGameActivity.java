package project.schafkopfzaehler_2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainGameActivity extends AppCompatActivity {

    private Button p1, p2, p3, p4, chooseGame; // Button init
    private TextView choice;
    private String playerNames[] = {"", "", "", ""}; // Player names init
    static final int REQUEST_VIDEO_CAPTURE = 1;

    private View.OnClickListener startClickListener = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            if (v == p1 || v == p2 || v == p3 || v == p4) {
                playerButtonClicked(v);
            }
            if (v == chooseGame) {
                gameChoosing();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainGame", "onCreate");
        setContentView(R.layout.main_game_layout);

        // Get player names from the activity that started this one
        Intent intent = getIntent();
        playerNames = intent.getStringArrayExtra("playerNames");

        p1 = findViewById(R.id.p1_game);
        p2 = findViewById(R.id.p2_game);
        p3 = findViewById(R.id.p3_game);
        p4 = findViewById(R.id.p4_game);
        chooseGame = findViewById(R.id.chooseGame);
        choice = findViewById(R.id.announcement);

        // Set player names into buttons
        p1.setText(playerNames[0]);
        p2.setText(playerNames[1]);
        p3.setText(playerNames[2]);
        p4.setText(playerNames[3]);

        // Start listening if buttons are clicked
        chooseGame.setOnClickListener(startClickListener);
        p1.setOnClickListener(startClickListener);
        p2.setOnClickListener(startClickListener);
        p3.setOnClickListener(startClickListener);
        p4.setOnClickListener(startClickListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
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

    private void playerButtonClicked (View v) {

        Log.d("MainGame", v + " clicked...");
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }


    }

    private void gameChoosing () {
        Intent choose = new Intent(this, ChooseGame.class);
        choose.putExtra("playerNames", playerNames);
        startActivityForResult(choose, 0);
        //startActivity(choose);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String announcement = data.getStringExtra("choice");
                choice.setText(announcement);
            }
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            // Do nothing...
        }
    }

}
