package example.slack;

import org.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.log4j.Logger;

// This is derived from spotify, jenkins, and seyren examples.
public class SlackMessageCreator {

  private final static Logger logger = Logger.getLogger(SlackMessageCreator.class);

  private String jsonFormattedSlackMessage;

  public SlackMessageCreator() {

  }

  public  SlackMessageCreator createJsonMessage(final String channel, final String username, final String emoji, final String message) {

    if ((StringUtils.isEmpty(message)) || (StringUtils.isEmpty(channel))) {
      this.jsonFormattedSlackMessage = new String("");
      return this;
    }

    try {
      JSONObject json = new JSONObject();
      if (StringUtils.isEmpty(channel) != true) {
        json.put("channel", channel);
      }
      if (StringUtils.isEmpty(username) != true) {
        json.put("username", username);
      }
      if (StringUtils.isEmpty(emoji) != true) {
        json.put("icon_emoji", emoji);
      }
      if (StringUtils.isEmpty(message) != true) {
        json.put("text", message);
      }
      this.jsonFormattedSlackMessage = new String(json.toString());
    } catch (Exception e) {

    }
    return this;
  }

  public StringEntity createStringEntitySlackMessage() throws Exception {
    if (StringUtils.isEmpty(this.jsonFormattedSlackMessage)) {
      // Throw error
    }
    StringEntity slackStringEntity = new StringEntity(this.jsonFormattedSlackMessage,"UTF-8");
    slackStringEntity.setContentType("application/json");
    return slackStringEntity;
  }

  public UrlEncodedFormEntity createUrlEncodedFormEntitySlackMessage() throws Exception {
    if (StringUtils.isEmpty(this.jsonFormattedSlackMessage)) {
      // Throw error
    }
    List<NameValuePair> slackPayloadNameValuePair = new ArrayList<NameValuePair>();
    slackPayloadNameValuePair.add(new BasicNameValuePair("payload", this.jsonFormattedSlackMessage));
    return new UrlEncodedFormEntity(slackPayloadNameValuePair, "UTF-8");
  }
}
