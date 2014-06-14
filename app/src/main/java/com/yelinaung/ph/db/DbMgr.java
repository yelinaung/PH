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

package com.yelinaung.ph.db;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Ye Lin Aung on 14/01/30.
 */
public class DbMgr {
  private DbHelper dbHelper = null;

  public DbHelper getHelper(Context context) {
    if (dbHelper == null) {
      dbHelper = OpenHelperManager.getHelper(context, DbHelper.class);
    }
    return dbHelper;
  }

  //releases the helper once usages has ended
  public void releaseHelper(DbHelper helper) {
    if (dbHelper != null) {
      OpenHelperManager.releaseHelper();
      dbHelper = null;
    }
  }
}
