package ca.mcnallydawes.dartsscoring.models;

public class x01Game {
	private long id;
	private String player1;
	private String player2;
	private String startingNumber;
	private String player1PPT;
	private String player2PPT;
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

	public String getStartingNumber() {
		return startingNumber;
	}

	public void setStartingNumber(String startingNumber) {
		this.startingNumber = startingNumber;
	}

	public String getPlayer1PPT() {
		return player1PPT;
	}

	public void setPlayer1PPT(String player1ppt) {
		player1PPT = player1ppt;
	}

	public String getPlayer2PPT() {
		return player2PPT;
	}

	public void setPlayer2PPT(String player2ppt) {
		player2PPT = player2ppt;
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