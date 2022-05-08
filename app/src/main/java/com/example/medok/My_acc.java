package com.example.medok;

import static com.example.medok.Medical.closeDrawer;
import static com.example.medok.Medical.redirectActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_acc extends AppCompatActivity {
    //Иницализация переменных
    DrawerLayout drawerLayout;
    EditText acc_name, acc_lastname, acc_age, acc_middlename, acc_snils, acc_polis;
    Button acc_save;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    //Меню
    Toolbar mainToolBar;
    ActionBarDrawerToggle drawerToggle;
    //Профиль
    CircleImageView setupImageProf;
    String user_id;
    private Uri mainImageURI = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acc);
        Toast.makeText(My_acc.this, "Загрузка...", Toast.LENGTH_SHORT).show();
        //Доступ к перменным
        drawerLayout = findViewById(R.id.drawer_layout);
        mainToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        acc_name = findViewById(R.id.acc_name);
        acc_lastname = findViewById(R.id.acc_lastname);
        acc_middlename = findViewById(R.id.acc_middlename);
        acc_snils = findViewById(R.id.acc_snils);
        acc_polis = findViewById(R.id.acc_polis);
        acc_age = findViewById(R.id.acc_age);
        acc_save = findViewById(R.id.acc_save);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        //Проверка прав и их выдача \ Старт загрузки фото
        setupImageProf = findViewById(R.id.profile_image);
        setupImageProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(My_acc.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(My_acc.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();

                    }
                } else {
                    BringImagePicker();

                }
            }
        });



        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("CheckResult")
            @Override

            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        String age = task.getResult().getString("user_age");
                        String image = task.getResult().getString("user_image");
                        String lastname = task.getResult().getString("user_lastname");
                        String name = task.getResult().getString("user_name");
                        String middlename = task.getResult().getString("user_middlename");
                        String snils = task.getResult().getString("user_snils");
                        String polis = task.getResult().getString("user_polis");


                        acc_name.setText(name);
                        acc_age.setText(age);
                        acc_lastname.setText(lastname);
                        acc_middlename.setText(middlename);
                        acc_polis.setText(polis);
                        acc_snils.setText(snils);

                        if (TextUtils.isEmpty(name) | TextUtils.isEmpty(lastname)){
                            acc_save.setVisibility(View.VISIBLE);
                        }else {
                            acc_name.setEnabled(false);
                            acc_lastname.setEnabled(false);
                            acc_age.setEnabled(false);
                            setupImageProf.setEnabled(false);
                        }
                        //Вставка фото через сторонюю библиотеку
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.def_prof);
                        Glide.with(My_acc.this).setDefaultRequestOptions(requestOptions).load(image).into(setupImageProf);

                    }
                } else {
                    Toast.makeText(My_acc.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //При нажатии на иконку профиля
        acc_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(My_acc.this, "Загрузка данных...", Toast.LENGTH_SHORT).show();
                String user_name = acc_name.getText().toString();
                String user_lastname = acc_lastname.getText().toString();
                String user_middlename = acc_middlename.getText().toString();
                String user_age = acc_age.getText().toString();
                String user_snils = acc_snils.getText().toString();
                String user_polis = acc_polis.getText().toString();
                // && !TextUtils.isEmpty(user_age) && mainImageURI!= null
                if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_lastname)){
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    StorageReference image_path = storageReference.child("profile_image").child(user_id+".jpg");
                    image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                //словарь Map
                                Map<String, String> productMap = new HashMap<>();
                                image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        productMap.put("user_name", user_name);
                                        productMap.put("user_lastname", user_lastname);
                                        productMap.put("user_middlename", user_middlename);
                                        productMap.put("user_snils", user_snils);
                                        productMap.put("user_polis", user_polis);
                                        productMap.put("user_age", user_age);
                                        productMap.put("user_image", uri.toString());
                                        firebaseFirestore.collection("Users").document(user_id).set(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(My_acc.this, "Данные загружены", Toast.LENGTH_SHORT).show();
                                                    acc_save.setVisibility(View.INVISIBLE);
                                                }else { Toast.makeText(My_acc.this, "Ошибка БД", Toast.LENGTH_SHORT).show(); }
                                            }
                                        });
                                    }
                                });
                            } else { Toast.makeText(My_acc.this, "Ошибка", Toast.LENGTH_SHORT).show(); }
                        }
                    });
                }else { Toast.makeText(My_acc.this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show();

                        }

            }
        });

        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("Мой профиль");
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolBar, R.string.app_name,
                R.string.app_name);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
    }

    //Вызов фуннкции посторения изображения
    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(My_acc.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setupImageProf.setImageURI(mainImageURI);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            case R.id.acc_red:
                acc_save.setVisibility(View.VISIBLE);
                acc_name.setEnabled(true);
                acc_lastname.setEnabled(true);
                acc_middlename.setEnabled(true);
                acc_polis.setEnabled(true);
                acc_snils.setEnabled(true);
                acc_age.setEnabled(true);
                setupImageProf.setEnabled(true);
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    public void  ClickMyAcc(View view){
        closeDrawer(drawerLayout);
    }
    public void ClickMedical(View view){
        redirectActivity(this, Medical.class);
    }
    public void ClickCart(View view){
        redirectActivity(this, Zapisi.class);
    }
    public void ClickToGo(View view){
        redirectActivity(this, Togo.class);
    }
    public  void ClickLogout(View view){
        Medical.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Закртые панели навигации
        closeDrawer(drawerLayout);
    }
}