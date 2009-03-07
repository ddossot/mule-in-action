Mule VM Transport Demos
---------------------------------

Start the examples with the script that is relevant for your OS.

Data can be posted to http://localhost:9756/orders using wget:

With wget, this can be achieved with: wget --post-file='mydata.xml'
localhost:9765/orders.

You can uncomment the "Console Service" in vm-config to see the JMS payloads
printed to the screen.  Please note that this will break the unit tests.  

You are encouraged, however, to setup an external JMS broker and use a JMS
browser, like Hermes JMS (http://www.hermesjms.com) to play with the examples.

