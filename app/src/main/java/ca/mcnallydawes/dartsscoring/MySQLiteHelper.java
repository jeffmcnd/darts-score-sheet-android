package ca.mcnallydawes.dartsscoring;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	// Database name and version
	public static final String DATABASE_NAME = "darts.sqlite";
	public static final int DATABASE_VERSION = 1;

	// Table names
	public static final String TABLE_PLAYER = "player";
	public static final String TABLE_CRICKET = "cricket";
	public static final String TABLE_X01 = "x01";
	public static final String TABLE_GOLF = "golf";
	public static final String TABLE_BASEBALL = "baseball";

	// General columns
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLAYER_1 = "player1";
	public static final String COLUMN_PLAYER_2 = "player2";
	public static final String COLUMN_WINNER = "winner";
	public static final String COLUMN_LOSER = "loser";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_START = "start_time";
	public static final String COLUMN_FINISH = "finish_time";

	// Player columns
	public static final String PLAYER_COLUMN_NAME = "name";

	// Crick columns
	public static final String CRICKET_COLUMN_PLAYER_1_SPT = "player1_score_per_turn";
	public static final String CRICKET_COLUMN_PLAYER_2_SPT = "player2_score_per_turn";

	// x01 columns
	public static final String X01_COLUMN_START_NUM = "start_num";
	public static final String X01_COLUMN_PLAYER_1_PPT = "player1_points_per_turn";
	public static final String X01_COLUMN_PLAYER_2_PPT = "player2_points_per_turn";

	// Golf columns
	public static final String GOLF_COLUMN_NUM_HOLES = "num_holes";
	public static final String GOLF_COLUMN_PLAYER_1_SCORES = "player1_scores";
	public static final String GOLF_COLUMN_PLAYER_2_SCORES = "player2_scores";

	// Baseball columns
	public static final String BASEBALL_COLUMN_NUM_EXTRA_INNINGS = "num_extra_innings";
	public static final String BASEBALL_COLUMN_PLAYER_1_SCORES = "player1_scores";
	public static final String BASEBALL_COLUMN_PLAYER_2_SCORES = "player2_scores";

	// Create table strings
	private static final String CREATE_TABLE_PLAYER = "create table "
			+ TABLE_PLAYER + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + PLAYER_COLUMN_NAME
			+ " text not null" + ")";

	private static final String CREATE_TABLE_CRICKET = "create table "
			+ TABLE_CRICKET + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_PLAYER_1
			+ " text not null," + COLUMN_PLAYER_2 + " text not null,"
			+ CRICKET_COLUMN_PLAYER_1_SPT + " text not null,"
			+ CRICKET_COLUMN_PLAYER_2_SPT + " text not null," + COLUMN_WINNER
			+ " text not null," + COLUMN_LOSER + " text not null,"
			+ COLUMN_DATE + " text not null," + COLUMN_START
			+ " text not null," + COLUMN_FINISH + " text not null" + ")";

	private static final String CREATE_TABLE_X01 = "create table " + TABLE_X01
			+ "(" + COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_PLAYER_1 + " text not null," + COLUMN_PLAYER_2
			+ " text not null," + X01_COLUMN_START_NUM + " text not null,"
			+ X01_COLUMN_PLAYER_1_PPT + " text not null,"
			+ X01_COLUMN_PLAYER_2_PPT + " text not null," + COLUMN_WINNER
			+ " text not null," + COLUMN_LOSER + " text not null,"
			+ COLUMN_DATE + " text not null," + COLUMN_START
			+ " text not null," + COLUMN_FINISH + " text not null" + ")";

	private static final String CREATE_TABLE_GOLF = "create table "
			+ TABLE_GOLF + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_PLAYER_1
			+ " text not null," + COLUMN_PLAYER_2 + " text not null,"
			+ GOLF_COLUMN_NUM_HOLES + " text not null,"
			+ GOLF_COLUMN_PLAYER_1_SCORES + " text not null,"
			+ GOLF_COLUMN_PLAYER_2_SCORES + " text not null," + COLUMN_WINNER
			+ " text not null," + COLUMN_LOSER + " text not null,"
			+ COLUMN_DATE + " text not null," + COLUMN_START
			+ " text not null," + COLUMN_FINISH + " text not null" + ")";

	private static final String CREATE_TABLE_BASEBALL = "create table "
			+ TABLE_BASEBALL + "(" + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_PLAYER_1
			+ " text not null," + COLUMN_PLAYER_2 + " text not null,"
			+ BASEBALL_COLUMN_NUM_EXTRA_INNINGS + " text not null,"
			+ BASEBALL_COLUMN_PLAYER_1_SCORES + " text not null,"
			+ BASEBALL_COLUMN_PLAYER_2_SCORES + " text not null,"
			+ COLUMN_WINNER + " text not null," + COLUMN_LOSER
			+ " text not null," + COLUMN_DATE + " text not null,"
			+ COLUMN_START + " text not null," + COLUMN_FINISH
			+ " text not null" + ")";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PLAYER);
		db.execSQL(CREATE_TABLE_CRICKET);
		db.execSQL(CREATE_TABLE_X01);
		db.execSQL(CREATE_TABLE_GOLF);
		db.execSQL(CREATE_TABLE_BASEBALL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRICKET);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_X01);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOLF);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BASEBALL);
		onCreate(db);
	}

	// remove this when you're done
	public void clearDB(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRICKET);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_X01);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOLF);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BASEBALL);
		onCreate(db);
	}

}
