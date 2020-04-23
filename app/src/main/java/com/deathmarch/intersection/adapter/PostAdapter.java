package com.deathmarch.intersection.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.GetTimeAgo;
import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.view.AnotherUserPageActivity;
import com.deathmarch.intersection.view.PostDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context context;
    private ArrayList<Post> arrayList;
    RecyclerView recyclerView;
    String currentUserId ;
    DatabaseReference usersReference ;
    DatabaseReference postReference ;
    DatabaseReference notifyReference ;


    public PostAdapter(Context context, ArrayList<Post> arrayList, RecyclerView recyclerView) {
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerView = recyclerView;
        this.currentUserId = FirebaseAuth.getInstance().getUid();
        this.usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        this.postReference = FirebaseDatabase.getInstance().getReference().child("Post");
        this.notifyReference = FirebaseDatabase.getInstance().getReference().child("Notify");
    }

    public void updateList(ArrayList<Post> newList) {
        PostDiffUtilCallback callback = new PostDiffUtilCallback(arrayList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.arrayList.clear();
        this.arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final boolean isLike;
        final Post post = arrayList.get(position);

        if (post.getPostUserId().equals(currentUserId)){
            holder.img_Option.setVisibility(View.VISIBLE);
        }

        holder.img_Option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(post.getPostUserId(), post.getPostId());
            }
        });

        usersReference.child(post.getPostUserId()).child("UserMain").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMain userMain = dataSnapshot.getValue(UserMain.class);
                holder.txt_Displayname.setText(userMain.getUserDisplayName());

                    Glide.with(context)
                            .load(userMain.getUserImage())
                            .placeholder(R.drawable.image_user_defalse)
                            .error(R.drawable.image_user_defalse)
                            .into(holder.img_Thump);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        GetTimeAgo getTimeAgo = new GetTimeAgo();
        final String postTime = getTimeAgo.getTimeAgo(post.getPostTime());
        holder.txt_Time.setText(postTime);
        final String type = post.getPostType();


        if (type.equals("double")) {
            holder.txt_Type.setText("Đã thêm bài đăng mới");
            holder.txt_PostText.setText("   " + post.getPostText());
            Glide.with(context)
                    .load(post.getPostImage())
                    .placeholder(R.drawable.image_user_defalse)
                    .error(R.drawable.image_user_defalse)
                    .into(holder.img_PostImage);

        } else if (type.equals("text")) {
            holder.txt_Type.setText("Đã thêm bài đăng mới");
            holder.img_PostImage.setVisibility(View.GONE);
            holder.txt_PostText.setText("   " + post.getPostText());
        } else if (type.equals("image")) {
            holder.txt_Type.setText("Đã thêm một ảnh mới");
            holder.txt_PostText.setVisibility(View.GONE);
            Glide.with(context)
                    .load(post.getPostImage())
                    .placeholder(R.drawable.image_user_defalse)
                    .error(R.drawable.image_user_defalse)
                    .into(holder.img_PostImage);
        }
        ValueEventListener likeValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("postLike")){
                    long countLike = dataSnapshot.child("postLike").getChildrenCount();
                    holder.txt_CountLike.setText(""+countLike);
                }else {
                    holder.txt_CountLike.setText(""+0);
                }

                if (dataSnapshot.hasChild("postComment")){
                    long countCmt = dataSnapshot.child("postComment").getChildrenCount();
                    holder.txt_Comment.setText(countCmt+" - Comment");
                }else {
                    holder.txt_Comment.setText("Comment");
                }


                if (dataSnapshot.hasChild("postLike/"+currentUserId)){
                    holder.img_Like.setImageResource(R.drawable.ic_heart_pink);
                    holder.isLike = true;
                }else {
                    holder.img_Like.setImageResource(R.drawable.ic_heart_white);
                    holder.isLike=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        postReference.child(post.getPostUserId()).child(post.getPostId()).addValueEventListener(likeValueEventListener);




        holder.img_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isLike==true){
                    postReference.child(post.getPostUserId()).child(post.getPostId())
                            .child("postLike").child(currentUserId).removeValue();

                }else {
                    Map likeMap = new HashMap();
                    likeMap.put("likeTime", ServerValue.TIMESTAMP);
                    postReference.child(post.getPostUserId()).child(post.getPostId())
                            .child("postLike").child(currentUserId).updateChildren(likeMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                if (!post.getPostUserId().equals(currentUserId)){
                                    Map notifyLikeMap = new HashMap();
                                    notifyLikeMap.put("time", ServerValue.TIMESTAMP);
                                    notifyLikeMap.put("userId", currentUserId);
                                    notifyLikeMap.put("seen", "false");
                                    notifyReference.child(post.getPostUserId()).child(post.getPostId())
                                           .child("likeNotify").updateChildren(notifyLikeMap);

                                }
                            }
                        }
                    });



                }
            }
        });

        holder.img_Thump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(context)){
                    if (!post.getPostUserId().equals(currentUserId)) goAnotherUserPageActivity(post.getPostUserId());
                }

            }
        });

        holder.txt_Displayname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(context)){
                    if (!post.getPostUserId().equals(currentUserId)) goAnotherUserPageActivity(post.getPostUserId());
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                PostDialogFragment postDialog  = PostDialogFragment.newInstane();
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                postDialog.setArguments(bundle);
                Log.d("kkkkkkkkkkk", "Bundle");
                postDialog.show(fragmentManager, "duc");

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        boolean isLike;
        CircleImageView img_Thump;
        ImageView img_Option;
        TextView txt_Displayname;
        TextView txt_Type;
        TextView txt_Time;
        TextView txt_PostText;
        ImageView img_PostImage;
        TextView txt_CountLike;
        ImageView img_Like;
        TextView txt_Comment;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Thump = itemView.findViewById(R.id.img_thump13);
            txt_Displayname = itemView.findViewById(R.id.txt_diplayname13);
            txt_Type = itemView.findViewById(R.id.txt_type13);
            txt_Time = itemView.findViewById(R.id.txt_time13);
            txt_PostText = itemView.findViewById(R.id.post_text13);
            img_PostImage = itemView.findViewById(R.id.post_image13);
            txt_CountLike = itemView.findViewById(R.id.txt_count_like13);
            img_Like = itemView.findViewById(R.id.img_like13);
            txt_Comment = itemView.findViewById(R.id.txt_comment13);
            img_Option = itemView.findViewById(R.id.img_option13);

        }
    }

    private void showBottomSheetDialog(final String postUserId, final String postId){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.post_option_bottom_sheet_dialog);
        LinearLayout delete_post = bottomSheetDialog.findViewById(R.id.delete_post);
        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                showDialogDeletePost(postUserId, postId);
            }
        });
        bottomSheetDialog.show();
    }

    private void showDialogDeletePost(final String postUserId, final String postId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bạn chắc chắn muốn xóa bài viết này?");
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CheckNetwork.check(context)){
                    postReference.child(postUserId).child(postId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                showSnackbar("Xóa bài viết thành công");
                                notifyReference.child(postUserId).child(postId).removeValue();
                            }
                        }
                    });
                }else {
                    showSnackbar("Không có kết nối mạng");
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showSnackbar(String message)
    {
        Snackbar snackbar = Snackbar.make(recyclerView, message, 2000);
        snackbar.show();
    }

    private void goAnotherUserPageActivity(String anotherUserId) {
        Intent intent = new Intent(context, AnotherUserPageActivity.class);
        intent.putExtra("anotherUserId", anotherUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
