package ca.mcnallydawes.dartsscoring.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.x01DataSource;

public class X01Activity extends Activity {
	TextView currentPlayerName;
	TextView currentPlayerScore;
	int currentPlayerNumber;

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

		currentPlayerScore = (TextView) findViewById(R.id.x01_player_remaining);

		playerScores = new int[2];
		playerScores[0] = 0;
		playerScores[1] = 0;
	}
}
