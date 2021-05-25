package com.pucpr.greyscale_tde;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView mImagemView;
    Button mEscolherBtn;
    Button mPretoeBrancoBtn;
    //Button mCamera_btn;

    private static final int IMAGE_PICK_CODE = 1;
    private static final int PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImagemView = findViewById(R.id.image_view);
        mEscolherBtn = findViewById(R.id.escolher_imagem_btn);
        mPretoeBrancoBtn = findViewById(R.id.greyscale_btn);
        //mCamera_btn = findViewById(R.id.camera_btn);

        //A camera no momento não está funcionando

        //if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //ActivityCompat.requestPermissions(MainActivity.this,
                    //new String[]{
                            //Manifest.permission.CAMERA
                    //},
                    //100);
                    //Bitmap captureImage = (Bitmap) data.getExtras().get("data");
                    //mImagemView.setImageBitmap(captureImage);
        //}

        //Botão escolher imagem da galeria
        mEscolherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verificar permissão de runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //necessidade de pedir permissão. requisição
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permissão já concedida
                        escolherImagemDaGalleria();
                    }
                } else {
                    //Sistema é inferior a marsmallow
                    escolherImagemDaGalleria();
                }
            }
        });

        mPretoeBrancoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageview = (ImageView) findViewById(R.id.image_view);
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageview.setColorFilter(filter);
            }
        });
    }

    private void escolherImagemDaGalleria(){
        //intent para pegar imagem
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    //lidar com o resultado da permissão de runtime

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissão garantida
                    escolherImagemDaGalleria();
                } else {
                    //permissão negada
                    Toast.makeText(this, "Permisão Negada", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //lidar com resultado da imagem selecionada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mImagemView.setImageURI(data.getData());
        }
    }

}