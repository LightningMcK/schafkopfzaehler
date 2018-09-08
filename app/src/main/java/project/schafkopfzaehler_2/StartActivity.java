package project.schafkopfzaehler_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    private Button startButton;
    private Button nextButton;
    private String players[] = {"", "", "", ""};

    private View.OnClickListener startClickListener = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            if (v == startButton) {
                startButtonClicked();
            }
            if (v == nextButton) {
                nextButtonClicked();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        startButton = findViewById(R.id.startChoosePlayers);
        startButton.setOnClickListener(startClickListener);
        Log.d("Start", "onCreate");
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

    private void startButtonClicked() {
        setContentView(R.layout.chooseplayers);
        nextButton = findViewById(R.id.startMain);
        nextButton.setOnClickListener(startClickListener);
    }

    private void nextButtonClicked() {
        // Get entered player names and store them in the players array
        Log.d("Start", "nextButtonClicked --> Get player names");
        EditText player1 = findViewById(R.id.player1);
        EditText player2 = findViewById(R.id.player2);
        EditText player3 = findViewById(R.id.player3);
        EditText player4 = findViewById(R.id.player4);

        // Raise error message if entry is empty
        if ( player1.getText().toString().equals("") ) {
            player1.setError(getString(R.string.emptyName));
        }  else {
            players[0] = player1.getText().toString();
        }

        if ( player2.getText().toString().equals("") ) {
            player2.setError(getString(R.string.emptyName));
        }  else {
            players[1] = player2.getText().toString();
        }

        if ( player3.getText().toString().equals("") ) {
            player3.setError(getString(R.string.emptyName));
        }  else {
            players[2] = player3.getText().toString();
        }

        if ( player4.getText().toString().equals("") ) {
            player4.setError(getString(R.string.emptyName));
        }  else {
            players[3] = player4.getText().toString();
        }

        Log.d("Start", "nextButtonClicked --> Intent follows");

        Intent intent = new Intent(this, MainGameActivity.class);
        intent.putExtra("playerNames", players);
        startActivity(intent);


    }


}
