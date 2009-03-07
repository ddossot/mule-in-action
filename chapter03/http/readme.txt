Mule HTTP Demos
---------------------------------

Start this example with the script that is relevant for your OS.

The http-inbound and http-outbond examples both start servers that listen on
port 9765.  You can post data for the http-inbound example via the following: 

With wget, this can be achieved with: wget --post-file='mydata.xml'
localhost:9765/backup-reports.

You submit data to the http-outbound example by placing files in
./data/providers

For both examples, data is output to ./data/reports.

For the http polling example, change the http.post global property to the host
you want to poll.

