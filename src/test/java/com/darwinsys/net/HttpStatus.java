package com.darwinsys.net;

// From http://en.wikipedia.org/wiki/List_of_HTTP_status_codes

public enum HttpStatus {
100 Continue
101 Switching_Protocols
102 Processing // WebDAV; RFC 2518

// 2xx Success

200 OK
201 Created
202 Accepted
203 Non_Authoritative_Information // since HTTP/1.1
204 No_Content
205 Reset_Content
206 Partial_Content
207 Multi_Status // WebDAV; RFC 4918
208 Already_Reported // WebDAV; RFC 5842
226 IM_Used // RFC 3229

// 3xx Redirection

300 Multiple_Choices
301 Moved_Permanently
302 Found
303 See_Other // since HTTP/1.1
304 Not_Modified
305 Use_Proxy // since HTTP/1.1
306 Switch_Proxy
307 Temporary_Redirect // since HTTP/1.1
308 Permanent_Redirect_X // approved as experimental RFC)[12]

// 4xx Client Error

400 Bad_Request
401 Unauthorized
402 Payment_Required
403 Forbidden
404 Not_Found
405 Method_Not_Allowed
406 Not_Acceptable
407 Proxy_Authentication_Required
408 Request_Timeout
409 Conflict
410 Gone
411 Length_Required
412 Precondition_Failed
413 Request_Entity_Too_Large
414 Request_URI_Too_Long
415 Unsupported_Media_Type
416 Requested_Range_Not_Satisfiable
417 Expectation_Failed
418 I_m_a_teapot // RFC 2324
419 Authentication_Timeout // not in RFC 2616
420 Method_Failure // Spring Framework
420 Enhance_Your_Calm // Twitter
422 Unprocessable_Entity // WebDAV; RFC 4918
423 Locked // WebDAV; RFC 4918
424 Failed_Dependency // WebDAV; RFC 4918
424 Method_Failure // WebDAV)[14]
425 Unordered_Collection // Internet draft
426 Upgrade_Required // RFC 2817
428 Precondition_Required // RFC 6585
429 Too_Many_Requests // RFC 6585
431 Request_Header_Fields_Too_Large // RFC 6585
440 Login_Timeout // Microsoft
444 No_Response // Nginx
449 Retry_With // Microsoft
450 Blocked_by_Windows_Parental_Controls // Microsoft
451 Unavailable_For_Legal_Reasons // Internet draft
451 Redirect // Microsoft
494 Request_Header_Too_Large // Nginx
495 Cert_Error // Nginx
496 No_Cert // Nginx
497 HTTP_to_HTTPS // Nginx
499 Client_Closed_Request // Nginx

// 5xx Server Error

500 Internal_Server_Error
501 Not_Implemented
502 Bad_Gateway
503 Service_Unavailable
504_Gateway_Timeout
505 HTTP_Version_Not_Supported
506 Variant_Also_Negotiates // RFC 2295
507 Insufficient_Storage // WebDAV; RFC 4918
508 Loop_Detected // WebDAV; RFC 5842
509 Bandwidth_Limit_Exceeded // Apache bw/limited extension
510 Not_Extended // RFC 2774
511 Network_Authentication_Required // RFC 6585
520 Origin_Error // Cloudflare
522 Connection_timed_out
523 Proxy_Declined_Request // Cloudflare
524 A_timeout_occurred // Cloudflare
598 Network_read_timeout_error // Unknown
599 Network_connect_timeout_error // Unknown
}
