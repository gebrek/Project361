package edu.uwm.cs361;

import java.net.*;
import java.io.*;
import java.util.regex.*;

public class Scrape {

	private static DatastoreServ _ds = new DatastoreServ();
	
	private static int _sectionID = 1;
	
	/**
	 * Processes all courses at a hard-coded hyperlink
	 * @throws IOException
	 */
	public static void getCourseListandStore() throws IOException{
		
		_ds.deleteCourses();
		
		getURL("http://www4.uwm.edu/schedule/index.cfm?a1=subject_details&subject=COMPSCI&strm=2149");
	}
	
	/**
	 * Gets a buffer containing all the html form an url
	 * @param url Url to get content from
	 * @throws IOException
	 */
	private static void getURL(String url) throws IOException {
		
		URL place = new URL(url);
		
		String buf = "";
		
		BufferedReader in = new BufferedReader(new InputStreamReader(place.openStream()));
		
		String inputLine;
		
		while ((inputLine = in.readLine()) != null) {
			
			if(inputLine.trim().isEmpty()) {
				
				continue;
			}
			
			buf += removeTags(inputLine).trim() + " ";
		}
		
		in.close();
		
		processCourses(buf);
	}
	
	/**
	 * Retrieves courses with regex
	 * @param text
	 */
	private static void processCourses(String text){
		
		Pattern pattern = Pattern.compile("COMPSCI[-\\ ]\\d{3}(?:(?!div_course_details).)*?div_course_details");
		Matcher matcher = pattern.matcher(text);
		
		int courseid = 1;
		
		while (matcher.find()){
			
			String g = matcher.group();
			
			_ds.addCourse(courseid+"", slurpTitle(g),slurpCourseNumber(g));
			
			processSections(courseid,g);
			
			courseid++;
		}
	}
	

	/**
	 * Retrieves sections with regex from text
	 * @param id
	 * @param text
	 */
	private static void processSections(int id, String text){
		
		for(String str : getSections(text)){
			
			_ds.addSection(_sectionID+"", id+"", slurpUnits(str), slurpSecDesignation(str), 
				slurpHours(str), slurpDays(str), slurpDates(str), 
				slurpInstructor(str), slurpRoom(str));
			
			_sectionID++;
		}
	}
	
	/**
	 * Removes unnecessary tags from line
	 * @param line
	 */
	private static String removeTags(String line){
		// who can see anything with all those angle brackets everywhere?
		return line.replaceAll("<[^<>]*>", ""); // regex a neat
	}
	
	/**
	 * Removes "nbsp" from text 
	 * @param text
	 * @return
	 */
	private static String killnbsp(String text){
		// ended up only needing this in one place
		// maybe should just factor it back it
		return text.replaceAll("&nbsp;", "");
	}
	
	/**
	 * Uses passed regex expression to retrieve various course info 
	 * @param text
	 * @param regex
	 * @return
	 */
	private static String slurper(String text, String regex){
		
		String group; 
		
		try {
			
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		
		group = matcher.group();
		
		} catch (IllegalStateException e) {
			
			return "";
		}
		
		return group;
	}
	
	/**
	 * Gets course number 
	 * @param text
	 * @return
	 */
	private static String slurpCourseNumber(String text){
		
		String number = slurper(text, "^.*?(?=:)");
		
		return number.replaceFirst(".*?(?=\\d)", "");
	}
	
	/**
	 * Gets course title
	 * 
	 * @param text
	 * @return
	 */
	private static String slurpTitle(String text){
		return slurper(text, "(?<=:).*?(?=\\()").trim();
	}
	
	/**
	 * Gets courses sections
	 * @param text
	 * @return
	 */
	private static String[] getSections(String text){
		// it sure would be nice if java had a map function
		return text.replaceFirst("^.*?\\(FEE\\)", "").split("\\(FEE\\)");
	}

	/**
	 * Gets courses units
	 * @param text
	 * @return
	 */
	private static String slurpUnits(String text){
		return killnbsp(slurper(text, "^.*?(?=[A-Z])")).trim();
	}
	
	/**
	 * gets sections designation
	 * @param text
	 * @return
	 */
	private static String slurpSecDesignation(String text){
		return slurper(text,"[A-Z]{3} \\d{3}");
	}
	/**
	 * gets sections hours
	 * @param text
	 * @return
	 */
	private static String slurpHours(String text){
		return slurper(text, "(?<=\\d{5}).*?(?=\\s{3})").trim();
	}
	/**
	 * gets sections days
	 * @param text
	 * @return
	 */
	private static String slurpDays(String text){
		return slurper(text,"(?<=\\s)[MTWRF\\-]{1,5}(?=\\s)");
	}
	/**
	 * gets sections dates
	 * @param text
	 * @return
	 */
	private static String slurpDates(String text){
		return slurper(text,"\\d{2}/\\d{2}-\\d{2}/\\d{2}");
	}
	/**
	 * gets sections instuctor
	 * @param text
	 * @return
	 */
	private static String slurpInstructor(String text){
		try{
			return slurper(text, "[A-Z][a-z]+, [A-Z][a-z]+");
		}
		catch(Exception e){
			return "";
		}
	}
	/**
	 * gets sections room
	 * @param text
	 * @return
	 */
	private static String slurpRoom(String text){
		String step = slurpInstructor(text);
		if(step.isEmpty())
			return slurper(text.replaceFirst("^.{10}", ""),"(?<=;).*?(?=&)").trim(); // probably a better way to do this
		return slurper(text,"(?<=" + step + ").*?(?=&)").trim();
		// regex a best
	}
}
