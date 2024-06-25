package logic;

public class Level implements LevelInfo {
	private int _id;
	private int _new_xp;
	private int _total_xp;

	Level(int id, int new_xp, int total_xp) {
		_id = id;
		_new_xp = new_xp;
		_total_xp = total_xp;
	}

	@Override
	public int get_id() {
		return _id;
	}

	@Override
	public int get_new_xp() {
		return _new_xp;
	}

	@Override
	public int get_total_xp() {
		return _total_xp;
	}
}
