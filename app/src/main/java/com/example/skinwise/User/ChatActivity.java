package com.example.skinwise.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.MessagesAdapter;
import com.example.skinwise.Model.Messages;
import com.example.skinwise.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends Fragment {

    private String dermId;
    private String dermName;
    private FirebaseAuth firebaseAuth;
    private ImageButton imageBack,imageInfo;
    private TextView textName;
    private RecyclerView chatRecyclerView;
    private ProgressBar progressBar;
    private FrameLayout layoutSend;
    private EditText inputMessage;
    private String enteredmessage;
    private String senderProfileImageUrl;
    String mrecievername,sendername,mrecieveruid,msenderuid;
    FirebaseDatabase firebaseDatabase;
    String senderroom,recieverroom;
    Intent intent;
    Calendar calendar;
    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;
    SimpleDateFormat simpleDateFormat;
    String currenttime;

    public ChatActivity() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_activity, container, false);

        // Initialize UI components
        textName = view.findViewById(R.id.textName);
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        layoutSend = view.findViewById(R.id.layoutSend);
        inputMessage = view.findViewById(R.id.inputMessage);
        imageInfo = view.findViewById(R.id.imageInfo);
        imageBack = view.findViewById(R.id.imageBack);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mrecieveruid = arguments.getString("derm_id");
            mrecievername = arguments.getString("derm_name");
            fetchProfileImageUrl(mrecieveruid);
        }
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        calendar = Calendar.getInstance();


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UConsult();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });

        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment fragment = new DInfo();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("user_id", mrecieveruid);
//                bundle.putString("user_name", mrecievername);
//                fragment.setArguments(bundle);
//
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.container2, fragment).commit();

                ShowUInfoDialog(ChatActivity.this);

            }
        });

        textName.setText(mrecievername);


        layoutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredmessage=inputMessage.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Enter message first", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Date date=new Date();
                    currenttime=simpleDateFormat.format(calendar.getTime());
                    //Messages messages=new Messages(enteredmessage,firebaseAuth.getUid(),date.getTime(),currenttime);
                    Messages messages = new Messages(enteredmessage, firebaseAuth.getUid(), date.getTime(), currenttime, senderProfileImageUrl);

                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderroom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference()
                                            .child("chats")
                                            .child(recieverroom)
                                            .child("messages")
                                            .push()
                                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });

                    inputMessage.setText(null);

                }
            }
        });
        return view;
    }

    private void ShowUInfoDialog(ChatActivity chatActivity) {
        Dialog DermInfoDialog = new Dialog(getActivity());
        DermInfoDialog.setContentView(R.layout.dialog_u_info);

        TextView yn1 = DermInfoDialog.findViewById(R.id.yn1);
        TextView yn2 = DermInfoDialog.findViewById(R.id.yn2);
        TextView yn3 = DermInfoDialog.findViewById(R.id.yn3);
        TextView yn4 = DermInfoDialog.findViewById(R.id.yn4);
        TextView yn5 = DermInfoDialog.findViewById(R.id.yn5);

        loadDermaInfo(mrecieveruid, yn1, yn2,yn3,yn4,yn5);

        ImageButton closeButton = DermInfoDialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DermInfoDialog.dismiss();
            }
        });

        // TODO: create book consultation function here

        Button bookconsulbtn = DermInfoDialog.findViewById(R.id.bookconsulbtn);
        bookconsulbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DermInfoDialog.dismiss();// Dismiss the dialog

                UBook bookFragment = new UBook();

                Bundle bundle = new Bundle();
                bundle.putString("derm_id", mrecieveruid);  // dermatologist's UID
                bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid()); // user's UID
                bookFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container1, bookFragment) // Use your container ID
                        .addToBackStack(null) // Optional, to add transaction to back stack
                        .commit();
            }
        });
        DermInfoDialog.show();
    }

    private void loadDermaInfo(String userId, TextView yn1, TextView yn2, TextView yn3, TextView yn4, TextView yn5) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String yn1Value = dataSnapshot.child("name").getValue(String.class);
                    String yn2Value = dataSnapshot.child("email").getValue(String.class);
                    String yn3Value = dataSnapshot.child("phnum").getValue(String.class);
                    String yn4Value = dataSnapshot.child("state").getValue(String.class);
                    String yn5Value = dataSnapshot.child("address").getValue(String.class);

                    yn1.setText(yn1Value);
                    yn2.setText(yn2Value);
                    yn3.setText(yn3Value);
                    yn4.setText(yn4Value);
                    yn5.setText(yn5Value);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

    }

    private void fetchProfileImageUrl(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("profileImage")) {
                    senderProfileImageUrl = snapshot.child("profileImage").getValue(String.class);
                    // Check if the URL is empty and set a default drawable if it is
                    if (senderProfileImageUrl == null || senderProfileImageUrl.trim().isEmpty()) {
                        senderProfileImageUrl = "android.resource://com.example.skinwise/" + R.drawable.ic_profile;
                    }
                    initChat();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }


    private void initChat() {
        // Initialize messages adapter with the image URL
        messagesAdapter = new MessagesAdapter(getActivity(), messagesArrayList, senderProfileImageUrl);
        chatRecyclerView.setAdapter(messagesAdapter);
        loadChatMessages();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve arguments and set up Firebase references here


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        msenderuid = firebaseAuth.getUid();
        senderroom = msenderuid + mrecieveruid;
        recieverroom = mrecieveruid + msenderuid;

        // Set up RecyclerView
        messagesArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(getActivity(), messagesArrayList, senderProfileImageUrl);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setAdapter(messagesAdapter);

        // Load chat messages
        loadChatMessages();

        // Setup other UI elements and listeners
        setupChat(view);
    }


    private void loadChatMessages() {
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats").child(senderroom).child("messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Messages message = snapshot1.getValue(Messages.class);
                    messagesArrayList.add(message);
                }
                messagesAdapter.notifyDataSetChanged();
                scrollToBottom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void scrollToBottom() {
        if (messagesAdapter.getItemCount() > 0) {
            chatRecyclerView.scrollToPosition(messagesAdapter.getItemCount() - 1);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }


    private void loadInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String uid = ""+ds.child("uid").getValue();
                            String role = ""+ds.child("role").getValue();
                            String email = ""+ds.child("email").getValue();
                            String name = ""+ds.child("name").getValue();
                            String timestamp = ""+ds.child("timestamp").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setupChat(View view) {
        TextView chatTitle = view.findViewById(R.id.textName);
        chatTitle.setText(mrecievername);
    }

}