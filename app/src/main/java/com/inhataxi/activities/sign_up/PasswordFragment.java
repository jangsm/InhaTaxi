package com.inhataxi.activities.sign_up;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhataxi.R;
import com.inhataxi.activities.BaseFragment;
import com.inhataxi.activities.SchoolCertificationActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.inhataxi.activities.sign_up.SignUpActivity.mViewPagerSignUp;


public class PasswordFragment extends BaseFragment {

    EditText mEditTextPassword, mEditTextPasswordCheck;
    ImageView mImageViewDone;
    TextView mTextViewError, mTextViewTitle;
    InputMethodManager mInputMethodManager;
    int mIndex;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        setComponentView(view);
        mEditTextPasswordCheck.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    mInputMethodManager.hideSoftInputFromWindow(mEditTextPasswordCheck.getWindowToken(), 0);
                    checkValidation();
                }
                return false;
            }
        });
        mImageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        return view;
    }

    private void checkValidation() {
        if (mEditTextPassword.getText().toString().equals(mEditTextPasswordCheck.getText().toString())) {
            if (passwordValidation(mEditTextPassword.getText().toString())) {
                mTextViewError.setVisibility(View.INVISIBLE);
                sPassword = mEditTextPasswordCheck.getText().toString();
                Intent intent = new Intent(getContext(), SchoolCertificationActivity.class);
                intent.putExtra("id", sSignUpId);
                intent.putExtra("password", sPassword);
                startActivity(intent);
            } else {
                mTextViewError.setText("영문, 숫자를 포함한 6~10자리의 비밀번호만 가능합니다");
                mTextViewError.setVisibility(View.VISIBLE);
            }
        } else {
            mTextViewError.setText("비밀번호가 일치하지 않습니다");
            mTextViewError.setVisibility(View.VISIBLE);
        }
    }

    public boolean passwordValidation(String str) {
        String password = "^.*(?=.{6,10})(?=.*[0-9])(?=.*[a-zA-Z]).*$"; //영문, 숫자조합 6~10자리
        Pattern pattern = Pattern.compile(password);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    @Override
    public void setComponentView(View v) {
        mEditTextPassword = v.findViewById(R.id.fragment_password_et_password);
        mEditTextPasswordCheck = v.findViewById(R.id.fragment_password_et_password_check);
        mTextViewTitle = v.findViewById(R.id.fragment_password_tv_title);
        mTextViewTitle.setText(Html.fromHtml("<b>비밀번호</b>를<br>입력해주세요."));

        mImageViewDone = v.findViewById(R.id.basic_info_done);
        mTextViewError = v.findViewById(R.id.basic_info_tv_error);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
    }
}
