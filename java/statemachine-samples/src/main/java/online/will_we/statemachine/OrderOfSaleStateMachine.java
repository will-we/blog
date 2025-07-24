package online.will_we.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import online.will_we.statemachine.dto.Order;
import online.will_we.statemachine.dto.OrderEventEnum;
import online.will_we.statemachine.dto.OrderStateMachineIdEnum;

import java.util.Date;

@Slf4j
public class OrderOfSaleStateMachine {

    private static final String MACHINE_ID = "OrderOfSaleStateMachine";

    public static void main(String[] args) {
        initialize();
    }


    public static void initialize() {
        // 定义 builder
        StateMachineBuilder<OrderStateMachineIdEnum, OrderEventEnum, Order> builder = StateMachineBuilderFactory.create();

        // 定义状态流转，from、to、on、when、perform
        builder.externalTransition()
                .from(OrderStateMachineIdEnum.INIT)
                .to(OrderStateMachineIdEnum.PAY_ONLINE)
                .on(OrderEventEnum.CREATE_ORDER)
                .when(checkOrder())
                .perform(createOrderAction());

        // 注册状态机
        StateMachine<OrderStateMachineIdEnum, OrderEventEnum, Order> machine = builder.build(MACHINE_ID);

        // 触发状态机
        Order order = Order.builder()
                .orderDate(new Date())
                .customerName("张三")
                .totalAmount(1.0)
                .build();
        OrderStateMachineIdEnum state = machine.fireEvent(OrderStateMachineIdEnum.INIT, OrderEventEnum.CREATE_ORDER, order);
        log.info("当前状态为：{}", state.getName());
    }

    private static Action<OrderStateMachineIdEnum, OrderEventEnum, Order> createOrderAction() {
        return (from, to, event, order) -> log.info("处理{}的订单，订单数量{}；订单创建", order.getCustomerName(), order.getTotalAmount());
    }

    private static Condition<Order> checkOrder() {
        log.info("状态机校验开始");
        return (order) -> order.getTotalAmount() > 0;
    }

}
