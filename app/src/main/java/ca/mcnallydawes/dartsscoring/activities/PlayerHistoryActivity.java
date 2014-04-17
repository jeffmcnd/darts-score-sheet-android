package ca.mcnallydawes.dartsscoring.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.MySQLiteHelper;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.CricketDataSource;
import ca.mcnallydawes.dartsscoring.models.CricketGame;

public class PlayerHistoryActivity extends Activity {
	private String playerName;
	private ListView gameList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_history);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			playerName = extras.getString(ExtrasNames.PLAYER_NAME);
		}

		gameList = (ListView) findViewById(R.id.player_history_LV);

		List<String> values = new ArrayList<String>();
		values.add("initial");
		values.add("intial");
		values.add("intial");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.darts_simple_list_item_1, values);
		gameList.setAdapter(adapter);
	}

	public void onClick(View v) {
		List<String> values = new ArrayList<String>();

		switch (v.getId()) {
		case R.id.player_history_cricket_btn:
			CricketDataSource cricketSource = new CricketDataSource(this);
			cricketSource.open();

			String[] args = new String[2];
			args[0] = playerName;
			args[1] = playerName;

			String question = MySQLiteHelper.COLUMN_PLAYER_1 + " = ? OR "
					+ MySQLiteHelper.COLUMN_PLAYER_2 + " = ?";

			List<CricketGame> cricketGames = cricketSource
					.getAllCricketGamesWhere(question, args);

			for (CricketGame game : cricketGames) {
				values.add(game.getDate() + " " + game.getWinner() + " beat "
						+ game.getLoser() + " at " + game.getFinish());
			}

			cricketSource.close();
			break;
		case R.id.player_history_x01_btn:
			// x01DataSource x01Source = new x01DataSource(this);
			// x01Source.open();
			// x01Source.close();
			values.add("2");
			values.add("2");
			values.add("2");
			break;
		case R.id.player_history_golf_btn:
			// GolfDataSource golfSource = new GolfDataSource(this);
			// golfSource.open();
			// golfSource.close();
			values.add("3");
			values.add("3");
			values.add("3");
			break;
		case R.id.player_history_baseball_btn:
			// BaseballDataSource baseballSource = new BaseballDataSource(this);
			// baseballSource.open();
			// baseballSource.close();
			values.add("4");
			values.add("4");
			values.add("4");
			values.add("4");
			values.add("4");
			values.add("4");
			values.add("4");
			values.add("4");
			values.add("4");
			values.add("4");
			break;
		default:
			values.add("fail");
			values.add("fail");
			values.add("fail");
			break;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.darts_simple_list_item_1, values);
		gameList.setAdapter(null);
		gameList.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, ViewHistoryActivity.class);
		startActivity(intent);
	}

}
