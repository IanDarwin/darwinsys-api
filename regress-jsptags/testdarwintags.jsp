<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="darwintags" uri="http://www.darwinsys.com/darwintags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
	<title>Test of DarwinTags</title>
</head>

<body>

<table border='1'>

<tr><td colspan='3'>Test of DarwinTags</td></tr>

<tr><th>Tag</th><th>Example args</th><th>Example Output</th></tr>

<tr><td>backref</td><td>none</td><td><darwintags:backref/></td></tr>

<tr><td>calendar</td><td>none yet</td><td><darwintags:calendar/></td></tr>

<tr><td>datatable</td><td></td><td>n/a yet</td></tr>

<tr><td>loggedinrole</td><td></td><td>
	<darwintags:loggedinrole role="guest">
	<p>You are seeing this because you are logged in as guest.</p>
	</darwintags:loggedinrole>
</td></tr>

<tr><td>loggedinuser</td><td>none</td><td><darwintags:loggedinuser/></td></tr>

<tr><td>commandoutput</td><td>command="hostname"</td><td><darwintags:commandoutput command="hostname"/></td></tr>

<tr><td>textimage</td><td>text="Foo123"</td><td><darwintags:textimage text="Foo123"/></td></tr>

</table>

</body>
</html>