package com.yelinaung.ph.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ye Lin Aung on 14/06/13.
 */

@DatabaseTable(tableName = "User")
public class User {
  @DatabaseField(generatedId = true) public int id;
  @DatabaseField private String username;
  @DatabaseField private String name;

  public User() {
  }

  public User(String username, String name) {
    this.username = username;
    this.name = name;
  }
}
