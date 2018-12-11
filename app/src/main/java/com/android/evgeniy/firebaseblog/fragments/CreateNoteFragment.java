package com.android.evgeniy.firebaseblog.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.evgeniy.firebaseblog.R;
import com.android.evgeniy.firebaseblog.adapters.UserNotesAdapter;
import com.android.evgeniy.firebaseblog.models.Location;
import com.android.evgeniy.firebaseblog.models.UserNote;
import com.android.evgeniy.firebaseblog.repositories.UserNotesDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class CreateNoteFragment extends Fragment {
    private View view;
    private Button button;
    private EditText noteText;

    private UserNotesDao userNotesDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_note, container, false);
        button = view.findViewById(R.id.btn_add_note);
        noteText = view.findViewById(R.id.et_new_note);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userNotesDao = new UserNotesDao();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                UserNote userNote = UserNote.builder()
                        .date(dateFormat.format(date))
                        .ownerId(user.getUid())
                        .time(timeFormat.format(date))
                        .text(noteText.getText().toString())
                        .location(Location.builder().lat("LAT").lng("LNG").build()).
                                build();

                userNotesDao.addOneByUid(userNote, user.getUid());

                showToast("Record added");
            }
        });
        return view;
    }

    private void showToast(String s) {
        Toast.makeText(view.getContext(), s,
                Toast.LENGTH_SHORT).show();
    }
}
