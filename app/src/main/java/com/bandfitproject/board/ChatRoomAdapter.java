package com.bandfitproject.board;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bandfitproject.BandFitDataBase;
import com.bandfitproject.BusEvent;
import com.bandfitproject.BusProvider;
import com.bandfitproject.R;
import com.bandfitproject.chat.ChatActivity;
import com.bandfitproject.chat.ChatData;
import com.bandfitproject.data.BoardData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bandfitproject.login.LoginActivity.user;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    Context context;
    List<BoardData> items;
    int item_layout;

    public ChatRoomAdapter(Context context, List<BoardData> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ChatRoomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_list_item,
                parent, false);
        return new ChatRoomAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ChatRoomAdapter.ViewHolder holder, int position) {
        final BoardData item = items.get(position);
        String type = "[" + item.type + "]" ;
        holder.text_type.setText(type);
        holder.text_topic.setText(item.topic);
        holder.text_date.setText(item.date);
        //if(!item.admin.equals(user.id))
        if(!user.id.equals(item.admin)) {
            holder.btn_removeBoard.setText("나가기");
            System.out.println("나가기 테스트");
        }

        holder.btn_removeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.admin.equals(user.id))
                    BandFitDataBase.getInstance().removeBoard(item);
                else{
                    /*AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
                    alt_bld.setMessage("당신은 방장이 아닙니다.").setCancelable(
                            false).setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Action for 'Yes' Button
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    // Title for AlertDialog
                    alert.setTitle("문제가 있어요!");
                    // Icon for AlertDialog
                    alert.show();*/
                    BandFitDataBase.getInstance().outBoard(item);
                    // 처음 입장했을때, 나타나는 메세지 //
                    ChatData mChatData = new ChatData();
                    mChatData.userName = "ADMIN";
                    mChatData.time = System.currentTimeMillis();
                    mChatData.message = user.id + "님이 나갔습니다." ;
                    DatabaseReference mRef =
                            FirebaseDatabase.getInstance().getReference("boardChat").child(item.chat_room_name);
                    mRef.push().setValue(mChatData);
                }

                //notifyDataSetChanged();
                //BusProvider.getInstance().post(new BusEvent("ChatRoomActivity"));
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                String chatRoomName = item.getChat_room_name();
                intent.putExtra("chatRoomName", chatRoomName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chatroom_cardview) CardView cardView;
        @BindView(R.id.chatroom_textview_type) TextView text_type;
        @BindView(R.id.chatroom_textview_topic) TextView text_topic;
        @BindView(R.id.chatroom_textview_date) TextView text_date;
        @BindView(R.id.btn_removeBoard) Button btn_removeBoard;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
    public void addItem(BoardData add_item) {
        items.add(add_item);
        notifyItemInserted(items.size() - 1);
    }
}