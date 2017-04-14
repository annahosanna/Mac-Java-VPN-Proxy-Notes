package example;

import example.slack.SlackMessageCreator;
import example.httpclient.senderimpl.HTTPPostSender;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

class Main {

	private final static Logger logger = Logger.getLogger(Main.class);

	public static void main (String args[]) {

		BasicConfigurator.configure();
		try {
			SlackMessageCreator smc = new SlackMessageCreator();
			HTTPPostSender hps = new HTTPPostSender(args[0]);
			hps.setEntity(smc.createJsonMessage(args[1],args[2],args[3],args[4]).createStringEntitySlackMessage()).postHTTPMessage();
		} catch (Exception e) {
			logger.info("There was a problem: " + e.getMessage());
		}
	}
}
