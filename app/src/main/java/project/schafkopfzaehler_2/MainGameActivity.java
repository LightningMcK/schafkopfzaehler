package project.schafkopfzaehler_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainGameActivity extends AppCompatActivity {

    private Button p1, p2, p3, p4;

    private View.OnClickListener startClickListener = new View.OnClickListener() {

        @Override
        public void onClick (View v) {
            if (v == p1 || v == p2 || v == p3 || v == p4) {
                playerButtonClicked(v);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainGame", "onCreate");

        setContentView(R.layout.main_game_layout);

        Intent intent = getIntent();
        String playerNames[] = intent.getStringArrayExtra("playerNames");

        Log.d("MainGame", "Find Buttons");
        p1 = findViewById(R.id.p1_game);
        p2 = findViewById(R.id.p2_game);
        p3 = findViewById(R.id.p3_game);
        p4 = findViewById(R.id.p4_game);

        Log.d("MainGame", "Set player names into buttons");
        // Set player names into buttons
        p1.setText(playerNames[0]);
        Log.d("MainGame", "Zero works -> one");
        p2.setText(playerNames[1]);
        Log.d("MainGame", "one works -> two");
        p3.setText(playerNames[2]);
        Log.d("MainGame", "Two works -> three");
        p4.setText(playerNames[3]);
        Log.d("MainGame", "Three works");

        p1.setOnClickListener(startClickListener);
        p2.setOnClickListener(startClickListener);
        p3.setOnClickListener(startClickListener);
        p4.setOnClickListener(startClickListener);

    }

    private void playerButtonClicked (View v) {

        Log.d("MainGame", v + " clicked...");

    }

}
