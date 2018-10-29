package io.scalecube.storages.common.entity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;

public class Fill implements Externalizable {

  private BigDecimal price;
  private BigDecimal quantity;
  private long timestamp;

  public Fill() {
  }

  public Fill(BigDecimal price, BigDecimal quantity, long timestamp) {
    this.price = price;
    this.quantity = quantity;
    this.timestamp = timestamp;
  }

  public BigDecimal price() {
    return price;
  }

  public BigDecimal quantity() {
    return quantity;
  }

  public long timestamp() {
    return timestamp;
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
