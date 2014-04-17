package ca.mcnallydawes.dartsscoring.datasources;

import java.util.ArrayList;
import java.util.List;

import ca.mcnallydawes.dartsscoring.MySQLiteHelper;
import ca.mcnallydawes.dartsscoring.models.CricketGame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CricketDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_PLAYER_1, MySQLiteHelper.COLUMN_PLAYER_2,
			MySQLiteHelper.CRICKET_COLUMN_PLAYER_1_SPT,
			MySQLiteHelper.CRICKET_COLUMN_PLAYER_2_SPT,
			MySQLiteHelper.COLUMN_WINNER, MySQLiteHelper.COLUMN_LOSER,
			MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_START,
			MySQLiteHelper.COLUMN_FINISH };

	public CricketDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public CricketGame createCricketGame(String player1, String player2,
			String player1SPT, String player2SPT, String winner, String loser,
			String date, String start, String finish) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_PLAYER_1, player1);
		values.put(MySQLiteHelper.COLUMN_PLAYER_2, player2);
		values.put(MySQLiteHelper.CRICKET_COLUMN_PLAYER_1_SPT, player1SPT);
		values.put(MySQLiteHelper.CRICKET_COLUMN_PLAYER_2_SPT, player2SPT);
		values.put(MySQLiteHelper.COLUMN_WINNER, winner);
		values.put(MySQLiteHelper.COLUMN_LOSER, loser);
		values.put(MySQLiteHelper.COLUMN_DATE, date);
		values.put(MySQLiteHelper.COLUMN_START, start);
		values.put(MySQLiteHelper.COLUMN_FINISH, finish);

		long insertId = database.insert(MySQLiteHelper.TABLE_CRICKET, null,
				values);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_CRICKET,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();

		CricketGame newCricketGame = cursorToCricketGame(cursor);
		cursor.close();

		return newCricketGame;
	}

	public void deleteCricketGame(CricketGame game) {
		long id = game.getId();
		System.out.println("Cricket game deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_CRICKET, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<CricketGame> getAllCricketGames() {
		List<CricketGame> games = new ArrayList<CricketGame>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_CRICKET,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CricketGame game = cursorToCricketGame(cursor);
			games.add(game);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return games;
	}

	public List<CricketGame> getAllCricketGamesWhere(String question,
			String[] args) {
		List<CricketGame> games = new ArrayList<CricketGame>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_CRICKET,
				allColumns, question, args, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CricketGame game = cursorToCricketGame(cursor);
			games.add(game);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return games;
	}

	private CricketGame cursorToCricketGame(Cursor cursor) {
		CricketGame game = new CricketGame();

		game.setId(cursor.getLong(0));
		game.setPlayer1(cursor.getString(1));
		game.setPlayer2(cursor.getString(2));
		game.setPlayer1SPT(cursor.getString(3));
		game.setPlayer2SPT(cursor.getString(4));
		game.setWinner(cursor.getString(5));
		game.setLoser(cursor.getString(6));
		game.setDate(cursor.getString(7));
		game.setStart(cursor.getString(8));
		game.setFinish(cursor.getString(9));

		return game;
	}
}
