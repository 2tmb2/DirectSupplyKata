package directSupplyKata;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.Timer;

public class GUI {
	
	// creates instance variables
	private HashMap<String, String> categories;
	private HashMap<String, String> difficulty;
	private int numberOfQuestions;
	JPanel panel;
	JFrame frame;
	QuestionSet qs;
	private String question;
	private ArrayList<String> answers;
	private ButtonGroup g;
	private int numCorrect;
	private int numWrong;
	private int i;
	
	// creates a new instance of a GUI object
	public static void main(String[] args)
	{
		new GUI();
	}
	
	/**
	 * Creates a new GUI object and sets all instance variables
	 */
	public GUI()
	{
		numCorrect = 0;
		numWrong = 0;
		setOptions();
		
		// creates and displays a new JFrame
		frame = new JFrame("Quiz");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900,600);
		panel = new JPanel();
		frame.add(panel);
		
		//creates the initial game menu
		createMenu();
		
		frame.setVisible(true);
	}
	
	/**
	 * Displays each menu object
	 */
	public void createMenu()
	{
		// creates label to ask for category
		JLabel lblQuestions = new JLabel("What category of questions?");
		panel.add(lblQuestions);
		
		// creates JComboBox to let user select category
		String[] category = Arrays.copyOf(categories.keySet().toArray(), categories.keySet().toArray().length, String[].class);
		JComboBox<String> categoryBox = new JComboBox<String>(category);
		categoryBox.setVisible(true);
		panel.add(categoryBox);
		
		// creates label to ask for difficulty
		JLabel lblDifficulty = new JLabel("What difficulty would you like?");
		panel.add(lblDifficulty);
		
		// creates JComboBox to let user select difficulty
		String[] difficultyChoice = Arrays.copyOf(difficulty.keySet().toArray(), difficulty.keySet().toArray().length, String[].class);
		JComboBox<String> difficultyBox = new JComboBox<String>(difficultyChoice);
		difficultyBox.setVisible(true);
		panel.add(difficultyBox);
		
		// creates label to ask how many questions the quiz should be
		JLabel lblNumOfQuestions = new JLabel("How many questions in your quiz?");
		panel.add(lblNumOfQuestions);
		
		// creates JSpinner to let the user select how many questions
		JSpinner numOfQuestions = new JSpinner();
		// increases the width of the JSpinner so you can see up to 3 didgits
		JComponent mySpinnerEditor = numOfQuestions.getEditor();
		JFormattedTextField jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
		jftf.setColumns(3);
		numOfQuestions.setVisible(true);
		panel.add(numOfQuestions);
		
		// creates the "ok" button to let the user move on to the quiz
		JButton btn = new JButton("OK");
		btn.addActionListener(new ActionListener() {
			@Override
			// actionPerformed is called when the button is clicked
			public void actionPerformed(ActionEvent e) {
				URL url;
				try {
					// creates a questionset based on the selected options
					numberOfQuestions = (int)numOfQuestions.getValue();
					qs = new QuestionSet("https://opentdb.com/api.php?amount=" + numberOfQuestions + categories.get(categoryBox.getSelectedItem()) + difficulty.get( difficultyBox.getSelectedItem())+ "&type=multiple" + "");
					panel.removeAll();
					frame.revalidate();
					frame.repaint();
					askQuestions();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		// adds the button to the panel and makes it visible
		btn.setVisible(true);
		panel.add(btn);
	}
	
	/**
	 * Handles logic for when the user selects an option
	 */
	public void askQuestions()
	{
		// makes sure there are still questions remaining
		if (i <= numberOfQuestions - 1)
		{
			panel.setBackground(Color.WHITE);
			panel.removeAll();
			frame.revalidate();
			frame.repaint();
			qs.setQuestionNumber(i);
			question = qs.getQuestion();
			answers = qs.getAnswers();
			// displays the 4 radial buttons and their associated answer options
			displayOptions();
			// creates a submit button
			JButton btnSubmit = new JButton("Submit");
			btnSubmit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					//actionPerformed is called when the user clicks the submit button. If they were right it flashes green, else it flashes red. If they didn't select anything nothing happens.
					if (getSelectedButton(g) != -1)
					{
						if (getSelectedButton(g) == qs.getCorrectNumber() + 1)
						{
							flashColor(Color.GREEN);
							numCorrect++;
						}
						else
						{
							flashColor(Color.RED);
							numWrong++;
						}
					}
				}
			});
			// positions the button at x=35, y=200, width=80, height=30
			btnSubmit.setBounds(35,200,80,30);
			panel.add(btnSubmit);
		}
		// if there are not questions remaining in the quiz, displays the game over screen
		else
		{
			panel.setBackground(Color.WHITE);
			panel.removeAll();
			frame.revalidate();
			// tells the user the number of questions in their quiz and their score
			JLabel endingText = new JLabel("You have finished your " + numberOfQuestions + " question quiz!");
			endingText.setBounds(100,250,800,30);
			JLabel endingText2 = new JLabel("You got " + numCorrect + " correct and " + numWrong + " incorrect, for a total score of " + 100*(double)(numCorrect)/numberOfQuestions + "%");
			endingText2.setBounds(100,265,800,30);
			panel.add(endingText2);
			panel.add(endingText);
			
			// creates a restart button
			JButton btnRestart = new JButton("Restart");
			btnRestart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					// when the restart button is selected, resets all variables to their starting values,
					// empties the frame, and calls createMenu() again to let the user reselect their category/difficulty
					numCorrect = 0;
					numWrong = 0;
					i = 0;
					panel.removeAll();
					frame.remove(panel);
					panel = new JPanel();
					frame.add(panel);
					createMenu();
					frame.setVisible(true);
				}
			});
			btnRestart.setBounds(100,300,100,30);
			panel.add(btnRestart);
		}
	}
	
	/**
	 * makes the screen flash a color, then moves to the next question
	 * @param Color color
	 */
	public void flashColor(Color color)
	{
		// change the color of the background to the inputed value
		panel.setBackground(color);
		// creates a new timer object
		Timer t = null;
		ActionListener task = new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				i++;
				// calls askQuestions() to move on to the next question
				askQuestions();
			}
		};
		// starts the timer on a one-second interval
		t = new Timer(1000, task);
		// tells the timer to only occur once
		t.setRepeats(false);
		// starts the timer
		t.start();
		panel.removeAll();
		frame.revalidate();
	}
	
	/**
	 * Displays options a, b, c, and d for the user to select.
	 */
	public void displayOptions()
	{
		// removes the layout manager to allow for manual positioning of objects
		panel.setLayout(null);
		
		// displays the question to the user
		JLabel lblQuestion = new JLabel(question);
		lblQuestion.setBounds(5,5,800,30);
		panel.add(lblQuestion);
		
		// displays option A
		JLabel labelA = new JLabel(QuestionSet.reformat(answers.get(0)));
		labelA.setBounds(35,40,800,20);
		JRadioButton optionA = new JRadioButton("answer1");
		optionA.setBounds(15,40,20,20);
		
		// displays option B
		JLabel labelB = new JLabel(QuestionSet.reformat(answers.get(1)));
		labelB.setBounds(35,80,800,20);
		JRadioButton optionB = new JRadioButton("answer2");
		optionB.setBounds(15,80,20,20);
		
		// displays option C
		JLabel labelC = new JLabel(QuestionSet.reformat(answers.get(2)));
		labelC.setBounds(35,120,800,20);
		JRadioButton optionC = new JRadioButton("answer3");
		optionC.setBounds(15,120,20,20);
		
		// displays option D
		JLabel labelD = new JLabel(QuestionSet.reformat(answers.get(3)));
		labelD.setBounds(35,160,800,20);
		JRadioButton optionD = new JRadioButton("answer4");
		optionD.setBounds(15,160,20,20);
		
		// creates a ButtonGroup of the buttons to allow them to be iterated through later
		g = new ButtonGroup();
		
		// adds each JRadioButton to the ButtonGroup g
		g.add(optionA);
		g.add(optionB);
		g.add(optionC);
		g.add(optionD);
		
		// adds every label and button to the panel so they can be displayed
		panel.add(optionA);
		panel.add(optionB);
		panel.add(optionC);
		panel.add(optionD);
		
		panel.add(labelA);
		panel.add(labelB);
		panel.add(labelC);
		panel.add(labelD);
		
		// displays the users current stats: number correct, number wrong, and number of questions remaining in the quiz
		JLabel lblNumberCorrect = new JLabel("Number Correct: " + numCorrect);
		lblNumberCorrect.setBounds(15,250,300,20);
		JLabel lblNumberIncorrect = new JLabel("Number Incorrect: " + numWrong);
		lblNumberIncorrect.setBounds(15,270,300,20);
		JLabel lblNumberRemaining = new JLabel("Number of Questions Remaining: " + (numberOfQuestions - (numWrong + numCorrect)));
		lblNumberRemaining.setBounds(15,290,300,20);
		
		// adds the current stats to the panel
		panel.add(lblNumberRemaining);
		panel.add(lblNumberCorrect);
		panel.add(lblNumberIncorrect);
		
		// updates the JFrame
		frame.setVisible(true);
		
	}
	
	/**
	 * creates HashMaps to connect each variation of difficulty/category to their corresponding URL segment for the API 
	 */
	public void setOptions()
	{
		numberOfQuestions = 3;
		difficulty = new HashMap<String, String>(Map.ofEntries(
				Map.entry("easy", "&difficulty=easy"),
				Map.entry("medium", "&difficulty=medium"),
				Map.entry("hard", "&difficulty=hard"),
				Map.entry("Any difficulty", "")
				));
		categories = new HashMap<String, String>(Map.ofEntries(
				Map.entry("General Knowledge", "&category=9"),
				Map.entry("Books", "&category=10"),
				Map.entry("Film", "&category=11"),
				Map.entry("Music", "&category=12"),
				Map.entry("Musicals & Theaters", "&category=13"),
				Map.entry("Television", "&category=14"),
				Map.entry("Video Games", "&category=15"),
				Map.entry("Board Games", "&category=16"),
				Map.entry("Science & Nature", "&category=17"),
				Map.entry("Computers", "&category=18"),
				Map.entry("Mathematics", "&category=19"),
				Map.entry("Mythology", "&category=20"),
				Map.entry("Sports", "&category=21"),
				Map.entry("Geography", "&category=22"),
				Map.entry("History", "&category=23"),
				Map.entry("Politics", "&category=24"),
				Map.entry("Art", "&category=25"),
				Map.entry("Celebrities", "&category=26"),
				Map.entry("Animals", "&category=27"),
				Map.entry("Vehicles", "&category=28"),
				Map.entry("Comics", "&category=29"),
				Map.entry("Gadgets", "&category=30"),
				Map.entry("Anime", "&category=31"),
				Map.entry("Cartoon & Animations", "&category=32"),
				Map.entry("Any Category", "")
				));
	}
	
	/**
	 * returns the number of the button currently pressed
	 * @param ButtonGroup buttonGroup
	 * @return integer representing the number of button pressed
	 */
	public int getSelectedButton(ButtonGroup buttonGroup)
	{
		// iterates through each button in ButtonGroup
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button =  buttons.nextElement();
			if (button.isSelected()) {
				// since the names of the button are in the form answer1, answer2, etc, you can take the 6th index of the button name to get it's number
				return Integer.parseInt(button.getText().substring(6));
			}
		}
		// if no button is selected, returns -1 to indicate that nothing was selected
		return -1;
	}
}