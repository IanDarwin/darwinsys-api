DarwinSys-API
=============

This is a collection of Java classes that I've found useful.
In most cases "found useful" means used more than once,
but not always. This codebase has been together for years,
and has somewhat grown like topsy, so lopsy may be lopping
off some of its leaves as time goes by. He will try to avoid
breaking any of the projects that uses it; if he does so, please
get in touch in the usual way(s).

N.B. You MUST install a minimal $HOME/.db.properties to run
the tests under Maven.  The older Ant build.xml has a task
for this, but is otherwise not up to date.

This needs to be fixed, but for now, there it is. Rather, here it is:

\# This totally fake .db.properties should be copied to your home directory.
\# Values here are just for running the JUnit tests on the ConnectionUtils
test.DBDriver=com.darwinsys.sql.MockJDBCDriver
test.DBURL=jdbc:mock:you_make_me_laugh
test.DBUser=testName
test.DBPassword=testPassword

Cheers

Ian Darwin

