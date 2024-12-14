package com.terte.service.order;

import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.order.OrderDetailResDTO;
import com.terte.dto.order.OrderResDTO;
import com.terte.dto.order.UpdateOrderReqDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Long createOrder(CreateOrderReqDTO createOrderReqDTO);
    Long updateOrder(UpdateOrderReqDTO updateOrderReqDTO);
    Long deleteOrder(Long id);

    List<OrderResDTO> getAllOrders();
    OrderDetailResDTO getOrderDetailById(Long id);
}
