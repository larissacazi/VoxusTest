package zimmermann.larissa.dashboard;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;

import static java.security.AccessController.getContext;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mSignIn;
    private Button mGoogleSignIn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSignIn = (Button) findViewById(R.id.email_sign_in_button);
        mGoogleSignIn = (Button) findViewById(R.id.google_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        mSignIn.setOnClickListener((OnClickListener) this);
        mGoogleSignIn.setOnClickListener((OnClickListener) this);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    Toast.makeText(LoginActivity.this, "Success on Google Authentication.", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    //Call Dashboard activity
                    callDashboard();

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success:: User " + mAuth.getCurrentUser().toString());
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        });
    }

    private void signInAccount(String email, String password) {
        Log.d(TAG, "signInAccount:" + email);

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Success on Authentication.", Toast.LENGTH_SHORT).show();

                    //Call Dashboard activity
                    callDashboard();
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError("Required.");
            valid = false;
        }
        else {
            mEmailView.setError(null);
        }

        String password = mPasswordView.getText().toString();
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError("Required.");
            valid = false;
        }
        else if(password.length() < 6) {
            mPasswordView.setError("At least 6 characters.");
            valid = false;
        }

        return valid;
    }

    private void signInOrCreateAccount() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if(!validateForm()) {
            return;
        }

        String userEmail = mEmailView.getText().toString();

        mAuth.fetchProvidersForEmail(userEmail).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "task.getResult().getProviders().size() = " + task.getResult().getProviders().isEmpty());
                    if(task.getResult().getProviders().isEmpty()) {
                        Log.d(TAG, "Email is not registered!");
                        createAccount(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    }
                    else {
                        Log.d(TAG, "Email is registered!");
                        signInAccount(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    }
                }
            }
        });
    }

    private boolean sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if(user.isEmailVerified()) {
            Log.d(TAG, "sendEmailVerification:: Email already verified!");
            return true;
        }

        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(LoginActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return user.isEmailVerified();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        mAuth.signOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_sign_in_button) {
            Log.d(TAG, "Email sign in button pressed!");
            signInOrCreateAccount();
        }
        else if (i == R.id.google_sign_in) {
            Log.d(TAG, "GOOGLE Email sign in button pressed!");
            signIn();
        }

        hideKeyboard();
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void callDashboard() {
        Intent myIntent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}

