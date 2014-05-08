package ca.mcnallydawes.dartsscoring.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.PlayerDataSource;

public class NewPlayerActivity extends PortraitActivity {
	private EditText newPlayerNameET;

	private PlayerDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_player);

		dataSource = new PlayerDataSource(this);
		dataSource.open();

		newPlayerNameET = (EditText) findViewById(R.id.player_ET);
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(newPlayerNameET, InputMethodManager.SHOW_IMPLICIT);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, 0);
	}

	public void cancelBtnTap(View v) {
        super.onBackPressed();
	}

	public void createBtnTap(View v) {
		if (dataSource.playerExists(newPlayerNameET.getText().toString())) {
			Toast.makeText(getApplicationContext(),	"Player exists, try a different name", Toast.LENGTH_SHORT).show();
		} else {
			dataSource.createPlayer(newPlayerNameET.getText().toString());
            super.onBackPressed();
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
}
