# Mac-Java-VPN-Proxy-Notes
### The workflow
```
A workstation with OS X/macOS IPv6 network stack -->
IPv4 VPN Tunnel with split routing -->
HTTP egress proxy with IP, Domain, and User-Agent filters  
```

### Requirements
* Support an IPv4 VPN client
  * Use IPv4 instead of OS X/macOS default of IPv6
    * Pass ```-Djava.net.preferIPv4Stack=true``` to the JVM
  * Select the correct (virtual) interface to route the connection through
* Forward a connection through the proxy
  * Pass HTTPS through an HTTP proxy
  * Do not try to tunnel HTTPS traffic through an HTTPS proxy, just in case the
    proxy is performing a MITM and resigning the content. (Like a BlueCoat)
  * Only route external addresses to the proxy
  * Pass an unblocked User-Agent string as a part of the proxy negotiation
    * User-Agent is not a required header, and definitely not in proxy negotiation

### TODO and Notes
* The current implemetation of the Custom Route Planner:
 1. Opens a socket to test reachability from each interface for every connection.
  * This is to deal with the split routing (Some traffic bound for the Inetnet routes there directly
  and some traverses aditional servers and then the Internet.) The additional
  servers provide services which access resources within a private network and
  thus, although the ultimate destination is the Internet, it is meaningless
  without first traversing the internal servers.  The routing/reachability tests
  could result in timeouts or blocking delays. Figure out a way to resolve the
  interface and its IP without creating a new connection. (For instance caching)
  2. The Default route planner does not  have an option to tunnel through a proxy even through HttpClient supports it.  Add that option.
* The CustomCloseableHttpClient:
 1. Just a CloseableHttpClient with some presets such as using the Custom Route Planner.
* Some remote web servers close the socket after a fixed period of time or number
  of requests to prevent resouce leaks, despite keep-alive values.  What is
  the most gracefull way to deal with this? HttpClient tries to reuse a
  connection, which may increase the likelyhood of such a condition with those
  systems.
* PoolingConnectionsManagers will see the connection to the proxy as a single route.
* Correctly passing User-Agent during proxy negotiation is generally applicable
  to any http library for any language (and their package managers).
* When using the native JRE http libraries, some issues were encountered with
  HTTPS where Java would overwrite the User-Agent header with that of the JRE/JDK
* Consider updating this to HttpClient library v5 to support HTTP 2
