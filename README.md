# Mac-Java-VPN-Proxy-Notes
### The workflow
```
A workstation with OS X/macOS IPv6 network stack -->
IPv4 VPN Tunnel -->
HTTP egress proxy with IP, Domain, and User-Agent filters  
```

### Requirements
* Support an IPv4 VPN client
  * Use IPv4 instead of OS X/macOS default of IPv6
    * Pass ```-Djava.net.preferIPv4Stack=true``` to the JVM
  * Select the correct (virtual) interface to route the connection through
* Forward a connection through the proxy
  * Pass HTTPS through an HTTP proxy
  * Only route external addresses to the proxy
  * Pass an unblocked User-Agent string as a part of the proxy negotiation
  * PoolingConnectionsManagers will see the connection to the proxy as a single route.

### TODO
* The current implemetation of the route planner opens a socket to test
  reachability from each interface for every connection. This could result
  in timeouts or blocking delays. Figure out a way to resolve the interface
  and its IP without creating a new connection.
* Some remote servers close the socket after a fixed period of time or number
  of requests to prevent resouce leaks, despite keep-alive values.  What is
  the most gracefull way to deal with this.
* HttpClient tries to reuse a connection, which may increase the number of
  
