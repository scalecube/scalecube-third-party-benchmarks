package io.scalecube.storages.common.entity;

public enum OrderStatus {

  Accepted(1) {
    @Override
    public boolean transitionAllowed(OrderStatus from) {
      return from == null;
    }
  },
  Validated(2) {
    @Override
    public boolean transitionAllowed(OrderStatus from) {
      return from == Accepted;
    }
  },
  New(3) {
    @Override
    public boolean transitionAllowed(OrderStatus from) {
      return from == Validated;
    }
  },
  PartiallyFilled(4) {
    @Override
    public boolean transitionAllowed(OrderStatus from) {
      return from == New || from == Validated || from == PartiallyFilled;
    }
  },
  Filled(5) {
    @Override
    public boolean transitionAllowed(OrderStatus from) {
      return from == New || from == Validated || from == PartiallyFilled;
    }
  },
  Cancelled(6) {
    @Override
    public boolean transitionAllowed(OrderStatus from) {
      return from == Accepted || from == Validated || from == New || from == PartiallyFilled;
    }
  },
  Rejected(7) {
    @Override
    public boolean transitionAllowed(OrderStatus from) {
      return from == Accepted;
    }
  };

  public final static OrderStatus[] ALL = values();

  final int code;

  OrderStatus(int code) {
    this.code = code;
  }

  public static OrderStatus valueOf(byte code) {
    for (OrderStatus status : values()) {
      if (status.code == code) {
        return status;
      }
    }

    throw new IllegalArgumentException("Unknown order status code: " + code);
  }

  public abstract boolean transitionAllowed(OrderStatus from);
}
