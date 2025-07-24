package online.will_we.statemachine.dto;

/**
 * 订单状态
 */
public enum OrderEventEnum {
    CREATE_ORDER(1, "创建订单"),
    REPAY(2, "支付"),
    CANCEL_ORDER(3, "取消订单"),
    TAKE_ORDER(4, "接单"),
    REJECT_ORDER(5, "拒单"),
    DELIVERY_PART(6, "部分发货"),
    DELIVERY_ALL(7, "全部发货"),
    CONFIRM_RECEIPT(8, "确认收货"),
    EXTEND_RECEIPT(9, "延长收货"),
    COMPLETE(10, "交易完成");

    private final int code;
    private String desc;

    OrderEventEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
