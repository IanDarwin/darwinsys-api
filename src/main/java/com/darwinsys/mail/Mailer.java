// BEGIN package
package com.darwinsys.mail;
// END package

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/** Mailer. No relation to Norman. Simply sends an email message.
 * Example usage:
 * <pre>
 * Mailer mb = new Mailer();
 * mb.setFrom("orders@YourDomain.com");
 * mb.addTo("orders@YourDomain.com");
 * mb.setSubject("LHBOOKS ORDER!!");
 * mb.setBody(order.toString());
 * mb.setServer(application.getInitParameter("mail.server.smtp"));
 * try {
 *     mb.doSend();
 * } catch (MessagingException ex) {
 *	   ...
 * }
 * </pre>
 * @author Ian F. Darwin
 */
// BEGIN main
public class Mailer {
	/** The javamail session object. */
	protected Session session;
	/** The sender's email address */
	protected String from;
	/** The subject of the message. */
	protected String subject;
	/** The recipient ("To:"), as Strings. */
	protected List<String> toList = new ArrayList<>();
	/** The CC list, as Strings. */
	protected List<String> ccList = new ArrayList<String>();
	/** The BCC list, as Strings. */
	protected List<String> bccList = new ArrayList<String>();
	/** The text of the message. */
	protected String body;
	/** The SMTP relay host */
	protected String mailHost;
	/** The verbosity setting */
	protected boolean verbose;

	public String getFrom() {
		return from;
	}

	public void setFrom(String fm) {
		from = fm;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subj) {
		subject = subj;
	}

	// SETTERS/GETTERS FOR TO: LIST

	/** Get tolist, as an array of Strings.
	 * @return The list of recipients
	 */
	public List<String> getToList() {
		return toList;
	}

	/** Set to list to an ArrayList of Strings
	 * @param to The list of recipients
	 */
	public void setToList(ArrayList<String> to) {
		toList = to;
	}

	/** Set to as a string like "tom, mary, robin@host". Loses any
	 * previously set values.
	 * @param to The list of recipients
	 */
	public void setToList(String to) {
		toList = Arrays.asList(to.split(",\\s+"));
	}

	/** Add one "to" recipient.
	 * @param to The recipient to add
	 */
	public void addTo(String to) {
		toList.add(to);
	}

	// SETTERS/GETTERS FOR CC: LIST

	/** @return cclist, as an array of Strings */
	public List<String> getCcList() {
		return ccList;
	}

	/** @param cc list to an ArrayList of Strings */
	public void setCcList(ArrayList<String> cc) {
		ccList = cc;
	}

	/** @param cc as a string like "tom, mary, robin@host". Loses any
	 * previously set values. */
	public void setCcList(String cc) {
		ccList = Arrays.asList(cc.split(",\\s+"));
	}

	/** @param cc one recipient */
	public void addCc(String cc) {
		ccList.add(cc);
	}

	// SETTERS/GETTERS FOR BCC: LIST

	/** @return bcclist, as an array of Strings */
	public List<String> getBccList() {
		return bccList;
	}

	/** @param bcc list to an ArrayList of Strings */
	public void setBccList(List<String> bcc) {
		bccList = bcc;
	}

	/** Set bcc as a string like "tom, mary, robin@host". Loses any
	 * previously set values.
	 * @param s The list of bcc's
	 */
	public void setBccList(String s) {
		bccList = Arrays.asList(s.split(",\\s+"));
	}

	/** @param bcc one "bcc" recipient to be added */
	public void addBcc(String bcc) {
		bccList.add(bcc);
	}

	// SETTER/GETTER FOR MESSAGE BODY

	public String getBody() {
		return body;
	}

	public void setBody(String text) {
		body = text;
	}

	// SETTER/GETTER FOR VERBOSITY

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean v) {
		verbose = v;
	}

	/** Check if all required fields have been set before sending.
	 * Normally called before doSend; called by doSend for verification.
	 * @return True if message is complete enough to send
	 */
	public boolean isComplete() {
		if (from == null    || from.length()==0) {
			System.err.println("doSend: no FROM");
			return false;
		}
		if (subject == null || subject.length()==0) {
			System.err.println("doSend: no SUBJECT");
			return false;
		}
		if (toList.size()==0) {
			System.err.println("doSend: no recipients");
			return false;
		}
		if (body == null || body.length()==0) {
			System.err.println("doSend: no body");
			return false;
		}
		if (mailHost == null || mailHost.length()==0) {
			System.err.println("doSend: no server host");
			return false;
		}
		return true;
	}

	public void setServer(String s) {
		mailHost = s;
	}

	/** Send the message.
	 * @throws MessagingException if the message cannot be sent
	 */
	public synchronized void doSend() throws MessagingException {

		if (!isComplete())
			throw new IllegalArgumentException(
				"doSend called before message was complete");

		/** Properties object used to pass props into the MAIL API */
		Properties props = new Properties();
		props.put("mail.smtp.host", mailHost);

		// Create the Session object
		if (session == null) {
			session = Session.getDefaultInstance(props, null);
			if (verbose)
				session.setDebug(true);		// Verbose!
		}

		// create a message
		final Message mesg = new MimeMessage(session);

		InternetAddress[] addresses;

		// TO Address list
		addresses = new InternetAddress[toList.size()];
		for (int i=0; i<addresses.length; i++)
			addresses[i] = new InternetAddress((String)toList.get(i));
		mesg.setRecipients(Message.RecipientType.TO, addresses);

		// From Address
		mesg.setFrom(new InternetAddress(from));

		// CC Address list
		addresses = new InternetAddress[ccList.size()];
		for (int i=0; i<addresses.length; i++)
			addresses[i] = new InternetAddress((String)ccList.get(i));
		mesg.setRecipients(Message.RecipientType.CC, addresses);

		// BCC Address list
		addresses = new InternetAddress[bccList.size()];
		for (int i=0; i<addresses.length; i++)
			addresses[i] = new InternetAddress((String)bccList.get(i));
		mesg.setRecipients(Message.RecipientType.BCC, addresses);

		// The Subject
		mesg.setSubject(subject);

		// Now the message body.
		mesg.setText(body);

		Transport.send(mesg);
	}

	/** Convenience method that does it all with one call.
	 * @param mailhost - SMTP server host
	 * @param recipient - domain address of email (user@host.domain)
	 * @param sender - your email address
	 * @param subject - the subject line
	 * @param message - the entire message body as a String with embedded \n's
	 * @throws MessagingException if the message cannot be sent
	 */
	public static void send(String mailhost,
		String recipient, String sender,
		String subject, String message)
		throws MessagingException {

		Mailer m = new Mailer();
		m.setServer(mailhost);
		m.addTo(recipient);
		m.setFrom(sender);
		m.setSubject(subject);
		m.setBody(message);
		m.doSend();
	}
}
// END main
