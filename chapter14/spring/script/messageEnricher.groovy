import org.mule.api.MuleEventContext
import org.mule.api.lifecycle.Callable

class MessageEnricher implements Callable {

    public Object onCall(MuleEventContext muleEventContext) {
        def message = muleEventContext.getMessage()
        if (message.payload =~ "STATUS: CRITICAL") {
            message.setProperty("PRIORITY", 'NORMAL')
        } else {
            message.setProperty("PRIORITY", 'LOW')

        }
        return message
    }
}