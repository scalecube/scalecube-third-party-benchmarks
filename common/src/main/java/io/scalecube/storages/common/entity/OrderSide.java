package io.scalecube.storages.common.entity;

public enum OrderSide {
  Buy(1),
  Sell(2);

  final int code;

  OrderSide(int code) {
    this.code = code;
  }

  /**
   * Returns the enum constant of this type with the specified code.
   *
   * @param code the code which represents enum constant
   * @return the enum constant with the specified code
   * @throws IllegalArgumentException – if this enum type has no constant with the specified code
   */
  public static OrderSide valueOf(byte code) {
    switch (code) {
      case 1:
        return Buy;
      case 2:
        return Sell;
      default:
        throw new IllegalArgumentException("Unknown order side code: " + code);
    }
  }
}
