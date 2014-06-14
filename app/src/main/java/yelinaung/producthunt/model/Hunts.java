package yelinaung.producthunt.model;

/**
 * Created by Ye Lin Aung on 14/06/13.
 */
public class Hunts {
  private String rank;
  private String title;
  private String permalink;
  private String votes;
  private User user;
  private String url;
  private int comment_count;
  private String tagline;

  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPermalink() {
    return permalink;
  }

  public void setPermalink(String permalink) {
    this.permalink = permalink;
  }

  public String getVotes() {
    return votes;
  }

  public void setVotes(String votes) {
    this.votes = votes;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getComments() {
    return comment_count;
  }

  public void setComments(int comments) {
    this.comment_count = comments;
  }

  public String getTagline() {
    return tagline;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
  }
}
