package com.inhataxi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhataxi.LoadingDialog;
import com.inhataxi.R;
import com.inhataxi.RetrofitInterface;
import com.inhataxi.activities.sign_up.SignUpActivity;
import com.inhataxi.response.LoginResponse;
import com.inhataxi.response.MakeRoomResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.inhataxi.IngaTaxiApp.MEDIA_TYPE_JSON;
import static com.inhataxi.IngaTaxiApp.getRetrofit;
import static com.inhataxi.IngaTaxiApp.sSharedPreferences;

public class LoginActivity extends BaseActivity {

    Context mContext;
    TextView mTextViewFind, mTextViewSignUp;
    EditText mEditTextEmail, mEditTextPassword;
    ImageView mImageViewButton;
    Boolean mIsValidation = false;
    LoadingDialog mDialog;

    public static int userNo;
    public static String userName;
    private TextView mTextViewLogout;
    private TextView mTextViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        init();
        mTextViewFind.setText(Html.fromHtml("<u>이메일/비밀번호 찾기</u>"));
        mTextViewSignUp.setText(Html.fromHtml("INHATAXI가 처음이신가요? <u>회원가입</u>"));

        Intent intent = getIntent();
        boolean check = intent.getBooleanExtra("logout", false);
        if (check) {
            mTextViewLogin.setVisibility(View.GONE);
            mTextViewLogout.setVisibility(View.VISIBLE);
            String content = mTextViewLogout.getText().toString();
            SpannableString spannableString = new SpannableString(content);

            String word = "로그아웃";
            int start = content.indexOf(word);
            int end = start + word.length();

            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mTextViewLogout.setText(spannableString);
        } else {
            mTextViewLogin.setVisibility(View.VISIBLE);
            mTextViewLogout.setVisibility(View.GONE);
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable edit) {
            // Text가 바뀌고 동작할 코드
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Text가 바뀌기 전 동작할 코드
        }

        //
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkValidation();
        }
    };

    void init() {
        mTextViewFind = findViewById(R.id.sing_in_tv_find);
        mTextViewSignUp = findViewById(R.id.sing_in_tv_sing_up);
        mEditTextEmail = findViewById(R.id.sing_in_et_email);
        mEditTextPassword = findViewById(R.id.sing_in_et_password);
        mImageViewButton = findViewById(R.id.sing_in_iv_sign_in);
        mEditTextPassword.addTextChangedListener(textWatcher);
        mTextViewLogout = findViewById(R.id.sign_in_tv_logout);
        mTextViewLogin = findViewById(R.id.sign_in_tv_login);
        mDialog = new LoadingDialog(this);
    }

    private void checkValidation() {
        if (checkEmail(mEditTextEmail.getText().toString())) {
            if (passwordValidation(mEditTextPassword.getText().toString())) {
                mImageViewButton.setImageResource(R.drawable.ic_login_true);
                mIsValidation = true;
            } else {
                mImageViewButton.setImageResource(R.drawable.ic_login);
                mIsValidation = false;
            }
        } else {
            mImageViewButton.setImageResource(R.drawable.ic_login);
            mIsValidation = false;
        }
    }

    private boolean checkEmail(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public boolean passwordValidation(String str) {
        String password = "^.*(?=.{6,10})(?=.*[0-9])(?=.*[a-zA-Z]).*$"; //영문, 숫자조합 6~10자리
        Pattern pattern = Pattern.compile(password);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public void customOnClick(View view) throws JSONException {
        switch (view.getId()) {
            case R.id.sing_in_iv_sign_in:

                this.postLogIn();

                break;
            case R.id.sing_in_tv_find:
                startActivity(new Intent(getApplication(), MainActivity.class));
                break;
            case R.id.sing_in_tv_sing_up:
                startActivity(new Intent(getApplication(), SignUpActivity.class));
                break;
            default:
                break;
        }
    }

    void postLogIn() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("id", mEditTextEmail.getText().toString());
        params.put("pw", mEditTextPassword.getText().toString());

        //로딩 다이얼로그
        mDialog.show();

        final RetrofitInterface retrofitInterface = getRetrofit(this).create(RetrofitInterface.class);
        retrofitInterface.postLogin(RequestBody.create(params.toString(), MEDIA_TYPE_JSON)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull final Call<LoginResponse> call,
                                   @NonNull final Response<LoginResponse> response) {
//                hideProgressDialog();
                LoginResponse loginResponse = response.body();
                if (loginResponse == null) {
                    showCustomToast("응답 없음");
                }

                if (loginResponse.getCode() == 100) {
                    //로그인 성공
                    showCustomToast(loginResponse.getMessage());
                    userNo = loginResponse.getResult().getUserNo();
                    userName = loginResponse.getResult().getName();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showCustomToast("로그인 실패");
                }
                //로딩 끝
                mDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull final Call<LoginResponse> call,
                                  @NonNull final Throwable throwable) {
                //mDialog.dismiss();
                showCustomToast("연결 실패");
            }
        });
    }

}
