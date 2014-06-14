package com.yelinaung.ph.db;

/*
 * Copyright 2014 Ye Lin Aung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;
import com.yelinaung.ph.model.Hunts;

/**
 * Created by Ye Lin Aung on 14/01/30.
 */
public class HuntsDao {
  private Dao<Hunts, Integer> mHuntsDao;
  private ConnectionSource source;

  public HuntsDao(Context ctx) {
    try {
      DbMgr dbManager = new DbMgr();
      DbHelper dbHelper = dbManager.getHelper(ctx);
      mHuntsDao = dbHelper.getHuntsDao();
      source = dbHelper.getConnectionSource();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int create(Hunts hunts) {
    try {
      return mHuntsDao.create(hunts);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public List<Hunts> getAll() {
    try {
      return mHuntsDao.queryForAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void deleteAllHunts() throws SQLException {
    TableUtils.clearTable(source, Hunts.class);
  }
}
