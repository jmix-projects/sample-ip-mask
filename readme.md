# Sample Jmix IP mask
This is sample of IP mask protection.

The Sample application is a simple example how can you implement IP mask protection in Jmix.

There are several ways to implement protection. In the example for protection, the following was implemented:
* In default Jmix user class added **ipMask** field
* Implemented IP mask validation mechanism
    * Added **IpMatcher** (implementation from CUBA Platform)
    * Added **IpMaskValidationChecks**

**IpMaskValidationChecks** uses _EventListener_ for handling PreAuthenticationCheckEvent and validate IP.

The mask is a list of IP addresses, separated with commas. Both the IPv4 and IPv6 address formats are supported. 
IPv4 address should consist of four numbers separated with periods. 
IPv6 address represents eight groups of four hexadecimal characters separated with colons. 
The "*” symbol can be used in place of an address part, to match any value. Only one type of address format (IPv4 or IPv6) can be used in the mask at the same time.

Example: _192.168.* .*_


Based on Jmix Framework 1.2.4 with JDK 11.
