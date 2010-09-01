import org.mule.*
import org.mule.config.*
import org.mule.construct.builder.*
import org.mule.api.config.*
import org.mule.api.model.*
import org.mule.api.service.*
import org.mule.endpoint.*
import org.mule.retry.policies.*
import org.mule.routing.inbound.*
import org.mule.routing.outbound.*
import org.mule.transport.*
import org.mule.transport.stdio.*
import org.mule.util.queue.*
import org.mule.security.*

// Register required system objects
queueManager = new TransactionalQueueManager()
queueManager.persistenceStrategy = new MemoryPersistenceStrategy()
muleContext.registry.registerObject(MuleProperties.OBJECT_QUEUE_MANAGER, queueManager)
muleContext.registry.registerObject(MuleProperties.OBJECT_SECURITY_MANAGER, new MuleSecurityManager())
muleContext.registry.registerObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY, new DefaultEndpointFactory())

defaultThreadingProfile = new ChainedThreadingProfile()
defaultThreadingProfile.setThreadWaitTimeout(30)
defaultThreadingProfile.setMaxThreadsActive(10)
defaultThreadingProfile.setMaxThreadsIdle(10)
defaultThreadingProfile.setMaxBufferSize(0)
defaultThreadingProfile.setThreadTTL(60000)
defaultThreadingProfile.setPoolExhaustedAction(ThreadingProfile.WHEN_EXHAUSTED_RUN)

muleContext.registry.registerObject(MuleProperties.OBJECT_DEFAULT_SERVICE_THREADING_PROFILE,
            new ChainedThreadingProfile(defaultThreadingProfile))
muleContext.registry.registerObject(MuleProperties.OBJECT_DEFAULT_MESSAGE_RECEIVER_THREADING_PROFILE,
            new ChainedThreadingProfile(defaultThreadingProfile))
muleContext.registry.registerObject(MuleProperties.OBJECT_DEFAULT_MESSAGE_DISPATCHER_THREADING_PROFILE,
            new ChainedThreadingProfile(defaultThreadingProfile))
muleContext.registry.registerObject(MuleProperties.OBJECT_DEFAULT_MESSAGE_REQUESTER_THREADING_PROFILE,
            new ChainedThreadingProfile(defaultThreadingProfile))
muleContext.registry.registerObject(MuleProperties.OBJECT_DEFAULT_RETRY_POLICY_TEMPLATE, new NoRetryPolicyTemplate())

// Register connector
c = new PromptStdioConnector(muleContext)
c.name = "SystemStreamConnector"
c.promptMessage="Please enter something: "
c.messageDelayTime=1000
muleContext.registry.registerConnector(c)

// Echo service
service = new BridgeBuilder()
  .name("echoService")
  .inboundAddress("stdio://IN")
  .outboundAddress("stdio://OUT")
  .exchangePattern(MessageExchangePattern.ONE_WAY)
  .build(muleContext)

muleContext.registry.registerObject(service.name, service)

