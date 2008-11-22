import org.mule.retry.policies.AbstractPolicyTemplate;
import org.mule.retry.PolicyStatus;
import org.mule.api.retry.RetryPolicy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleRetryPolicy implements RetryPolicy {

    public PolicyStatus applyPolicy(Throwable throwable) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return PolicyStatus.policyOk();
    }
}

