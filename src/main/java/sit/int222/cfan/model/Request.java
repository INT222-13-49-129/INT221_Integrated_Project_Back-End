package sit.int222.cfan.model;


public class Request {

  private long requestid;
  private String status;
  private long userUserid;


  public long getRequestid() {
    return requestid;
  }

  public void setRequestid(long requestid) {
    this.requestid = requestid;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public long getUserUserid() {
    return userUserid;
  }

  public void setUserUserid(long userUserid) {
    this.userUserid = userUserid;
  }

}
