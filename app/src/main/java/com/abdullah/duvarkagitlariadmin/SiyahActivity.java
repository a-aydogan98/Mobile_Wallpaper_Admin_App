package com.abdullah.duvarkagitlariadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class SiyahActivity extends AppCompatActivity {

    private Button btnSelect, btnUpload;
    private ImageView imageView;
    private Toolbar toolbar_siyah;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 22;

    DatabaseReference reference;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siyah);

        toolbar_siyah = findViewById(R.id.toolbar_siyah);
        setSupportActionBar(toolbar_siyah);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SelectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });
    }

    private void SelectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim seçiniz..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Yükleniyor...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("Siyah/"+ UUID.randomUUID().toString());

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {

                            Uri downloadUri = uri;

                            reference = FirebaseDatabase.getInstance().getReference("Siyah/" + UUID.randomUUID()).child("url");

                            reference.setValue(downloadUri.toString());
                        }
                    });

                    progressDialog.dismiss();

                    Toast.makeText(SiyahActivity.this, "Resim yüklendi.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(SiyahActivity.this, "Başarısız!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Yükleniyor " + (int)progress + "%");
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }
}