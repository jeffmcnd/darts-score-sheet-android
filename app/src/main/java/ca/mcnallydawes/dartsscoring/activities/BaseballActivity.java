package ca.mcnallydawes.dartsscoring.activities;

import android.app.Activity;
import android.os.Bundle;
import ca.mcnallydawes.dartsscoring.R;

public class BaseballActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baseball);
	}
}
