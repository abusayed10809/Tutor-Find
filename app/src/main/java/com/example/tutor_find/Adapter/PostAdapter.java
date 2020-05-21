package com.example.tutor_find.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutor_find.ConfirmActivity;
import com.example.tutor_find.MainActivity;
import com.example.tutor_find.Model.Post;
import com.example.tutor_find.R;
import com.example.tutor_find.StartActivity;
import com.example.tutor_find.StudentRegActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import org.w3c.dom.Text;

public class PostAdapter extends FirebaseRecyclerAdapter<Post, PostAdapter.PostViewHolder> {

    public PostAdapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PostViewHolder holder, int position, @NonNull final Post model) {

        holder.postGroup.setText("Group: " + model.getGroup());
        holder.postCurriculum.setText("Curriculum: " + model.getCurriculum());
        holder.postStudyClass.setText("Class: " + model.getStudyClass());
        holder.postSubjectList.setText("Subjects: " + model.getSubjectList());
        holder.postSalary.setText("Salary: " + model.getSalary());
        holder.postDescription.setText("Description: " + model.getDescription());
        holder.postAddress.setText("Address: " + model.getAddress());

        holder.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.applyButton.getContext(), ConfirmActivity.class);

                String postId = model.getPostId();

                String group = model.getGroup();
                String curriculum = model.getCurriculum();
                String studyClass = model.getStudyClass();
                String subjectList = model.getSubjectList();
                String salary = model.getSalary();
                String description = model.getDescription();
                String address = model.getAddress();
                String userId = model.getUserId();

                intent.putExtra("group", group);
                intent.putExtra("curriculum", curriculum);
                intent.putExtra("studyClass", studyClass);
                intent.putExtra("subjectList", subjectList);
                intent.putExtra("salary", salary);
                intent.putExtra("description", description);
                intent.putExtra("address", address);
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);

                holder.applyButton.getContext().startActivity(intent);
            }
        });
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlepost, parent, false);
        return new PostViewHolder(view);

    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        TextView postGroup;
        TextView postCurriculum;
        TextView postStudyClass;
        TextView postSubjectList;
        TextView postSalary;
        TextView postDescription;
        TextView postAddress;
        Button applyButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            postGroup = itemView.findViewById(R.id.postGroup);
            postCurriculum = itemView.findViewById(R.id.postCurriculum);
            postStudyClass = itemView.findViewById(R.id.postStudyClass);
            postSubjectList = itemView.findViewById(R.id.postSubjectList);
            postSalary = itemView.findViewById(R.id.postSalary);
            postDescription = itemView.findViewById(R.id.postDescription);
            postAddress = itemView.findViewById(R.id.postAddress);
            applyButton = itemView.findViewById(R.id.applyButton);
        }
    }

}
