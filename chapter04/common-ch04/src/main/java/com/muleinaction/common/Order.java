package com.muleinaction.common;

public class Order implements java.io.Serializable {
  private long purchaserId;
  private long productId;
  private String status;

  public long getPurchaserId() {
      return purchaserId;
  }

  public void setPurchaserId(long purchaserId) {
      this.purchaserId = purchaserId;
  }

  public long getProductId() {
      return productId;
  }

  public void setProductId(long purchaserId) {
      this.productId = productId;
  }

  public String getStatus() {
      return status;
  }

  public void setStatus(String status) {
      this.status = status;
  }
} 
