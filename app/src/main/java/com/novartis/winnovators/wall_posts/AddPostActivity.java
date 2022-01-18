package com.novartis.winnovators.wall_posts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novariscyclemeeting2022.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.novartis.winnovators.utils.UserUtils;
import com.novartis.winnovators.webservice.Webservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {

    TextView screenTitle;
    ImageView add_image, post_img;
    EditText content;
    Button submit;
    EasyImage easyImage;
    private ProgressDialog dialog;
    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());

        submit.setOnClickListener(v -> {
            if (content.length() == 0 || filePath.equals("")) {
                Toast.makeText(getBaseContext(), "Please add content to post", Toast.LENGTH_SHORT).show();
            } else if (filePath.equals("")) {
                addPostWithoutImage(filePath, content.getText().toString());
            } else {
                addPostWithImage(filePath, content.getText().toString());
            }
        });

        add_image.setOnClickListener(view -> Dexter.withContext(getBaseContext())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            easyImage.openGallery(AddPostActivity.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check());
    }

    private void initFields() {
        easyImage = new EasyImage.Builder(getBaseContext())
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("EasyImage sample")
                .build();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);

        screenTitle = findViewById(R.id.screen_title);
        add_image = findViewById(R.id.add_image);
        post_img = findViewById(R.id.post_img);
        submit = findViewById(R.id.btn_submit);
        content = findViewById(R.id.content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                File returnedFile = imageFiles[0].getFile();
                long fileSize = returnedFile.length() / 1024 / 1024;
                Log.e("File Size", fileSize + "");
                if (fileSize < 2) {
                    Bitmap imageBitmap = BitmapFactory.decodeFile(returnedFile.getAbsolutePath());
                    filePath = returnedFile.getAbsolutePath();
                    post_img.setImageBitmap(imageBitmap);
                } else {
                    Toast.makeText(getBaseContext(), "Image Should be less than 2mb", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }

    public void addPostWithImage(String filePath, final String content) {
        dialog.show();
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody teamNameBody = RequestBody.create(MediaType.parse("text/plain"), content);

        Webservice.getInstance().getApi().addPostWithImage(UserUtils.getAccessToken(getBaseContext()), part, teamNameBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());
                        Log.e("success", res.toString());
                        Toast.makeText(getBaseContext(), res.getString("data"), Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    } else {

                        Log.e("fail", response.errorBody().string());
                        Toast.makeText(getBaseContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                Log.d("Request failurr", call.toString() + " , " + t.getMessage());
                dialog.dismiss();
            }
        });
    }

    public void addPostWithoutImage(String filePath, final String content) {
        dialog.show();
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody myContent = RequestBody.create(MediaType.parse("text/plain"), content);

        Webservice.getInstance().getApi().addPostWithoutImage(UserUtils.getAccessToken(getBaseContext()), myContent).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());
                        Log.e("success", res.toString());
                        Toast.makeText(getBaseContext(), res.getString("data"), Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    } else {

                        Log.e("fail", response.errorBody().string());
                        Toast.makeText(getBaseContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                Log.d("Request failurr", call.toString() + " , " + t.getMessage());
                dialog.dismiss();
            }
        });
    }
}