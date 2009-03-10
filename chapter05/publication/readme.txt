Mule Publication Application Demo
---------------------------------

You must first build this project with: mvn clean install


Start this example with the script that is relevant for your OS.

After Mule is started, HTTP POST the sample data file in /data to localhost:9756/publicationService

With wget, this can be achieved with: wget --post-file='data/test.docbook' localhost:9756/publicationService
