import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*; 

/** Mailer -- send an email message.
 * My old Sender class, recast as a Bean for use in JSP's.
 * @author Ian F. Darwin
 * @version $Id$
 */
public class Mailer {
	String from;
	String subject;
	String to;
	String message;
	boolean verbose;

	/** Get from */
	public String getFrom() {
		return from;
	}

	/** Set from */
	public void setFrom(String from) {
		this.from = from;
	}

	/** Get subject */
	public String getSubject() {
		return subject;
	}

	/** Set subject */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/** Get to */
	public String getTo() {
		return to;
	}

	/** Set to */
	public void setTo(String to) {
		this.to = to;
	}

	/** Get message */
	public String getMessage() {
		return message;
	}

	/** Set message */
	public void setMessage(String message) {
		this.message = message;
	}

	/** Get verbose */
	public boolean isVerbose() {
		return verbose;
	}

	/** Set verbose */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/** Check if all required fields have been set before sending */
	public boolean isComplete() {
		if (from == null    || from.length()==0 ||
		    subject == null || subject.length()==0 ||
		    to == null      || to.length()==0 ||
		    message == null || message.length()==0 )
			return false;
		return true;
	}

	/** Send the message.
	 * @param mailHost The SMTP relay host
	 * @param from The sender's email address
	 * @param subject The subject of the message.
	 * @param recip The recipient ("To:").
	 * @param message The text of the message.
	 */
	public void send(String mailHost) throws MessagingException {

		/** Properties object used to pass props into the MAIL API */
		Properties props = new Properties();
		props.put("mail.smtp.host", mailHost);

		// Create the Session object
		Session session = Session.getDefaultInstance(props, null);
		if (verbose)
			session.setDebug(true);		// Verbose!
		
		// create a message
		Message mesg = new MimeMessage(session);

		// TO Address 
		InternetAddress toAddress = new InternetAddress(to);
		mesg.addRecipient(Message.RecipientType.TO, toAddress);

		// From Address - this should come from a Properties...
		mesg.setFrom(new InternetAddress(from));

		// The Subject
		mesg.setSubject(subject);

		// Now the message body.
		mesg.setText(message);

		// Finally, send the message! (use static Transport method)
		Transport.send(mesg);
	}
}
