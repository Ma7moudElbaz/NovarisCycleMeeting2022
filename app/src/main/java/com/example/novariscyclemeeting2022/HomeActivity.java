package com.example.novariscyclemeeting2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novariscyclemeeting2022.agenda.AgendaActivity;
import com.example.novariscyclemeeting2022.chat.ChatActivity;
import com.example.novariscyclemeeting2022.gm_message.GmMessageActivity;
import com.example.novariscyclemeeting2022.notifications.NotificationsActivity;
import com.example.novariscyclemeeting2022.profile.ProfileActivity;
import com.example.novariscyclemeeting2022.voting_system.VotingActivity;
import com.example.novariscyclemeeting2022.wall_posts.PostsActivity;

public class HomeActivity extends AppCompatActivity {

    ImageView notifications;
    TextView posts, agenda, voting, chat, gm_message, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initFileds();
        posts.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), PostsActivity.class)));
        agenda.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), AgendaActivity.class)));
        voting.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), VotingActivity.class)));
        chat.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), ChatActivity.class)));
        gm_message.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), GmMessageActivity.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), ProfileActivity.class)));
        notifications.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), NotificationsActivity.class)));
    }

    private void initFileds() {
        notifications = findViewById(R.id.notifications);
        posts = findViewById(R.id.posts);
        agenda = findViewById(R.id.agenda);
        voting = findViewById(R.id.voting);
        chat = findViewById(R.id.chat);
        gm_message = findViewById(R.id.gm_message);
        profile = findViewById(R.id.profile);
    }
}