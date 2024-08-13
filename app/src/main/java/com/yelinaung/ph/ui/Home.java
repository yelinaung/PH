package com.yelinaung.ph.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.yelinaung.ph.Config;
import com.yelinaung.ph.db.HuntsDao;
import com.yelinaung.ph.model.Hunts;
import java.sql.SQLException;
import java.util.ArrayList;
import yelinaung.producthunt.R;

import static com.yelinaung.ph.Config.PH_URL;
import static com.yelinaung.ph.utils.NetUtils.isOnline;

public class Home extends ActionBarActivity implements OnRefreshListener {

  @InjectView(R.id.swipe_refresh_widget) SwipeRefreshLayout mSwipeRefreshWidget;
  @InjectView(R.id.list_view) ListView mHuntListView;

  private HuntAdapter huntAdapter;
  private HuntsDao huntsDao;
  private ArrayList<Hunts> huntsList = new ArrayList<Hunts>();
  private Context mContext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);

    this.huntsDao = new HuntsDao(getApplicationContext());
    this.mContext = getApplicationContext();

    ButterKnife.inject(this);

    getSupportActionBar().setTitle(R.string.app_name);

    mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2, R.color.color3,
        R.color.color4);
    mSwipeRefreshWidget.setOnRefreshListener(this);

    //Add your components

    mHuntListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, final int position,
          long id) {
        AlertDialog.Builder ab = new AlertDialog.Builder(Home.this).setTitle(R.string.go_to)
            .setItems(R.array.links, new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                  case 0:
                    Intent permalLinkIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(PH_URL + huntsList.get(position).getPermalink()));
                    startActivity(permalLinkIntent);
                    break;
                  case 1:
                    Intent urlIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse(huntsList.get(position).getUrl()));
                    startActivity(urlIntent);
                    break;
                }
              }
            });
        Dialog d = ab.create();
        d.show();

        showCustomDialog(d);
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();

    loadData();

    huntsList = (ArrayList<Hunts>) huntsDao.getAll();
    huntAdapter = new HuntAdapter(Home.this, huntsList);
    mHuntListView.setAdapter(huntAdapter);
  }

  @Override
  public void onRefresh() {
    //Call hideRefreshProgress(); to notify the widget that refresh state has ended
    loadData();
  }

  /**
   * It shows the SwipeRefreshLayout progress
   */
  public void showRefreshProgress() {
    mSwipeRefreshWidget.setRefreshing(true);
  }

  /**
   * It shows the SwipeRefreshLayout progress
   */
  public void hideRefreshProgress() {
    mSwipeRefreshWidget.setRefreshing(false);
  }

  /**
   * Enables swipe gesture
   */
  public void enableSwipe() {
    mSwipeRefreshWidget.setEnabled(true);
  }

  /**
   * Disables swipe gesture.
   */
  public void disableSwipe() {
    mSwipeRefreshWidget.setEnabled(false);
  }

  private void loadData() {
    showRefreshProgress();
    if (isOnline(Home.this)) {
      huntsList.clear();
      try {
        huntsDao.deleteAllHunts();
      } catch (SQLException e) {
        e.printStackTrace();
      }

      Ion.with(getApplicationContext())
          .load(Config.TODAY_URL)
          .asJsonObject()
          .setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
              try {
                if (e != null) throw e;

                hideRefreshProgress();
                JsonArray array = result.getAsJsonArray("hunts");
                Gson gson = new Gson();
                for (int i = 0; i < array.size(); i++) {
                  Hunts h = gson.fromJson(array.get(i).toString(), Hunts.class);
                  huntsList.add(h);
                  huntsDao.create(h);
                }
                huntAdapter.notifyDataSetChanged();
                mHuntListView.setAdapter(huntAdapter);
              } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(Home.this, R.string.cannot_connect, Toast.LENGTH_SHORT).show();
                hideRefreshProgress();
              }
            }
          });
    } else {
      Toast.makeText(Home.this, R.string.cannot_connect, Toast.LENGTH_SHORT).show();
      hideRefreshProgress();
    }
  }

  public static class HuntAdapter extends ArrayAdapter<Hunts> {

    ArrayList<Hunts> huntList;
    Context mContext;

    public HuntAdapter(Context context, ArrayList<Hunts> objects) {
      super(context, R.layout.hunt_row, objects);
      this.mContext = context;
      this.huntList = objects;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      final ViewHolder holder;
      LayoutInflater inflater = LayoutInflater.from(mContext);
      if (convertView != null) {
        holder = (ViewHolder) convertView.getTag();
      } else {
        convertView = inflater.inflate(R.layout.hunt_row, parent, false);
        holder = new ViewHolder(convertView);
        assert convertView != null;
        convertView.setTag(holder);
      }

      holder.mVotePoints.setText(huntList.get(position).getVotes());

      String title = huntList.get(position).getTitle();
//      SpannableString content = new SpannableString(title);
//      content.setSpan(new UnderlineSpan(), 0, title.length(), 0);

      holder.mTitle.setText(title);
      holder.mTagLine.setText(huntList.get(position).getTagline());
      holder.mCommentCount.setText(huntList.get(position).getComments() + " comments");

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.vote_points) TextView mVotePoints;
      @InjectView(R.id.title) TextView mTitle;
      @InjectView(R.id.tag_line) TextView mTagLine;
      @InjectView(R.id.comment_count) TextView mCommentCount;

      public ViewHolder(View view) {
        ButterKnife.inject(this, view);
      }
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    EasyTracker.getInstance(Home.this).activityStart(Home.this);  // Add this method.
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.reset(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
      case R.id.action_about:
        showAbout();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showAbout() {
    PackageManager pm = mContext.getPackageManager();
    String packageName = mContext.getPackageName();
    String versionName;
    try {
      assert pm != null;
      PackageInfo info = pm.getPackageInfo(packageName, 0);
      versionName = info.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      versionName = "";
    }
    AlertDialog.Builder b = new AlertDialog.Builder(Home.this).setTitle(R.string.about)
        .setMessage(new SpannableStringBuilder().append(
            Html.fromHtml(getString(R.string.about_body, versionName))));
    Dialog d = b.show();
    showCustomDialog(d);
  }

  private void showCustomDialog(Dialog d) {
    int newCodeViewId =
        d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
    View newCodeDivider = d.findViewById(newCodeViewId);
    newCodeDivider.setBackgroundColor(getResources().getColor(R.color.theme_color));
    int newCodeTextViewId =
        d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
    TextView newCodeTv = (TextView) d.findViewById(newCodeTextViewId);
    newCodeTv.setTextColor(getResources().getColor(R.color.theme_color));
  }
}

