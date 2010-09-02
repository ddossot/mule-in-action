mvn exec:java -Dexec.mainClass="org.mule.MuleServer" -Dexec.args="-config conf/echo-config.groovy -builder org.mule.module.scripting.builders.ScriptConfigurationBuilder"
