package com.muleinaction.component;

import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

import us.geocoder.rpc.GeocoderAddressResult;

/**
 * 
 * @author David Dossot (david@dossot.net)
 */
public class SoapComponentFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/soap-wrapper-config.xml";
    }

    public void testGeocodeAddress() throws Exception {
        final GeocoderAddressResult[] results =
                (GeocoderAddressResult[]) new MuleClient(muleContext).send(
                        "vm://GeoCoderSearch.In", "111 Main St, Anytown, KS",
                        null).getPayload();

        assertNotNull(results);
        assertEquals(1, results.length);
    }

}
