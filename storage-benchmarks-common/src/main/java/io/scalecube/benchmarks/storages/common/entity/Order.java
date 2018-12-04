package io.scalecube.benchmarks.storages.common.entity;

import io.scalecube.benchmarks.storages.common.utils.BigDecimalUtil;
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

  public Order() {}

  /**
   * Generates a random order by the given order identifier.
   *
   * @param orderId order identifier
   */
  public Order(UUID orderId) {
    id = orderId;
    userId = UUID.randomUUID().toString();
    instrumentInstanceId = UUID.randomUUID().toString();
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
    fills =
        Arrays.asList(
            new Fill(
                BigDecimal.valueOf(RANDOM.nextLong()),
                BigDecimal.valueOf(RANDOM.nextLong()),
                System.currentTimeMillis()),
            new Fill(
                BigDecimal.valueOf(RANDOM.nextLong()),
                BigDecimal.valueOf(RANDOM.nextLong()),
                System.currentTimeMillis()));
  }

  public UUID id() {
    return id;
  }

  public Order id(UUID id) {
    this.id = id;
    return this;
  }

  public String userId() {
    return userId;
  }

  public Order userId(String userId) {
    this.userId = userId;
    return this;
  }

  public String instrumentInstanceId() {
    return instrumentInstanceId;
  }

  public Order instrumentInstanceId(String instrumentInstanceId) {
    this.instrumentInstanceId = instrumentInstanceId;
    return this;
  }

  public String instrumentName() {
    return instrumentName;
  }

  public Order instrumentName(String instrumentName) {
    this.instrumentName = instrumentName;
    return this;
  }

  public BigDecimal quantity() {
    return quantity;
  }

  public Order quantity(BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }

  public BigDecimal remainingQuantity() {
    return remainingQuantity;
  }

  public Order remainingQuantity(BigDecimal remainingQuantity) {
    this.remainingQuantity = remainingQuantity;
    return this;
  }

  public OrderType orderType() {
    return orderType;
  }

  public Order orderType(OrderType orderType) {
    this.orderType = orderType;
    return this;
  }

  public OrderSide side() {
    return side;
  }

  public Order side(OrderSide side) {
    this.side = side;
    return this;
  }

  public BigDecimal price() {
    return price;
  }

  public Order price(BigDecimal price) {
    this.price = price;
    return this;
  }

  public LocalDateTime clientTimestamp() {
    return clientTimestamp;
  }

  public Order clientTimestamp(LocalDateTime clientTimestamp) {
    this.clientTimestamp = clientTimestamp;
    return this;
  }

  public LocalDateTime serverTimestamp() {
    return serverTimestamp;
  }

  public Order serverTimestamp(LocalDateTime serverTimestamp) {
    this.serverTimestamp = serverTimestamp;
    return this;
  }

  public String userIpAddress() {
    return userIpAddress;
  }

  public Order userIpAddress(String userIpAddress) {
    this.userIpAddress = userIpAddress;
    return this;
  }

  public OrderStatus status() {
    return status;
  }

  public Order status(OrderStatus status) {
    this.status = status;
    return this;
  }

  public List<Fill> fills() {
    return fills;
  }

  public Order fills(List<Fill> fills) {
    this.fills = new ArrayList<>(fills);
    return this;
  }

  /**
   * Creates a new instance of this order with new order status.
   *
   * @param status order status
   * @return an order
   */
  public Order withNewStatus(OrderStatus status) {
    Order order = clone();
    order.status = status;
    return order;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeLong(id.getMostSignificantBits());
    out.writeLong(id.getLeastSignificantBits());
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
    id = new UUID(in.readLong(), in.readLong());
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

  /**
   * Converts this order to an array of bytes.
   *
   * @return array of bytes
   */
  public byte[] toBytes() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    writeExternal(oos);
    oos.flush();
    return baos.toByteArray();
  }

  /**
   * Converts the give array of bytes to an order.
   *
   * @param valBytes array of bytes
   * @return an order
   */
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
    final StringBuilder sb = new StringBuilder("Order{");
    sb.append("id=").append(id);
    sb.append(", userId='").append(userId).append('\'');
    sb.append(", instrumentInstanceId='").append(instrumentInstanceId).append('\'');
    sb.append(", instrumentName='").append(instrumentName).append('\'');
    sb.append(", quantity=").append(quantity);
    sb.append(", remainingQuantity=").append(remainingQuantity);
    sb.append(", orderType=").append(orderType);
    sb.append(", side=").append(side);
    sb.append(", price=").append(price);
    sb.append(", clientTimestamp=").append(clientTimestamp);
    sb.append(", serverTimestamp=").append(serverTimestamp);
    sb.append(", userIpAddress='").append(userIpAddress).append('\'');
    sb.append(", status=").append(status);
    sb.append(", fills=").append(fills);
    sb.append('}');
    return sb.toString();
  }

  @Override
  protected Order clone() {
    Order order = new Order();
    order.id = this.id;
    order.userId = this.userId;
    order.instrumentInstanceId = this.instrumentInstanceId;
    order.instrumentName = this.instrumentName;
    order.quantity = this.quantity;
    order.remainingQuantity = this.remainingQuantity;
    order.orderType = this.orderType;
    order.side = this.side;
    order.price = this.price;
    order.clientTimestamp = this.clientTimestamp;
    order.serverTimestamp = this.serverTimestamp;
    order.userIpAddress = this.userIpAddress;
    order.fills = this.fills;
    order.status = this.status;
    return order;
  }
}
