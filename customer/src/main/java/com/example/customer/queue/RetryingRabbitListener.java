package com.example.customer.queue;

import com.example.customer.service.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.customer.config.RabbitConfiguration.PARKINGLOT_QUEUE;
import static com.example.customer.config.RabbitConfiguration.PRIMARY_QUEUE;

@Service
@Log4j2
public class RetryingRabbitListener {

    private Logger logger = LoggerFactory.getLogger(RetryingRabbitListener.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CustomerService customerService;

    public RetryingRabbitListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = PRIMARY_QUEUE)
    public void primary(Message in) throws Exception {
        logger.info("Message read from test-queue: " + in);
        if (hasExceededRetryCount(in)) {
            putIntoParkingLot(in);
            return;
        }

        customerService.updateCustomer(new String(in.getBody()));

    }

    private boolean hasExceededRetryCount(Message in) {
        List<Map<String, ?>> xDeathHeader = in.getMessageProperties().getXDeathHeader();
        if (xDeathHeader != null && xDeathHeader.size() >= 1) {
            Long count = (Long) xDeathHeader.get(0).get("count");
            return count >= 3;
        }

        return false;
    }

    private void putIntoParkingLot(Message failedMessage) {
        logger.info("Retries exceeded putting into parking lot");
        this.rabbitTemplate.send(PARKINGLOT_QUEUE, failedMessage);
    }

}