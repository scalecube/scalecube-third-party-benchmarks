package io.scalecube.benchmarks.storages.chronicle.marshallers;

import io.scalecube.benchmarks.storages.chronicle.utils.BigDecimalUtil;
import io.scalecube.benchmarks.storages.chronicle.utils.LocalDateTimeUtil;
import io.scalecube.benchmarks.storages.common.entity.Fill;
import io.scalecube.benchmarks.storages.common.entity.Order;
import io.scalecube.benchmarks.storages.common.entity.OrderSide;
import io.scalecube.benchmarks.storages.common.entity.OrderStatus;
import io.scalecube.benchmarks.storages.common.entity.OrderType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.hash.serialization.BytesReader;
import net.openhft.chronicle.hash.serialization.BytesWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OrderMarshaller implements BytesReader<Order>, BytesWriter<Order> {

  private final FillMarshaller fillMarshaller;

  public OrderMarshaller() {
    fillMarshaller = new FillMarshaller();
  }

  @NotNull
  @Override
  public Order read(Bytes in, @Nullable Order using) {
    Order order = new Order();
    order.id(new UUID(in.readLong(), in.readLong()));
    order.userId(in.readUtf8());
    order.instrumentInstanceId(in.readUtf8());
    String value = in.readUtf8();
    order.instrumentName(value == null || value.isEmpty() ? null : value);
    order.orderType(OrderType.valueOf(in.readByte()));
    order.side(OrderSide.valueOf(in.readByte()));
    order.quantity(BigDecimalUtil.readObject(in));
    order.remainingQuantity(BigDecimalUtil.readObject(in));
    order.price(BigDecimalUtil.readObject(in));
    order.clientTimestamp(LocalDateTimeUtil.readObject(in));
    order.serverTimestamp(LocalDateTimeUtil.readObject(in));
    order.userIpAddress(in.readUtf8());
    order.status(OrderStatus.valueOf(in.readByte()));
    int fillsLength = in.readInt();
    List<Fill> fills = new ArrayList<>(fillsLength);
    for (int i = 0; i < fillsLength; i++) {
      fills.add(fillMarshaller.read(in, null));
    }
    order.fills(fills);
    return order;
  }

  @Override
  public void write(Bytes out, @NotNull Order order) {
    out.writeLong(order.id().getMostSignificantBits());
    out.writeLong(order.id().getLeastSignificantBits());
    out.writeUtf8(order.userId());
    out.writeUtf8(order.instrumentInstanceId());
    out.writeUtf8(order.instrumentName() != null ? order.instrumentName() : "");
    out.writeByte((byte) order.orderType().code());
    out.writeByte((byte) order.side().code());
    BigDecimalUtil.writeObject(order.quantity(), out);
    BigDecimalUtil.writeObject(order.remainingQuantity(), out);
    BigDecimalUtil.writeObject(order.price(), out);
    LocalDateTimeUtil.writeObject(order.clientTimestamp(), out);
    LocalDateTimeUtil.writeObject(order.serverTimestamp(), out);
    out.writeUtf8(order.userIpAddress());
    out.writeByte((byte) order.status().code());
    out.writeInt(order.fills().size());
    for (Fill fill : order.fills()) {
      fillMarshaller.write(out, fill);
    }
  }
}
