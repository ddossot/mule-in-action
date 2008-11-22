import org.mule.retry.policies.AbstractPolicyTemplate;
import org.mule.api.retry.RetryPolicy;

public class SimpleRetryPolicyTemplate extends AbstractPolicyTemplate {

    public RetryPolicy createRetryInstance() {
        return new SimpleRetryPolicy();
    }
}
