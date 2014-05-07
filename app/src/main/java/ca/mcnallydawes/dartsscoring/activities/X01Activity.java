package ca.mcnallydawes.dartsscoring.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.x01DataSource;

public class X01Activity extends Activity {
	TextView currentPlayerName;
	TextView currentPlayerScore;
	int currentPlayerNumber;

    List<Integer> scoreHistory = new ArrayList<Integer>();

	String[] playerNames;
	int[] playerScores;

	x01DataSource dataSource;

	String startTime;
	int gameNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_x01);

		Time rightNow = new Time();
		rightNow.setToNow();
		startTime = rightNow.format("%k:%M:%S");

		playerNames = new String[2];

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			playerNames[0] = extras.getString(ExtrasNames.SELECTED_PLAYER_1);
			playerNames[1] = extras.getString(ExtrasNames.SELECTED_PLAYER_2);
			gameNumber = extras.getInt(ExtrasNames.SELECTED_X01_NUMBER);

			dataSource = new x01DataSource(this);
			dataSource.open();

			Log.d(getClass().getName(), String.format(
					"Player 1: %s\nPlayer 2: %s\n", playerNames[0],
					playerNames[1]));
		}

		TextView title = (TextView) findViewById(R.id.x01_title);
		title.setText(String.valueOf(gameNumber));

		currentPlayerNumber = 0;

		currentPlayerName = (TextView) findViewById(R.id.x01_player_name);
		currentPlayerName.setText(playerNames[0] + "'s Turn");

		currentPlayerScore = (TextView)findViewById(R.id.x01_player_remaining);
        currentPlayerScore.setText(String.valueOf(gameNumber));

		playerScores = new int[2];
		playerScores[0] = gameNumber;
		playerScores[1] = gameNumber;
	}

    private void addToCurrentScore(int num) {
        int result = playerScores[currentPlayerNumber] + num;
        if(result < 0) {
            //Bust, undo all scoring for this turn and switch to other player
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bust!").setMessage("You lose your turn.");

            builder.setPositiveButton("Continue", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            while(scoreHistory.size() > 0) {
                undoLastScore();
            }
            endTurnBtnTap(null);
        } else if(result > 0) {
            playerScores[currentPlayerNumber] = result;
            currentPlayerScore.setText(String.valueOf(result));
        } else {
            // Winner
            saveGame();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Saving game.").setTitle("We have a winner!");

            final X01Activity act = this;

            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(act, MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void undoLastScore() {
        if(scoreHistory.size() > 0) {
            addToCurrentScore(scoreHistory.get(scoreHistory.size() - 1));
            scoreHistory.remove(scoreHistory.size() - 1);
        }
    }

    public void saveGame() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        String date = df.format(c.getTime());

        Time rightNow = new Time();
        rightNow.setToNow();
        String finishTime = rightNow.format("%k:%M:%S");

        dataSource.createX01Game(playerNames[0], playerNames[1], String.valueOf(gameNumber), String.valueOf(playerScores[0]), String.valueOf(playerScores[1]),
                playerNames[currentPlayerNumber], playerNames[(currentPlayerNumber + 1) % 2], date, startTime, finishTime);
    }

    public void scoreBtnTap(View v) {
        int score = Integer.valueOf((String)v.getTag());
        addToCurrentScore(-score);
        scoreHistory.add(score);
    }

    public void endTurnBtnTap(View v) {
        currentPlayerNumber = (currentPlayerNumber + 1) % 2;
        currentPlayerName.setText(playerNames[currentPlayerNumber]);
        currentPlayerScore.setText(String.valueOf(playerScores[currentPlayerNumber]));
    }

    public void undoBtnTap(View v) {
        undoLastScore();
    }
}
