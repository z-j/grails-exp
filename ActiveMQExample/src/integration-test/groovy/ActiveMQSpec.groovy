import ActiveMQExample.MessageListenerService
import grails.plugin.jms.JmsService
import grails.test.mixin.integration.Integration
import org.apache.activemq.command.ActiveMQMessage
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

/**
 * Created by Zohaib on 12/17/2016.
 */
@Integration
class ActiveMQSpec extends Specification {

    @Autowired
    JmsService jmsService

    final String queue_1 = "TestQueue1"
    final String queue_2 = "TestQueue2"
    final String queue_3 = "TestQueue3"

    final String topic_1 = "TestTopic1"
    final String topic_2 = "VirtualTopic.TestTopic2"

    /**
     * Since messages are being sent to queue, all the messages will be received by one and only one consumer
     */
    void "Test Queue One Produce One Consumer"() {
        setup:
        MessageListenerService.TestQueue1Counter=0
        MessageListenerService.TestQueue1Counter == 0

        when:
        jmsService.send(queue: queue_1, "Test Message 1")
        jmsService.send(queue: queue_1, "Test Message 2")
        Thread.sleep(1000)

        then:
        MessageListenerService.TestQueue1Counter == 2

    }

    /**
     * Test Queue with Multiple Consumers. In this test both consumers are listening to
     * specific messages. If selectors are removed then consumers will listen to any message pending on queue.
     */
    void "Test Queue One Producer Two Consumers With Selectors"() {
        setup:
        MessageListenerService.TestQueue2Consumer1Counter=0
        MessageListenerService.TestQueue2Consumer2Counter=0
        MessageListenerService.TestQueue2Consumer1Counter == 0 && MessageListenerService.TestQueue2Consumer2Counter == 0

        when:
        ActiveMQMessage activeMQMessage_1 = new ActiveMQMessage()
        activeMQMessage_1.setProperty("type", "type_1")
        activeMQMessage_1.setProperty("message", "testMessage")

        ActiveMQMessage activeMQMessage_2 = new ActiveMQMessage()
        activeMQMessage_2.setProperty("type", "type_2")
        activeMQMessage_2.setProperty("message", "testMessage")

        jmsService.send(queue: queue_2, activeMQMessage_1)
        jmsService.send(queue: queue_2, activeMQMessage_1)
        jmsService.send(queue: queue_2, activeMQMessage_2)
        jmsService.send(queue: queue_2, activeMQMessage_1)
        Thread.sleep(1000)

        then:
        (MessageListenerService.TestQueue2Consumer1Counter
                +MessageListenerService.TestQueue2Consumer2Counter) == 4

        MessageListenerService.TestQueue2Consumer1Counter == 3
        MessageListenerService.TestQueue2Consumer2Counter == 1

        println "Counters:"+
                MessageListenerService.TestQueue2Consumer1Counter+","+
        MessageListenerService.TestQueue2Consumer2Counter
    }

    /**
     * Since messages are being sent to topic, all the consumers will get copy of all the messages.
     */
    void "Test Topic One Producer Two Consumers"() {
        setup:
        MessageListenerService.TestTopic1Consumer1Counter=0
        MessageListenerService.TestTopic1Consumer2Counter=0
        MessageListenerService.TestTopic1Consumer1Counter == 0 && MessageListenerService.TestTopic1Consumer2Counter == 0

        when:
        ActiveMQMessage activeMQMessage = new ActiveMQMessage()
        activeMQMessage.setProperty("message", "TestMessage")

        jmsService.send(topic: topic_1, activeMQMessage)
        jmsService.send(topic: topic_1, activeMQMessage)
        Thread.sleep(1000)

        then:
        (MessageListenerService.TestTopic1Consumer1Counter
                +MessageListenerService.TestTopic1Consumer2Counter) == 4

        MessageListenerService.TestTopic1Consumer1Counter == 2
        MessageListenerService.TestTopic1Consumer2Counter == 2

        println "Counters:"+
                MessageListenerService.TestTopic1Consumer1Counter+","+
                MessageListenerService.TestTopic1Consumer1Counter
    }


    /**
     * Virtual Topics are very powerful and an excellent way for load balancing when sending messages on topics.
     * If three different systems have subscribed to topic, then instead of sending messages to Topic, messages can
     * be sent to Virtual Topic. Which means now three different systems can have one or multiple consumers listening to queues.
     * Result being that all three systems will receive all messages, but if for any systems more consumers are listening,
     * (think of multiple instances)then load will be balanced as messgaes can now be processed in parallel fashion.
     * balanced (observe naming convention in message listener service
     */
    void "Test Virtual Topic One Producer One Listener (But 2 Consumers)"() {
        setup:
        MessageListenerService.TestTopic1Consumer1Counter=0
        MessageListenerService.TestTopic1Consumer2Counter=0
        MessageListenerService.TestTopic1Consumer1Counter == 0 && MessageListenerService.TestTopic1Consumer2Counter == 0

        when:
        ActiveMQMessage activeMQMessage = new ActiveMQMessage()
        activeMQMessage.setProperty("message", "TestMessage")

        jmsService.send(topic: topic_2, activeMQMessage)
        jmsService.send(topic: topic_2, activeMQMessage)
        Thread.sleep(1000)

        then:
        (MessageListenerService.TestTopic1Consumer1Counter
                +MessageListenerService.TestTopic1Consumer2Counter) == 2

        println "Counters:"+
                MessageListenerService.TestTopic1Consumer1Counter+","+
                MessageListenerService.TestTopic1Consumer2Counter
    }


    /**
     * Send messages using Template where session is created with transaction flag set to true
     * if listener returns fine, then transaction is implicitly committed
     * if listener message throws exception then transaction is implicitly rolled back
     * Test that message is redelivered, according to redelivery policy, if exception is thrown
     * Test by browsing the queue that queue contains right number of elements
     */
    void "Test Queue in Transactional Environment"() {
        setup:

        MessageListenerService.TestQueue3Consumer1Counter=0
        MessageListenerService.TestQueue3Consumer1Counter == 0

        when:
        List deadMessages = jmsService.browse("ActiveMQ.DLQ") //dead letter queue msgs should be zero, before this test
        then:
        (deadMessages.size() == 0)

        when:
        ActiveMQMessage activeMQMessage1 = new ActiveMQMessage()
        activeMQMessage1.setProperty("count", "1")
        jmsService.send(queue: queue_3, activeMQMessage1)
        Thread.sleep(2000)

        then:
        (MessageListenerService.TestQueue3Consumer1Counter == 2)//one increment for normal and one for delivery
        (jmsService.browse("ActiveMQ.DLQ")).size() == 1  //dead letter queue should have 1 entry now
    }


}
