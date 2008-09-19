package com.clood.component;

import org.mule.api.MuleEventContext;
import org.mule.api.transformer.TransformerException;
import org.mule.tck.AbstractMuleTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class AuditComponentTestCase extends AbstractMuleTestCase {

    private static final String TEST_PROPERTY_VALUE = "foo.value";

    private static final String TEST_PROPERTY_KEY = "foo.key";

    private static final String TEST_PAYLOAD = "foo.payload";

    private AuditComponent auditComponent;

    private MuleEventContext eventContext;

    @Override
    protected void doSetUp() throws Exception {
        super.doSetUp();

        auditComponent = new AuditComponent();
        auditComponent.setLogName("unit.test.auditComponent");

        eventContext = AbstractMuleTestCase.getTestEventContext(TEST_PAYLOAD);
        eventContext.getMessage().setStringProperty(TEST_PROPERTY_KEY,
                TEST_PROPERTY_VALUE);
    }

    public void testCallReturnsNull() throws Exception {
        assertNull(auditComponent.onCall(eventContext));
        assertNull(auditComponent.onCall(AbstractMuleTestCase.getTestEventContext(null)));
    }

    public void testCorrectlyLogsPropertiesAndPayload()
            throws TransformerException {

        final String logEntry = auditComponent.buildLogEntry(eventContext);
        assertTrue(logEntry.contains(TEST_PAYLOAD));
        assertTrue(logEntry.contains(TEST_PROPERTY_KEY));
        assertTrue(logEntry.contains(TEST_PROPERTY_VALUE));
    }
}
