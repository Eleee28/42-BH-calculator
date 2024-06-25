package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logic.Calculator;
import logic.ProjectInfo;

public class MainWindow extends JFrame {
	
	private Calculator _calculator;
	
	private DefaultComboBoxModel<String> _projectsModel;
	private List<ProjectInfo> _projects;
	
	private JSpinner _curr_level;
	private JComboBox<String> _projectNameBox;
	private JSpinner _grade;
	
	private JButton _okButton;
	private JButton _resetButton;
	private JButton _deleteButton;
	
	private JLabel _bhLabel;
	private double _bh_days = 0;
	
	private DefaultTableModel _dataTableModel;
	private String[] _headers = { "Project", "Grade", "XP", "BH days", "New Level" };

	private int _scaling_factor = 1;
	
	
	public MainWindow(Calculator calculator) {
		super("[42 CALCULATOR]");
		_calculator = calculator;
		
		initGUI();
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		mainPanel.setPreferredSize(new Dimension(560, 400));
		
		ImageIcon icon = new ImageIcon("resources/icons/42_Logo.svg.png");
		setIconImage(icon.getImage());

		// Resize window option
		/*
		JPanel resizePanel = new JPanel();
		JLabel resizeInfo = new JLabel("Introduce the window scaling factor: ");
		JSpinner multVar = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		
		resizePanel.add(resizeInfo);
		resizePanel.add(multVar);
		*/
		
		// Help text
		JPanel helpPanel = new JPanel();
		JLabel helpText = new JLabel("<html> Fill in the fields and click on the OK button to obtain the BH days </html>");
		helpText.setFont(new Font("Arial", Font.BOLD, 14));
		mainPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				helpText.setPreferredSize(new Dimension(mainPanel.getWidth() - 10, 50));
				helpText.revalidate();
				helpText.repaint();
			}
		});

		helpPanel.add(helpText);

		JPanel comboBoxPanel = new JPanel();
		JPanel tablePanel = new JPanel(new BorderLayout());
		JPanel resultPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		//mainPanel.add(resizePanel);
		mainPanel.add(helpPanel);
		mainPanel.add(comboBoxPanel);
		mainPanel.add(tablePanel);
		mainPanel.add(resultPanel);
		mainPanel.add(buttonsPanel);
		
		
		// Level spinner
		comboBoxPanel.add(new JLabel("Current level: "));
		SpinnerNumberModel levelModel = new SpinnerNumberModel(0, 0, 30, 0.01);
		_curr_level = new JSpinner(levelModel);
		_curr_level.setPreferredSize(new Dimension(70, _curr_level.getPreferredSize().height + 5));
		_curr_level.setFont(new Font("Arial", Font.BOLD, 14));
		comboBoxPanel.add(_curr_level);
		
		comboBoxPanel.add(new JSeparator(SwingConstants.VERTICAL));
		
		// Project names comboBox
		_projectsModel = new DefaultComboBoxModel<String>();
		_projects = _calculator.get_projects();
		for (ProjectInfo p : _projects)
			_projectsModel.addElement(p.get_name());
		
		_projectNameBox = new JComboBox<>(_projectsModel);
		_projectNameBox.setSelectedIndex(-1);
		
		comboBoxPanel.add(new JLabel("Project: "));
		comboBoxPanel.add(_projectNameBox);
		
		comboBoxPanel.add(new JSeparator(SwingConstants.VERTICAL));
		
		// Grade spinner
		comboBoxPanel.add(new JLabel("Grade: "));
		SpinnerNumberModel gradeModel = new SpinnerNumberModel(0, 0, 125, 1);
		_grade = new JSpinner(gradeModel);
		_grade.setPreferredSize(new Dimension(70, _grade.getPreferredSize().height + 5));
		_grade.setFont(new Font("Arial", Font.BOLD, 14));
		comboBoxPanel.add(_grade);
		
		// Table panel
		_dataTableModel = new DefaultTableModel() {
				@Override public boolean isCellEditable(int row, int col) {
					// no cells are editable
					return false;
				}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		
		// JTable that uses _dataTableModel
		JTable data = new JTable(_dataTableModel);
		TableColumnModel columnModel = data.getColumnModel();
		JScrollPane scrollPane = new JScrollPane(data);
		scrollPane.setPreferredSize(new Dimension(mainPanel.getWidth() - 100, mainPanel.getHeight() / 2));
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		
		// Result Panel
		JLabel result = new JLabel("Total BH days: ");
		_bhLabel = new JLabel();
		_bhLabel.setText("" + _bh_days);
		resultPanel.add(result);
		resultPanel.add(_bhLabel);		
		
		// Buttons
		_okButton = new JButton("OK");
		_okButton.setVisible(true);
		_okButton.addActionListener((e) -> handle_ok_button());
		buttonsPanel.add(_okButton);
		
		buttonsPanel.add(new JSeparator(SwingConstants.VERTICAL));
		
		_resetButton = new JButton("Reset");
		_resetButton.setVisible(true);
		_resetButton.addActionListener((e) -> handle_reset_button());
		buttonsPanel.add(_resetButton);
		
		_deleteButton = new JButton("Delete row");
		_deleteButton.setVisible(true);
		_deleteButton.addActionListener((e) -> handle_del_button());
		buttonsPanel.add(_deleteButton);
		
		// Rescale window and elements
		/* TODO: add an scalign factor so that elements can be resize to how the user wants -> improve user experience on different OS
		multVar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int scale_factor = Integer.valueOf(multVar.getValue().toString());
				JOptionPane.showMessageDialog(null, scale_factor, "new value", JOptionPane.INFORMATION_MESSAGE, null);
				mainPanel.setPreferredSize(new Dimension(560 * scale_factor, 400 * scale_factor));
				_curr_level.setPreferredSize(new Dimension(70 * scale_factor, _curr_level.getPreferredSize().height + 5));
				_grade.setPreferredSize(new Dimension(70 * scale_factor, _grade.getPreferredSize().height + 5));
				repaint();
			}
		});
		*/

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				ViewUtils.quit(MainWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
			
		});
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void handle_ok_button() {
		try {
			if (_projectNameBox.getSelectedItem() == null)
				return;
				
			double curr_level = Double.valueOf(_curr_level.getValue().toString());
			String proj_name = _projectNameBox.getSelectedItem().toString();
			int grade = Integer.valueOf(_grade.getValue().toString());
			
			double proj_xp = _calculator.get_project_xp(proj_name, grade);
			
			double curr_xp = _calculator.get_current_xp(curr_level);
			
			double bh_days = _calculator.calc(curr_xp, proj_xp);
			
			double new_level = _calculator.get_level(curr_xp + proj_xp);
			String level = String.format("%.02f", new_level);
			
			_bh_days += bh_days;
			_bhLabel.setText("" + _bh_days);
			
			// For debugging
			//JOptionPane.showMessageDialog(null, _bh_days, _curr_level.getValue().toString(), JOptionPane.INFORMATION_MESSAGE, null); 
			
			String[] content = new String[_headers.length];
			content[0] = proj_name;
			content[1] = "" + grade;
			content[2] = "" + proj_xp;
			content[3] = "" + bh_days;
			content[4] = level;
			
			_dataTableModel.addRow(content);
			level = level.replace(',', '.');
			_curr_level.setValue(Double.valueOf(level));
			
			repaint();
		} catch (Exception e) {
			ViewUtils.showErrorMsg(e.getMessage());
		}
	}
	
	private void handle_reset_button() {
		_curr_level.setValue(0);
		_projectNameBox.setSelectedIndex(-1);
		_grade.setValue(0);
		_dataTableModel.setRowCount(0);
		_bh_days = 0;
		_bhLabel.setText("" + _bh_days);
	}
	
	private void handle_del_button() {
		int num_rows = _dataTableModel.getRowCount();
		if (num_rows > 0) {
			_bh_days -= Double.valueOf(_dataTableModel.getValueAt(num_rows - 1, 3).toString());
			_bhLabel.setText("" + _bh_days);
			_dataTableModel.setRowCount(num_rows - 1);
			
			String level;
			if (_dataTableModel.getRowCount() > 0) {
				level = _dataTableModel.getValueAt(_dataTableModel.getRowCount() - 1, 4).toString();
				level = level.replace(',', '.');
			}
			else
				level = "0";
			_curr_level.setValue(Double.valueOf(level));
		}
		
	}
}
