package example.httpclient.httpclientimpl;

import org.apache.http.HttpRequestInterceptor;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.protocol.HTTP;


// https://github.com/spring-projects/spring-ws/blob/master/spring-ws-core/src/main/java/org/springframework/ws/transport/http/HttpComponentsMessageSender.java
/**
	 * HttpClient HttpRequestInterceptor implementation that removes Content-Length header because
	 * it is optional and HttpClient throws an exception if it has already been set.
	 https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html
	 4.4:
		Content-Length is optional
		If the Transfer-Encoding header field exists, but has a value different than "identity", then the Content-Length field must not be present.
		If this is HTTP/1.0 then Content-Length is required
*/

public class RemoveDuplicateHeaderInterceptor implements HttpRequestInterceptor {

	public RemoveContentLengthHeaderInterceptor() {
		super();
	}

	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		if (request instanceof HttpEntityEnclosingRequest) {
			if (request.containsHeader(HTTP.CONTENT_LEN)) {
				request.removeHeaders(HTTP.CONTENT_LEN);
			}
		}
	}
}
