package com.darwinsys.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/** Mailer. No relation to Norman. Sends an email message.
 * My old Sender class, recast as a Bean for use in JSP's and elsewhere.
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
// package com.darwinsys.mail;
public class Mailer {
	/** The javamail session object. */
	protected Session session;
	/** The sender's email address */
	protected String from;
	/** The subject of the message. */
	protected String subject;
	/** The recipient ("To:"), as Strings. */
	protected List<String> toList = new ArrayList<String>();
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

	/** Get from */
	public String getFrom() {
		return from;
	}

	/** Set from */
	public void setFrom(String fm) {
		from = fm;
	}

	/** Get subject */
	public String getSubject() {
		return subject;
	}

	/** Set subject */
	public void setSubject(String subj) {
		subject = subj;
	}

	// SETTERS/GETTERS FOR TO: LIST

	/** Get tolist, as an array of Strings */
	public List<String> getToList() {
		return toList;
	}

	/** Set to list to an ArrayList of Strings */
	public void setToList(ArrayList<String> to) {
		toList = to;
	}

	/** Set to as a string like "tom, mary, robin@host". Loses any
	 * previously-set values. */
	public void setToList(String s) {
		toList = tokenize(s);
	}

	/** Add one "to" recipient */
	public void addTo(String to) {
		toList.add(to);
	}

	// SETTERS/GETTERS FOR CC: LIST

	/** Get cclist, as an array of Strings */
	public List<String> getCcList() {
		return ccList;
	}

	/** Set cc list to an ArrayList of Strings */
	public void setCcList(ArrayList<String> cc) {
		ccList = cc;
	}

	/** Set cc as a string like "tom, mary, robin@host". Loses any
	 * previously-set values. */
	public void setCcList(String s) {
		ccList = tokenize(s);
	}

	/** Add one "cc" recipient */
	public void addCc(String cc) {
		ccList.add(cc);
	}

	// SETTERS/GETTERS FOR BCC: LIST

	/** Get bcclist, as an array of Strings */
	public List<String> getBccList() {
		return bccList;
	}

	/** Set bcc list to an ArrayList of Strings */
	public void setBccList(List<String> bcc) {
		bccList = bcc;
	}

	/** Set bcc as a string like "tom, mary, robin@host". Loses any
	 * previously-set values. */
	public void setBccList(String s) {
		bccList = tokenize(s);
	}

	/** Add one "bcc" recipient */
	public void addBcc(String bcc) {
		bccList.add(bcc);
	}

	// SETTER/GETTER FOR MESSAGE BODY

	/** Get message */
	public String getBody() {
		return body;
	}

	/** Set message */
	public void setBody(String text) {
		body = text;
	}

	// SETTER/GETTER FOR VERBOSITY

	/** Get verbose */
	public boolean isVerbose() {
		return verbose;
	}

	/** Set verbose */
	public void setVerbose(boolean v) {
		verbose = v;
	}

	/** Check if all required fields have been set before sending.
	 * Normally called e.g., by a JSP before calling doSend.
	 * Is also called by doSend for verification.
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

		// Finally, send the message! (use static Transport method)
		// Do this in a Thread as it sometimes is too slow for JServ
		// new Thread() {
			// public void run() {
				// try {

					Transport.send(mesg);

				// } catch (MessagingException e) {
					// throw new IllegalArgumentException(
					// "Transport.send() threw: " + e.toString());
				// }
			// }
		// }.start();
	}

	/** Convenience method that does it all with one call.
	 * @param mailhost - SMTP server host
	 * @param recipient - domain address of email (user@host.domain)
	 * @param sender - your email address
	 * @param subject - the subject line
	 * @param message - the entire message body as a String with embedded \n's
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

	/** Convert a list of email addresses to an ArrayList. This will work
	 * for simple names like "tom, mary@foo.com, 123.45@c$.com"
	 * but will fail on certain complex (but RFC-valid) names like
	 * "(Darwin, Ian) &lt;http://www.darwinsys.com/&gt;".
	 * Or even "Ian Darwin &lt;http://www.darwinsys.com/&gt;".
	 */
	protected List<String> tokenize(String addrs) {
		List<String> al = new ArrayList<String>();
		if (addrs != null) {
			StringTokenizer tf = new StringTokenizer(addrs, ",");
			// For each word found in the line
			while (tf.hasMoreTokens()) {
				// trim blanks, and add to list.
				al.add(tf.nextToken().trim());
			}
		}
		return al;
	}
}
// END main
