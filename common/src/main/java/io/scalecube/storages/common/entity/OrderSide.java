package io.scalecube.storages.common.entity;

public enum OrderSide {
  Buy(1),
  Sell(2);

  final int code;

  OrderSide(int code) {
    this.code = code;
  }

  public static OrderSide valueOf(byte code) {
    for (OrderSide side : values()) {
      if (side.code == code) {
        return side;
      }
    }

    throw new IllegalArgumentException("Unknown order side code: " + code);
  }
}
