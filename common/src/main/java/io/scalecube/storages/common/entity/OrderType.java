package io.scalecube.storages.common.entity;

public enum OrderType {
  Limit(1),
  Market(2);

  final int code;

  OrderType(int code) {
    this.code = code;
  }

  /**
   * Returns the enum constant of this type with the specified code.
   *
   * @param code the code which represents enum constant
   * @return the enum constant with the specified code
   * @throws IllegalArgumentException – if this enum type has no constant with the specified code
   */
  public static OrderType valueOf(byte code) {
    for (OrderType type : values()) {
      if (type.code == code) {
        return type;
      }
    }

    throw new IllegalArgumentException("Unknown order type code: " + code);
  }
}
