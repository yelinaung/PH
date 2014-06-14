package yelinaung.producthunt;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;
import yelinaung.producthunt.model.Hunts;

public class MyActivity extends ActionBarActivity implements OnRefreshListener {

  @InjectView(R.id.swipe_refresh_widget) SwipeRefreshLayout mSwipeRefreshWidget;
  @InjectView(R.id.list_view) ListView mHuntListView;

  private HuntAdapter huntAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);

    ButterKnife.inject(this);

    getSupportActionBar().setTitle(R.string.app_name);

    mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2, R.color.color3,
        R.color.color4);
    mSwipeRefreshWidget.setOnRefreshListener(this);

    //Add your components

  }

  @Override protected void onResume() {
    super.onResume();
    loadData();
  }

  @Override
  public void onRefresh() {
    // TODO:Write your logic here
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
    final ArrayList<Hunts> huntsArrayList = new ArrayList<Hunts>();
    Ion.with(getApplicationContext())
        .load(Config.TODAY_URL)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            hideRefreshProgress();
            JsonArray array = result.getAsJsonArray("hunts");
            Gson gson = new Gson();
            for (int i = 0; i < array.size(); i++) {
              huntsArrayList.add(gson.fromJson(array.get(i).toString(), Hunts.class));
            }

            huntAdapter = new HuntAdapter(getApplicationContext(), huntsArrayList);
            huntAdapter.notifyDataSetChanged();
            mHuntListView.setAdapter(huntAdapter);
          }
        });
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
      holder.mTitle.setText(huntList.get(position).getTitle());
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
}
