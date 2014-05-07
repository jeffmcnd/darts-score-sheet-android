package ca.mcnallydawes.dartsscoring.datasources;

import java.util.ArrayList;
import java.util.List;

import ca.mcnallydawes.dartsscoring.MySQLiteHelper;
import ca.mcnallydawes.dartsscoring.models.x01Game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class x01DataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_PLAYER_1, MySQLiteHelper.COLUMN_PLAYER_2,
			MySQLiteHelper.X01_COLUMN_START_NUM,
			MySQLiteHelper.X01_COLUMN_PLAYER_1_PPT,
			MySQLiteHelper.X01_COLUMN_PLAYER_2_PPT,
			MySQLiteHelper.COLUMN_WINNER, MySQLiteHelper.COLUMN_LOSER,
			MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_START,
			MySQLiteHelper.COLUMN_FINISH };

	public x01DataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public x01Game createX01Game(String player1, String player2,
			String startingNumber, String player1PPT, String player2PPT,
			String winner, String loser, String date, String start,
			String finish) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_PLAYER_1, player1);
		values.put(MySQLiteHelper.COLUMN_PLAYER_2, player2);
		values.put(MySQLiteHelper.X01_COLUMN_START_NUM, startingNumber);
		values.put(MySQLiteHelper.X01_COLUMN_PLAYER_1_PPT, player1PPT);
		values.put(MySQLiteHelper.X01_COLUMN_PLAYER_2_PPT, player2PPT);
		values.put(MySQLiteHelper.COLUMN_WINNER, winner);
		values.put(MySQLiteHelper.COLUMN_LOSER, loser);
		values.put(MySQLiteHelper.COLUMN_DATE, date);
		values.put(MySQLiteHelper.COLUMN_START, start);
		values.put(MySQLiteHelper.COLUMN_FINISH, finish);

		long insertId = database.insert(MySQLiteHelper.TABLE_X01, null, values);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_X01, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();

		x01Game newX01Game = cursorToX01Game(cursor);
		cursor.close();

		return newX01Game;
	}

	public void deleteX01Game(x01Game game) {
		long id = game.getId();
		System.out.println("x01 game deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_X01, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<x01Game> getAllX01Games() {
		List<x01Game> games = new ArrayList<x01Game>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_X01, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			x01Game game = cursorToX01Game(cursor);
			games.add(game);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return games;
	}

    public List<x01Game> getAllX01GamesWhere(String question,
                                                     String[] args) {
        List<x01Game> games = new ArrayList<x01Game>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_X01,
                allColumns, question, args, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            x01Game game = cursorToX01Game(cursor);
            games.add(game);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();
        return games;
    }

	private x01Game cursorToX01Game(Cursor cursor) {
		x01Game game = new x01Game();

		game.setId(cursor.getLong(0));
		game.setPlayer1(cursor.getString(1));
		game.setPlayer2(cursor.getString(2));
		game.setStartingNumber(cursor.getString(3));
		game.setPlayer1PPT(cursor.getString(4));
		game.setPlayer2PPT(cursor.getString(5));
		game.setWinner(cursor.getString(6));
		game.setLoser(cursor.getString(7));
		game.setDate(cursor.getString(8));
		game.setStart(cursor.getString(9));
		game.setFinish(cursor.getString(10));

		return game;
	}

}
