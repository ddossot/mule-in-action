package com.muleinaction.component;

import org.mule.api.MuleEventContext;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.lifecycle.Callable;

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
        final ExpressionManager expressionManager = eventContext
                .getMuleContext().getExpressionManager();

        return expressionManager.parse(expression, eventContext.getMessage(),
                true);
    }
    // <end id="ExprParser-Code" />

}
