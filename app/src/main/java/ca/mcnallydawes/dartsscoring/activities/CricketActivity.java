package ca.mcnallydawes.dartsscoring.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.CricketDataSource;

public class CricketActivity extends PortraitActivity {
	int currentPlayerNumber;
	int[] currentScores;
	int[][] playerScores;

    boolean[] hasTouchedButton;

    String startTime;
	String[] playerScoresPerTurn;
	String[] playerNames;

	CricketDataSource dataSource;

    TextView currentPlayerName;
    TextView[] textScores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cricket);

		Time rightNow = new Time();
		rightNow.setToNow();
		startTime = rightNow.format("%k:%M:%S");

		playerNames = new String[2];

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			playerNames[0] = extras.getString(ExtrasNames.SELECTED_PLAYER_1);
			playerNames[1] = extras.getString(ExtrasNames.SELECTED_PLAYER_2);

			dataSource = new CricketDataSource(this);
			dataSource.open();

			Log.d(getClass().getName(), String.format(
					"Player 1: %s\nPlayer 2: %s\n", playerNames[0],
					playerNames[1]));
		}

		currentPlayerName = (TextView) findViewById(R.id.player_name);
		currentPlayerName.setText(playerNames[0] + "'s Turn");

		currentPlayerNumber = 0;

		textScores = new TextView[7];
		textScores[0] = (TextView) findViewById(R.id.text_15s);
		textScores[1] = (TextView) findViewById(R.id.text_16s);
		textScores[2] = (TextView) findViewById(R.id.text_17s);
		textScores[3] = (TextView) findViewById(R.id.text_18s);
		textScores[4] = (TextView) findViewById(R.id.text_19s);
		textScores[5] = (TextView) findViewById(R.id.text_20s);
		textScores[6] = (TextView) findViewById(R.id.text_Bs);

		for (int i = 0; i < 7; i++) {
			textScores[i].setText(String.valueOf(0));
		}

		playerScores = new int[2][7];
		currentScores = new int[7];

		playerScoresPerTurn = new String[2];
		playerScoresPerTurn[0] = "";
		playerScoresPerTurn[1] = "";

		for (int i = 0; i < 7; i++) {
			playerScores[0][i] = 0;
			playerScores[1][i] = 0;

			currentScores[i] = 0;
		}

		hasTouchedButton = new boolean[7];

		for (int i = 0; i < 7; i++) {
			hasTouchedButton[i] = false;
		}
	}

	@Override
	public void onBackPressed() {
		dataSource.close();

        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
	}

	public void enterScore(View v) {
        setScoresForPlayer();
	}

	public void setScoresForPlayer() {
        int winnerNum = -1;

        if(checkWin(0)) {
            winnerNum = 0;
        } else {
            if(checkWin(1)) winnerNum = 1;
        }


		int nextPlayerNumber = (currentPlayerNumber + 1) %2;;

		// In cases where the player's name ends with an "s"
		if (playerNames[nextPlayerNumber].substring(playerNames[nextPlayerNumber].length() - 1).equals("s")) {
			currentPlayerName.setText(playerNames[nextPlayerNumber] + "' Turn");
		} else {
			currentPlayerName.setText(playerNames[nextPlayerNumber] + "'s Turn");
		}

		for (int i = 0; i < 7; i++) {
			if (playerScores[currentPlayerNumber][i] != currentScores[i]) {
				Log.d(getClass().getName(), "i: " + i);
				addToScorePerTurn(Math.abs(playerScores[currentPlayerNumber][i]	- currentScores[i]), i);
			}

			playerScores[currentPlayerNumber][i] = currentScores[i];
			currentScores[i] = playerScores[nextPlayerNumber][i];
			textScores[i].setText(String.valueOf(currentScores[i]));
		}

		if (winnerNum > -1 && currentPlayerNumber == 1) {
			Log.d(getClass().getName(), playerNames[currentPlayerNumber]
					+ " wins! Saving...");
			saveGame(winnerNum);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Saving game.").setTitle("We have a winner!");

            final CricketActivity act = this;

			builder.setPositiveButton("Continue", new OnClickListener() {
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

		currentPlayerNumber = nextPlayerNumber;

		resetHasTouchedButton();
	}

	public void addToScore(View v) {
		switch (v.getId()) {
		case R.id.btn_add_15:
			addScoreAtIndex(0);
			break;
		case R.id.btn_add_16:
			addScoreAtIndex(1);
			break;
		case R.id.btn_add_17:
			addScoreAtIndex(2);
			break;
		case R.id.btn_add_18:
			addScoreAtIndex(3);
			break;
		case R.id.btn_add_19:
			addScoreAtIndex(4);
			break;
		case R.id.btn_add_20:
			addScoreAtIndex(5);
			break;
		case R.id.btn_add_B:
			addScoreAtIndex(6);
			break;
		default:
			break;
		}
	}

	public void subFromScore(View v) {
		switch (v.getId()) {
		case R.id.btn_sub_15:
			subScoreAtIndex(0);
			break;
		case R.id.btn_sub_16:
			subScoreAtIndex(1);
			break;
		case R.id.btn_sub_17:
			subScoreAtIndex(2);
			break;
		case R.id.btn_sub_18:
			subScoreAtIndex(3);
			break;
		case R.id.btn_sub_19:
			subScoreAtIndex(4);
			break;
		case R.id.btn_sub_20:
			subScoreAtIndex(5);
			break;
		case R.id.btn_sub_B:
			subScoreAtIndex(6);
			break;
		default:
			break;
		}
	}

	public void addScoreAtIndex(int index) {
		if (numOfTouchedButtons() >= 3 && hasTouchedButton[index] == false) {
			Toast.makeText(
					getApplicationContext(),
					"You can't change more than three scores. So far you've changed "
							+ changedDartValues(), Toast.LENGTH_SHORT).show();
		} else if (currentScores[index] < 3) {
			currentScores[index]++;
			textScores[index].setText(String.valueOf(currentScores[index]));

			if (currentScores[index] > playerScores[currentPlayerNumber][index])
				hasTouchedButton[index] = true;
		}
	}

	public void subScoreAtIndex(int index) {
		if (currentScores[index] > playerScores[currentPlayerNumber][index])
		{
			currentScores[index]--;
			textScores[index].setText(String.valueOf(currentScores[index]));

			if (currentScores[index] <= playerScores[currentPlayerNumber][index])
				hasTouchedButton[index] = false;
		} else {
			Toast.makeText(getApplicationContext(),
					"You cannot decrease this value.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void saveGame(int winner) {
		int nextPlayerNumber = (winner + 1) % 2;

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		String date = df.format(c.getTime());

		Time rightNow = new Time();
		rightNow.setToNow();
		String finishTime = rightNow.format("%k:%M:%S");

        dataSource.createCricketGame(playerNames[0], playerNames[1],
                playerScoresPerTurn[0], playerScoresPerTurn[1],
                playerNames[winner],
                playerNames[nextPlayerNumber], date, startTime, finishTime);
	}

	public void addToScorePerTurn(int times, int index) {
		for (int i = 0; i < times; i++) {
			if (playerScoresPerTurn[currentPlayerNumber].equals("")) {
				playerScoresPerTurn[currentPlayerNumber] = stringForIndex(index);
			} else
				playerScoresPerTurn[currentPlayerNumber] += ","
						+ stringForIndex(index);
		}
	}

	public String stringForIndex(int index) {
		switch (index) {
		case 0:
			return "15";
		case 1:
			return "16";
		case 2:
			return "17";
		case 3:
			return "18";
		case 4:
			return "19";
		case 5:
			return "20";
		case 6:
			return "B";
		default:
			return "Uknown";
		}
	}

	public boolean checkWin(int playerNum) {
        for (int i = 0; i < 7; i++) {
            if (playerScores[playerNum][i] != 3) {
                return false;
            }
        }
		return true;
	}

	public int numOfTouchedButtons() {
		int numOfTouchedButtons = 0;

		for (int i = 0; i < 7; i++) {
			if (hasTouchedButton[i] == true)
				numOfTouchedButtons++;
		}

		if (numOfTouchedButtons > 3) {
			Log.d(getClass().getName(), "Touched buttons is "
					+ numOfTouchedButtons + ". That's a problem.");
		}

		return numOfTouchedButtons;
	}

	public String changedDartValues() {
		String changed = "";

		for (int i = 0; i < 7; i++) {
			if (hasTouchedButton[i] == true) {
				if (changed.equals(""))
					changed = stringForIndex(i);
				else
					changed += ", " + stringForIndex(i);
			}
		}

		return changed;
	}

	public void resetHasTouchedButton() {
		for (int i = 0; i < 7; i++) {
			hasTouchedButton[i] = false;
		}
	}

	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		dataSource.close();
		super.onDestroy();
	}
}
