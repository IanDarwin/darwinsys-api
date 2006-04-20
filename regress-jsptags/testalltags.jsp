<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="darwintags" uri="http://www.darwinsys.com/darwintags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test of Darwin Tags</title>
</head>
<body>
<table border='1'>
<tr><th>Tag</th><th>Example args</th><th>Example Output</th></tr>

<tr><td>backref</td><td>none</td></tr>

<tr><td>Calendar</td><td></td><td><darwintags:calendar/></tr>

<tr><td>DataTable</td><td></td><td></td></tr>

<tr><td>loggedinrole</td><td></td><td></td></tr>

<tr><td>loggedinuser</td><td>none</td></tr>

<tr><td>commandoutput</td><td>command="hostname"</td><td><darwintags:commandoutput command="hostname"/></td></tr>

<tr><td>textimage</td><td>text="Foo123"</td><td><darwintags:textimage text="Foo123"</darwintags:textimage></tr>

</table>

</body>
</html>