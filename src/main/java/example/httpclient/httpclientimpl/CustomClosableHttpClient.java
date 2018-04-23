package examples.httpclient.httpclientimpl;

// These are all examples pretty much straight off of the Apache web site

public class CustomCloseableHttpClient {

  private static CloseableHttpClient createClient() {
  final String httpAgent = SlackClient.userAgent();

  // Some systems do not set a keep alive which would then default to forever.  
ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {

      public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
          // Honor keep-alive header
          HeaderElementIterator it = new BasicHeaderElementIterator(
                  response.headerIterator(HTTP.CONN_KEEP_ALIVE));
          while (it.hasNext()) {
              HeaderElement he = it.nextElement();
              String param = he.getName();
              String value = he.getValue();
              if (value != null && param.equalsIgnoreCase("timeout")) {
                  try {
                      return Long.parseLong(value) * 1000;
                  } catch(NumberFormatException ignore) {
                  }
              }
          }
          // Or set it to 10 seconds
          return 10 * 1000;
      }

  };

    RequestConfig requestConfig = RequestConfig.custom()
    .setConnectTimeout(120 * 1000)
    .setSocketTimeout(300 * 1000)
    .build();

  HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

  httpClientBuilder
  .setConnectionManager(new BasicHttpClientConnectionManager())
  .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
  .setRetryHandler(new StandardHttpRequestRetryHandler(3, true))
  // Choose route planner based on the connection type needed
  // These are the three connection types:
  // No proxy: HttpRoute(target, localAddress, secure)
  // Proxy with PLAIN Tunnel: HttpRoute(target, localAddress, proxy , secure)
  // If destination scheme is not the same as proxy scheme: HttpRoute(target, localAddress, proxy, secure, TunnelType.TUNNELLED, LayerType.LAYERED)
  // .setRoutePlanner()
  // localAddress can be specified by?
  .setDefaultRequestConfig(requestConfig)
  .addInterceptorFirst(new RemoveContentLengthHeaderInterceptor())
  .setKeepAliveStrategy(keepAliveStrategy);
  if (httpAgent != null ) {
    // Some proxies filter on user agent
    httpClientBuilder.setUserAgent(httpAgent);
  }

  final CloseableHttpClient client = httpClientBuilder.build();
  return client;

}


}
