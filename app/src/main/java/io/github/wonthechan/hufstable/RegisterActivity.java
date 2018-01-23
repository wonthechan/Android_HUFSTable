package io.github.wonthechan.hufstable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by YeChan on 2018-01-22.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextRegisterEmail;
    private EditText editTextRegisterPassword;

    ProgressDialog asyncDialog;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        auth = FirebaseAuth.getInstance(); // 현재 auth 를 가져온다.

        asyncDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("잠시만 기다리세요...");

        // Views
        //mStatusTextView = findViewById(R.id.status);
        //mDetailTextView = findViewById(R.id.detail);
        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);

        // Buttons
        findViewById(R.id.emailRegisterButton).setOnClickListener(this);
        findViewById(R.id.goBackTextView).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.verify_email_button).setOnClickListener(this);
    }

    // 이메일 회원가입
    private void createAccount(String email, String password) {
        Log.d("TAG", "createAccount:" + email);
        // 사용자가 회원정보를 올바르게 입력했는지 검사한다.
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();
        asyncDialog.show();

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Toast.makeText(RegisterActivity.this, "회원가입 및 로그인 성공!",
                                    Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "이미 사용중인 이메일 입니다.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        asyncDialog.dismiss();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    // 올바른 회원정보를 입력했는지 확인하는 메소드
    private boolean validateForm() {
        boolean valid = true;

        String email = editTextRegisterEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextRegisterEmail.setError("Required.");
            valid = false;
        } else {
            editTextRegisterEmail.setError(null);
        }

        String password = editTextRegisterPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextRegisterPassword.setError("Required.");
            valid = false;
        } else {
            editTextRegisterPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.emailRegisterButton:
                createAccount(editTextRegisterEmail.getText().toString(), editTextRegisterPassword.getText().toString());
                break;
            case R.id.goBackTextView:
                finish(); // 그냥 finish()만 적으면 되나?
            default:
                break;
        }
    }
}
