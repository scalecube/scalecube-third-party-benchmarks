package io.scalecube.storages.common.entity;

public enum OrderType {
  Limit(1),
  Market(2);

  final int code;

  OrderType(int code) {
    this.code = code;
  }

  public static OrderType valueOf(byte code) {
    for (OrderType type : values()) {
      if (type.code == code) {
        return type;
      }
    }

    throw new IllegalArgumentException("Unknown order type code: " + code);
  }
}
