package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inscripts.custom.EmojiTextView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.ChatroomChatMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChatroomChatAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ChatroomChatMessage> messagelist;
	private static final int TYPES_COUNT = 2;
	private static final int TYPE_LEFT = 0;
	private static final int TYPE_RIGHT = 1;

	public ChatroomChatAdapter(Context context, ArrayList<ChatroomChatMessage> messages) {
		this.context = context;
		messagelist = messages;
	}

	@Override
	public int getViewTypeCount() {
		return TYPES_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		ChatroomChatMessage message = (ChatroomChatMessage) getItem(position);
		if (message.getIsMyMessage()) {
			return TYPE_RIGHT;
		}
		return TYPE_LEFT;
	}

	@Override
	public int getCount() {
		return messagelist.size();
	}

	@Override
	public Object getItem(int position) {
		return messagelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder {
		TextView timestamp, userName;
		ImageView image;
		EmojiTextView message;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (view == null) {
			if (getItemViewType(position) == TYPE_RIGHT) {
				view = LayoutInflater.from(context).inflate(R.layout.chatroom_chat_bubble_right, parent, false);
				holder.message = (EmojiTextView) view.findViewById(R.id.textViewMessage);
				holder.timestamp = (TextView) view.findViewById(R.id.textViewTime);
				holder.userName = (TextView) view.findViewById(R.id.textViewUserName);
				holder.image = (ImageView) view.findViewById(R.id.imageViewImageMessage);
			} else {
				view = LayoutInflater.from(context).inflate(R.layout.chatroom_chat_bubble_left, parent, false);
				holder.message = (EmojiTextView) view.findViewById(R.id.textViewMessage);
				holder.timestamp = (TextView) view.findViewById(R.id.textViewTime);
				holder.userName = (TextView) view.findViewById(R.id.textViewUserName);
				holder.image = (ImageView) view.findViewById(R.id.imageViewImageMessage);
			}
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		ChatroomChatMessage message = messagelist.get(position);
		String messagetype = message.getMessagetype();
		if (messagetype.equals("12")) {
			holder.message.setVisibility(View.GONE);
			holder.image.setVisibility(View.VISIBLE);
			Picasso.with(context).load(message.getMessage()).into(holder.image);
		} else if (messagetype.equals("14") || messagetype.equals("16") || messagetype.equals("17")) {
			holder.message.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.GONE);
			holder.message.setText(message.getMessage());
		} /*else if (messagetype.equals("18")) {
			holder.message.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.GONE);
			holder.message.setText(StickerKeyboard.showSticker(context, message.getMessage()));
		}*/ else {
			holder.message.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.GONE);
			holder.message.setEmojiText(message.getMessage());
		}

		holder.timestamp.setText(message.getTimestamp());
		holder.userName.setText(message.getUserName());
		return view;
	}

}
