package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class ImapJdbcTransportFunctionalTestCase extends AbstractEmailFunctionalTestCase {

    public ImapJdbcTransportFunctionalTestCase()
    {
        super(65433, STRING_MESSAGE, "imap");
    }

    @Override
	protected String getConfigResources() {
		return "conf/imap-jdbc-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service service = muleContext.getRegistry().lookupService(
				"imapJdbcService");

		assertNotNull(service);
		assertEquals("imapJdbcModel", service.getModel().getName());
	}

}
