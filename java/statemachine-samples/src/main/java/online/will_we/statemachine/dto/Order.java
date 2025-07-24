package online.will_we.statemachine.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class Order {
    private String orderId;
    private String customerName;
    private Date orderDate;
    private List<String> items;
    private double totalAmount;
}
