package online.will_we.statemachine.dto;

public enum OrderStateMachineIdEnum {
    INIT("初始化", 0),

    PAY_ONLINE("待支付", 1),

    PART_DELIVERY("部分发货", 4),

    DELIVER_ALL("待收货", 5),

    RECEIVED("已收货", 6),

    DONE("已完成", 7),

    CANCEL("已关闭", 8);

    String name;
    int state;

    OrderStateMachineIdEnum(String name, int state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
