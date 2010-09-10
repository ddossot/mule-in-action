import org.mule.api.routing.filter.Filter
import org.mule.api.MuleMessage
import org.apache.commons.codec.digest.*

class MD5Filter implements Filter {

    static String PROPERTY_NAME = "MD5SUM";

    public boolean accept(MuleMessage muleMessage) {
    	def md5Sum = muleMessage.getInboundProperty(PROPERTY_NAME)
    	
        if (md5Sum)
            return md5Sum == DigestUtils.md5Hex(muleMessage.getPayloadAsString())
        else
            return false
    }

}
