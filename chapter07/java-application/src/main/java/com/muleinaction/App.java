package com.muleinaction;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.module.client.MuleClient;

import us.geocoder.rpc.GeocoderAddressResult;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author David Dossot (david@dossot.net)
 */
public class App {

    /**
     * This could very well be a GUI...
     */
    public static void main(final String[] args) throws Exception {
        final MuleContext muleContext =
                new DefaultMuleContextFactory().createMuleContext("soap-wrapper-config.xml");

        muleContext.start();

        final MuleClient muleClient = new MuleClient(muleContext);

        callGeolocationService(muleClient);

        System.err.println("Bye!");
        muleClient.dispose();
        muleContext.dispose();
        System.exit(0);
    }

    private static void callGeolocationService(final MuleClient muleClient)
            throws MuleException {
        final GeocoderAddressResult[] results =
                (GeocoderAddressResult[]) muleClient.send(
                        "vm://GeoCoderSearch.In", "111 Main St, Anytown, KS",
                        null).getPayload();

        System.out.println();
        System.out.println("Geolocation Results:");
        System.out.println(Arrays.toString(results));
        System.out.println();
    }

}
