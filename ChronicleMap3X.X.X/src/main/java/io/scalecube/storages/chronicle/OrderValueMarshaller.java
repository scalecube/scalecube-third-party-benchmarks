package io.scalecube.storages.chronicle;

import io.scalecube.storages.common.entity.Fill;
import io.scalecube.storages.common.entity.Order;
import net.openhft.chronicle.hash.serialization.BytesReader;
import net.openhft.chronicle.hash.serialization.BytesWriter;

public class OrderValueMarshaller extends Order.Marshaller implements BytesReader<Order>,
    BytesWriter<Order> {

  public OrderValueMarshaller() {
    super(new Fill.Marshaller());
  }
}
