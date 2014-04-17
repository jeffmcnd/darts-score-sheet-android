package ca.mcnallydawes.dartsscoring.datasources;

import java.util.ArrayList;
import java.util.List;

import ca.mcnallydawes.dartsscoring.MySQLiteHelper;
import ca.mcnallydawes.dartsscoring.models.Player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PlayerDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.PLAYER_COLUMN_NAME };

	public PlayerDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		// call to clear DB every time you try to access player table
		// dbHelper.clearDB(database);
	}

	public void close() {
		dbHelper.close();
	}

	public Player createPlayer(String name) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.PLAYER_COLUMN_NAME, name);
		long insertId = database.insert(MySQLiteHelper.TABLE_PLAYER, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLAYER, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Player newPlayer = cursorToPlayer(cursor);
		cursor.close();
		return newPlayer;
	}

	public void deletePlayer(Player player) {
		long id = player.getId();
		System.out.println("Player deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_PLAYER, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Player> getAllPlayers() {
		List<Player> players = new ArrayList<Player>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLAYER, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Player player = cursorToPlayer(cursor);
			players.add(player);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return players;
	}

	public boolean playerExists(String name) {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLAYER, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if (name.toLowerCase().equals(cursor.getString(1).toLowerCase())) {
				cursor.close();
				return true;
			}
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return false;
	}

	public Player getFirstPlayerWhere(String question, String[] args) {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLAYER, allColumns,
				question, args, null, null, null);

		cursor.moveToFirst();
		Player player = cursorToPlayer(cursor);

		cursor.close();
		return player;
	}

	private Player cursorToPlayer(Cursor cursor) {
		Player player = new Player();
		player.setId(cursor.getLong(0));
		player.setName(cursor.getString(1));
		return player;
	}
}