package ee.ttu.shop.order;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class OrderQueueSender {

	private static Logger logger = Logger.getLogger(OrderQueueSender.class);

	@Autowired
	private AmqpAdmin admin;

	@Autowired
	private AmqpTemplate amqpTemplate;

	public void createOrder(OrderQueueDTO dto) {

		amqpTemplate.convertAndSend("oQ.1", dto);

	}

	public void removeOK(int orderId) {
		amqpTemplate.convertAndSend("oQ.3", orderId);
	}

}
