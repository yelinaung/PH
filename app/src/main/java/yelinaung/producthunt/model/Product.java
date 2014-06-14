package yelinaung.producthunt.model;

import java.util.List;

/**
 * Created by Ye Lin Aung on 14/06/13.
 */
public class Product {

  private String status;

  public List<Hunts> getHunts() {
    return hunts;
  }

  public void setHunts(List<Hunts> hunts) {
    this.hunts = hunts;
  }

  private List<Hunts> hunts;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
