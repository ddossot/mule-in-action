package com.clood.it;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;
import org.jdom.xpath.XPath;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;

public class ResolveGroup implements DecisionHandler {

    private XPath groupExpression;

    public ResolveGroup() {
        try {
            groupExpression = XPath.newInstance("//group");
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        }
    }

    public String decide(ExecutionContext executionContext) throws Exception {

        String account = (String) executionContext.getContextInstance().getVariable("incoming");        

        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new ByteArrayInputStream(account.getBytes()));

        return (groupExpression.valueOf(doc));
    }

}
