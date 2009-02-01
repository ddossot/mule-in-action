package com.muleinaction.component;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.util.expression.ExpressionEvaluatorManager;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ExpressionParser implements Callable {

    private String expression;

    public void setExpression(final String expression) {
        this.expression = expression;
    }

		// <start id="ExprParser-Code" />
    public Object onCall(final MuleEventContext eventContext) throws Exception {
        return ExpressionEvaluatorManager.parse(expression, eventContext
                .getMessage(), true);
    }
		// <end id="ExprParser-Code" />

}
