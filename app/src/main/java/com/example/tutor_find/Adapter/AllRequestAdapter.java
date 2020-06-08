package com.example.tutor_find.Adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutor_find.Model.UserInfo;
import com.example.tutor_find.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AllRequestAdapter extends FirebaseRecyclerAdapter<UserInfo, AllRequestAdapter.AllRequestPostViewHolder> {

    DatabaseReference databaseReference;
    DatabaseReference userReference;

    String postId;
    String userId;


    public AllRequestAdapter(@NonNull FirebaseRecyclerOptions<UserInfo> options, String postId) {
        super(options);
        this.postId=postId;
    }

    @Override
    protected void onBindViewHolder(@NonNull final AllRequestPostViewHolder holder, int position, @NonNull UserInfo model) {

        holder.userName.setText("Name: "+model.getName());
        holder.userInstitution.setText("Institution: "+model.getInstitution());
        holder.userDepartment.setText("Department: "+model.getDepartment());
        holder.userStudyYear.setText("Year: "+model.getYear());
        holder.userEmail.setText("Email: "+model.getEmail());
        holder.userNumber.setText("Mobile No: "+model.getNumber());

        userId = model.getId();

        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requests").child(postId);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String check = dataSnapshot.getValue().toString();
                if(check.equals("true"))
                {
                    holder.userStatus.setText("Accepted");
                    holder.userStatus.setTextColor(Color.parseColor("#008577"));
                }
                else
                {
                    holder.userStatus.setText("pending");
                    holder.userStatus.setTextColor(Color.parseColor("#D81B60"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder acceptConfirmation;
                acceptConfirmation = new AlertDialog.Builder(holder.acceptButton.getContext());
                acceptConfirmation.setTitle("Select Tutor");
                acceptConfirmation.setMessage("Click OK to select this tutor");

                acceptConfirmation.setCancelable(false);

                acceptConfirmation.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.acceptButton.getContext(), "Tutor Selected", Toast.LENGTH_SHORT).show();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).child("requests");
                        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("requests").child(postId);

                        databaseReference.removeValue();

                        databaseReference.child(userId).setValue(true);

                        userReference.setValue(true);

                    }
                });

                acceptConfirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = acceptConfirmation.create();
                alertDialog.show();

            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @NonNull
    @Override
    public AllRequestPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_request, parent, false);
        return new AllRequestAdapter.AllRequestPostViewHolder(view);
    }

    public class AllRequestPostViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userInstitution;
        TextView userDepartment;
        TextView userStudyYear;
        TextView userEmail;
        TextView userNumber;
        TextView userStatus;
        Button acceptButton;
        Button rejectButton;


        public AllRequestPostViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            userInstitution = itemView.findViewById(R.id.userInstitution);
            userDepartment = itemView.findViewById(R.id.userDepartment);
            userStudyYear = itemView.findViewById(R.id.userStudyYear);
            userEmail = itemView.findViewById(R.id.userEmail);
            userNumber = itemView.findViewById(R.id.userNumber);
            userStatus = itemView.findViewById(R.id.userStatus);

            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);

        }
    }
}