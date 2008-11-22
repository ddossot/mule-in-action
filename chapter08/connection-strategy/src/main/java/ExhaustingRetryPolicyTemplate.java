import org.mule.retry.policies.AbstractPolicyTemplate;
import org.mule.api.retry.RetryPolicy;

public class ExhaustingRetryPolicyTemplate extends AbstractPolicyTemplate {

    public RetryPolicy createRetryInstance() {
        return new ExhaustingRetryPolicy();
    }
}