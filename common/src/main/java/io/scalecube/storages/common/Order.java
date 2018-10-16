package io.scalecube.storages.common;

import static io.scalecube.storages.common.Order.OrderStatus.Accepted;

//import com.eatthepath.uuid.FastUUID;
//import com.fasterxml.uuid.Generators;
//import com.fasterxml.uuid.impl.TimeBasedGenerator;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Order implements Externalizable {

  private UUID id;
  private String userId;
  private String instrumentInstanceId;
  private String instrumentName;
  private BigDecimal quantity;
  private BigDecimal remainingQuantity;
  private String orderType;
  private String side;
  private BigDecimal price;
  private LocalDateTime clientTimestamp;
  private LocalDateTime serverTimestamp;
  private String userIpAddress;
  private OrderStatus status;
  private List<Fill> fills;

  public Order(int ignore) {
    id = UUID.randomUUID();
    userId = "01234567-8901-2345-6789-012345678901";
    instrumentInstanceId = "01234567-8901-2345-6789-012345678901";
    instrumentName = "BTC";
    quantity = BigDecimal.valueOf(Long.MAX_VALUE);
    remainingQuantity = BigDecimal.valueOf(Long.MAX_VALUE);
    orderType = "orderType";
    side = "Side";
    price = BigDecimal.valueOf(Long.MAX_VALUE);
    clientTimestamp = LocalDateTime.now();
    serverTimestamp = LocalDateTime.now();
    userIpAddress = "1234:5678:9012:3456:7890:1234:5678:9012";
    status = Accepted;
    fills = Arrays.asList(
        new Fill(BigDecimal.valueOf(Long.MAX_VALUE), BigDecimal.valueOf(Long.MAX_VALUE), System.currentTimeMillis()),
        new Fill(BigDecimal.valueOf(Long.MAX_VALUE), BigDecimal.valueOf(Long.MAX_VALUE), System.currentTimeMillis()));
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
    Order order = new Order(0);
    order.readExternal(ois);
    return order;
  }

  private Order(Builder builder) {
    id = builder.id;
    userId = builder.userId;
    instrumentInstanceId = builder.instrumentInstanceId;
    instrumentName = builder.instrumentName;
    quantity = builder.quantity;
    remainingQuantity = builder.remainingQuantity != null ? builder.remainingQuantity : quantity;
    orderType = builder.orderType;
    side = builder.side;
    price = builder.price;
    clientTimestamp = builder.clientTimestamp;
    serverTimestamp = builder.serverTimestamp;
    userIpAddress = builder.userIpAddress;
    status = builder.status;
    fills = new ArrayList<>(builder.fills);
  }

  public UUID id() {
    return id;
  }

  public String userId() {
    return userId;
  }

  public String instrumentInstanceId() {
    return instrumentInstanceId;
  }

  public String instrumentName() {
    return instrumentName;
  }

  public BigDecimal quantity() {
    return quantity;
  }

  public BigDecimal remainingQuantity() {
    return remainingQuantity;
  }

  public String orderType() {
    return orderType;
  }

  public String side() {
    return side;
  }

  public BigDecimal price() {
    return price;
  }

  public List<Fill> fills() {
    return Collections.unmodifiableList(fills);
  }

  public LocalDateTime clientTimestamp() {
    return clientTimestamp;
  }

  public LocalDateTime serverTimestamp() {
    return serverTimestamp;
  }

  public String userIpAddress() {
    return userIpAddress;
  }

  public OrderStatus status() {
    return status;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
//    out.writeUTF(FastUUID.toString(id));
    out.writeUTF(id.toString());
    out.writeUTF(userId);
    out.writeUTF(instrumentInstanceId);
    out.writeObject(instrumentName);
    out.writeUTF(orderType);
    out.writeUTF(side);
    out.writeObject(quantity);
    out.writeObject(remainingQuantity);
    out.writeObject(price);
    out.writeObject(clientTimestamp);
    out.writeObject(serverTimestamp);
    out.writeUTF(userIpAddress);
    out.writeUTF(status.name());
    out.writeObject(fills);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//    id = FastUUID.parseUUID(in.readUTF());
    id = UUID.fromString(in.readUTF());
    userId = in.readUTF();
    instrumentInstanceId = in.readUTF();
    instrumentName = (String) in.readObject();
    orderType = in.readUTF();
    side = in.readUTF();
    quantity = (BigDecimal) in.readObject();
    remainingQuantity = (BigDecimal) in.readObject();
    price = (BigDecimal) in.readObject();
    clientTimestamp = (LocalDateTime) in.readObject();
    serverTimestamp = (LocalDateTime) in.readObject();
    userIpAddress = in.readUTF();
    status = OrderStatus.valueOf(in.readUTF());
    fills = (List<Fill>) in.readObject();
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
//    sb.append("id=").append(FastUUID.toString(id));
    sb.append("id=").append(id);
    sb.append(", userId='").append(userId).append('\'');
    sb.append(", instrumentInstanceId='").append(instrumentInstanceId).append('\'');
    sb.append(", instrumentName='").append(instrumentName).append('\'');
    sb.append(", quantity=").append(quantity);
    sb.append(", remainingQuantity=").append(remainingQuantity);
    sb.append(", orderType='").append(orderType).append('\'');
    sb.append(", side='").append(side).append('\'');
    sb.append(", price=").append(price);
    sb.append(", clientTimestamp=").append(clientTimestamp);
    sb.append(", serverTimestamp=").append(serverTimestamp);
    sb.append(", userIpAddress='").append(userIpAddress).append('\'');
    sb.append(", status=").append(status);
    sb.append(", fills=").append(fills);
    sb.append('}');
    return sb.toString();
  }

  public static Builder from(Order order) {
    return new Builder(order.id, order.status)
        .clientTimestamp(order.clientTimestamp)
        .instrumentInstanceId(order.instrumentInstanceId)
        .instrumentName(order.instrumentName)
        .orderType(order.orderType)
        .price(order.price)
        .quantity(order.quantity)
        .remainingQuantity(order.remainingQuantity)
        .serverTimestamp(order.serverTimestamp)
        .side(order.side)
        .userId(order.userId)
        .userIpAddress(order.userIpAddress)
        .fills(order.fills);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
//    private static final TimeBasedGenerator GENERATOR = Generators.timeBasedGenerator();

    private UUID id;
    private String userId;
    private String instrumentInstanceId;
    private String instrumentName;
    private BigDecimal quantity;
    private BigDecimal remainingQuantity;
    private String orderType;
    private String side;
    private BigDecimal price;
    private LocalDateTime clientTimestamp;
    private LocalDateTime serverTimestamp;
    private String userIpAddress;
    private OrderStatus status;
    private List<Fill> fills = new ArrayList<>();

    private Builder() {

//      this.id = GENERATOR.generate();
      this.id = UUID.randomUUID();
      this.status = Accepted;
    }

    private Builder(UUID id, OrderStatus status) {
      this.id = id;
      this.status = status;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder instrumentInstanceId(String instrumentInstanceId) {
      this.instrumentInstanceId = instrumentInstanceId;
      return this;
    }

    public Builder instrumentName(String instrumentName) {
      this.instrumentName = instrumentName;
      return this;
    }

    public Builder quantity(BigDecimal quantity) {
      this.quantity = quantity;
      return this;
    }

    public Builder remainingQuantity(BigDecimal remainingQuantity) {
      this.remainingQuantity = remainingQuantity;
      return this;
    }

    public Builder orderType(String orderType) {
      this.orderType = orderType;
      return this;
    }

    public Builder side(String side) {
      this.side = side;
      return this;
    }

    public Builder price(BigDecimal price) {
      this.price = price;
      return this;
    }

    public Builder clientTimestamp(LocalDateTime clientTimestamp) {
      this.clientTimestamp = clientTimestamp;
      return this;
    }

    public Builder serverTimestamp(LocalDateTime serverTimestamp) {
      this.serverTimestamp = serverTimestamp;
      return this;
    }

    public Builder userIpAddress(String userIpAddress) {
      this.userIpAddress = userIpAddress;
      return this;
    }

    public Builder status(OrderStatus status) {
      if (!status.transitionAllowed(this.status)) {
        throw new IllegalStateException(
            "Invalid status transition: " + this.status + " -> " + status);
      }
      this.status = status;
      return this;
    }

    public Builder fills(List<Fill> fills) {
      if (fills != null) {
        this.fills.addAll(fills);
      }
      return this;
    }

    public Builder fill(BigDecimal fillPrice, BigDecimal fillQuantity, long timestamp) {
      this.fills.add(new Fill(fillPrice, fillQuantity, timestamp));
      return this;
    }

    public Order build() {
      return new Order(this);
    }
  }

  public static class Fill implements Externalizable {
    private BigDecimal price;
    private BigDecimal quantity;
    private long timestamp;

    public Fill() {}

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
      out.writeObject(price);
      out.writeObject(quantity);
      out.writeLong(timestamp);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
      price = (BigDecimal) in.readObject();
      quantity = (BigDecimal) in.readObject();
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

  public enum OrderStatus {
    Accepted {
      @Override
      public boolean transitionAllowed(OrderStatus from) {
        return from == null;
      }
    },
    Validated {
      @Override
      public boolean transitionAllowed(OrderStatus from) {
        return from == Accepted;
      }
    },
    New {
      @Override
      public boolean transitionAllowed(OrderStatus from) {
        return from == Validated;
      }
    },
    PartiallyFilled {
      @Override
      public boolean transitionAllowed(OrderStatus from) {
        return from == New || from == Validated || from == PartiallyFilled;
      }
    },
    Filled {
      @Override
      public boolean transitionAllowed(OrderStatus from) {
        return from == New || from == Validated || from == PartiallyFilled;
      }
    },
    Cancelled {
      @Override
      public boolean transitionAllowed(OrderStatus from) {
        return from == Accepted || from == Validated || from == New || from == PartiallyFilled;
      }
    },
    Rejected {
      @Override
      public boolean transitionAllowed(OrderStatus from) {
        return from == Accepted;
      }
    };

    public abstract boolean transitionAllowed(OrderStatus from);
  }

}

