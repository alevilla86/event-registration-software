# event-registration-software
ERS back end &amp; database

The Event Registration Software (ERS) is a system that allow users to create and join events.

Setting up the database:<br />
FIRST: create an empty database in MySQL named 'ers'<br />
SECOND: run the Netbeans custom maven goal called 'update'<br />
THIRD: That's it!!!<br />

You can create the database with a different name, just make sure to change the 'database.name.local' property in the parent ERS maven project's pom file.

Running the project:<br />
FIRST: compile ers-core project.<br />
SECOND: run the project.<br />
