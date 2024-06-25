package logic;

import java.util.ArrayList;
import java.util.List;

import java.lang.Math;

public class Calculator {
	
	private List<Level> _levels;
	private List<Project> _projects;
	
	public Calculator() {
		init_levels();
		init_projects();
	}
	
	private void init_levels() {
		_levels = new ArrayList<Level>();
		
		_levels.add(new Level(0, 0, 0));
		_levels.add(new Level(1, 462, 462));
		_levels.add(new Level(2, 2226, 2688));
		_levels.add(new Level(3, 3197, 5885));
		_levels.add(new Level(4, 5892, 11777));
		_levels.add(new Level(5, 17440, 29217));
		_levels.add(new Level(6, 17038, 46255));
		_levels.add(new Level(7, 17304, 63559));
		_levels.add(new Level(8, 10781, 74340));
		_levels.add(new Level(9, 11143, 85483));
		_levels.add(new Level(10, 9517, 95000));
		_levels.add(new Level(11, 10630, 105630));
		_levels.add(new Level(12, 18816, 124446));
	}
	
	private void init_projects() {
		_projects = new ArrayList<Project>();
		
		_projects.add(new Project("Libft", 0, 462, 125));
		
		_projects.add(new Project("get_next_line", 1, 882, 125));
		_projects.add(new Project("ft_printf", 1, 882, 125));
		_projects.add(new Project("Born2beRoot", 1, 577, 125));
		
		_projects.add(new Project("push_swap", 2, 1855, 125));
		_projects.add(new Project("pipex", 2, 1142, 125));
		_projects.add(new Project("minitalk", 2, 1142, 125));
		_projects.add(new Project("so_long", 2, 1000, 125));
		_projects.add(new Project("fract-ol", 2, 1000, 125));
		_projects.add(new Project("fdf", 2, 1000, 125));
		
		_projects.add(new Project("Philosophers", 3, 3360, 125));
		_projects.add(new Project("minishell", 3, 2814, 125));
		
		_projects.add(new Project("Cub3D", 4, 5775, 125));
		_projects.add(new Project("miniRT", 4, 5775, 125));
		_projects.add(new Project("NetPractice", 4, 3160, 100));
		_projects.add(new Project("CPP Module 04", 4, 9660, 100));
		
		_projects.add(new Project("Inception", 5, 10042, 125));
		_projects.add(new Project("webserv", 5, 21630, 125));
		_projects.add(new Project("ft_irc", 5, 21630, 125));
		_projects.add(new Project("CPP Module 09", 5, 10042, 100));
		
		_projects.add(new Project("ft_transcendence", 6, 24360, 100));
	}

	public List<ProjectInfo> get_projects() {
		List<ProjectInfo> projects = new ArrayList<>(_projects.size());
		for (ProjectInfo p : _projects) {
			projects.add(p);
		}
		return projects;
	}
	
	public double calc(double curr_xp, double xp_project) {
		double x = curr_xp;
		double y = curr_xp + xp_project;
		
		double div1 = y / Constants.OLD_MAX_XP;
		double div2 = x / Constants.OLD_MAX_XP;
		
		double pow1 = Math.pow(div1, Constants.ARB_VAL);
		double pow2 = Math.pow(div2, Constants.ARB_VAL);
		
		double r = (pow1 - pow2) * Constants.NEW_NB_DAYS;
		
		return (double)(Math.round((r * 100) / 100));		
	}
	
	public double get_project_xp(String proj_name, int grade) {
		double xp = -1;
		
		for (Project p : _projects) {
			if (p.get_name().equals(proj_name)) {
				if (grade < 0 || grade > p.get_max_grade())
					return -1;
				xp = (double) p.get_xp() * grade / 100;
				break;
			}
		}
		
		return xp;
	}
	
	public double get_current_xp(double curr_level) {
		
		int int_level = (int) Math.floor(curr_level);
		double dec_level = curr_level - int_level;
				
		return _levels.get(int_level).get_total_xp() + (_levels.get(int_level + 1).get_new_xp() * dec_level);
	}

	public double get_level(double xp) {
		double level = 0;
		int int_level = 0;

		int int_xp = (int) Math.floor(xp);
		double dec_xp = xp - int_xp;

		for (Level l : _levels) {
			if (l.get_total_xp() <= int_xp)
				int_level = l.get_id();
			else	break;
		}
		level = int_level;
		dec_xp = xp - _levels.get(int_level).get_total_xp();

		if (_levels.get(int_level).get_total_xp() < xp)
			level = int_level + (dec_xp / _levels.get(int_level + 1).get_new_xp());
	
		return level;
	}
}
