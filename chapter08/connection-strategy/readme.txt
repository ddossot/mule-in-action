Mule Connection Strategies Demos
---------------------------------

To run these examples you'll first need to install the Common Retry Policies
from the MuleForge here:

http://www.mulesource.org/display/COMMONRETRYPOLICIES/Home

After putting the commonretrypolicies jar into $MULE_HOME/lib/user, run:

mvn clean install

from this directory.

Start the examples with the script that is relevant for your OS.

The examples use an embedded ActiveMQ broker.  To test the retry policies,
you'll likely want to change the global properties to point to an external JMS
broker.  You can take the broker up and down and observe the retry policies
reconnecting.

