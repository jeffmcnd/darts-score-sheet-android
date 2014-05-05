package ca.mcnallydawes.dartsscoring.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.PlayerDataSource;
import ca.mcnallydawes.dartsscoring.models.Player;

public class ViewHistoryActivity extends Activity {
	private PlayerDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_history);

		ListView listView = (ListView) findViewById(R.id.view_history_name_LV);

		dataSource = new PlayerDataSource(this);
		dataSource.open();

		List<Player> values = dataSource.getAllPlayers();

		ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this,
				R.layout.darts_simple_list_item_1, values);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name = ((TextView) view).getText().toString();
//				Intent intent = new Intent(getApplicationContext(),	PlayerHistoryActivity.class);
                Intent intent = new Intent(getApplicationContext(), TabbedHistoryActivity.class);
				intent.putExtra(ExtrasNames.PLAYER_NAME, name);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
