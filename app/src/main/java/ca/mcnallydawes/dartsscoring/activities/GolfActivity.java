package ca.mcnallydawes.dartsscoring.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.GolfDataSource;

public class GolfActivity extends Activity {

    String startTime;

    String[] playerNames;
    int[] playerScores;
    int currentPlayerNumber = 0;
    int currentHole = 1;
    int maxHoles = 9;
    int extraHoles = 0;

    GolfDataSource dataSource;

    NumberPicker picker1;

    TextView player1NameTextView;
    TextView player2NameTextView;
    TextView player1ScoreTextView;
    TextView player2ScoreTextView;
    TextView titleTextView;
    TextView holeTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_golf);

        Time time = new Time();
        time.setToNow();
        startTime = time.format("%k:%M:%S");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            playerNames = new String[2];
            playerNames[0] = extras.getString(ExtrasNames.SELECTED_PLAYER_1);
            playerNames[1] = extras.getString(ExtrasNames.SELECTED_PLAYER_2);
            
            maxHoles = extras.getInt(ExtrasNames.GOLF_MAX_HOLES);

            dataSource = new GolfDataSource(this);
            dataSource.open();
        }

        playerScores = new int[2];
        playerScores[0] = 0;
        playerScores[1] = 0;

        picker1 = (NumberPicker)findViewById(R.id.golf_1_numberPicker);

        picker1.setMinValue(1);
        picker1.setMaxValue(5);
        picker1.setWrapSelectorWheel(false);

        titleTextView = (TextView)findViewById(R.id.golf_title_textView);
        holeTextView = (TextView)findViewById(R.id.golf_inning_textView);

        titleTextView.setText(playerNames[currentPlayerNumber] + "'s turn");
        holeTextView.setText(String.valueOf(currentHole));

        player1NameTextView = (TextView)findViewById(R.id.golf_player_1_name_textView);
        player2NameTextView = (TextView)findViewById(R.id.golf_player_2_name_textView);
        player1ScoreTextView = (TextView)findViewById(R.id.golf_player_1_score_textView);
        player2ScoreTextView = (TextView)findViewById(R.id.golf_player_2_score_textView);

        player1NameTextView.setText(playerNames[0]);
        player2NameTextView.setText(playerNames[1]);
        player1ScoreTextView.setText("0");
        player2ScoreTextView.setText("0");
	}

    @Override
    public void onBackPressed() {
        // Warn that progress is lost, unless you want to implement saving.
        super.onBackPressed();
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void saveGame() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Saving game.");

        final GolfActivity act = this;

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(act, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        if(playerScores[0] < playerScores[1]) {
            currentPlayerNumber = 0;
            builder.setTitle(playerNames[0] + " wins!");
        } else {
            currentPlayerNumber = 1;
            builder.setTitle(playerNames[1] + " wins!");
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        String date = df.format(c.getTime());

        Time rightNow = new Time();
        rightNow.setToNow();
        String finishTime = rightNow.format("%k:%M:%S");

        dataSource.createGolfGame(playerNames[0], playerNames[1], String.valueOf(0), String.valueOf(playerScores[0]), String.valueOf(playerScores[1]),
                playerNames[currentPlayerNumber], playerNames[(currentPlayerNumber + 1) % 2], date, startTime, finishTime);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void enterScoreBtnTap(View v) {
        playerScores[currentPlayerNumber] += picker1.getValue();

        player1ScoreTextView.setText(String.valueOf(playerScores[0]));
        player2ScoreTextView.setText(String.valueOf(playerScores[1]));

        currentPlayerNumber = (currentPlayerNumber + 1) % 2;
        titleTextView.setText(playerNames[currentPlayerNumber] + "'s turn");

        picker1.setValue(1);

        if(currentPlayerNumber == 0) {
            if(currentHole < 9) {
                currentHole++;
                holeTextView.setText(String.valueOf(currentHole));
            } else if(playerScores[0] == playerScores[1]) {
                if(extraHoles == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Sudden Death!").setMessage("Aim for the bullseye. Lowest score wins!");
                    builder.setPositiveButton("Let's Go!", null);
                    builder.create().show();
                }
                extraHoles++;
                currentHole = maxHoles + extraHoles;
                holeTextView.setText(String.valueOf(currentHole));
            } else {
                saveGame();
            }
        }
    }
}
