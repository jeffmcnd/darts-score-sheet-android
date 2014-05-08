package ca.mcnallydawes.dartsscoring.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.BaseballDataSource;
import ca.mcnallydawes.dartsscoring.datasources.PlayerDataSource;
import ca.mcnallydawes.dartsscoring.models.Player;

public class MatchupMainActivity extends Activity {
	private PlayerDataSource dataSource;
	private int gameType;
	private String selectedName1;
	private String selectedName2;

	ListView listView1;
	ListView listView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matchup_main);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gameType = extras.getInt(ExtrasNames.GAME_TYPE);
		}

		listView1 = (ListView) findViewById(R.id.player_list_1);
		listView2 = (ListView) findViewById(R.id.player_list_2);

		listView1.setDivider(null);
		listView2.setDivider(null);

		dataSource = new PlayerDataSource(this);
		dataSource.open();

		List<Player> values = dataSource.getAllPlayers();

		ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this,
				R.layout.darts_simple_list_item_single_choice, values);

		listView1.setAdapter(adapter);
		listView2.setAdapter(adapter);

		listView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		AdapterView.OnItemLongClickListener deletePlayerListener = new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final View view = arg1;

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MatchupMainActivity.this);
				builder.setTitle("Delete player?");

				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deletePlayer(((TextView) view).getText().toString());
							}
						});

				builder.setNegativeButton("No", null);

				AlertDialog dialog = builder.create();
				dialog.show();

				Log.d(getClass().getName(), "Long click.");

				return false;
			}
		};

		listView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedName1 = ((TextView) view).getText().toString();
			}
		});

		listView1.setOnItemLongClickListener(deletePlayerListener);

		listView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedName2 = ((TextView) view).getText().toString();
			}
		});

		listView2.setOnItemLongClickListener(deletePlayerListener);
	}

	public void startGame(View v) {
		if (selectedName1 == null || selectedName2 == null) {
			Toast.makeText(getApplicationContext(),
					"You must select two names", Toast.LENGTH_SHORT).show();
		} else if (selectedName1.equals(selectedName2)) {
			Toast.makeText(getApplicationContext(),
					"You must select different names", Toast.LENGTH_SHORT)
					.show();
		} else {
			switch (gameType) {
			case 1:
				Intent intent = new Intent(this, CricketActivity.class);
				intent.putExtra(ExtrasNames.SELECTED_PLAYER_1, selectedName1);
				intent.putExtra(ExtrasNames.SELECTED_PLAYER_2, selectedName2);
                startActivity(intent);
				break;
			case 2:
				Intent x01Intent = new Intent(this, X01Activity.class);
				x01Intent.putExtra(ExtrasNames.SELECTED_PLAYER_1, selectedName1);
				x01Intent.putExtra(ExtrasNames.SELECTED_PLAYER_2, selectedName2);
                x01Intent.putExtra(ExtrasNames.SELECTED_X01_NUMBER, 501);
				startActivity(x01Intent);
				break;
			case 3:
				Intent golfIntent = new Intent(this, GolfActivity.class);
				golfIntent.putExtra(ExtrasNames.SELECTED_PLAYER_1, selectedName1);
				golfIntent.putExtra(ExtrasNames.SELECTED_PLAYER_2, selectedName2);
                golfIntent.putExtra(ExtrasNames.GOLF_MAX_HOLES, 9);
				startActivity(golfIntent);
				break;
			case 4:
				Intent baseballIntent = new Intent(this, BaseballActivity.class);
				baseballIntent.putExtra(ExtrasNames.SELECTED_PLAYER_1, selectedName1);
				baseballIntent.putExtra(ExtrasNames.SELECTED_PLAYER_2, selectedName2);
				startActivity(baseballIntent);
				break;
			default:
				break;
			}
			overridePendingTransition(0, 0);
		}
	}

	public void createMatchup(View v) {
		Intent intent = new Intent(this, NewPlayerActivity.class);
		intent.putExtra(ExtrasNames.GAME_TYPE, gameType);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}

	public void deletePlayer(String playerName) {
		String[] args = new String[1];
		args[0] = playerName;
		dataSource.deletePlayer(dataSource.getFirstPlayerWhere("name == ?",
				args));

		List<Player> values = dataSource.getAllPlayers();

		ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this,
				R.layout.darts_simple_list_item_single_choice, values);

		listView1.setAdapter(null);
		listView2.setAdapter(null);

		listView1.setAdapter(adapter);
		listView2.setAdapter(adapter);

		listView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	@Override
	public void onBackPressed() {
//		Intent intent = new Intent(this, MainMenuActivity.class);
//		startActivity(intent);
		super.onBackPressed();
		overridePendingTransition(0, 0);
	}

	@Override
	protected void onResume() {
		dataSource.open();

        List<Player> values = dataSource.getAllPlayers();

        ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this,
                R.layout.darts_simple_list_item_single_choice, values);

        listView1.setAdapter(adapter);
        listView2.setAdapter(adapter);

		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
}
