package com.lions.app.sportsstore.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.*;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.structs.UserSession;

import java.util.Arrays;


public class LoginScreen extends AppCompatActivity {

     private GoogleSignInClient mGoogleSignInClient;
     private int RC_SIGN_IN = 67;
     Button signInGoogle;
     LoginButton signInFacebook;
     private CallbackManager callbackManager;
     private FirebaseAuth mAuth;
     UserSession userSession;
    FirebaseAuth.AuthStateListener authStateListener;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
         FacebookSdk.sdkInitialize(getApplicationContext());
         AppEventsLogger.activateApp(this);
         userSession = new UserSession(getApplicationContext());
         callbackManager = CallbackManager.Factory.create();
         mAuth = FirebaseAuth.getInstance();
         authStateListener = new FirebaseAuth.AuthStateListener() {
             @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 if(firebaseAuth.getCurrentUser()==null)
                 {

                 }
                 else
                 {
                     userSession.CreateUser(firebaseAuth.getCurrentUser().getDisplayName(),firebaseAuth.getCurrentUser().getUid(),firebaseAuth.getCurrentUser().getPhotoUrl().toString(),firebaseAuth.getCurrentUser().getEmail());
                     startActivity(new Intent(LoginScreen.this,HomeScreen.class));
                     finish();
                 }
             }
         };

         mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
             @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

             }
         });


         signInFacebook = (LoginButton) findViewById(R.id.facebook_sign_in);
         signInGoogle = (Button)findViewById(R.id.google_sign_in);
        // Configure Google Sign In

         signInFacebook.setReadPermissions(Arrays.asList("public_profile","email"));
         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
         signInGoogle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 signIn();
             }
         });

         signInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
             @Override
             public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());

             }

             @Override
             public void onCancel() {

                 Log.d("facebook","cancel");
                 Toast.makeText(getApplicationContext(),"Login Cancelled, Try Again Later",Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onError(FacebookException error) {

                 Log.d("facebook_error",error.getMessage());
                 Toast.makeText(getApplicationContext(),"Error Occured, Try Again Later",Toast.LENGTH_SHORT).show();
             }
         });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void signIn() {
         Intent signInIntent = mGoogleSignInClient.getSignInIntent();
         startActivityForResult(signInIntent, RC_SIGN_IN);
     }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("google_signin", "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }



    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("google_firebase", "firebaseAuthWithGoogle:" + acct.getId());

        // [START_EXCLUDE silent]
        // showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("google_firebase", "signInWithCredential:onComplete:" + task.isSuccessful());


                        if (!task.isSuccessful()) {

                            Log.w("google_firebase", "signInWithCredential", task.getException());
                            Toast.makeText(LoginScreen.this, "Google Authentication failed. Try again.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else
                        {

                            Log.d("google_logged_in",acct.getEmail());

                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("accessToken", "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("facebook_success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userSession.CreateUser(user.getDisplayName(),user.getUid(),user.getPhotoUrl().toString(),user.getEmail());
                            startActivity(new Intent(LoginScreen.this,HomeScreen.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d( "signInfailure", task.getException().getMessage().toString());
                            Toast.makeText(LoginScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

}
