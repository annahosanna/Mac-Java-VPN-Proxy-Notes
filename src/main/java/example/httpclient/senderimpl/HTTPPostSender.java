
/* Based on MIT licensed code from https://github.com/bradfogle/slack-spotify-playlist */

package example.httpclient.senderimpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.System;
import org.apache.http.client.utils.HttpClientUtils;

import org.apache.http.HttpEntity;
import org.apache.log4j.Logger;
import example.httpclient.httpclientimpl.CustomCloseableHttpClient;

public class HTTPPostSender {

  private final static Logger logger = Logger.getLogger(HTTPPostSender.class);

  String url;
  HttpPost httpPost;

  public HTTPPostSender(final String url) {
    this.httpPost = null;
    try {
      this.httpPost = new HttpPost(url);
      if (httpPost == null) {
        logger.info("The HttpPost object which was passed was null");
        throw new Exception("Could not create the HttpPost object");
      }
      final String userAgent = HTTPPostSender.userAgent();
      if (StringUtils.isEmpty(userAgent) == false) {
        httpPost.setHeader("User-Agent", userAgent);
      }
    } catch (Exception e) {

    }
  }

  public HTTPPostSender setEntity(final HttpEntity entityToSend) throws Exception {
    try {
      this.httpPost.setEntity(entityToSend);
    } catch (Exception g) {
      logger.error("There was a problem creating the entity: " + g.getMessage());
      throw new Exception("There was a problem creating the entity");
    }
    return this;
  }

  public boolean postHTTPMessage() {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    boolean success = false;
    try {
      httpClient = CustomCloseableHttpClient.createClient();
      response = httpClient.execute(httpPost);
      EntityUtils.consumeQuietly(response.getEntity());
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != 200) {
        logger.info("The remote server responded with an error to the post request: " + EntityUtils.toString(response.getEntity()));
      } else {
        success = true;
      }
    } catch (Exception e) {
      logger.info("There was a local problem executing the post request");
      e.printStackTrace();
    } finally {
      if (response != null) {
        HttpClientUtils.closeQuietly(response);
      }
      httpPost.completed();
      httpPost.reset();
      if (httpClient != null) {
        HttpClientUtils.closeQuietly(httpClient);
      }
    }
    return success;
  }
  
  public static String userAgent() {
    return System.getProperty("http.agent") != null ? new String(System.getProperty("http.agent")) : null;
  }
}
