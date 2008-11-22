import org.mule.retry.PolicyStatus;
import org.mule.api.retry.RetryPolicy;

public class ExhaustingRetryPolicy implements RetryPolicy {
    private static int RETRY_LIMIT = 5;
    private int retryCounter = 0;

    public PolicyStatus applyPolicy(Throwable throwable) {
        if (retryCounter >= RETRY_LIMIT) {
            return PolicyStatus.policyExhausted(throwable);
        } else {
            try {

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            retryCounter++;
            return PolicyStatus.policyOk();
        }
    }
}
