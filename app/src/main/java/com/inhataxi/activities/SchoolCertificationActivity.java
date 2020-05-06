package com.inhataxi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageContext;
import com.google.api.services.vision.v1.model.TextAnnotation;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inhataxi.LoadingDialog;
import com.inhataxi.PackageManagerUtils;
import com.inhataxi.PermissionUtils;
import com.inhataxi.R;
import com.inhataxi.RetrofitInterface;
import com.inhataxi.model.ChatRoom;
import com.inhataxi.model.ChatRoomItem;
import com.inhataxi.response.ChattingRoomResponse;
import com.inhataxi.response.SignUpResponse;
import com.inhataxi.response.SuperResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.inhataxi.IngaTaxiApp.MEDIA_TYPE_JSON;
import static com.inhataxi.IngaTaxiApp.getRetrofit;
import static com.inhataxi.activities.GoogleCloudeVisionActivity.CAMERA_IMAGE_REQUEST;
import static com.inhataxi.activities.GoogleCloudeVisionActivity.CAMERA_PERMISSIONS_REQUEST;
import static com.inhataxi.activities.GoogleCloudeVisionActivity.FILE_NAME;

public class SchoolCertificationActivity extends BaseActivity {

    private Context mContext;
    private TabLayout mTabLayout;
    String url;
    String mGender;
    Intent mIntent;
    private static final int PICK_FROM_CAMERA = 1; //카메라 촬영으로 사진 가져오기
    private static final int PICK_FROM_ALBUM = 2; //앨범에서 사진 가져오기
    private static final int CROP_FROM_CAMERA = 3; //가져온 사진을 자르기 위한 변수

    private static final int BEFORE_IMAGE = 4; //가져온 사진을 자르기 위한 변수
    private static final int AFTER_IMAGE = 5; //가져온 사진을 자르기 위한 변수
    private static final int AFTER_SEVER_UPLOAD = 6; //가져온 사진을 자르기 위한 변수

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}; //권한 설정 변수
    private static final int MULTIPLE_PERMISSIONS = 101; //권한 동의 여부 문의 후 CallBack 함수에 쓰일 변수
    private Uri photoUri;
    private ImageView mImageViewThumbnail, mImageViewButton, mImageViewDone;
    private EditText mTextViewName, mTextViewCode, mTextViewDept;
    private int mMode = BEFORE_IMAGE;

    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    private static final int MAX_DIMENSION = 1200;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLOUD_VISION_API_KEY = "AIzaSyDWgCoxrW3F93kneyob_0zFWvy6JngwM00";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    private Button mButtonMan, mButtonWoman;

    private boolean genderCheck = false;

    private TextView error;

    LoadingDialog loadingDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_certification);
        mContext = this;
        init();
        loadingDialog = new LoadingDialog(mContext);
        initTab();
        checkPermissions();
        mImageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mTextViewName.getText().toString().equals("") && !mTextViewCode.getText().toString().equals("") && !mTextViewDept.getText().toString().equals("") && genderCheck) {
                    try {
                        postSignUp();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    void postSignUp() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("id", mIntent.getExtras().getString("id"));
        //Log.d("로그", "id: " + mIntent.getExtras().getString("id"));
        params.put("name", mTextViewName.getText().toString());
        //Log.d("로그", mTextViewName.getText().toString());
        params.put("password", mIntent.getExtras().getString("password"));
        //Log.d("로그", "pw: " + mIntent.getExtras().getString("password"));
        params.put("card_url", url);
        //Log.d("로그", url);
        params.put("dept", mTextViewDept.getText().toString());
        //Log.d("로그", mTextViewDept.getText().toString());
        params.put("gender", mGender);
        //Log.d("로그", mGender);
        params.put("code", Integer.parseInt(mTextViewCode.getText().toString()));

        //로딩 다이얼로그
        //mDialog.show();

        final RetrofitInterface retrofitInterface = getRetrofit(mContext).create(RetrofitInterface.class);
        retrofitInterface.postSignUp(RequestBody.create(params.toString(), MEDIA_TYPE_JSON)).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(@NonNull final Call<SignUpResponse> call,
                                   @NonNull final Response<SignUpResponse> response) {
//                hideProgressDialog();
                SignUpResponse signUpResponse = response.body();
                if (signUpResponse == null) {
                    showCustomToast("응답 없음");
                }

                if (signUpResponse.getCode() == 100) {
                    //회원가입 성공
                    showCustomToast(signUpResponse.getMessage());
                    Intent intent = new Intent(SchoolCertificationActivity.this, LoginActivity.class);
                    intent.putExtra("name", mTextViewName.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    showCustomToast("회원가입 실패");
                    Intent intent = new Intent(SchoolCertificationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                //로딩 끝
                //mDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull final Call<SignUpResponse> call,
                                  @NonNull final Throwable throwable) {
                //mDialog.dismiss();
                showCustomToast("연결 실패");
            }
        });
    }



    void init() {
        mTabLayout = findViewById(R.id.activity_school_certification_tab);
        mImageViewThumbnail = findViewById(R.id.activity_school_certification_iv_thumbnail);
        mImageViewThumbnail.setClipToOutline(true);
        mImageViewButton = findViewById(R.id.activity_school_certification_iv_upload);
        mTextViewName = findViewById(R.id.school_certification_et_name);
        mTextViewCode = findViewById(R.id.school_certification_et_code);
        mTextViewDept = findViewById(R.id.school_certification_et_dept);
        mButtonMan = findViewById(R.id.school_certification_btn_genderMan);
        mButtonWoman = findViewById(R.id.school_certification_btn_genderWoman);
        mImageViewDone = findViewById(R.id.basic_info_done);
        error = findViewById(R.id.tv_error);
        mIntent = getIntent();
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showCustomToast(mContext.getString(R.string.school_certification_permission));
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showCustomToast(mContext.getString(R.string.school_certification_permission));
                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showCustomToast(mContext.getString(R.string.school_certification_permission));
                            }
                        }
                    }
                } else {
                    showCustomToast(mContext.getString(R.string.school_certification_permission));
                }
                return;
            }
        }
    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            showCustomToast(mContext.getString(R.string.school_certification_image_error));
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(SchoolCertificationActivity.this,
                    "com.inhataxi.provider", photoFile); //FileProvider의 경우 이전 포스트를 참고하세요.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }

//        if (PermissionUtils.requestPermission(
//                this,
//                CAMERA_PERMISSIONS_REQUEST,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA)) {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
//        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/inha/"); //test라는 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(SchoolCertificationActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            MediaScannerConnection.scanFile(SchoolCertificationActivity.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            try { //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 800, 500);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축


                mImageViewThumbnail.setImageBitmap(thumbImage);
                Glide.with(mContext).load(R.drawable.btn_school_certification).into(mImageViewButton);
                mMode = AFTER_IMAGE;
            } catch (Exception e) {
            }
//        }

//
//            if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
//                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
//                uploadImage(photoUri);
//            }

//            if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            uploadImage(data.getData());
//            } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
//            uploadImage(photoUri);
            uploadImage(data.getData());


//            }
        }
    }


    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                bitmap = rotate(bitmap, 90); //샘플이미지파일
                callCloudVision(bitmap);
//                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_LONG).show();
        }
        loadingDialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        }, 1500); //1초 있다가 메인으로 넘어가게.
    }


    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
//        mImageDetails.setText(R.string.loading_message);
        //로딩넣으면될듯

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("");

//        List<TextAnnotation> labels = response.getResponses().get(0).getFullTextAnnotation();

        final TextAnnotation text = response.getResponses().get(0).getFullTextAnnotation();

//        if (labels != null) {
//            for (EntityAnnotation label : labels) {
//                message.append(String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription()));
//                message.append("\n");
//            }
//        } else {
//            message.append("nothing");
//        }

        if (text != null) {
            message.append(text.getText());
        }

        return message.toString();
    }


    private class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<SchoolCertificationActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(SchoolCertificationActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            SchoolCertificationActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                System.out.println(result);
                int a = result.indexOf("-");
                if (a != -1) {
                    String gender = result.substring(a + 1, a + 2);
                    System.out.println("a= " + gender);
                    if (gender.equals("1")) {
                        mButtonMan.setPressed(true);
                        mButtonMan.setSelected(true);
                        mGender = "M";
                        genderCheck = true;
                    } else if (gender.equals("2")) {
                        mButtonWoman.setPressed(true);
                        mButtonWoman.setSelected(true);
                        mGender = "W";
                        genderCheck = true;
                    }
                }
                int b = result.indexOf("성명");
                if (b != -1) {
                    String name = result.substring(b + 3, b + 6);
                    System.out.println("b= " + name);
                    mTextViewName.setText(name);
                }
                int c = result.indexOf("학과");
                if (c != -1) {
                    String dept = result.substring(c + 3, c + 9);
                    System.out.println("c= " + dept);
                    mTextViewDept.setText(dept);
                }
                int d = result.indexOf("121");
                if (d != -1) {
                    String code = result.substring(d, d + 8);
                    System.out.println("d= " + code);
                    mTextViewCode.setText(code);
                }
                error.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
            try {
                uploadFileToFireBase(photoUri);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void genderClick(View view) {
        switch (view.getId()) {
            case R.id.school_certification_btn_genderWoman:
                mButtonWoman.setPressed(true);
                mButtonWoman.setSelected(true);
                mButtonMan.setPressed(false);
                mButtonMan.setSelected(false);
                genderCheck = true;
                mGender = "W";
                break;
            case R.id.school_certification_btn_genderMan:
                mButtonMan.setPressed(true);
                mButtonMan.setSelected(true);
                mButtonWoman.setPressed(false);
                mButtonWoman.setSelected(false);
                genderCheck = true;
                mGender = "M";
                break;
        }
    }


    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("DOCUMENT_TEXT_DETECTION");
//                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});
            annotateImageRequest.setImageContext(new ImageContext());

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }


    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    public void cropImage() {
        this.grantUriPermission("com.android.camera", photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 16);
            intent.putExtra("aspectY", 10);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/inha/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(SchoolCertificationActivity.this,
                    "com.inhataxi.provider", tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);

        }

    }

    void initTab() {
        mTabLayout.addTab(mTabLayout.newTab().setText("기본정보입력"));
        mTabLayout.addTab(mTabLayout.newTab().setText("학교인증"));

        //탭 터치안되게 막기//
        LinearLayout tabStrip = ((LinearLayout) mTabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }
        /////////////////
        TabLayout.Tab tab = mTabLayout.getTabAt(1);
        tab.select();
    }

    void imageUploadChoiceDialog() {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto();
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToAlbum();
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(mContext)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }

    public void customOnClick(View view) throws JSONException {
        switch (view.getId()) {
            case R.id.activity_school_certification_iv_back:
                finish();
                break;
            case R.id.activity_school_certification_iv_upload:
                if (mMode == BEFORE_IMAGE) {
                    imageUploadChoiceDialog();
                } else if (mMode == AFTER_SEVER_UPLOAD) {
                    startActivity(new Intent(getApplication(), LoginActivity.class));
                }
                break;

            case R.id.basic_info_done:
            default:
                break;
        }
    }


    void uploadFileToFireBase(Uri mImageUri) throws JSONException {
        final LoadingDialog loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();
//        progressDialog.setTitle("업로드중...");
//        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Date now = new Date();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://inha-taxi.appspot.com/").child("user/" + mImageUri.getLastPathSegment());
        UploadTask uploadTask = storageRef.putFile(mImageUri);
        storageRef.putFile(mImageUri)
                //성공시
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                    }
                })
                //실패시
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        showCustomToast("업로드 실패");
                    }
                })
                //진행중
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                        @SuppressWarnings("VisibleForTests")
//                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                    }
                });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
//                    mProfileImageUrl = downloadUri.toString();


                    Glide.with(mContext)
                            .load(downloadUri.toString())
                            .fitCenter()
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.imagenotfound)
                            .into(mImageViewThumbnail);
//                    mImageViewThumbnail

                    url = downloadUri.toString();
                    //Log.d("로그", url);

                    loadingDialog.dismiss();
//                    mTextViewTitle.setText("이미지 첨부가\n완료되었습니다.");
//                    mTextViewContent.setText("빠른 시일 내에 처리하겠습니다.");

//                    Glide.with(mContext).load(R.drawable.btn_yellow_ok).into(mImageViewButton);

                    mMode = AFTER_SEVER_UPLOAD;//                    try {
////                        postPofileImage();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    showCustomToast(mContext, "업로드!");
                } else {
                    //실패시
                }
            }
        });
    }

}
