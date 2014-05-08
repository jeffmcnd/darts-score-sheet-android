package ca.mcnallydawes.dartsscoring.datasources;

import java.util.ArrayList;
import java.util.List;

import ca.mcnallydawes.dartsscoring.MySQLiteHelper;
import ca.mcnallydawes.dartsscoring.models.BaseballGame;
import ca.mcnallydawes.dartsscoring.models.CricketGame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseballDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_PLAYER_1, MySQLiteHelper.COLUMN_PLAYER_2,
			MySQLiteHelper.BASEBALL_COLUMN_NUM_EXTRA_INNINGS,
			MySQLiteHelper.BASEBALL_COLUMN_PLAYER_1_SCORES,
			MySQLiteHelper.BASEBALL_COLUMN_PLAYER_2_SCORES,
			MySQLiteHelper.COLUMN_WINNER, MySQLiteHelper.COLUMN_LOSER,
			MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_START,
			MySQLiteHelper.COLUMN_FINISH };

	public BaseballDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public BaseballGame createBaseballGame(String player1, String player2,
			String numExtraInnings, String player1Scores, String player2Scores,
			String winner, String loser, String date, String start,
			String finish) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_PLAYER_1, player1);
		values.put(MySQLiteHelper.COLUMN_PLAYER_2, player2);
		values.put(MySQLiteHelper.BASEBALL_COLUMN_NUM_EXTRA_INNINGS,
				numExtraInnings);
		values.put(MySQLiteHelper.BASEBALL_COLUMN_PLAYER_1_SCORES,
				player1Scores);
		values.put(MySQLiteHelper.BASEBALL_COLUMN_PLAYER_2_SCORES,
				player2Scores);
		values.put(MySQLiteHelper.COLUMN_WINNER, winner);
		values.put(MySQLiteHelper.COLUMN_LOSER, loser);
		values.put(MySQLiteHelper.COLUMN_DATE, date);
		values.put(MySQLiteHelper.COLUMN_START, start);
		values.put(MySQLiteHelper.COLUMN_FINISH, finish);

		long insertId = database.insert(MySQLiteHelper.TABLE_BASEBALL, null,
				values);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_BASEBALL,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();

		BaseballGame newBaseballGame = cursorToBaseballGame(cursor);
		cursor.close();

		return newBaseballGame;
	}

	public void deleteBaseballGame(BaseballGame game) {
		long id = game.getId();
		System.out.println("Baseball game deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_BASEBALL, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<BaseballGame> getAllBaseballGames() {
		List<BaseballGame> games = new ArrayList<BaseballGame>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_BASEBALL,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			BaseballGame game = cursorToBaseballGame(cursor);
			games.add(game);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return games;
	}

    public List<BaseballGame> getAllBaseballGamesWhere(String question, String[] args) {
        List<BaseballGame> games = new ArrayList<BaseballGame>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BASEBALL,
                allColumns, question, args, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BaseballGame game = cursorToBaseballGame(cursor);
            games.add(game);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();
        return games;
    }

	private BaseballGame cursorToBaseballGame(Cursor cursor) {
		BaseballGame game = new BaseballGame();

		game.setId(cursor.getLong(0));
		game.setPlayer1(cursor.getString(1));
		game.setPlayer2(cursor.getString(2));
		game.setNumExtraInnings(cursor.getString(3));
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
