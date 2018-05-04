import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {

  private final UUID id;
  private final String userId;
  private final String instrumentInstanceId;
  private final int quantity;
  private final String orderType;
  private final BigDecimal price;
  private final LocalDateTime clientTimestamp;
  private final LocalDateTime serverTimestamp;
  private final String userIpAddress;
  private final Status status;

  public Order(String userId, String instrumentId, int quantity, String orderType, BigDecimal price,
      LocalDateTime clientTimestamp,
      LocalDateTime serverTimestamp, String userIpAddress) {

    this.id = UUID.randomUUID();
    this.userId = userId;
    this.instrumentInstanceId = instrumentId;
    this.quantity = quantity;
    this.orderType = orderType;
    this.price = price;
    this.clientTimestamp = clientTimestamp;
    this.serverTimestamp = serverTimestamp;
    this.userIpAddress = userIpAddress;
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


  public UUID getId() {
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

}
