package directSupplyKata;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * QuestionSet stores the information retrieved by one call to the open trivia database API.
 * It allows the user to access each question returned by openTDB.
 */
public class QuestionSet {
	
	//creates instance variables
	private JSONObject obj;
	private String correctAnswer;
	private String question;
	private ArrayList<String> answers;
	private int correctNumber;
	private HashMap<String, Integer> questionLettersReversed;
	
	/**
	 * Creates a question set object to store the set of all questions returned by the API
	 * @param s
	 */
	public QuestionSet(String s)
	{
		URL url;
		try {
			url = new URL(s);
			// retreives information from the given url and adds it to the JSONObject obj
			obj = new JSONObject(stream(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return integer representing the correct answer (0, 1, 2, or 3 for a, b, c, and d)
	 */
	public int getCorrectNumber()
	{
		return correctNumber;
	}
	/**
	 * sets the question number of the questionset. the question number represents which question number to return when asked for question/answers
	 * @param newQuestionNumber integer
	 */
	public void setQuestionNumber(int newQuestionNumber)
	{
		JSONObject results = (JSONObject)(obj.getJSONArray("results").get(newQuestionNumber));
		question = reformat(results.getString("question"));
		correctAnswer = results.getString("correct_answer");
		answers = new ArrayList<String>();
		JSONArray incorrectAnswers = results.getJSONArray("incorrect_answers");
		for (int j = 0; j < incorrectAnswers.length(); j++)
		{
			answers.add(reformat(results.getJSONArray("incorrect_answers").getString(j)));
		}
		correctNumber = (int)(Math.floor(Math.random() * (incorrectAnswers.length() + 1)));
		answers.add(correctNumber, correctAnswer);
	}	
	/**
	 * @return String representing the question
	 */
	public String getQuestion()
	{
		return question;
	}
	/**
	 * @return ArrayList<String> representing the list of possible answers
	 */
	public ArrayList<String> getAnswers()
	{
		return answers;
	}
	/**
	 * returns a string with temporary characters replaced (e.g. &quot;, which represents ", is replaced with it's actual character)
	 * @param s String
	 * @return String
	 */
	public static String reformat(String s)
	{
		return s.replace("&quot;", "\"").replace("&#039;", "\'").replace("&amp;", "&").replace("&#039", "\'").replace("&amp", "&");
	}
	/**
	 * Retrieves JSON data from the given URL
	 * @param URL
	 * @return String representation of JSON
	 */
	public static String stream(URL url) {
	    try (InputStream input = url.openStream()) {
	        InputStreamReader isr = new InputStreamReader(input);
	        BufferedReader reader = new BufferedReader(isr);
	        StringBuilder json = new StringBuilder();
	        int c;
	        while ((c = reader.read()) != -1) {
	            json.append((char) c);
	        }
	        return json.toString();
	    }
	    catch (Exception e)
	    {
	    	System.out.println(e);
	    }
	    return "";
	}
}
