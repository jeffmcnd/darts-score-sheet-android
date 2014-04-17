package ca.mcnallydawes.dartsscoring.models;

public class CricketGame {
	private long id;
	private String player1;
	private String player2;
	private String player1SPT;
	private String player2SPT;
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

	public String getPlayer1SPT() {
		return player1SPT;
	}

	public void setPlayer1SPT(String player1spt) {
		player1SPT = player1spt;
	}

	public String getPlayer2SPT() {
		return player2SPT;
	}

	public void setPlayer2SPT(String player2spt) {
		player2SPT = player2spt;
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
