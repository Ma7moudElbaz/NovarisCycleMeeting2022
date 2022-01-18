package com.novartis.winnovators.profile;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ProfileActivity extends AppCompatActivity implements BottomSheet_change_password.PasswordSubmitListener {

    public void showChangePassSheet() {
        BottomSheet_change_password passBottomSheet =
                new BottomSheet_change_password(getBaseContext());
        passBottomSheet.show(getSupportFragmentManager(), "change_password");
    }

    TextView screenTitle, change_password, employer_code, name;
    ImageView change_pp, img_profile;
    ProgressBar loading;
    EasyImage easyImage;
    private ProgressDialog dialog;
    String filePath = "";
    String user_name, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
        change_password.setOnClickListener(v -> showChangePassSheet());
        change_pp.setOnClickListener(view -> Dexter.withContext(getBaseContext())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            easyImage.openGallery(ProfileActivity.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check());
        getData();
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
//                    img_profile.setImageBitmap(imageBitmap);
                    changePP(filePath, user_name, user_email);
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

    public void changePP(String filePath, final String name, final String email) {
        dialog.show();
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody userEmail = RequestBody.create(MediaType.parse("text/plain"), email);

        Webservice.getInstance().getApi().updatePP(UserUtils.getAccessToken(getBaseContext()), part, userName, userEmail).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());
                        Log.e("success", res.toString());
                        Toast.makeText(getBaseContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        getData();
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

    private void initFields() {
        easyImage = new EasyImage.Builder(getBaseContext())
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("EasyImage sample")
                .build();
        screenTitle = findViewById(R.id.screen_title);
        change_password = findViewById(R.id.change_password);
        employer_code = findViewById(R.id.employer_code);
        name = findViewById(R.id.name);
        change_pp = findViewById(R.id.change_pp);
        img_profile = findViewById(R.id.img_profile);
        loading = findViewById(R.id.loading);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);
    }


    private void setFields(JSONObject dataObj) throws JSONException {

        user_email = dataObj.getString("email");
        user_name = dataObj.getString("name");
        employer_code.setText(user_email);
        name.setText(user_name);


        Glide.with(getBaseContext())
                .load(dataObj.getString("photo"))
                .centerCrop()
                .into(img_profile);

    }

    public void getData() {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().getProfileData(UserUtils.getAccessToken(getBaseContext())).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONObject dataObj = responseObject.getJSONObject("data");
                    setFields(dataObj);
                    loading.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });
    }

    public void updatePassword(String currentPass, String newPass) {
        Map<String, String> map = new HashMap<>();

        map.put("current_password", currentPass);
        map.put("password", newPass);
        dialog.show();
        Webservice.getInstance().getApi().updatePassword(UserUtils.getAccessToken(getBaseContext()),map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        JSONObject res = new JSONObject(response.body().string());
                        Toast.makeText(getBaseContext(), res.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void passwordSubmitListener(String currentPass, String newPass) {
        updatePassword(currentPass, newPass);
    }
}