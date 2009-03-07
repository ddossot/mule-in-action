Mule IMAP Demos
---------------------------------

You must first build this project with: mvn clean install

You'll need to edit the IMAP host properties using the global-properties for
each example to point to a valid IMAP server (the unit tests use a mock IMAP
server.)

For the imap-jdbc example you'll additinally need to modify the
dataSource in spring-config.xml if you want to use a database other then the
embedded Apache Derby one that is configured. A schema for MySQL is included
in src/main/sql. 

Start the examples with the script that is relevant for your OS.


