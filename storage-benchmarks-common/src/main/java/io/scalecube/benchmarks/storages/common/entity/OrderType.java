package io.scalecube.benchmarks.storages.common.entity;

public enum OrderType {
  Limit(1),
  Market(2);

  final int code;

  OrderType(int code) {
    this.code = code;
  }

  public int code() {
    return code;
  }

  /**
   * Returns the enum constant of this type with the specified code.
   *
   * @param code the code which represents enum constant
   * @return the enum constant with the specified code
   * @throws IllegalArgumentException – if this enum type has no constant with the specified code
   */
  public static OrderType valueOf(byte code) {
    switch (code) {
      case 1:
        return Limit;
      case 2:
        return Market;
      default:
        throw new IllegalArgumentException("Unknown order type code: " + code);
    }
  }
}
