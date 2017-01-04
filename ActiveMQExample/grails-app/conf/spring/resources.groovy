import org.apache.activemq.RedeliveryPolicy
import org.springframework.jms.connection.SingleConnectionFactory
import org.apache.activemq.ActiveMQConnectionFactory

beans = {

    redeliveryPolicy(RedeliveryPolicy) {
        maximumRedeliveries = 1
        queue = "*"
    }

    // used by the standard template by convention
    jmsConnectionFactory(SingleConnectionFactory) {
        targetConnectionFactory = { ActiveMQConnectionFactory cf ->
            brokerURL = 'vm://localhost'
            redeliveryPolicy = ref(redeliveryPolicy)
        }
    }
}