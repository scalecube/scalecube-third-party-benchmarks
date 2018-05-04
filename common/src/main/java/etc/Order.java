package etc;

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Order implements Externalizable {

    private static final AtomicInteger idCnt = new AtomicInteger();

    private final int id;
    private final String userId;
    private final String instrumentInstanceId;
    private final int quantity;
    private final String orderType;
    private final BigDecimal price;
    private final LocalDateTime clientTimestamp;
    private final LocalDateTime serverTimestamp;
    private final String userIpAddress;
    private final Status status;


    public byte[] serialized() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        this.writeExternal(oos);
        oos.flush();
        return baos.toByteArray();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id);
        out.writeChars(userId);
        out.writeChars(instrumentInstanceId);
        out.writeInt(quantity);
        out.writeChars(orderType);
        out.writeInt(price.intValue());
        out.writeLong(clientTimestamp.toInstant(ZoneOffset.UTC).toEpochMilli());
        out.writeLong(serverTimestamp.toInstant(ZoneOffset.UTC).toEpochMilli());
        out.writeChars(userIpAddress);
        out.writeChars(status.name());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        // todo
    }

    public Order() {
        this.id = idCnt.getAndIncrement();
        this.userId = UUID.randomUUID().toString();
        this.instrumentInstanceId = UUID.randomUUID().toString();
        this.quantity = UUID.randomUUID().hashCode();
        this.orderType = UUID.randomUUID().toString();
        this.price = BigDecimal.ONE;
        this.clientTimestamp = LocalDateTime.now();
        this.serverTimestamp = LocalDateTime.now();
        this.userIpAddress = "127.0.0.1";
        this.status = Status.PendingVerification;
    }

    public enum Status {
        PendingVerification("Pending"),
        Approved("Approved"),
        Rejected("Rejected");

        private String statusStr;

        Status(String statusStr) {
            this.statusStr = statusStr;
        }

        public String getStatusStr() {
            return statusStr;
        }

    }

    public int getId() {
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

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "etc.Order{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", instrumentInstanceId='" + instrumentInstanceId + '\'' +
                ", quantity=" + quantity +
                ", orderType='" + orderType + '\'' +
                ", price=" + price +
                ", clientTimestamp=" + clientTimestamp +
                ", serverTimestamp=" + serverTimestamp +
                ", userIpAddress='" + userIpAddress + '\'' +
                ", status=" + status +
                '}';
    }
}
