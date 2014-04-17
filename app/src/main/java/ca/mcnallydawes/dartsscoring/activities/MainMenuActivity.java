package ca.mcnallydawes.dartsscoring.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.R;

public class MainMenuActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	public void onClick(View v) {
		Intent intent = new Intent(this, MatchupMainActivity.class);

		switch (v.getId()) {
		case R.id.cricket_select_btn:
			intent.putExtra(ExtrasNames.GAME_TYPE, 1);
			startActivity(intent);
			break;
		case R.id.x01_select_btn:
			intent.putExtra(ExtrasNames.GAME_TYPE, 2);
			startActivity(intent);
			break;
		case R.id.golf_select_btn:
			intent.putExtra(ExtrasNames.GAME_TYPE, 3);
			startActivity(intent);
			break;
		case R.id.baseball_select_btn:
			intent.putExtra(ExtrasNames.GAME_TYPE, 4);
			startActivity(intent);
			break;
		case R.id.view_history_btn:
			Intent historyIntent = new Intent(this, ViewHistoryActivity.class);
			startActivity(historyIntent);
			break;
		default:
			System.out.println("I don't know what happened.");
			break;
		}
		overridePendingTransition(0, 0);
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}
}
