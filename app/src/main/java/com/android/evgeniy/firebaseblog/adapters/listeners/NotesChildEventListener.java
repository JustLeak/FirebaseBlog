package com.android.evgeniy.firebaseblog.adapters.listeners;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.evgeniy.firebaseblog.adapters.NotesRecyclerAdapter;
import com.android.evgeniy.firebaseblog.models.UserNote;
import com.android.evgeniy.firebaseblog.services.NotesContainer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class NotesChildEventListener implements ChildEventListener {
    private NotesRecyclerAdapter adapter;

    public void setAdapter(NotesRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        UserNote note = dataSnapshot.getValue(UserNote.class);

        if (note != null) {
            note.setKey(dataSnapshot.getKey());
        }

        NotesContainer.getInstance().addNote(note);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        UserNote userNote = dataSnapshot.getValue(UserNote.class);
        adapter.notifyItemChanged(NotesContainer.getInstance().changeNote(userNote, dataSnapshot.getKey()));
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        adapter.notifyItemRemoved(NotesContainer.getInstance().deleteNote(dataSnapshot.getKey()));
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
