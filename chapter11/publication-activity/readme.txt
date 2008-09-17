Mule Publication Activity Monitoring Demo
-----------------------------------------

Start Chainsaw with: mvn exec:java 

The, in another console, start the publication application with the script that is relevant for your OS.

After Mule is started, HTTP POST the sample data file in /data to localhost:8080/publicationService

With wget, this can be achieved with: wget --post-file='data/test.docbook' localhost:8080/publicationService

Look at the activity events getting logged in the Chainsaw console.

