package edu.uwm.cs361;
//space comment
// cargo cult imports from CreateStaffServlet. clean up later
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.BaseDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import edu.uwm.cs361.ProjectServlet;

@SuppressWarnings("serial")
public class StaffHomeServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DatastoreServ ds = new DatastoreServ();
	 	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		page.banner(req,resp);
		page.courseListLayout(buildPage(),req,resp);
		page.menu(req,resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	/**
	 * Build the main page content 
	 * @return String containing built html code
	 */
	private String buildPage(){
		
		Staff guy = page.getCurrentUser();
		
		int hourPieces = 2;

		String days[] = {"Monday","Tuesday","Wednesday",
						"Thursday","Friday", "Saturday","Sunday"};
		
		int startTime = 4;
		int endTime = 23;
		
		int officeIndex = -1;
		
		ArrayList<Integer> officeForCalendar = new ArrayList<Integer>(); 
		if(guy.getOfficeHours() != null){
			
			
			for(String s : guy.getOfficeHours()) {
				
				int dayBump = -1;
				for(int n = 0; n < days.length; ++n) {
					if(s.contains(days[n].subSequence(0, 3))) dayBump = n;
				}
				
				String guyOfficeTime = s.substring(s.indexOf("> ")+1);
				
				String guyTimes[] = guyOfficeTime.split("--");
				
				String hourMinuteStart[] = guyTimes[0].split(":");
				String hourMinuteEnd[] = guyTimes[1].split(":");
				
				int hourStart = Integer.parseInt(hourMinuteStart[0].trim());
				int minuteStart = Integer.parseInt(hourMinuteStart[1].trim().substring(0, 2));
				int amPmStart = hourMinuteStart[1].contains("a") ? 0 : 1;
				int hourEnd = Integer.parseInt(hourMinuteEnd[0].trim());
				int minuteEnd = Integer.parseInt(hourMinuteEnd[1].trim().substring(0, 2));
				int amPmEnd = hourMinuteEnd[1].contains("a") ? 0 : 1;
				
				minuteStart = minuteStart >= 30 ? 1 : 0;
				hourStart += amPmStart * 12;
				
				//if(hourStart < startTime) startTime = hourStart;
				
				int cellStart = (hourStart - startTime)*days.length*hourPieces + minuteStart*days.length + dayBump;
				
				minuteEnd = minuteEnd >= 30 ? 1 : 0;
				hourEnd += amPmEnd * 12;
				
				//if(hourEnd > endTime) endTime = hourEnd;
				
				int cellEnd = (hourEnd - startTime)*days.length*hourPieces + minuteEnd*days.length + dayBump - days.length;
				
				for(int m = cellStart; m <= cellEnd; m+=days.length) {
					officeForCalendar.add(m);
				}
			}
		
			Collections.sort(officeForCalendar);
		}
		
		String http = "";
		http += "<form id=\"ccf\">"
		+			"<div id=\"title-create-staff\">"
		+				"Weekly Calendar - "
		+			"</div>"
		+		 	"<div id=\"sub1\">"
		+ 				"<table id=\"month\">"
		+					"<thead>"
		+						"<tr>"
		+							"<th class=\"side_time\">Time</th>";
		
		
		for(int i=0; i<days.length;++i) {
			
			http += "<th class=\"days\">"+days[i]+"</th>";
		}
		
		http +=					"</tr>"
		+					"</thead>"
		+					"<tbody class = \"events\" id=heq_schedule_body\">";
		
		int addedAny = -1;

		if(!officeForCalendar.isEmpty())
		{ 
			for(int i=startTime;i<endTime;++i)
			{
				String timeBreak[] = {"00","30"};
				
				for(int dub = 0; dub < hourPieces; ++dub) {
					
					int cur = officeForCalendar.size() > 0 ? officeForCalendar.get(0) : -1;
					
					int shallowNum = ( (i-startTime)*days.length*hourPieces + dub*days.length);
					
					if(cur != -1 && cur - days.length < shallowNum ) 
					{
						addedAny = 0;
						
						http += "<tr>";
						
						http += "<td class=\"times\">";
						int displayTime = i > 12 ? i - 12 : i;
						String amPm = i >= 12 ? "PM" : "AM";
						
						http += displayTime+":"+timeBreak[dub]+amPm+"</td>";
						
						for(int j = 0; j < days.length; ++j) {
							
							http += "<td class=\"event_details\">";
							
							int num = ( (i-startTime)*days.length*hourPieces + j + dub*days.length);
							
							cur = officeForCalendar.size() > 0 ? officeForCalendar.get(0) : -1;
							
							if(num == cur) {
								
								officeForCalendar.remove(0);
								
								http += "Office hours";
								
								
							} else { http += " "; }
							
							http+= "</td>";
						}
						
						http += "</tr>";
					} else { if(addedAny != -1 && addedAny < 5) http += "<tr><td class=\"littlecells\"></td> </tr>"; addedAny++; }
				
				}
			}
		}
		
		http +=					"</tbody>"
		+					"</table>"
		+			"</div>"
		+		"</form>";
		return http;
	}

}
