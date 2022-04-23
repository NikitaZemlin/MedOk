package com.example.medok;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Togo extends AppCompatActivity {
    DrawerLayout drawerLayout;
    //Меню
    Toolbar mainToolBar;
    ActionBarDrawerToggle drawerToggle;
    //БД
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_togo);
        //Доступ к переменным
        drawerLayout = findViewById(R.id.drawer_Layout);
        //БД
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        mainToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("Путь");
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolBar, R.string.app_name,
                R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

    }


    //Гамбургер
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    //Метод открытия панели навигации
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Закрытие панели навигации
        closeDrawer(drawerLayout);
    }
    //Метод закрытия панели навигации
    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Проверяем состояние
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //Когда панель навигации открыта закрываем её
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        //Закрываем меню
        closeDrawer(drawerLayout);
    }

    public void ClickMyAcc(View view){
        //Перевыбираем окно на мой аккаунт
        redirectActivity(this, My_acc.class);
    }

    public void ClickCart(View view){
        //Перевыбираем окно на корзину
        redirectActivity(this, Zapisi.class);
    }

    public void ClickSettings(View view){
        //Перевыбираем окно на доску
        redirectActivity(this, Togo.class);
    }


    public void ClickLogout(View view){
        //закрытие приложения
        logout(this);
    }

    public static void logout(final Activity activity) {
        //Иницализация диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Устновка заголовка
        builder.setTitle("Выход");
        //Установка сообщения
        builder.setMessage("Вы действительно хотите выйти ?");
        //Кнопка ДА
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Закрываем окно
                activity.finishAffinity();
                //Выход из приложения
                System.exit(0);
            }
        });
        //Кнопка НЕТ
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Заркываем диалог
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Инициализация намерения
        Intent intent  = new Intent(activity, aClass);
        //Установка флага
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Запускаем новое окно
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Заркываем панель
        closeDrawer(drawerLayout);
    }

}