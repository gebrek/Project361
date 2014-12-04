package edu.uwm.cs361;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class EmailRequestServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/html");
		resp.getWriter().println("<form method='POST'>");
		resp.getWriter().println("<input type='text' name='email' />");
		resp.getWriter().println("<input type='submit' value='Send Email' />");
		resp.getWriter().println("</form>");
	}

	/**
	 * See https://developers.google.com/appengine/docs/java/mail/usingjavamail.
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "...";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("eric@eric-fritz.com", "Eric Fritz"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(req.getParameter("email"), req.getParameter("email")));
			msg.setSubject("Test Email");
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (Exception e) {
			resp.getWriter().println("Problem sending email to " + req.getParameter("email") + ".");
			return;
		}

		resp.getWriter().println("Email sent.");
	}
}
