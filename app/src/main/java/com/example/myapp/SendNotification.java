package com.example.myapp;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {


    public SendNotification(String msg, String heading, String notify_key) {

        /*DatabaseReference ref=FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notification_key");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notify_key=dataSnapshot.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
        notify_key=;*/
        try {
            JSONObject notificationContent = new JSONObject(
                    "{'contents':{'en':'" + msg + "'}," +
                    "'include_player_ids':['" + notify_key + "']," +
                    "'heading':{'en':'" + heading + "'}}");
            OneSignal.postNotification(notificationContent,null);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
