package ee.ttu.shop.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderQueueListener {

	private static Logger logger = Logger.getLogger(OrderQueueListener.class);

	@Autowired
	OrderService orderService;

	@Autowired
	OrderPDFBuilderMail orderPDFBuilderMail;

	public void handleOrderDTO(OrderQueueDTO dto) throws Exception {
		for (int i = 0; i < 3; i++) {

			Order order = orderService.getOrderById(dto.getOrderId());
			Map<String, Object> model = new HashMap<>();
			model.put("order", order);
			try {
				orderPDFBuilderMail.renderMergedOutputModel(model, null, null);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.sleep(3000);
				continue;
			}
			break;
		}
	}

	public void removeOK(int orderId) {

		orderService.remove_OK(orderId);

	}

}
