Mule Managed Publication Application Demo
-----------------------------------------

You must first build this project with: mvn clean install


Start this example with the script that is relevant for your OS.

Test the ping service with: curl http://localhost:9756/ping

Navigate to the JMX console at: http://127.0.0.1:8089 using pub/pub to log in.

Use JConsole to connect to service:jmx:rmi:///jndi/rmi://127.0.0.1:58889/server using the same credentials.
