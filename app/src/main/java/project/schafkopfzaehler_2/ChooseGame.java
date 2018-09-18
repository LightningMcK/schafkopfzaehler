package project.schafkopfzaehler_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ChooseGame extends AppCompatActivity {

    RadioGroup style, call, soloPlay; // Radio groups init
    RadioButton heart; // Heart button init for first invisibility
    Button confirmChoice; // Confirm button init
    TextView choice; // Choice view init for first choice
    String playStyle, playCall, playerName; // Init for choice manipulation
    private String names[] = {"", "", "", ""}; // Player names init

    private RadioGroup.OnCheckedChangeListener startChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            if (radioGroup == style) {
                playStyle = getPlayStyle(checkedId);
                playCall = getPlayCall(call.getCheckedRadioButtonId());
                playerName = getSoloPlayer(soloPlay.getCheckedRadioButtonId());
            } else if (radioGroup == call) {
                playStyle = getPlayStyle(style.getCheckedRadioButtonId());
                playCall = getPlayCall(checkedId);
                playerName = getSoloPlayer(soloPlay.getCheckedRadioButtonId());
            } else if (radioGroup == soloPlay) {
                playStyle = getPlayStyle(style.getCheckedRadioButtonId());
                playCall = getPlayCall(call.getCheckedRadioButtonId());
                playerName = getSoloPlayer(checkedId);
            }

            configureChoice(playStyle, playCall, playerName);

        }
    };

    private View.OnClickListener startClickListener = new View.OnClickListener() {

        @Override
        public void onClick (View v) {

            if ( v == confirmChoice ) {
                // Send choice to the previous activity
                // and then finishes the current activity
                Intent data = new Intent();
                data.putExtra("choice", choice.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosegame);

        // Get player names from the activity that started this one
        Intent intent = getIntent();
        names = intent.getStringArrayExtra("playerNames");

        // Fill in the player names in the solo choice
        soloPlay = findViewById(R.id.soloplay);
        for (int i = 0; i < soloPlay.getChildCount(); i++) {
            ((RadioButton) soloPlay.getChildAt(i)).setText(names[i]);
        }

        // Start click listener on confirm button
        confirmChoice = findViewById(R.id.gameChoosed);
        confirmChoice.setOnClickListener(startClickListener);

        // Assign variables radio group and start the change listener
        style = findViewById(R.id.playstyle);
        style.setOnCheckedChangeListener(startChangeListener);
        call = findViewById(R.id.calls);
        call.setOnCheckedChangeListener(startChangeListener);
        soloPlay.setOnCheckedChangeListener(startChangeListener);

        // At first Sauspiel & Eichel is checked, that means that the
        // heart can not be called and no solo is played. Therefore
        // both options has to be invisible at first
        heart = findViewById(R.id.herz);
        heart.setVisibility(View.GONE);
        TextView titleSolo = findViewById(R.id.title_soloplayer);
        titleSolo.setVisibility(View.GONE);
        soloPlay.setVisibility(View.GONE);
        choice = findViewById(R.id.choice);
        choice.setText(getString(R.string.sauspiel1));

    }

    private String getPlayStyle (int checked) {

        // Certain play styles do not allow a call
        // In these cases the call view should be invisible
        TextView titleCall = findViewById(R.id.title_playcall);
        TextView titleSolo = findViewById(R.id.title_soloplayer);

        // Return value
        String returnStyle = "";

        // Find which radio button is selected
        switch (checked) {
            case R.id.sauspiel:
                // Sauspiel

                // Toggle visibility
                titleCall.setVisibility(View.VISIBLE);
                titleCall.setText(getString(R.string.call));
                call.setVisibility(View.VISIBLE);
                heart.setVisibility(View.GONE);
                titleSolo.setVisibility(View.GONE);
                soloPlay.setVisibility(View.GONE);
                // -----------------

                Log.d("ChooseGame", "Sauspiel selected");

                returnStyle = "Sauspiel";

                break;

            case R.id.wenz:
                // Wenz

                // Toggle visibility
                titleCall.setVisibility(View.GONE);
                call.setVisibility(View.GONE);
                titleSolo.setVisibility(View.GONE);
                soloPlay.setVisibility(View.GONE);
                // -----------------

                Log.d("ChooseGame", "Wenz selected");

                returnStyle = "Wenz";

                break;

            case R.id.solo:
                // Solo

                // Toggle visibility
                titleCall.setVisibility(View.VISIBLE);
                titleCall.setText(getString(R.string.soloCall));
                call.setVisibility(View.VISIBLE);
                titleSolo.setVisibility(View.VISIBLE);
                soloPlay.setVisibility(View.VISIBLE);
                heart.setVisibility(View.VISIBLE);
                // -----------------

                Log.d("ChooseGame", "Solo selected");

                returnStyle = "Solo";

                break;

            default:
                Log.d("ChooseGame", "!! ERROR - Nothing is selected");
                break;

        }

        return returnStyle;

    }

    private String getPlayCall (int checked) {

        Log.d("ChooseGame", "Call changed");

        // Return value
        String returnCall = "";

        switch (checked) {

            case R.id.eichel:

                Log.d("ChooseGame", "Eichel selected");
                returnCall = "Eichel";

                break;

            case R.id.blatt:

                Log.d("ChooseGame", "Blatt selected");
                returnCall = "Blatt";

                break;

            case R.id.schelle:

                Log.d("ChooseGame", "Schelle selected");
                returnCall = "Schelle";

                break;

            case R.id.herz:

                Log.d("ChooseGame", "Herz selected");
                returnCall = "Herz";

                break;

        }

        return returnCall;
    }

    private String getSoloPlayer (int checked) {

        Log.d("ChooseGame", "Name changed");

        // Return value
        String name = "";

        switch (checked) {

            case R.id.player1:
                name = ((RadioButton) soloPlay.getChildAt(0)).getText().toString();
                break;

            case R.id.player2:
                name = ((RadioButton) soloPlay.getChildAt(1)).getText().toString();
                break;

            case R.id.player3:
                name = ((RadioButton) soloPlay.getChildAt(2)).getText().toString();
                break;

            case R.id.player4:
                name = ((RadioButton) soloPlay.getChildAt(3)).getText().toString();
                break;

            default:
                // Do nothing
                break;
        }

        return name;
    }

    private void configureChoice (String style, String call, String name) {

        if ( style.equals("Wenz") ) {

            choice.setText(style);

        } else if ( style.equals("Sauspiel") ) {

            switch (call) {
                case "Eichel":
                    choice.setText(getString(R.string.sauspiel1));
                    break;
                case "Blatt":
                    choice.setText(getString(R.string.sauspiel2));
                    break;
                case "Schelle":
                    choice.setText(getString(R.string.sauspiel3));
                    break;
            }

        } else if ( style.equals("Solo") ) {

            switch (call) {
                case "Eichel":
                    choice.setText(getString(R.string.solo1));
                    choice.append(" " + name);
                    break;
                case "Blatt":
                    choice.setText(getString(R.string.solo2));
                    choice.append(" " + name);
                    break;
                case "Schelle":
                    choice.setText(getString(R.string.solo3));
                    choice.append(" " + name);
                    break;
                case "Herz":
                    choice.setText(getString(R.string.solo4));
                    choice.append(" " + name);
                    break;
            }
        }


    }
}
