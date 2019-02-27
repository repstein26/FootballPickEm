class Game {

	Team t1, t2;

	public Game(Team t1, Team t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	public Team getHome() {
		return t2;
	}

	public Team getAway() {
		return t1;
	}
}