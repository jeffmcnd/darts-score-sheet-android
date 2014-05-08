package ca.mcnallydawes.dartsscoring.datasources;

import java.util.ArrayList;
import java.util.List;

import ca.mcnallydawes.dartsscoring.MySQLiteHelper;
import ca.mcnallydawes.dartsscoring.models.BaseballGame;
import ca.mcnallydawes.dartsscoring.models.GolfGame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GolfDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_PLAYER_1, MySQLiteHelper.COLUMN_PLAYER_2,
			MySQLiteHelper.GOLF_COLUMN_NUM_HOLES,
			MySQLiteHelper.GOLF_COLUMN_PLAYER_1_SCORES,
			MySQLiteHelper.GOLF_COLUMN_PLAYER_2_SCORES,
			MySQLiteHelper.COLUMN_WINNER, MySQLiteHelper.COLUMN_LOSER,
			MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_START,
			MySQLiteHelper.COLUMN_FINISH };

	public GolfDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public GolfGame createGolfGame(String player1, String player2,
			String numHoles, String player1Scores, String player2Scores,
			String winner, String loser, String date, String start,
			String finish) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_PLAYER_1, player1);
		values.put(MySQLiteHelper.COLUMN_PLAYER_2, player2);
		values.put(MySQLiteHelper.GOLF_COLUMN_NUM_HOLES, numHoles);
		values.put(MySQLiteHelper.GOLF_COLUMN_PLAYER_1_SCORES, player1Scores);
		values.put(MySQLiteHelper.GOLF_COLUMN_PLAYER_2_SCORES, player2Scores);
		values.put(MySQLiteHelper.COLUMN_WINNER, winner);
		values.put(MySQLiteHelper.COLUMN_LOSER, loser);
		values.put(MySQLiteHelper.COLUMN_DATE, date);
		values.put(MySQLiteHelper.COLUMN_START, start);
		values.put(MySQLiteHelper.COLUMN_FINISH, finish);

		long insertId = database
				.insert(MySQLiteHelper.TABLE_GOLF, null, values);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_GOLF, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();

		GolfGame newGolfGame = cursorToGolfGame(cursor);
		cursor.close();

		return newGolfGame;
	}

	public void deleteGolfGame(GolfGame game) {
		long id = game.getId();
		System.out.println("Golf game deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_GOLF, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<GolfGame> getAllGolfGames() {
		List<GolfGame> games = new ArrayList<GolfGame>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_GOLF, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GolfGame game = cursorToGolfGame(cursor);
			games.add(game);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return games;
	}

    public List<GolfGame> getAllGolfGamesWhere(String question, String[] args) {
        List<GolfGame> games = new ArrayList<GolfGame>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_GOLF,
                allColumns, question, args, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GolfGame game = cursorToGolfGame(cursor);
            games.add(game);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();
        return games;
    }

	private GolfGame cursorToGolfGame(Cursor cursor) {
		GolfGame game = new GolfGame();

		game.setId(cursor.getLong(0));
		game.setPlayer1(cursor.getString(1));
		game.setPlayer2(cursor.getString(2));
		game.setNumHoles(cursor.getString(3));
		game.setPlayer1Scores(cursor.getString(4));
		game.setPlayer2Scores(cursor.getString(5));
		game.setWinner(cursor.getString(6));
		game.setLoser(cursor.getString(7));
		game.setDate(cursor.getString(8));
		game.setStart(cursor.getString(9));
		game.setFinish(cursor.getString(10));

		return game;
	}
}
