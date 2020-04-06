package com.deathmarch.intersection.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.CommentAdapter;
import com.deathmarch.intersection.model.Comment;
import com.deathmarch.intersection.model.GetTimeAgo;
import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.viewmodel.CommentViewModel;
import com.deathmarch.intersection.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

public class PostDialogFragment extends DialogFragment {
    View view;
    Post post;
    ImageView btn_ClosePost;
    String currentUserId;
    DatabaseReference usersReference ;
    DatabaseReference postReference ;

    boolean isLike;
    CircleImageView img_Thump;
    TextView txt_Displayname;
    TextView txt_Type;
    TextView txt_Time;
    TextView txt_PostText;
    ImageView img_PostImage;
    TextView txt_CountLike;
    ImageView img_Like;
    TextView txt_Comment;
    EditText edt_Cmt_Content;
    ImageView btn_Sent_Cmt;
    DatabaseReference currentPostReference;
    RecyclerView recyclerView;
    CommentAdapter adapter;
    ImageView img_option;

    public static PostDialogFragment newInstane(){
        return new PostDialogFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeDialogFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_post, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Bundle bundle = getArguments();
        post = (Post) bundle.getSerializable("post");
        Log.d("kkkkkkkkkkk", post.getPostUserId());
        init();
        loadUserCreatePost();
        eventPost();
        eventHandler();
        getArrComment(post.getPostUserId(), post.getPostId());

        return view;
    }

    private void init(){
        currentUserId = FirebaseAuth.getInstance().getUid();
       usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        postReference = FirebaseDatabase.getInstance().getReference().child("Post");

        btn_ClosePost = view.findViewById(R.id.img_close_dialog_post14);
        img_Thump = view.findViewById(R.id.img_thump14);
        txt_Displayname = view.findViewById(R.id.txt_diplayname14);
        txt_Type = view.findViewById(R.id.txt_type14);
        txt_Time = view.findViewById(R.id.txt_time14);
        txt_PostText = view.findViewById(R.id.post_text14);
        img_PostImage = view.findViewById(R.id.post_image14);
        txt_CountLike = view.findViewById(R.id.txt_count_like14);
        img_Like = view.findViewById(R.id.img_like14);
        txt_Comment = view.findViewById(R.id.txt_comment14);
        edt_Cmt_Content = view.findViewById(R.id.edt_comment14);
        btn_Sent_Cmt = view.findViewById(R.id.btn_sent_comment14);
        img_option = view.findViewById(R.id.img_option14);

        recyclerView=view.findViewById(R.id.recycler_comment14);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CommentAdapter(getActivity());
        recyclerView.setAdapter(adapter);

    }

    private void loadUserCreatePost(){
        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.getLiveDataUser(post.getPostUserId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                UserMain userMain = user.getUserMain();
                txt_Displayname.setText(userMain.getUserDisplayName());

                Glide.with(getContext())
                        .load(userMain.getUserImage())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(img_Thump);
            }
        });


    }

    private void eventHandler(){
        btn_Sent_Cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentCmt = edt_Cmt_Content.getText().toString().trim();
                if (!TextUtils.isEmpty(contentCmt)){
                    if (CheckNetwork.check(getContext())) createComment(contentCmt);
                }else {
                    Toast.makeText(getContext(), "Hãy nhập bình luận", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_ClosePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void createComment(String cmtContent){
            currentPostReference = FirebaseDatabase.getInstance().getReference().child("Post")
                    .child(post.getPostUserId()).child(post.getPostId());
            DatabaseReference cmt_push = currentPostReference.child("postComment").push();
            String cmtId = cmt_push.getKey();

            Map cmtMap = new HashMap();
            cmtMap.put("cmtId", cmtId);
            cmtMap.put("cmtUserId", currentUserId);
            cmtMap.put("cmtPostId", post.getPostId());
            cmtMap.put("cmtContent", cmtContent);
            cmtMap.put("cmtTime", ServerValue.TIMESTAMP);
            currentPostReference.child("postComment").child(cmtId).updateChildren(cmtMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        edt_Cmt_Content.setText("");
                        Toast.makeText(getContext(), "Đã bình luận", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    private void eventPost() {

        if (post.getPostUserId().equals(currentUserId)){
            img_option.setVisibility(View.VISIBLE);
        }

        img_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });


        GetTimeAgo getTimeAgo = new GetTimeAgo();
        final String postTime = getTimeAgo.getTimeAgo(post.getPostTime());
        txt_Time.setText(postTime);
        final String type = post.getPostType();
        if (type.equals("double")) {
            txt_Type.setText("Đã thêm bài đăng mới");
            txt_PostText.setText("   " + post.getPostText());
            Glide.with(getActivity())
                    .load(post.getPostImage())
                    .placeholder(R.drawable.image_user_defalse)
                    .error(R.drawable.image_user_defalse)
                    .into(img_PostImage);

        } else if (type.equals("text")) {
            txt_Type.setText("Đã thêm bài đăng mới");
            img_PostImage.setVisibility(View.GONE);
            txt_PostText.setText("   " + post.getPostText());
        } else if (type.equals("image")) {
            txt_Type.setText("Đã thêm một ảnh mới");
            txt_PostText.setVisibility(View.GONE);
            Glide.with(getActivity())
                    .load(post.getPostImage())
                    .placeholder(R.drawable.image_user_defalse)
                    .error(R.drawable.image_user_defalse)
                    .into(img_PostImage);
        }



        postReference.child(post.getPostUserId()).child(post.getPostId()).addValueEventListener(valueEventListener);

        img_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLike == true) {
                    postReference.child(post.getPostUserId()).child(post.getPostId())
                            .child("postLike").child(currentUserId).removeValue();
                } else {
                    Map likeMap = new HashMap();
                    likeMap.put("likeTime", ServerValue.TIMESTAMP);
                    postReference.child(post.getPostUserId()).child(post.getPostId()
                    )
                            .child("postLike").child(currentUserId).updateChildren(likeMap);
                }
            }
        });

        //event handler comment



    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild("postLike")) {
                long countLike = dataSnapshot.child("postLike").getChildrenCount();
                txt_CountLike.setText("" + countLike);
            } else {
                txt_CountLike.setText("" + 0);
            }

            if (dataSnapshot.hasChild("postLike/" + currentUserId)) {
                img_Like.setImageResource(R.drawable.ic_heart_pink);
                isLike = true;
            } else {
                img_Like.setImageResource(R.drawable.ic_heart_white);
                isLike = false;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void getArrComment(String postUserId, String postId){
        CommentViewModel commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        commentViewModel.getLiveDataComment(postUserId, postId).observe(this, new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                adapter.updateList(comments);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postReference.child(post.getPostUserId()).child(post.getPostId()).removeEventListener(valueEventListener);

    }

    private void showBottomSheetDialog(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.post_option_bottom_sheet_dialog);
        LinearLayout delete_post = bottomSheetDialog.findViewById(R.id.delete_post);
        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Xóa bài đăng", Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.show();
    }


}
