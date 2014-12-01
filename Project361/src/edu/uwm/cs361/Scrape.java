package edu.uwm.cs361;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
//			buf += removeTags(inputLine).trim() + " ";
			buf += inputLine;
		}
		in.close();
//		processCourses(buf);
		ArrayList<Course> crs = getAllCourses(buf);
		for(Course c : crs){
			for(Section s : c.getSections()){
				System.out.println(c.getID() + "\t:\t" + s.getID() + ":" + s.getSection());
			}
		}
		_ds.addCourseAll(crs);
//		for(Course c : crs){
//			_ds.addSections(c.getSections());
//		}
	}
	
	private static ArrayList<Course> getAllCourses(String text) throws FileNotFoundException, UnsupportedEncodingException{
		Pattern pattern = Pattern.compile("<td class=\"body copy\">\\s*<span class=\"subhead\">.*?</div>\\s*</td>\\s*</tr>\\s*<tr>");
		Matcher matcher = pattern.matcher(text);
		ArrayList<String> rawCourses = new ArrayList<String>();
		while(matcher.find()){
			rawCourses.add(matcher.group());
		}
		ArrayList<Course> courses = new ArrayList<Course>();
		for(String s : rawCourses){
			courses.add(courses.size(),getCourseAttributes(s));
		}
		return courses;
	}
	private static Course getCourseAttributes(String course){
		Pattern pattern = Pattern.compile("<span class=\"subhead\">.*?</span>");
		Matcher matcher = pattern.matcher(course);
		matcher.find();
		String[] temp = removeTags(matcher.group()).split(":");
		String[] attr = new String[2];
		attr[0] = temp[0].trim();
		attr[1] = temp[1].trim();
		//COMPSCI 101(111)
		//COMPSCI-201
		Course c = new Course(attr[0],attr[1],attr[0].substring(8, 11),null);
		c.setSections(getCourseSections(course,c));
		return c;
	}
	private static ArrayList<Section> getCourseSections(String course, Course crs){
		Pattern pattern = Pattern.compile("<tr class=\"body copy\".*?>.*?</tr>");
		Matcher matcher = pattern.matcher(course);
		ArrayList<String> rawSections = new ArrayList<String>();
		while(matcher.find()){
			rawSections.add(matcher.group());
		}
		ArrayList<Section> secs = new ArrayList<Section>();
		for(String s : rawSections){
			if(getSectionAttributes(s,crs)==null) continue;
			secs.add(secs.size(),getSectionAttributes(s,crs));
		}
		return secs;
	}
	private static Section getSectionAttributes(String section, Course crs) {
		Pattern pattern = Pattern.compile("<td style.*?>.*?</td>");
		Matcher matcher = pattern.matcher(section);
		String[] attributes = new String[11]; 
		// all sections have 11 <td> tags though not all are needed
		for(int i=0; i<11 ; i++){
			matcher.find();
			attributes[i] = removeTags(matcher.group()).trim();
		}
		// a couple sections are stubs that only contain less than minimum information
		if(attributes[3].equals("&nbsp;")){
			return null;
		}
		return new Section(attributes[3],crs.getID(),attributes[2],attributes[3].substring(0,3),
				attributes[3].substring(attributes[3].length()-3, attributes[3].length()),
				attributes[5],attributes[6],attributes[7],attributes[8],attributes[9],crs);
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
