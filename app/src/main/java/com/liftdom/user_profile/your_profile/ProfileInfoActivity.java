package com.liftdom.user_profile.your_profile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import id.zelory.compressor.Compressor;
import org.joda.time.LocalDate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileInfoActivity extends AppCompatActivity {

    @BindView(R.id.usernameEditText) EditText usernameEditText;
    @BindView(R.id.usernameTextView) TextView usernameTextView;
    @BindView(R.id.bodyWeightEditText) EditText bodyWeightEditText;
    @BindView(R.id.heightFeet) EditText heightFeet;
    @BindView(R.id.heightInches) EditText heightInches;
    @BindView(R.id.benchPress1rm) EditText benchPress1rm;
    @BindView(R.id.squat1rm) EditText squat1rm;
    @BindView(R.id.deadlift1rm) EditText deadlift1rm;
    @BindView(R.id.saveButtonProfileSettings) Button saveButton;
    @BindView(R.id.currentFocus) Spinner currentFocusSpinner;
    @BindView(R.id.ageYears) EditText ageEditText;
    @BindView(R.id.weightUnitView) TextView weightUnitView;
    @BindView(R.id.heightCmEdit) EditText heightCmEdit;
    @BindView(R.id.heightCmText) TextView heightCmText;
    @BindView(R.id.feetTextView) TextView feetTextView;
    @BindView(R.id.inchesTextView) TextView inchesTextView;
    @BindView(R.id.profilePicImageView) ImageView profilePicView;
    @BindView(R.id.headerImageView) ImageView headerImageView;
    @BindView(R.id.profilePicLoadingView) AVLoadingIndicatorView profilePicLoadingView;

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private String selectedProfilePicPath;
    private static final int IMAGE_VARIABLE = 11;
    private Context mContext;

    String email = "error";
    private static final String TAG = "EmailPassword";

    UserModelClass userModelClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_stats);

        ButterKnife.bind(this);


        //TODO: Make sure this is using identical units as set in Settings

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
        }

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    email = user.getEmail();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(ProfileInfoActivity.this, SignInActivity.class));
                }
            }
        };
        // [END auth_state_listener]

        //currentFocusSpinner.setOnItemSelectedListener(this);

        List<String> focuses = new ArrayList<String>();
        focuses.add("Bodybuilding");
        focuses.add("Powerlifting");
        focuses.add("Powerbuilding");
        focuses.add("General Weightlifting");
        focuses.add("General Fitness");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_style_new_1,
                focuses);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        currentFocusSpinner.setAdapter(dataAdapter);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        usernameTextView.setText(sharedPref.getString("userName", "loading..."));
        usernameTextView.setTypeface(lobster);

        final DatabaseReference userRef = mRootRef.child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModelClass = dataSnapshot.getValue(UserModelClass.class);

                String currentFocus = userModelClass.getCurrentFocus();
                if(currentFocus.equals("Bodybuilding")){
                    currentFocusSpinner.setSelection(0);
                }else if(currentFocus.equals("Powerlifting")){
                    currentFocusSpinner.setSelection(1);
                }else if(currentFocus.equals("Powerbuilding")){
                    currentFocusSpinner.setSelection(2);
                }else if(currentFocus.equals("General Weightlifting")){
                    currentFocusSpinner.setSelection(3);
                }else if(currentFocus.equals("General Fitness")){
                    currentFocusSpinner.setSelection(4);
                }

                ageEditText.setText(userModelClass.getAge());

                if(userModelClass.isIsImperial()){
                    // imperial
                    inchesTextView.setVisibility(View.VISIBLE);
                    feetTextView.setVisibility(View.VISIBLE);
                    heightInches.setVisibility(View.VISIBLE);
                    heightFeet.setVisibility(View.VISIBLE);
                    weightUnitView.setText("lbs");
                    bodyWeightEditText.setText(userModelClass.getPounds());
                    heightCmEdit.setVisibility(View.GONE);
                    heightCmText.setVisibility(View.GONE);
                    String[] heightTokens = userModelClass.getFeetInchesHeight().split("_");
                    heightFeet.setText(heightTokens[0]);
                    heightInches.setText(heightTokens[1]);
                }else{
                    // metric
                    inchesTextView.setVisibility(View.GONE);
                    feetTextView.setVisibility(View.GONE);
                    heightInches.setVisibility(View.GONE);
                    heightFeet.setVisibility(View.GONE);
                    weightUnitView.setText("kgs");
                    bodyWeightEditText.setText(userModelClass.getKgs());
                    heightCmEdit.setVisibility(View.VISIBLE);
                    heightCmText.setVisibility(View.VISIBLE);
                    heightCmEdit.setText(userModelClass.getCmHeight());
                }

                //TODO: get maxes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String currentFocus = currentFocusSpinner.getSelectedItem().toString();
                String bodyweight = bodyWeightEditText.getText().toString();
                String height = heightFeet.getText().toString() + "_" + heightInches.getText().toString();
                String age = ageEditText.getText().toString();

                String benchMax = benchPress1rm.getText().toString();
                String squatMax = squat1rm.getText().toString();
                String deadliftMax = deadlift1rm.getText().toString();

                //userRef1.child("maxes").child("benchMax").setValue(benchMax);
                //userRef1.child("maxes").child("squatMax").setValue(squatMax);
                //userRef1.child("maxes").child("deadliftMax").setValue(deadliftMax);

                userModelClass.setCurrentFocus(currentFocus);
                if(userModelClass.isIsImperial()){
                    userModelClass.setPounds(bodyweight);
                    userModelClass.setFeetInchesHeight(height);
                    userModelClass.setAge(age);
                    userModelClass.updateUnits(true);
                }else{
                    userModelClass.setKgs(bodyweight);
                    userModelClass.setCmHeight(heightCmEdit.getText().toString());
                    userModelClass.setAge(age);
                    userModelClass.updateUnits(false);
                }

                userRef.setValue(userModelClass);

                Intent intent = new Intent(v.getContext(), CurrentUserProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        profilePicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilePicView.setVisibility(View.GONE);
                profilePicLoadingView.setVisibility(View.VISIBLE);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){

            Uri selectedImageUri = data.getData();
            Uri file = Uri.fromFile(new File(selectedImageUri.getPath()));
            File imageFile = new File(selectedImageUri.getPath());

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profilePicRef = storageRef.child("images/user/" + uid + "/profilePic.png");

            try{
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Matrix matrix = new Matrix();
                matrix.setRectToRect(new RectF(0, 0, bmp.getWidth(), bmp.getHeight()), new RectF(0, 0, 200, 200),
                        Matrix.ScaleToFit.CENTER);
                //Bitmap resized = Bitmap.createScaledBitmap(bmp, 180, 180, true);
                Bitmap resized = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());

                UploadTask uploadTask = profilePicRef.putStream(inputStream);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Glide.with(getApplicationContext()).load(downloadUrl).into(profilePicView);
                        profilePicView.setVisibility(View.VISIBLE);
                        profilePicLoadingView.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("firebase", "upload failed");
                        profilePicLoadingView.setVisibility(View.GONE);
                        profilePicView.setVisibility(View.VISIBLE);
                    }
                });

            } catch(IOException e){

            }
        }
    }

    public String getPath(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    @Override
    public void onBackPressed(){

        //TODO: Have these things only called if changes are made
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set title
        builder.setTitle("Discard changes?");

        // set dialog message
        builder
                .setMessage("Are you sure you want to discard these changes?")
                .setCancelable(false)
                .setPositiveButton("Discard",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        Intent intent = new Intent(ProfileInfoActivity.this, CurrentUserProfile.class);
                        startActivity(intent);

                        //finish();
                    }
                })
                .setNegativeButton("Continue",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();
    }
}
