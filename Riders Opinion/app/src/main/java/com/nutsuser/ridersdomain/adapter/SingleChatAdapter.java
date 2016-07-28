package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inscripts.Keyboards.StickerKeyboard;
import com.inscripts.custom.EmojiTextView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.database.Keys;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.pojos.SingleChatMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SingleChatAdapter extends BaseAdapter {

    private ArrayList<SingleChatMessage> messageList;
    private static final int TYPES_COUNT = 2;
    private static final int TYPE_LEFT = 0;
    private static final int TYPE_RIGHT = 1;
    private Context context;
    private static String username;
    StickerKeyboard stickerKeyboard = new StickerKeyboard();
    PrefsManager prefsManager;

    public SingleChatAdapter(Context context, ArrayList<SingleChatMessage> messages,String name) {
        messageList = messages;
        this.context = context;
        username = name;
        prefsManager= new PrefsManager(context);
        sortByDate();
    }

    @Override
    public int getViewTypeCount() {
        return TYPES_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        SingleChatMessage message = (SingleChatMessage) getItem(position);
        if (message.getIsMyMessage()) {
            return TYPE_RIGHT;
        }
        return TYPE_LEFT;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        //sortByTime();
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView timestamp,chatUsername;
        ImageView image, msgtick;
        EmojiTextView message;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (view == null) {
            if (getItemViewType(position) == TYPE_RIGHT) {
                view = LayoutInflater.from(context).inflate(R.layout.oneonone_chat_bubble_right, parent, false);
                holder.message = (EmojiTextView) view.findViewById(R.id.textViewMessage);
                holder.timestamp = (TextView) view.findViewById(R.id.textViewTime);
                holder.image = (ImageView) view.findViewById(R.id.imageViewImageMessage);
                holder.msgtick = (ImageView) view.findViewById(R.id.imageViewmessageTicks);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.oneonone_chat_bubble_left, parent, false);
                holder.message = (EmojiTextView) view.findViewById(R.id.textViewMessage);
                holder.timestamp = (TextView) view.findViewById(R.id.textViewTime);
                holder.image = (ImageView) view.findViewById(R.id.imageViewImageMessage);
                holder.msgtick = (ImageView) view.findViewById(R.id.imageViewmessageTicks);
                holder.chatUsername = (TextView) view.findViewById(R.id.chatUsername);
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SingleChatMessage message = messageList.get(position);
        String messageType = message.getMessageType();
        if (messageType.equals("12")) {
            holder.message.setVisibility(View.GONE);

            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(context).load(message.getMessage()).into(holder.image);
        } else if (messageType.equals("14") || messageType.equals("16") || messageType.equals("17")) {
            holder.image.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(message.getMessage());
        } /*else if (messageType.equals("18")) {
            holder.message.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.GONE);
			holder.message.setText(StickerKeyboard.showSticker(context, message.getMessage()));
		}*/ else {
            holder.message.setVisibility(View.VISIBLE);
//            holder.messageRight.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            String msg = message.getMessage();
           // if(!message.getFrom().endsWith(prefsManager.getCaseId())) {
            try {
                    holder.chatUsername.setText(username);

            }catch (NullPointerException e){

            }
                Log.e("isMyMsg",""+message.getIsMyMessage());
            Log.e("msg",""+msg);

            holder.message.setEmojiText(msg);
//            }else{
//                holder.message.setEmojiText(msg);
//            }

        }
        int messtick = message.getTickStatus();
        holder.msgtick.setVisibility(View.VISIBLE);
        switch (messtick) {
            case Keys.MessageTicks.sent:
                holder.msgtick.setImageResource(R.drawable.iconsent);
                break;
            case Keys.MessageTicks.deliverd:
                holder.msgtick.setImageResource(R.drawable.icondeliverd);
                break;
            case Keys.MessageTicks.read:
                holder.msgtick.setImageResource(R.drawable.iconread);
                break;
            case Keys.MessageTicks.notick:
                holder.msgtick.setVisibility(View.INVISIBLE);
                break;
            default:
                holder.msgtick.setVisibility(View.INVISIBLE);
                break;
        }

        //Log.e("getTimestamp()",message.getTimestamp());
        holder.timestamp.setText(message.getTimestamp());

        return view;
    }

    public void sortByDate() {
        Comparator<SingleChatMessage> comparator = new Comparator<SingleChatMessage>() {

            @Override
            public int compare(SingleChatMessage object1, SingleChatMessage object2) {
                String [] date2 =object2.getTimestamp().split(" ");
                String[] date1 = object1.getTimestamp().split(" ");
                return date1[0].compareToIgnoreCase(date2[0]);
            }
        };
        Collections.sort(messageList, comparator);
        notifyDataSetChanged();
    }


}
