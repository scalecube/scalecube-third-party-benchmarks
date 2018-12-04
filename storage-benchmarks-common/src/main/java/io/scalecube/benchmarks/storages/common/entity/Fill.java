package io.scalecube.benchmarks.storages.common.entity;

import io.scalecube.benchmarks.storages.common.utils.BigDecimalUtil;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;

public class Fill implements Externalizable {

  private BigDecimal price;
  private BigDecimal quantity;
  private long timestamp;

  public Fill() {}

  /**
   * Creates a fill with the given args.
   *
   * @param price price
   * @param quantity quantity
   * @param timestamp timestamp
   */
  public Fill(BigDecimal price, BigDecimal quantity, long timestamp) {
    this.price = price;
    this.quantity = quantity;
    this.timestamp = timestamp;
  }

  public BigDecimal price() {
    return price;
  }

  public Fill price(BigDecimal price) {
    this.price = price;
    return this;
  }

  public BigDecimal quantity() {
    return quantity;
  }

  public Fill quantity(BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }

  public long timestamp() {
    return timestamp;
  }

  public Fill timestamp(long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    BigDecimalUtil.writeObject(price, out);
    BigDecimalUtil.writeObject(quantity, out);
    out.writeLong(timestamp);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException {
    price = BigDecimalUtil.readObject(in);
    quantity = BigDecimalUtil.readObject(in);
    timestamp = in.readLong();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Fill{");
    sb.append("price=").append(price);
    sb.append(", quantity=").append(quantity);
    sb.append(", timestamp=").append(timestamp);
    sb.append('}');
    return sb.toString();
  }
}
