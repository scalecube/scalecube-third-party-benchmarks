package io.scalecube.storages.common.entity;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public final class Order implements Externalizable {

  private static final Random RANDOM = new Random(42);

  private UUID id;
  private String userId;
  private String instrumentInstanceId;
  private String instrumentName;
  private BigDecimal quantity;
  private BigDecimal remainingQuantity;
  private OrderType orderType;
  private OrderSide side;
  private BigDecimal price;
  private LocalDateTime clientTimestamp;
  private LocalDateTime serverTimestamp;
  private String userIpAddress;
  private OrderStatus status;
  private List<Fill> fills;

  private Order() {
  }

  public Order(UUID id) {
    this.id = id;
    userId = "01234567-8901-2345-6789-012345678901";
    instrumentInstanceId = "01234567-8901-2345-6789-012345678901";
    instrumentName = "BTC";
    quantity = BigDecimal.valueOf(RANDOM.nextLong());
    remainingQuantity = BigDecimal.valueOf(RANDOM.nextLong());
    orderType = RANDOM.nextBoolean() ? OrderType.Limit : OrderType.Market;
    side = RANDOM.nextBoolean() ? OrderSide.Buy : OrderSide.Sell;
    price = BigDecimal.valueOf(RANDOM.nextLong());
    clientTimestamp = LocalDateTime.now();
    serverTimestamp = LocalDateTime.now();
    userIpAddress = "127.100.101.196";
    status = OrderStatus.ALL[RANDOM.nextInt(OrderStatus.ALL.length)];
    fills = Arrays.asList(
        new Fill(BigDecimal.valueOf(RANDOM.nextLong()), BigDecimal.valueOf(RANDOM.nextLong()),
            System.currentTimeMillis()),
        new Fill(BigDecimal.valueOf(RANDOM.nextLong()), BigDecimal.valueOf(RANDOM.nextLong()),
            System.currentTimeMillis()));
  }

  public UUID id() {
    return id;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeUTF(id.toString());
    out.writeUTF(userId);
    out.writeUTF(instrumentInstanceId);
    out.writeUTF(instrumentName != null ? instrumentName : "");
    out.writeByte(orderType.code);
    out.writeByte(side.code);
    BigDecimalUtil.writeObject(quantity, out);
    BigDecimalUtil.writeObject(remainingQuantity, out);
    BigDecimalUtil.writeObject(price, out);
    out.writeObject(clientTimestamp);
    out.writeObject(serverTimestamp);
    out.writeUTF(userIpAddress);
    out.writeByte(status.code);
    out.writeInt(fills.size());
    for (Fill fill : fills) {
      fill.writeExternal(out);
    }
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    id = UUID.fromString(in.readUTF());
    userId = in.readUTF();
    instrumentInstanceId = in.readUTF();
    String value = in.readUTF();
    instrumentName = value.isEmpty() ? null : value;
    orderType = OrderType.valueOf(in.readByte());
    side = OrderSide.valueOf(in.readByte());
    quantity = BigDecimalUtil.readObject(in);
    remainingQuantity = BigDecimalUtil.readObject(in);
    price = BigDecimalUtil.readObject(in);
    clientTimestamp = (LocalDateTime) in.readObject();
    serverTimestamp = (LocalDateTime) in.readObject();
    userIpAddress = in.readUTF();
    status = OrderStatus.valueOf(in.readByte());
    int fillsLength = in.readInt();
    List<Fill> fills = new ArrayList<>(fillsLength);
    for (int i = 0; i < fillsLength; i++) {
      Fill fill = new Fill();
      fill.readExternal(in);
      fills.add(fill);
    }
    this.fills = fills;
  }

  public byte[] toBytes() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    writeExternal(oos);
    oos.flush();
    return baos.toByteArray();
  }

  public static Order fromBytes(byte[] valBytes) throws Exception {
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(valBytes));
    Order order = new Order();
    order.readExternal(ois);
    return order;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(id, order.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", userId='" + userId + '\'' +
        ", instrumentInstanceId='" + instrumentInstanceId + '\'' +
        ", instrumentName='" + instrumentName + '\'' +
        ", quantity=" + quantity +
        ", remainingQuantity=" + remainingQuantity +
        ", orderType=" + orderType +
        ", side=" + side +
        ", price=" + price +
        ", clientTimestamp=" + clientTimestamp +
        ", serverTimestamp=" + serverTimestamp +
        ", userIpAddress='" + userIpAddress + '\'' +
        ", status=" + status +
        ", fills=" + fills +
        '}';
  }
}
