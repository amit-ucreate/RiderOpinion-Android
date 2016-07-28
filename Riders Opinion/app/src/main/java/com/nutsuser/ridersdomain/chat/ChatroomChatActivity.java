package com.nutsuser.ridersdomain.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.inscripts.Keyboards.SmileyKeyBoard;
import com.inscripts.Keyboards.StickerKeyboard;
import com.inscripts.adapters.EmojiGridviewImageAdapter;
import com.inscripts.adapters.StickerGridviewImageAdapter;
import com.inscripts.cometchat.sdk.CometChatroom;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.utils.Logger;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.ChatroomChatAdapter;
import com.nutsuser.ridersdomain.database.DatabaseHandler;
import com.nutsuser.ridersdomain.database.Keys;
import com.nutsuser.ridersdomain.database.SharedPreferenceHelper;
import com.nutsuser.ridersdomain.database.Utils;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.pojos.ChatroomChatMessage;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ChatroomChatActivity extends ActionBarActivity implements EmojiGridviewImageAdapter.EmojiClickInterface {

    private String chatroomName, chatroomId;
    private EditText messageField;
    private Button sendbtn;
    private CometChatroom cometChatroom;
    private ArrayList<ChatroomChatMessage> messageList;
    private ChatroomChatAdapter adapter;
    private ListView chatListView;
    private BroadcastReceiver customReceiver;
    private static Uri fileUri;
    private SmileyKeyBoard smiliKeyBoard;
    private ImageButton smilieyButton, stickerButton;
    private StickerKeyboard stickerKeyboard;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.sdvFriendImage)
    SimpleDraweeView sdvFriendImage;
    private PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_chat);
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prefsManager= new PrefsManager(this);
        cometChatroom = CometChatroom.getInstance(getApplicationContext());
        Intent intent = getIntent();

        if (intent.hasExtra("cName")) {
            chatroomName = intent.getStringExtra("cName");
            Log.e("chatroomName ",chatroomName);
            tvTitleToolbar.setText(chatroomName.toUpperCase());
        }
        if (intent.hasExtra("chatroomid")) {
           chatroomId = intent.getStringExtra("chatroomid");

            Log.e("chatroomId ",chatroomId+" : "+cometChatroom.isSubscribedToChatroom(chatroomId));

        }


      //  getSupportActionBar().setTitle(chatroomName);

        messageField = (EditText) findViewById(R.id.editTextChatMessage);
        sendbtn = (Button) findViewById(R.id.buttonSendMessage);


        chatListView = (ListView) findViewById(R.id.listViewChatMessages);
        messageList = new ArrayList<>();
        DatabaseHandler helper = new DatabaseHandler(this);
        messageList = helper.getAllChatroomMessage(
                Long.parseLong(prefsManager.getCaseId()), Long.parseLong(chatroomId));
        adapter = new ChatroomChatAdapter(this, messageList);
        chatListView.setAdapter(adapter);

        smilieyButton = (ImageButton) findViewById(R.id.buttonSendSmiley);
        stickerButton = (ImageButton) findViewById(R.id.buttonSendSticker);
        smiliKeyBoard = new SmileyKeyBoard();
        smiliKeyBoard.enable(this, this, R.id.footer_for_emoticons, messageField);
        final RelativeLayout chatFooter = (RelativeLayout) findViewById(R.id.relativeBottomArea);
        smiliKeyBoard.checkKeyboardHeight(chatFooter);
        smiliKeyBoard.enableFooterView(messageField);

        stickerKeyboard = new StickerKeyboard();
        stickerKeyboard.enable(this, new StickerGridviewImageAdapter.StickerClickInterface() {
            @Override
            public void getClickedSticker(int gridviewItemPosition) {
                final String data = stickerKeyboard.getClickedSticker(gridviewItemPosition);
                cometChatroom.sendSticker(data, new Callbacks() {

                    @Override
                    public void successCallback(JSONObject response) {
                        try {
                            ChatroomChatMessage newmessage = new ChatroomChatMessage(response.getString("id"), data,
                                    Utils.convertTimestampToDate(System.currentTimeMillis()), "Me :", true, "18",
                                    prefsManager.getCaseId(), chatroomId);
                            DatabaseHandler helper = new DatabaseHandler(getApplicationContext());
                            helper.insertChatroomMessage(newmessage);
                            addMessage(newmessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failCallback(JSONObject response) {

                    }
                });
            }
        }, R.id.footer_for_emoticons, messageField);
        stickerKeyboard.checkKeyboardHeight(chatFooter);
        //StickerKeyboard.setStickerSize(400);
        stickerKeyboard.enableFooterView(messageField);

        sendbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                sendMessage();
            }
        });

        smilieyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                smiliKeyBoard.showKeyboard(chatFooter);
            }
        });

        stickerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerKeyboard.showKeyboard(chatFooter);
            }
        });

        customReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra("Newmessage")) {
                    String message = intent.getStringExtra("Message");
                    String userName = intent.getStringExtra("from") + " :";
                    String messageId = intent.getStringExtra("message_id");
                    String time = Utils.convertTimestampToDate(Utils.correctTimestamp(Long.parseLong(intent
                            .getStringExtra("time"))));
                    String from = intent.getStringExtra("fromid");
                    String to;
                    String messagetype = intent.getStringExtra("message_type");

                    if (intent.hasExtra("to")) {
                        to = intent.getStringExtra("to");
                    } else {
                        to = prefsManager.getCaseId();
                    }
                    boolean ismyMessage = intent.getBooleanExtra("selfmessage", false);
                    ChatroomChatMessage newmessage;
                    if (intent.hasExtra("imageMessage")) {
                        if (intent.hasExtra("myphoto")) {
                            newmessage = new ChatroomChatMessage(messageId, message, time, userName, true,
                                    messagetype, from, to);
                        } else {
                            newmessage = new ChatroomChatMessage(messageId, message, time, userName, false,
                                    messagetype, from, to);
                        }
                    } else if (intent.hasExtra("videoMessage")) {
                        if (intent.hasExtra("myvideo")) {
                            newmessage = new ChatroomChatMessage(messageId, message, time, userName, true,
                                    messagetype, from, to);
                        } else {
                            newmessage = new ChatroomChatMessage(messageId, message, time, userName, false,
                                    messagetype, from, to);
                        }
                    } else {
                        if (ismyMessage) {
                            newmessage = new ChatroomChatMessage(messageId, message, time, userName, true,
                                    messagetype, from, to);
                        } else {
                            newmessage = new ChatroomChatMessage(messageId, message, time, userName, false,
                                    messagetype, from, to);
                        }
                    }
                    addMessage(newmessage);
                }
            }

        };
    }

    @Override
    public void getClickedEmoji(int gridviewItemPosition) {
        smiliKeyBoard.getClickedEmoji(gridviewItemPosition);
    }

    private void sendMessage() {
        final String message = messageField.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            messageField.setText("");
            /*
			 * Send message to active chatroom.
			 */
            cometChatroom.sendMessage(message, new Callbacks() {

                @Override
                public void successCallback(JSONObject response) {
                    Log.e("send response = ", ""+ response);
                    Toast.makeText(ChatroomChatActivity.this,""+ response, Toast.LENGTH_SHORT).show();
                    try {
                        ChatroomChatMessage newmessage = new ChatroomChatMessage(response.getString("id"), message,
                                Utils.convertTimestampToDate(System.currentTimeMillis()), "Me :", true, "10",
                                prefsManager.getCaseId(), chatroomId);
                        DatabaseHandler helper = new DatabaseHandler(getApplicationContext());
                        helper.insertChatroomMessage(newmessage);
                        addMessage(newmessage);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failCallback(JSONObject response) {
                    Log.e("send message fail = ", ""+ response);
                    Toast.makeText(ChatroomChatActivity.this,""+ response, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addMessage(ChatroomChatMessage newmessage) {
        if (newmessage != null) {
            boolean duplicate = false;
            for (ChatroomChatMessage msg : messageList) {
                if (msg != null) {
                    if (msg.getMessage_id().equals(newmessage.getMessage_id())) {
                        duplicate = true;
                        break;
                    }
                }
            }
            if (!duplicate) {
                messageList.add(newmessage);
                adapter.notifyDataSetChanged();
                chatListView.setSelection(messageList.size() - 1);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (customReceiver != null) {
            registerReceiver(customReceiver, new IntentFilter("Chatroom_message"));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (customReceiver != null) {
            unregisterReceiver(customReceiver);
        }
    }


    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));

    }
}
