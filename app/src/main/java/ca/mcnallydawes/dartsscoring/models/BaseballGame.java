package ca.mcnallydawes.dartsscoring.models;

public class BaseballGame {
	private long id;
	private String player1;
	private String player2;
	private String numExtraInnings;
	private String player1Scores;
	private String player2Scores;
	private String winner;
	private String loser;
	private String date;
	private String start;
	private String finish;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public String getNumExtraInnings() {
		return numExtraInnings;
	}

	public void setNumExtraInnings(String numExtraInnings) {
		this.numExtraInnings = numExtraInnings;
	}

	public String getPlayer1Scores() {
		return player1Scores;
	}

	public void setPlayer1Scores(String player1Scores) {
		this.player1Scores = player1Scores;
	}

	public String getPlayer2Scores() {
		return player2Scores;
	}

	public void setPlayer2Scores(String player2Scores) {
		this.player2Scores = player2Scores;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getLoser() {
		return loser;
	}

	public void setLoser(String loser) {
		this.loser = loser;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

}
