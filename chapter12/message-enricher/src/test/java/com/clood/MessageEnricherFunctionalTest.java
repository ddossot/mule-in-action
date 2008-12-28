package com.clood;

import org.mule.tck.FunctionalTestCase;

public class MessageEnricherFunctionalTest  extends FunctionalTestCase {

    protected String getConfigResources() {
        return "conf/message-enricher-conf.xml";
    }
}
