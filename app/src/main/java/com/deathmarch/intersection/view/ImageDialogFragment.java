package com.deathmarch.intersection.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.Post;

public class ImageDialogFragment extends DialogFragment  {
    View view;
    ImageView img_back, imageView;
    String urlImage;


    public static ImageDialogFragment newInstance(){
        return new ImageDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeImageDialogFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ImageDialogAnimation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_view_image, container, false);
        imageView = view.findViewById(R.id.img_20);
        img_back = view.findViewById(R.id.img_back_20);
        Bundle bundle = getArguments();
        urlImage =  bundle.getString("url_image");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Glide.with(getContext())
                .load(urlImage)
                .placeholder(R.drawable.image_user_defalse)
                .error(R.drawable.image_user_defalse)
                .into(imageView);


        return view;
    }
}
