package logic;

public class Project implements ProjectInfo {
	private String _name;
	private int _circle;
	private int _xp;
	private int _max_grade;
	
	Project(String name, int circle, int xp, int max_grade) {
		_name = name;
		_circle = circle;
		_xp = xp;
		_max_grade = max_grade;
	}

	@Override
	public String get_name() {
		return _name;
	}

	@Override
	public int get_circle() {
		return _circle;
	}

	@Override
	public int get_xp() {
		return _xp;
	}

	@Override
	public int get_max_grade() {
		return _max_grade;
	}

}
