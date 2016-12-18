package ActiveMQExample

import grails.plugin.jms.Subscriber
import grails.transaction.Transactional
import grails.plugin.jms.Queue

@Transactional
class MessageListenerService {
    static exposes = ['jms']

    static int TestQueue1Counter =0

    static int TestQueue2Consumer1Counter =0
    static int TestQueue2Consumer2Counter =0

    static int TestTopic1Consumer1Counter=0
    static int TestTopic1Consumer2Counter=0


    @Queue(name="TestQueue1")
    def void receiveTestQueue1(message) {
        println ("receiveTestQueue:"+message.toString())
        TestQueue1Counter++
    }

    @Queue(name="TestQueue2", selector = "type='type_1'")
    def void receiveTestQueue2Consumer1(message) {
        println ("receiveTestQueue2:"+message.toString())
        TestQueue2Consumer1Counter++
    }

    @Queue(name="TestQueue2", selector = "type='type_2'")
    def void receiveTestQueue2Consumer2(message) {
        println ("receiveTestQueue2:"+message.toString())
        TestQueue2Consumer2Counter++
    }

    @Subscriber(topic="TestTopic1")
    def void receiveTestTopic1Consumer1(message) {
        println ("receiveTestQueue2:"+message.toString())
        TestTopic1Consumer1Counter++
    }

    @Subscriber(topic="TestTopic1")
    def void receiveTestTopic1Consumer2(message) {
        println ("receiveTestQueue2:"+message.toString())
        TestTopic1Consumer2Counter++
    }

    @Queue(name="Consumer.A.VirtualTopic.TestTopic2")
    def void receiveTestVirtualTopicConsumer1(message) {
        println ("receiveTestQueue1 vt:"+message.toString())
        TestTopic1Consumer1Counter++
    }

    @Queue(name="Consumer.A.VirtualTopic.TestTopic2")
    def void receiveTestVirtualTopicConsumer2(message) {
        println ("receiveTestQueue2: vt"+message.toString())
        TestTopic1Consumer2Counter++
    }


}
