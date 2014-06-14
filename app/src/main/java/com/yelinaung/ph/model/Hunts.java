package com.yelinaung.ph.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ye Lin Aung on 14/06/13.
 */

@DatabaseTable(tableName = "Hunts")
public class Hunts {
  @DatabaseField(generatedId = true) public int id;
  @DatabaseField private String rank;
  @DatabaseField private String title;
  @DatabaseField private String permalink;
  @DatabaseField private String votes;
  @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private User user;
  @DatabaseField private String url;
  @DatabaseField private int comment_count;
  @DatabaseField private String tagline;

  public Hunts() {
  }

  public Hunts(String rank, String title, String permalink, String votes,
      com.yelinaung.ph.model.User user, String url, int comment_count, String tagline) {
    this.rank = rank;
    this.title = title;
    this.permalink = permalink;
    this.votes = votes;
    this.user = user;
    this.url = url;
    this.comment_count = comment_count;
    this.tagline = tagline;
  }

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
