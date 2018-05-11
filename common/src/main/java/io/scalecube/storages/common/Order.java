package io.scalecube.storages.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Order implements Externalizable {

  private String id;
  private String userId;
  private String instrumentInstanceId;
  private int quantity;
  private String orderType;
  private BigDecimal price;
  private LocalDateTime clientTimestamp;
  private LocalDateTime serverTimestamp;
  private String userIpAddress;

  public byte[] toBytes() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    writeExternal(oos);
    oos.flush();
    return baos.toByteArray();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeUTF(id);
    out.writeUTF(userId);
    out.writeUTF(instrumentInstanceId);
    out.writeInt(quantity);
    out.writeUTF(orderType);
    out.writeInt(price.intValue());
    out.writeLong(clientTimestamp.toInstant(ZoneOffset.UTC).toEpochMilli());
    out.writeLong(serverTimestamp.toInstant(ZoneOffset.UTC).toEpochMilli());
    out.writeUTF(userIpAddress);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException {
    id = in.readUTF();
    userId = in.readUTF();
    instrumentInstanceId = in.readUTF();
    quantity = in.readInt();
    orderType = in.readUTF();
    price = BigDecimal.valueOf(in.readInt());
    clientTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(in.readLong()), ZoneOffset.UTC);
    serverTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(in.readLong()), ZoneOffset.UTC);
    userIpAddress = in.readUTF();
  }

  public static Order fromBytes(byte[] valBytes) throws IOException {
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(valBytes));
    Order order = new Order();
    order.readExternal(ois);
    return order;
  }

  public Order() {}

  public Order(int i) {
    this.id = Constants.ID_PREFIX + i;
    this.userId = UUID.randomUUID().toString();
    this.instrumentInstanceId = UUID.randomUUID().toString();
    this.quantity = UUID.randomUUID().hashCode();
    this.orderType = UUID.randomUUID().toString();
    this.price = BigDecimal.ONE;
    this.clientTimestamp = LocalDateTime.now();
    this.serverTimestamp = LocalDateTime.now();
    this.userIpAddress = "127.0.0.1";
  }

  public String getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getInstrumentInstanceId() {
    return instrumentInstanceId;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getOrderType() {
    return orderType;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public LocalDateTime getClientTimestamp() {
    return clientTimestamp;
  }

  public LocalDateTime getServerTimestamp() {
    return serverTimestamp;
  }

  public String getUserIpAddress() {
    return userIpAddress;
  }

  @Override
  public String toString() {
    return "Order{" +
        "id='" + id + '\'' +
        ", userId='" + userId + '\'' +
        ", instrumentInstanceId='" + instrumentInstanceId + '\'' +
        ", quantity=" + quantity +
        ", orderType='" + orderType + '\'' +
        ", price=" + price +
        ", clientTimestamp=" + clientTimestamp +
        ", serverTimestamp=" + serverTimestamp +
        ", userIpAddress='" + userIpAddress + '\'' +
        '}';
  }
}
