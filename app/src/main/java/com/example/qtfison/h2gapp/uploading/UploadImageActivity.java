package com.example.qtfison.h2gapp.uploading;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.UtilFunctions;
import com.example.qtfison.h2gapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class UploadImageActivity extends AppCompatActivity {

    Button btnUpload,btnChooseImg;
    EditText edtImageName;
    TextView txtImgUploaded;
    ProgressBar progressBar;
    ImageView receiptImage;
    private Uri imageUri;
    private static int PICK_IMAGE_REQUEST=1;
    private StorageTask mUploadTask;
    DatabaseReference mDatabaseRef;
    StorageReference mStorageRef;
    StorageReference fileReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload_layout);
        btnUpload=findViewById(R.id.btn_Upload);
        btnChooseImg=findViewById(R.id.btn_choose);
        edtImageName=findViewById(R.id.edt_file_name);
        txtImgUploaded=findViewById(R.id.txt_view_Uplaoad);
        progressBar=findViewById(R.id.progressBar);
        receiptImage=findViewById(R.id.img_upload);
        mStorageRef=FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef=FirebaseDatabase.getInstance().getReference("uploads");
        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    btnUpload.setVisibility(View.INVISIBLE);
                    uploadFile();
            }
        });
        txtImgUploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(),ShowUploadsActivity.class);
                startActivity(i);
            }
        });
    }
    private String getExtensions(Uri uri) {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if(imageUri !=null){
            /*
            final StorageReference fileReference=mStorageRef.child(UtilFunctions.getUniqueId()+"."+getExtensions(imageUri));
            mUploadTask=fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            },500);
                            Toast.makeText(getBaseContext(),"Receipt uploaded successful",Toast.LENGTH_SHORT).show();
                            Upload upload=new Upload(edtImageName.getText().toString().trim(),fileReference.getDownloadUrl().toString());
                            mDatabaseRef.push().setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });
                    */
            fileReference=mStorageRef.child(UtilFunctions.getUniqueId()+"."+getExtensions(imageUri));
            fileReference.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            })

                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                                btnUpload.setVisibility(View.VISIBLE);
                            }
                        },500);
                        Uri downUri = task.getResult();
                       // Log.d(TAG, "onComplete: Url: "+ downUri.toString());
                        Toast.makeText(getBaseContext(),"Receipt uploaded successful",Toast.LENGTH_SHORT).show();
                        Upload upload=new Upload(edtImageName.getText().toString().trim(),downUri.toString());
                        mDatabaseRef.push().setValue(upload);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
        && data!=null && data.getData()!=null){
                imageUri=data.getData();
                if(imageUri!=null)
                    Toast.makeText(this,"okey",Toast.LENGTH_SHORT).show();
                 Picasso.with(this).load(imageUri).into(receiptImage);
           // Toast.makeText(getBaseContext(),"okey2",Toast.LENGTH_SHORT).show();
            //receiptImage.setImageURI(imageUri);

        }
    }
}
