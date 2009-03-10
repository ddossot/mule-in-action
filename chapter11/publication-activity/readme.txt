Mule Publication Activity Monitoring Demo
-----------------------------------------

You must first build this project with: mvn clean install


Start Chainsaw with: mvn exec:java


Then, in another console, start the publication application with the script that is relevant for your OS.


After Mule is started, navigate to the HTML Dashboard: http://localhost:9756/dashboard


You can fiddle with the Ping service at: http://localhost:9756/ping


Then HTTP POST the sample data file in /data to localhost:9756/publicationService

With wget, this can be achieved with: wget --post-file='data/test.docbook' localhost:9756/publicationService


Look at the activity events getting logged in the Chainsaw console.
