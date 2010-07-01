package singletons;

public class Phase {
	public enum State {
		TITLE, INTRO, GAMEPLAY, BOSS, LEVELCHANGE, DEATH, GAMEOVER
	}

	private static final Phase instance = new Phase();

	private State state;
	private long lastPhaseChange;
	private long freezeElaspedTime;

	private Phase() {
		this.state = Phase.State.TITLE;
		this.lastPhaseChange = System.currentTimeMillis();
		this.freezeElaspedTime = 0;
	}

	public static Phase Instance() {
		return Phase.instance;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
		this.lastPhaseChange = System.currentTimeMillis();
	}

	public long getPhaseChangeElaspedMillis() {
		return System.currentTimeMillis() - this.lastPhaseChange;
	}

	public void freezePhaseChangleTimeMillis() {
		if (this.freezeElaspedTime != 0)
			Debug.error("freezeElaspedTime", "!=0");
		this.freezeElaspedTime = this.getPhaseChangeElaspedMillis();
	}

	public void unfreezePhaseChangleTimeMillis() {
		if (this.freezeElaspedTime == 0)
			Debug.error("freezeElaspedTime", "==0");
		this.lastPhaseChange = System.currentTimeMillis() - this.freezeElaspedTime;
		this.freezeElaspedTime = 0;
	}
}
