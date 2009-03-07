Mule Async Request Reply Example
---------------------------------

You must first build and install common-ch04 and this project with: mvn clean install

Start this example with the script that is relevant for your OS.

You can send JMS messges with "Fare" payloads to the "fares" queue and watch
the results output on the outbound topic.  See
CollectionAggregatorFunctionalTestCase.java in src/test/java/com/muleinaction/
for an example using MuleClient.
