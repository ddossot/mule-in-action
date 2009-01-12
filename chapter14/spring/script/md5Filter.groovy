import org.mule.api.routing.filter.Filter
import org.mule.api.MuleMessage
import org.apache.commons.codec.digest.*

class MD5Filter implements Filter {

    static String PROPERTY_NAME = "MD5SUM";

    public boolean accept(MuleMessage muleMessage) {
        if (muleMessage.getProperty(PROPERTY_NAME))
            return muleMessage.getProperty(PROPERTY_NAME) == DigestUtils.md5Hex(muleMessage.getPayloadAsString())
        else
            return false
    }

}
