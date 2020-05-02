package com.deathmarch.intersection.view;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;

import java.util.UUID;

import static android.content.Context.DOWNLOAD_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class ImageDialogFragment extends DialogFragment  {
    View view;
    ImageView img_back, imageView, img_download;
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
        img_download = view.findViewById(R.id.img_save_20);
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

        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        getActivity().requestPermissions( new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, 1);
                    }else {
                        downloadManager(urlImage);
                    }
                }else {
                    downloadManager(urlImage);
                }




            }
        });
        return view;
    }





    private void downloadManager( String url){
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid+".jpg";
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)
                .setDescription("Downloading...")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName )
                .setAllowedOverRoaming(false);
        downloadManager.enqueue(request);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }


    }
}
