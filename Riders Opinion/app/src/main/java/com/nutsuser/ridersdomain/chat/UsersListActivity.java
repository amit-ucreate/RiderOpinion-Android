package com.nutsuser.ridersdomain.chat;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.utils.Logger;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.BuddylistAdapter;
import com.nutsuser.ridersdomain.database.Keys;
import com.nutsuser.ridersdomain.database.SharedPreferenceHelper;
import com.nutsuser.ridersdomain.web.pojos.SingleUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class UsersListActivity extends ActionBarActivity implements OnItemClickListener {

	private ListView usersListView;
	private static BuddylistAdapter adapter;

	/* List for the simple adapter */
	private static ArrayList<String> list;

	/* For mapping userId and name */
	private static ArrayList<SingleUser> usersList = new ArrayList<SingleUser>();

	CometChat cometchat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_list);
		SharedPreferenceHelper.initialize(UsersListActivity.this);
//		getSupportActionBar().setTitle("User list");

		usersListView = (ListView) findViewById(R.id.listViewUsers);
		list = new ArrayList<String>();

		usersListView.setOnItemClickListener(this);
		populateList();
		adapter = new BuddylistAdapter(this, usersList);
		usersListView.setAdapter(adapter);

		cometchat = CometChat.getInstance(getApplicationContext(),
                SharedPreferenceHelper.get(Keys.SharedPreferenceKeys.API_KEY));
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (list.size() <= 0) {
			populateList();
		}
	}

	public static void populateList() {
		try {
			if (null != list && null != usersList && null != adapter) {
				JSONObject onlineUsers;

				if (SharedPreferenceHelper.contains(Keys.SharedPreferenceKeys.USERS_LIST)) {
					onlineUsers = new JSONObject(SharedPreferenceHelper.get(Keys.SharedPreferenceKeys.USERS_LIST));
				} else {
					onlineUsers = new JSONObject();
				}

				Iterator<String> keys = onlineUsers.keys();
				list.clear();
				usersList.clear();
				while (keys.hasNext()) {
					JSONObject user = onlineUsers.getJSONObject(keys.next().toString());
					String username = user.getString("n");
					list.add(username);
					String channel = "";
					if (user.has("ch")) {
						channel = user.getString("ch");
					}
					usersList.add(new SingleUser(username, user.getInt("id"), user.getString("m"), user.getString("s"),
							channel));
				}
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	//	SingleUser user = usersList.get(arg2);

//		Intent intent = new Intent(this, SampleSingleChatActivity.class);
//		intent.putExtra("user_id", user.getId());
//		intent.putExtra("user_name", user.getName());
//		intent.putExtra("channel", user.getChannel());
//		startActivity(intent);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.users_list, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		switch (id) {
//		case R.id.action_unblock_user:
//			Intent intent = new Intent(getApplicationContext(), UnblockUser.class);
//			startActivity(intent);
//			break;
//		case R.id.action_broadcast_message:
//			sendBroadcastMessage();
//			break;
//		case R.id.action_createuser:
//			adduser();
//			break;
//		case R.id.action_removeuser:
//			removeuser();
//			break;
//		case R.id.action_updateuser:
//			updateuser();
//			break;
//		case R.id.action_addFriend:
//			addFriends();
//			break;
//		case R.id.action_removefriend:
//			removefriends();
//			break;
//
//		default:
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	private void sendBroadcastMessage() {
		JSONArray aa = new JSONArray();
		aa.put(1);
		aa.put(2);
		aa.put(3);
		CometChat cometchat = CometChat.getInstance(getApplicationContext(),
				SharedPreferenceHelper.get(Keys.SharedPreferenceKeys.API_KEY));

		cometchat.broadcastMessage("HI", aa, new Callbacks() {

			@Override
			public void successCallback(JSONObject response) {
				Logger.debug("broadcastMessage success =" + response);
			}

			@Override
			public void failCallback(JSONObject response) {
				Logger.debug("broadcastMessage fail =" + response);
			}
		});
	}

	private void removefriends() {
		try {
			JSONArray aa = new JSONArray();
			aa.put(1);
			aa.put(2);
			aa.put(3);
			aa.put(4);


			cometchat.removeFriends(aa, new Callbacks() {

				@Override
				public void successCallback(JSONObject response) {
				}

				@Override
				public void failCallback(JSONObject response) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addFriends() {
		try {
			JSONArray aa = new JSONArray();
			aa.put(5);
			aa.put(2);
			aa.put(3);
			aa.put(4);


			cometchat.addFriends(aa, new Callbacks() {

				@Override
				public void successCallback(JSONObject response) {

				}

				@Override
				public void failCallback(JSONObject response) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateuser() {

		cometchat.updateUser("", "123", null, "abc.com", null, null, false, new Callbacks() {

			@Override
			public void successCallback(JSONObject response) {
				Logger.debug("success of update user " + response);
			}

			@Override
			public void failCallback(JSONObject response) {
				Logger.debug("fail of update user " + response);
			}
		});
	}

	private void removeuser() {

		cometchat.removeUser("6", new Callbacks() {

			@Override
			public void successCallback(JSONObject response) {
				Logger.error("suces remove " + response);
			}

			@Override
			public void failCallback(JSONObject response) {
				Logger.error("fail remove user " + response);
			}
		});
	}

	private void adduser() {

		String[] projection = new String[] { MediaColumns.DATA, };

		Uri videos = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Cursor cur = managedQuery(videos, projection, "", null, "");

		final ArrayList<String> imagesPath = new ArrayList<String>();
		if (cur.moveToFirst()) {
			int dataColumn = cur.getColumnIndex(MediaColumns.DATA);
			do {
				imagesPath.add(cur.getString(dataColumn));
			} while (cur.moveToNext());
		}
		cometchat.createUser("abc333", "pwd", "abcd", "", new File(imagesPath.get(2)), new Callbacks() {

			@Override
			public void successCallback(JSONObject response) {
				Logger.debug("success add user " + response);
			}

			@Override
			public void failCallback(JSONObject response) {
				Logger.debug("failed add user " + response);
			}
		});

	}

}