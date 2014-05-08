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
import ca.mcnallydawes.dartsscoring.datasources.BaseballDataSource;

public class BaseballActivity extends Activity {

    String startTime;

    String[] playerNames = new String[2];
    int[] playerScores = new int[2];
    int currentPlayerNumber = 0;
    int extraInnings = 0;

    BaseballDataSource dataSource;

    NumberPicker picker1;
    NumberPicker picker2;
    NumberPicker picker3;

    TextView titleTextView;
    TextView inningTextView;

    TextView player1NameTextView;
    TextView player2NameTextView;
    TextView player1ScoreTextView;
    TextView player2ScoreTextView;

    int currentInning = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baseball);

        Time rightNow = new Time();
        rightNow.setToNow();
        startTime = rightNow.format("%k:%M:%S");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerNames[0] = extras.getString(ExtrasNames.SELECTED_PLAYER_1);
            playerNames[1] = extras.getString(ExtrasNames.SELECTED_PLAYER_2);

            dataSource = new BaseballDataSource(this);
            dataSource.open();
        }

        playerScores[0] = 0;
        playerScores[1] = 0;

        picker1 = (NumberPicker)findViewById(R.id.baseball_1_numberPicker);
        picker2 = (NumberPicker)findViewById(R.id.baseball_2_numberPicker);
        picker3 = (NumberPicker)findViewById(R.id.baseball_3_numberPicker);

        picker1.setMinValue(0);
        picker1.setMaxValue(3);
        picker2.setMinValue(0);
        picker2.setMaxValue(3);
        picker3.setMinValue(0);
        picker3.setMaxValue(3);

        titleTextView = (TextView)findViewById(R.id.baseball_title_textView);
        inningTextView = (TextView)findViewById(R.id.baseball_inning_textView);

        titleTextView.setText(playerNames[currentPlayerNumber] + "'s turn");
        inningTextView.setText(String.valueOf(currentInning));

        player1NameTextView = (TextView)findViewById(R.id.baseball_player_1_name_textView);
        player2NameTextView = (TextView)findViewById(R.id.baseball_player_2_name_textView);
        player1ScoreTextView = (TextView)findViewById(R.id.baseball_player_1_score_textView);
        player2ScoreTextView = (TextView)findViewById(R.id.baseball_player_2_score_textView);

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

        final BaseballActivity act = this;

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(act, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if(playerScores[0] > playerScores[1]) {
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

        dataSource.createBaseballGame(playerNames[0], playerNames[1], String.valueOf(0), String.valueOf(playerScores[0]), String.valueOf(playerScores[1]),
                playerNames[currentPlayerNumber], playerNames[(currentPlayerNumber + 1) % 2], date, startTime, finishTime);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void enterScoreBtnTap(View v) {
        playerScores[currentPlayerNumber] += picker1.getValue();
        playerScores[currentPlayerNumber] += picker2.getValue();
        playerScores[currentPlayerNumber] += picker3.getValue();

        player1ScoreTextView.setText(String.valueOf(playerScores[0]));
        player2ScoreTextView.setText(String.valueOf(playerScores[1]));

        currentPlayerNumber = (currentPlayerNumber + 1) % 2;
        titleTextView.setText(playerNames[currentPlayerNumber] + "'s turn");

        picker1.setValue(0);
        picker2.setValue(0);
        picker3.setValue(0);

        if(currentPlayerNumber == 0) {
            if(currentInning < 9) {
                currentInning++;
                inningTextView.setText(String.valueOf(currentInning));
            } else if(playerScores[0] == playerScores[1]) {
                if(extraInnings == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Sudden Death!").setMessage("Aim for the bullseye. Highest score wins!");
                    builder.setPositiveButton("Let's Go!", null);
                    builder.create().show();
                }
                extraInnings++;
                currentInning = 9 + extraInnings;
                inningTextView.setText(String.valueOf(currentInning));
            } else {
                saveGame();
            }
        }
    }
}
