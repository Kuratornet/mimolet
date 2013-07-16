package entity;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

  private static final long serialVersionUID = 6123072980683019086L;

  private Integer id;

  private String link;

  private String imagelink;

  private Integer status;

  private String description;

  private Integer ownerId;

  private Binding binding;

  private Size paper;

  private Integer print;

  private Integer blocksize;

  private Integer pages;

  private Date createData;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getImagelink() {
    return imagelink;
  }

  public void setImagelink(String imagelink) {
    this.imagelink = imagelink;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Integer ownerId) {
    this.ownerId = ownerId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Binding getBinding() {
    return binding;
  }

  public void setBinding(Binding binding) {
    this.binding = binding;
  }

  public Size getPaper() {
    return paper;
  }

  public void setPaper(Size paper) {
    this.paper = paper;
  }

  public Integer getPrint() {
    return print;
  }

  public void setPrint(Integer print) {
    this.print = print;
  }

  public Integer getBlocksize() {
    return blocksize;
  }

  public void setBlocksize(Integer blocksize) {
    this.blocksize = blocksize;
  }

  public Integer getPages() {
    return pages;
  }

  public void setPages(Integer pages) {
    this.pages = pages;
  }

  public void setCreateData(Date createData) {
    this.createData = createData;
  }

  public Date getCreateData() {
    return createData;
  }

  @Override
  public String toString() {
    return "Order [id=" + id + ", link=" + link + ", imagelink="
        + imagelink + ", status=" + status + ", description="
        + description + ", ownerId=" + ownerId + ", binding=" + binding
        + ", paper=" + paper + ", print=" + print + ", blocksize="
        + blocksize + ", pages=" + pages + ", createData=" + createData
        + "]";
  }
}
