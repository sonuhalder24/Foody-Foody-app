package com.example.myfood;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    ImageView imageView1,imageView2;
    View v;
    StorageReference storageReference;
    SharedPreferences.Editor editor;
    SharedPreferences preferences,pref;
    int  position,val=0;
    BottomSheetDialog bottomSheetDialog;
    ByteArrayOutputStream byteArrayOutputStream;
    DatabaseReference fdb;
    ProgressDialog dialognew;
    String location;
    DrawerLayout drawerLayout;
    TextView tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LodeLocale();
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        drawerLayout=findViewById(R.id.drawer_layout);
        preferences=getSharedPreferences("Mode",MODE_PRIVATE);
        editor=preferences.edit();

        pref=getSharedPreferences("Email",MODE_PRIVATE);
        location=pref.getString("mail","abcd@gmail.com");

        for(int i=0;i<location.length();i++){
            int check=location.charAt(i);
            if((check>=32&&check<=47)||(check>=58&&check<=64)){
                break;
            }
            else{
                val++;
            }
        }

        boolean bool=preferences.getBoolean("mode",false);
        if(bool==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    recreate();
                    break;
            }
            position=1;
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    recreate();
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    break;
            }
            position=0;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.snackbar, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action, null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_snacks,R.id.nav_softDrinks)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id=destination.getId();
                switch (id){
                    case R.id.nav_home:
                        Toast.makeText(MainActivity2.this, R.string.veg_clicked, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_gallery:
                        Toast.makeText(MainActivity2.this, R.string.nonveg_clicked, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_slideshow:
                        Toast.makeText(MainActivity2.this, R.string.sweets_clicked, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_snacks:
                        Toast.makeText(MainActivity2.this, R.string.snacks_clicked, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_softDrinks:
                        Toast.makeText(MainActivity2.this, R.string.softdrinks_clicked, Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
        v=navigationView.getHeaderView(0);

        tv1=v.findViewById(R.id.person);
        tv2=v.findViewById(R.id.person_email);
        imageView1=v.findViewById(R.id.imageView1);
        imageView2=v.findViewById(R.id.imageView2);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(location.substring(0,val)).child("imageUrl");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    String ss=dataSnapshot.getValue().toString();
                    if(ss.isEmpty()==false){
                        Picasso.get().load(ss).placeholder(R.drawable.whitem).into(imageView1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity2.this, "Loading of image failed", Toast.LENGTH_SHORT).show();
            }
        });

        tv2.setText(location);
        DatabaseReference name=FirebaseDatabase.getInstance().getReference().child(location.substring(0,val)).child("name");
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    String s=snapshot.getValue().toString();
                    if(s.isEmpty()==false){
                        tv1.setText(s);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView2.setOnClickListener(this);
        bottomSheetDialog=new BottomSheetDialog(MainActivity2.this);
        View view=getLayoutInflater().inflate(R.layout.bottom_sheet,null);
        bottomSheetDialog.setContentView(view);
        View iv1=view.findViewById(R.id.iv1);
        View iv2=view.findViewById(R.id.iv2);
        View iv3=view.findViewById(R.id.iv3);
        iv1.setOnClickListener( this);
        iv2.setOnClickListener( this);
        iv3.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.imageView2:

                bottomSheetDialog.show();
                break;
            case R.id.iv1:
                imageView1.setImageResource(R.drawable.whitem);

                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(R.drawable.whitem)
                        + '/' + getResources().getResourceTypeName(R.drawable.whitem)
                        + '/' + getResources().getResourceEntryName(R.drawable.whitem) );
                uploadimage(imageUri,getString(R.string.deleting));

                break;
            case R.id.iv2:
                Intent intent1=new Intent();
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");
                startActivityForResult(intent1,200);
                break;
            case R.id.iv3:
                if(ContextCompat.checkSelfPermission(MainActivity2.this,
                        Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity2.this,
                            new String[]{
                                    Manifest.permission.CAMERA
                            },
                            300);
                }
                else{
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,100);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==200&&resultCode==RESULT_OK){
            Uri imageUri=data.getData();
            imageView1.setImageURI(imageUri);
           uploadimage(imageUri,getString(R.string.uploading));
        }

        else if(requestCode == 100&&resultCode==RESULT_OK){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView1.setImageBitmap(captureImage);

            byteArrayOutputStream=new ByteArrayOutputStream();
            captureImage.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte []dat=byteArrayOutputStream.toByteArray();

            storageReference= FirebaseStorage.getInstance().getReference().child(location.substring(0,val)).child("image");
            dialognew=new ProgressDialog(this);
            dialognew.setTitle(R.string.uploading);
            dialognew.show();
            StorageTask storageTask=storageReference.putBytes(dat);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isComplete()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    dialognew.dismiss();
                    bottomSheetDialog.dismiss();
                    Uri downloaduri=task.getResult();
                    String Duri=downloaduri.toString();

                    fdb= FirebaseDatabase.getInstance().getReference().child(location.substring(0,val)).child("imageUrl");
                    fdb.setValue(Duri);
                }
            });


        }

        super.onActivityResult(requestCode, resultCode, data);

    }
    void uploadimage(Uri uri,String msg){
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle(msg);
        dialog.show();
        storageReference= FirebaseStorage.getInstance().getReference().child(location.substring(0,val)).child("image");
        StorageTask task=storageReference.putFile(uri);
        task.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                dialog.dismiss();
                bottomSheetDialog.dismiss();
                Uri downloadUri=task.getResult();
                String uri=downloadUri.toString();
                DatabaseReference dbf= FirebaseDatabase.getInstance().getReference().child(location.substring(0,val)).child("imageUrl");
                dbf.setValue(uri);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_languages:
                showChangeLanguageDialog();
                break;
            case R.id.action_mode:
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity2.this);
                String[] list = {"Light", "Dark"};
                mbuilder.setTitle("Choose Theme");
                mbuilder.setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;

                    }
                })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position == 0) {
                                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                        editor.putBoolean("mode", false);
                                        editor.apply();
                                        recreate();
                                    }


                                } else {

                                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                        editor.putBoolean("mode", true);
                                        editor.apply();
                                        recreate();
                                    }
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog mDialog = mbuilder.create();
                mDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showChangeLanguageDialog() {
        final String[]ListItems ={"English","हिन्दी","বাংলা"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity2.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(ListItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    setLocale("en");
                    recreate();
                }
                else if(which==1){
                    setLocale("hi");
                    recreate();
                }
                else if(which==2){
                    setLocale("bn");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog=mBuilder.create();
        mDialog.show();
    }
    private void setLocale(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My Lang",lang);
        editor.apply();
    }
    public void LodeLocale(){
        SharedPreferences preferences=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=preferences.getString("My Lang","");
        setLocale(language);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
   public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
           int uiMode = overrideConfiguration.uiMode;
           overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
             overrideConfiguration.uiMode = uiMode;
       }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }

    }
}