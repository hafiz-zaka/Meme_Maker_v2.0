package com.mememaker.mememakerpro.creatememe.activity;


import static android.graphics.Typeface.SANS_SERIF;
import static com.mememaker.mememakerpro.creatememe.Common.load;
import static com.mememaker.mememakerpro.creatememe.Common.mInterstitialAd;
import static com.mememaker.mememakerpro.creatememe.Constant.ADMOB_TEST_AD;
import static com.mememaker.mememakerpro.creatememe.Constant.BANNER;
import static com.mememaker.mememakerpro.creatememe.Constant.EDIT_ACT;
import static com.mememaker.mememakerpro.creatememe.Constant.EXAMPLE;
import static com.mememaker.mememakerpro.creatememe.Constant.INTERSTITIAL;
import static com.mememaker.mememakerpro.creatememe.Constant.SAVE;
import static com.mememaker.mememakerpro.creatememe.Constant.SHAPE;
import static com.mememaker.mememakerpro.creatememe.Constant.UNDO;
import static com.mememaker.mememakerpro.creatememe.activity.CategoriesDisplayAct.categoriesDisplayAct;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appbrain.InterstitialBuilder;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mememaker.mememakerpro.creatememe.BuildConfig;
import com.mememaker.mememakerpro.creatememe.ClearEditText;
import com.mememaker.mememakerpro.creatememe.ItemDecoration;
import com.mememaker.mememakerpro.creatememe.OutlineSpan;
import com.mememaker.mememakerpro.creatememe.R;
import com.mememaker.mememakerpro.creatememe.SavedImages;
import com.mememaker.mememakerpro.creatememe.SharePrefConfig;
import com.mememaker.mememakerpro.creatememe.adapters.MenuAdapter;
import com.mememaker.mememakerpro.creatememe.adapters.StickerAdapter;
import com.mememaker.mememakerpro.creatememe.adsManager.AdId;
import com.mememaker.mememakerpro.creatememe.adsManager.RafaqatAdMobManager;
import com.mememaker.mememakerpro.creatememe.customize.ClipArt;
import com.mememaker.mememakerpro.creatememe.customize.ClipArt_Example;
import com.mememaker.mememakerpro.creatememe.customize.ClipArt_Image;
import com.mememaker.mememakerpro.creatememe.databinding.ActivityEditBinding;
import com.mememaker.mememakerpro.creatememe.model.AdModel;
import com.mememaker.mememakerpro.creatememe.model.MenuItem;
import com.mememaker.mememakerpro.creatememe.model.StickerItem;
import com.mememaker.mememakerpro.creatememe.myinterface.MenuItemClick;
import com.mememaker.mememakerpro.creatememe.myinterface.StickerItemClick;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dmax.dialog.SpotsDialog;

import yuku.ambilwarna.AmbilWarnaDialog;

/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */

public class EditActivity extends AppCompatActivity implements MenuItemClick, StickerItemClick {

    private static ActivityEditBinding editBinding;

    private static int retrieveImage;
    private final String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    private final int permissionCode = 9090;
    private static int defaultColor;

    private MenuAdapter menuAdapter;
    private StickerAdapter stickerAdapter;
    private int count = 1000;
    private final ArrayList<View> list = new ArrayList<>();
    private String which_text = "example1";
    private boolean mobilePackageResponse;

    private ClipArt_Example clipArt_example, clipArt_example2, clipArt_example3, clipArt_example4, clipArt_example5;


    private String storeExValue = "";

    SpotsDialog progressDialog;

    CountDownTimer countDownTimer;
    private long COUNTER_TIME = 5L;
    private long COUNTER_TIME_2 = 2L;
    DatabaseReference ref;
    boolean packageCheck, newCheckValue;
    String shapeAndStickerValueChecker = "sticker";

    String spinnerSelectedItemName;

    float x, y;
    private long COUNTER_TIME_AD = 5L;

    private String tempName;
    private boolean permissionCheck=false;
    private ArrayList<Uri> saveUri;
    public static String fontName="";
    private InterstitialBuilder interstitialBuilder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit);

        interstitialBuilder = InterstitialBuilder.create()
                .setAdId(com.appbrain.AdId.LEVEL_COMPLETE)
                .setOnDoneCallback(new Runnable() {
                    @Override
                    public void run() {
                        // Preload again, so we can use interstitialBuilder again.
                        interstitialBuilder.preload(EditActivity.this);

                    }
                })
                .preload(this);
//        if (permissionCheck)
//        {
//
//        }
//        else {
//            //Permission
//            runTimePermission();
//        }


        saveUri=SharePrefConfig.Companion.getUriListFromSharedPreferences(this);

        ref = FirebaseDatabase.getInstance().getReference().child(ADMOB_TEST_AD);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (dataSnapshot.getKey().equals(EDIT_ACT)) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                AdModel model = dataSnapshot1.getValue(AdModel.class);
                                if (dataSnapshot1.getKey().equals(BANNER)) {
                                    if (model != null && dataSnapshot1.getKey()!= null) {
                                        String status, adId;
                                        status = model.getStatus();
                                        if (BuildConfig.DEBUG) {
                                            adId=AdId.AD_MOB_BANNER_TEST;
                                        } else {
                                            adId=model.getAdId();
                                        }

                                        if (status.equals("true")) {
                                            RafaqatAdMobManager.getInstance().loadBanner(EditActivity.this, editBinding.bannerLayout,adId);
                                        }
                                    }

                                }

                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      //This code work for pic image from gallery or camera and retrieve image from MainActivity
        try {
            String picImage = getIntent().getStringExtra("main");
            Uri picImgUri = Uri.parse(picImage);
            if (picImgUri != null) {
                editBinding.i.setImageURI(picImgUri);
            }
        } catch (Exception e) {
            Log.i("rafaqat", "onCreate: " + e.getMessage());
        }

        //These Code Work For Meme Retrieve from Main Activity
        Uri urii = getIntent().getParcelableExtra("key");
        tempName = getIntent().getStringExtra("tempName");

        if (urii!=null && tempName!=null)
        {
            Glide.with(this)
                    .load(urii)
                    .into(editBinding.i);

            //Default Example Show Functionality
             newDefaultExampleShow();
        }






        //Back btn Click
        editBinding.topbarlayout.back.setOnClickListener(view -> onBackPressed());

        //Add text Click
        editBinding.addText.setOnClickListener(view -> addText());


        //Color get And Set on Txt
        defaultColor = ContextCompat.getColor(this, R.color.orange);
        editBinding.textColorPicker.setOnClickListener(view -> {
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {

                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    defaultColor = color;

                    if (storeExValue.equals("SimpleText")) {
                        ClipArt.getSelectedText().getTextView().setTextColor(defaultColor);
                    } else if (storeExValue.equals("ExampleText")) {
                        ClipArt_Example.getSelectedText().getTextView().setTextColor(defaultColor);

                    }
                }
            });
            dialog.show();
        });


        //Main Menu Listener
        adapterInit(this, menuAdapter, editBinding.menuRecycleView, populateData());

        //Sticker Listener
        adapterInitForSticker(this, stickerAdapter, editBinding.stickerRecycleView, stickerItems());
        adapterInitForShape(this, stickerAdapter, editBinding.shapeRecycleView, shapeItems());
        ItemDecoration itemDecoration = new ItemDecoration(6);
        editBinding.menuRecycleView.addItemDecoration(itemDecoration);


        //EditText Listener
        editBinding.edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (storeExValue.equals("SimpleText")) {
                    ClipArt.getSelectedText().setText(charSequence.toString());
                } else if (storeExValue.equals("ExampleText")) {
                    ClipArt_Example.getSelectedText().setText(charSequence.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Font Listener PopUp
        editBinding.font.setOnClickListener(view -> fontsettings_popup());


        //Text Size Change Listener
        changeTextSize();

        //Text Style Change
        textStyleFun();

        // Done Listener
        editBinding.donebtn.setOnClickListener(view -> {

            editBinding.fontSettingCardView.setVisibility(View.GONE);
            switch (spinnerSelectedItemName) {
                case "Default":
                    Typeface typeface1 = Typeface.create(SANS_SERIF, Typeface.BOLD);
                    break;
                case "Abeezee":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.abeezee);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.abeezee);
                    }

                    break;
                case "Acme":
                    if (storeExValue.equals("SimpleText")) {

                        selectedTextFontFun(R.font.acme);
                    } else if (storeExValue.equals("ExampleText")) {
                        selectedExampleFontFun(R.font.acme);
                    }
                    break;
                case "Aclonica":

                    if (storeExValue.equals("SimpleText")) {

                        selectedTextFontFun(R.font.aclonica);

                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.aclonica);
                    }
                    break;
                case "Akaya Telivigala":

                    if (storeExValue.equals("SimpleText")) {

                        selectedTextFontFun(R.font.akaya_telivigala);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.akaya_telivigala);
                    }
                    break;
                case "Bungee Shade":

                    if (storeExValue.equals("SimpleText")) {

                        selectedTextFontFun(R.font.bungee_shade);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.bungee_shade);
                    }
                    break;
                case "Creepster":

                    if (storeExValue.equals("SimpleText")) {

                        selectedTextFontFun(R.font.creepster);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.creepster);
                    }
                    break;
                case "Homemade Apple":

                    if (storeExValue.equals("SimpleText")) {

                        selectedTextFontFun(R.font.homemade_apple);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.homemade_apple);
                    }
                    break;
                case "Faster One":

                    if (storeExValue.equals("SimpleText")) {

                        selectedTextFontFun(R.font.faster_one);
                    } else if (storeExValue.equals("ExampleText")) {
                        selectedExampleFontFun(R.font.faster_one);
                    }
                    break;
                case "Monoton":

                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.monoton);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.monoton);
                    }
                    break;


                    //............................New Font Add
                case "Agent Orange":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.agentorange);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.agentorange);
                    }
                    break;
                case "Aldrich":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.aldrich);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.aldrich);
                    }
                    break;
                case "Barberians":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.barberians);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.barberians);
                    }
                    break;
                case "Black":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.black);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.black);
                    }
                    break;
                case "Black City Bold":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.blackcityb);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.blackcityb);
                    }
                    break;
                case "Black City Normal":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.blackcityn);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.blackcityn);
                    }
                    break;
                case "Bone Chiller Free":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.bonechillerfree);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.bonechillerfree);
                    }
                    break;
                case "Bone Chiller Free Bold":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.bonechillerfreebold);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.bonechillerfreebold);
                    }
                    break;
                case "Challenge Contour":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.challengecontour);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.challengecontour);
                    }
                    break;
                case "Challenge Shadow":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.challengeshadow);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.challengeshadow);
                    }
                    break;
                case "Hrinkes Decor Personal":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.hrinkesdecorpersonal);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.hrinkesdecorpersonal);
                    }
                    break;
                case "krinkes Regular Personal":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.krinkesregularpersonal);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.krinkesregularpersonal);
                    }
                    break;
                case "Milky White":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.milkywhite);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.milkywhite);
                    }
                    break;
                case "Monserga Regular":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.monserga_regular_ffp);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.monserga_regular_ffp);
                    }
                    break;
                case "My Girl Retro":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.mygirlretro);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.mygirlretro);
                    }
                    break;
                case "PlayFull":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.playfull);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.playfull);
                    }
                    break;
                case "Snacker Comic":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.snackercomic_perosnaluseonly);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.snackercomic_perosnaluseonly);
                    }
                    break;
                case "Start Out":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.starout);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.starout);
                    }
                    break;
                case "Tuskergrotesk 6700 Bold":
                    if (storeExValue.equals("SimpleText")) {
                        selectedTextFontFun(R.font.tuskergrotesk_6700bold);
                    } else if (storeExValue.equals("ExampleText")) {

                        selectedExampleFontFun(R.font.tuskergrotesk_6700bold);
                    }
                    break;


            }
        });

        //Out Side Click Disable All Frame
        editBinding.framelayout.setOnClickListener(view -> {
            disableall();
            editBinding.mid.setVisibility(View.INVISIBLE);
            editBinding.shapeColorPicker.setVisibility(View.INVISIBLE);
            editBinding.shapeRecycleView.setVisibility(View.INVISIBLE);
            editBinding.cancelShapeRecycelView.setVisibility(View.INVISIBLE);
            editBinding.sendToFront.setVisibility(View.INVISIBLE);
            editBinding.stickerRecycleView.setVisibility(View.INVISIBLE);
            editBinding.cancelStckerRecycelView.setVisibility(View.INVISIBLE);
            editBinding.fontSettingCardView.setVisibility(View.INVISIBLE);
        });

        //Sticker Listener
        editBinding.stickerPicker.setOnClickListener(view -> stickerShow());


        //Internet is Available then Show this Dialog
        progressDialog = new SpotsDialog(EditActivity.this, R.style.Custom);
        progressDialog.setCanceledOnTouchOutside(false);




        editBinding.sendToFront.setOnClickListener(v -> {
            try {
                switch (storeExValue) {
                    case "":
                        Toast.makeText(this, "Please Select Text", Toast.LENGTH_SHORT).show();
                        break;
                    case "SimpleText":
                        ClipArt.getSelectedText().bringToFront();
                        break;
                    case "ExampleText":
                        ClipArt_Example.getSelectedText().bringToFront();
                        break;
                }
            } catch (Exception e) {
                Log.i("TAG", "onCreate: " + e.getMessage());
            }
        });

    }

    void newDefaultExampleShow() {

        //New Meme Defualt Example Show
        if (tempName.equals(getString(R.string.tempName_1))) {
            editBinding.temp1A.setVisibility(View.VISIBLE);
            editBinding.temp1B.setVisibility(View.VISIBLE);
            editBinding.temp1A.setText(getString(R.string.temp1A_2));
            editBinding.temp1B.setText(getString(R.string.temp1B_2));
           // SpannableString spannableString = new SpannableString(getString(R.string.temp1A_2));

            // Apply outline to specific characters (e.g., "lined")
//            int start = 2;
//            int end = 6;
//            spannableString.setSpan(new OutlineSpan(2f, getResources().getColor(R.color.orange)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        }

        //71 temp
//        else if (retrieveImage == R.drawable.temp71) {
//            editBinding.temp71A.setVisibility(View.VISIBLE);
//            editBinding.temp71B.setVisibility(View.VISIBLE);
//            editBinding.temp71A.setText(getString(R.string.temp71A_2));
//            editBinding.temp71B.setText(getString(R.string.temp71B_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_2))) {
            editBinding.temp2A.setVisibility(View.VISIBLE);
            editBinding.temp2A.setText(getString(R.string.temp2A_2));
        }

        //72 temp
//        else if (retrieveImage == R.drawable.temp72) {
//            editBinding.temp72A.setVisibility(View.VISIBLE);
//            editBinding.temp72B.setVisibility(View.VISIBLE);
//            editBinding.temp72A.setText(getString(R.string.temp72A_2));
//            editBinding.temp72B.setText(getString(R.string.temp72B_2));
//        }
        else if (tempName.equals(getString(R.string.tempName_3))) {
            editBinding.temp3A.setVisibility(View.VISIBLE);
            editBinding.temp3A.setText(getString(R.string.temp3A_2));

        }
        //73 temp
//        else if (retrieveImage == R.drawable.temp73)
//        {
//            editBinding.temp73A.setVisibility(View.VISIBLE);
//            editBinding.temp73A.setText(getString(R.string.temp73A_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_4))) {
            editBinding.temp4A.setVisibility(View.VISIBLE);
            editBinding.temp4B.setVisibility(View.VISIBLE);
            editBinding.temp4A.setText(getString(R.string.temp4A_2));
            editBinding.temp4B.setText(getString(R.string.temp4B_2));

        }

        //74 temp
//        else if (retrieveImage == R.drawable.temp74) {
//            editBinding.temp74C.setVisibility(View.VISIBLE);
//            editBinding.temp74B.setVisibility(View.VISIBLE);
//            editBinding.temp74C.setText(getString(R.string.temp74C_2));
//            editBinding.temp74B.setText(getString(R.string.temp74B_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_5))) {
            editBinding.temp5A.setVisibility(View.VISIBLE);
            editBinding.temp5B.setVisibility(View.VISIBLE);
            editBinding.temp5A.setText(getString(R.string.temp5A_2));
            editBinding.temp5B.setText(getString(R.string.temp5B_2));

        }
        //75 temp
//        else if (retrieveImage == R.drawable.temp75) {
//            editBinding.temp75A.setVisibility(View.VISIBLE);
//            editBinding.temp75B.setVisibility(View.VISIBLE);
//            editBinding.temp75C.setVisibility(View.VISIBLE);
//            editBinding.temp75D.setVisibility(View.VISIBLE);
//            editBinding.temp75A.setText(getString(R.string.temp75A_2));
//            editBinding.temp75B.setText(getString(R.string.temp75B_2));
//            editBinding.temp75C.setText(getString(R.string.temp75C_2));
//            editBinding.temp75D.setText(getString(R.string.temp75D_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_6))) {
            editBinding.temp6A.setVisibility(View.VISIBLE);
            editBinding.temp6B.setVisibility(View.VISIBLE);
            editBinding.temp6A.setText(getString(R.string.temp6A_2));
            editBinding.temp6B.setText(getString(R.string.temp6B_2));

        }

        //76 temp
//        else if (retrieveImage == R.drawable.temp76) {
//            editBinding.temp76A.setVisibility(View.VISIBLE);
//            editBinding.temp76B.setVisibility(View.VISIBLE);
//            editBinding.temp76A.setText(getString(R.string.temp76A_2));
//            editBinding.temp76B.setText(getString(R.string.temp76B_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_7))) {
            editBinding.temp7A.setVisibility(View.VISIBLE);
            editBinding.temp7A.setText(getString(R.string.temp7A_2));

        }

        //77 temp
//        else if (retrieveImage == R.drawable.temp77) {
//            editBinding.temp77A.setVisibility(View.VISIBLE);
//            editBinding.temp77A.setText(getString(R.string.temp77A_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_8))) {
            editBinding.temp8A.setVisibility(View.VISIBLE);
            editBinding.temp8A.setText(getString(R.string.temp8A_2));

        }

        //78 temp
//        else if (retrieveImage == R.drawable.temp78) {
//            editBinding.temp78A.setVisibility(View.VISIBLE);
//            editBinding.temp78B.setVisibility(View.VISIBLE);
//            editBinding.temp78A.setText(getString(R.string.temp78A_2));
//            editBinding.temp78B.setText(getString(R.string.temp78B_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_9))) {
            editBinding.temp9A.setVisibility(View.VISIBLE);
            editBinding.temp9A.setText(getString(R.string.temp9A_2));

        }
        //79 temp
//        else if (retrieveImage == R.drawable.temp79) {
//            editBinding.temp79A.setVisibility(View.VISIBLE);
//            editBinding.temp79A.setText(getString(R.string.temp79A_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_10))) {
            editBinding.temp10A.setVisibility(View.VISIBLE);
            editBinding.temp10B.setVisibility(View.VISIBLE);
            editBinding.temp10A.setText(getString(R.string.temp10A_2));
            editBinding.temp10B.setText(getString(R.string.temp10B_2));

        }

//        //80 temp
//        else if (retrieveImage == R.drawable.temp80) {
//            editBinding.temp80A.setVisibility(View.VISIBLE);
//            editBinding.temp80A.setText(getString(R.string.temp80A_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_11))) {
            editBinding.temp11A.setVisibility(View.VISIBLE);
            editBinding.temp11B.setVisibility(View.VISIBLE);
            editBinding.temp11A.setText(getString(R.string.temp11A_2));
            editBinding.temp11B.setText(getString(R.string.temp11B_2));

        }
//        //81 temp
//        else if (retrieveImage == R.drawable.temp81) {
//            editBinding.temp81A.setVisibility(View.VISIBLE);
//            editBinding.temp81B.setVisibility(View.VISIBLE);
//            editBinding.temp81C.setVisibility(View.VISIBLE);
//            editBinding.temp81A.setText(getString(R.string.temp81A_2));
//            editBinding.temp81B.setText(getString(R.string.temp81B_2));
//            editBinding.temp81C.setText(getString(R.string.temp81C_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_12))) {
            editBinding.temp12A.setVisibility(View.VISIBLE);
            editBinding.temp12B.setVisibility(View.VISIBLE);
            editBinding.temp12A.setText(getString(R.string.temp12A_2));
            editBinding.temp12B.setText(getString(R.string.temp12B_2));

        }

//        //82 temp
//        else if (retrieveImage == R.drawable.temp82) {
//            editBinding.temp82A.setVisibility(View.VISIBLE);
//            editBinding.temp82A.setText(getString(R.string.temp82A_2));
//
//        }
        else if (tempName.equals(getString(R.string.tempName_13))) {
            editBinding.temp13A.setVisibility(View.VISIBLE);
            editBinding.temp13B.setVisibility(View.VISIBLE);
            editBinding.temp13A.setText(getString(R.string.temp13A_2));
            editBinding.temp13B.setText(getString(R.string.temp13B_2));

        }

//        //83 temp
//        else if (retrieveImage == R.drawable.temp83) {
//            editBinding.temp83A.setVisibility(View.VISIBLE);
//            editBinding.temp83A.setText(getString(R.string.temp83A_2));

//        }
        else if (tempName.equals(getString(R.string.tempName_14))) {
            editBinding.temp14A.setVisibility(View.VISIBLE);
            editBinding.temp14B.setVisibility(View.VISIBLE);
            editBinding.temp14A.setText(getString(R.string.temp14A_2));
            editBinding.temp14B.setText(getString(R.string.temp14B_2));

        }

        //Popular Meme Default Example show 15 to 28
        else if (tempName.equals(getString(R.string.tempName_15))) {
            editBinding.temp15A.setVisibility(View.VISIBLE);
            editBinding.temp15B.setVisibility(View.VISIBLE);
            editBinding.temp15A.setText(getString(R.string.temp15A_2));
            editBinding.temp15B.setText(getString(R.string.temp15B_2));

        } else if (tempName.equals(getString(R.string.tempName_16))) {
            editBinding.temp16A.setVisibility(View.VISIBLE);
            editBinding.temp16A.setText(getString(R.string.temp16A_2));

        } else if (tempName.equals(getString(R.string.tempName_17))) {
            editBinding.temp17A.setVisibility(View.VISIBLE);
            editBinding.temp17A.setText(getString(R.string.temp17A_2));

        } else if (tempName.equals(getString(R.string.tempName_18))) {
            editBinding.temp18A.setVisibility(View.VISIBLE);
            editBinding.temp18B.setVisibility(View.VISIBLE);
            editBinding.temp18A.setText(getString(R.string.temp18A_2));
            editBinding.temp18B.setText(getString(R.string.temp18B_2));

        } else if (tempName.equals(getString(R.string.tempName_19))) {
            editBinding.temp19A.setVisibility(View.VISIBLE);
            editBinding.temp19A.setText(getString(R.string.temp19A_2));

        } else if (tempName.equals(getString(R.string.tempName_20))) {
            editBinding.temp20A.setVisibility(View.VISIBLE);
            editBinding.temp20B.setVisibility(View.VISIBLE);
            editBinding.temp20A.setText(getString(R.string.temp20B_2));
            editBinding.temp20B.setText(getString(R.string.temp20B_1));

        }
//        else if (tempName.equals(getString(R.string.tempName_21))) {
//            editBinding.temp21A.setVisibility(View.VISIBLE);
////            editBinding.temp21B.setVisibility(View.VISIBLE);
//            editBinding.temp21A.setText(getString(R.string.temp21A_2));
//            //editBinding.temp21A.setVisibility(View.GONE);
//
//        }
        else if (tempName.equals(getString(R.string.tempName_22))) {
            editBinding.temp22A.setVisibility(View.VISIBLE);
            editBinding.temp22B.setVisibility(View.VISIBLE);
            editBinding.temp22C.setVisibility(View.VISIBLE);
            editBinding.temp22A.setText(getString(R.string.temp22A_2));
            editBinding.temp22B.setText(getString(R.string.temp22B_2));
            editBinding.temp22C.setText(getString(R.string.temp22C_2));

        } else if (tempName.equals(getString(R.string.tempName_23))) {
            editBinding.temp23A.setVisibility(View.VISIBLE);
            editBinding.temp23A.setText(getString(R.string.temp23A_2));


        } else if (tempName.equals(getString(R.string.tempName_24))) {
            editBinding.temp24A.setVisibility(View.VISIBLE);
            editBinding.temp24A.setText(getString(R.string.temp24A_2));

        } else if (tempName.equals(getString(R.string.tempName_25))) {
            editBinding.temp25A.setVisibility(View.VISIBLE);
            editBinding.temp25B.setVisibility(View.VISIBLE);
            editBinding.temp25C.setVisibility(View.VISIBLE);
            editBinding.temp25A.setText(getString(R.string.temp25A_2));
            editBinding.temp25B.setText(getString(R.string.temp25B_2));
            editBinding.temp25C.setText(getString(R.string.temp25C_2));

        } else if (tempName.equals(getString(R.string.tempName_26))) {
            editBinding.temp26A.setVisibility(View.VISIBLE);
            editBinding.temp26B.setVisibility(View.VISIBLE);
            editBinding.temp26A.setText(getString(R.string.temp26A_2));
            editBinding.temp26B.setText(getString(R.string.temp26B_2));

        } else if (tempName.equals(getString(R.string.tempName_27))) {
            editBinding.temp27A.setVisibility(View.VISIBLE);
            editBinding.temp27B.setVisibility(View.VISIBLE);
            editBinding.temp27A.setText(getString(R.string.temp27A_2));
            editBinding.temp27B.setText(getString(R.string.temp27B_2));

        } else if (tempName.equals(getString(R.string.tempName_28))) {
            editBinding.temp28A.setVisibility(View.VISIBLE);
            editBinding.temp28B.setVisibility(View.VISIBLE);
            editBinding.temp28C.setVisibility(View.VISIBLE);
            editBinding.temp28D.setVisibility(View.VISIBLE);
            editBinding.temp28A.setText(getString(R.string.temp28A_2));
            editBinding.temp28B.setText(getString(R.string.temp28B_2));
            editBinding.temp28D.setText(getString(R.string.temp28D_2));

        }

        //    Islamic Meme 86 to 100
        else if (tempName.equals(getString(R.string.tempName_86))) {
            editBinding.temp86A.setVisibility(View.VISIBLE);
            editBinding.temp86A.setText(getString(R.string.temp86B_1));

        }

        //87
        else if (tempName.equals(getString(R.string.tempName_87))) {
            editBinding.temp87A.setVisibility(View.VISIBLE);
            editBinding.temp87A.setText(getString(R.string.temp87B_1));

        }

        //88
        else if (tempName.equals(getString(R.string.tempName_88))) {
            editBinding.temp88A.setVisibility(View.VISIBLE);
            editBinding.temp88A.setText(getString(R.string.temp88B_1));

        }
        //89
        else if (tempName.equals(getString(R.string.tempName_89))) {
            editBinding.temp89A.setVisibility(View.VISIBLE);
            editBinding.temp89A.setText(getString(R.string.temp89B_1));

        }
        //90
        else if (tempName.equals(getString(R.string.tempName_90))) {
            editBinding.temp90A.setVisibility(View.VISIBLE);
            editBinding.temp90A.setText(getString(R.string.temp90B_1));

        }
        //91
        else if (tempName.equals(getString(R.string.tempName_91))) {
            editBinding.temp91A.setVisibility(View.VISIBLE);
            editBinding.temp91A.setText(getString(R.string.temp91B_1));

        }
        //92
        else if (tempName.equals(getString(R.string.tempName_92))) {
            editBinding.temp92A.setVisibility(View.VISIBLE);
            editBinding.temp92A.setText(getString(R.string.temp92B_1));

        }
        //93
        else if (tempName.equals(getString(R.string.tempName_93))) {
            editBinding.temp93A.setVisibility(View.VISIBLE);
            editBinding.temp93A.setText(getString(R.string.temp93B_1));

        }
        //94
        else if (tempName.equals(getString(R.string.tempName_94))) {
            editBinding.temp94A.setVisibility(View.VISIBLE);
            editBinding.temp94A.setText(getString(R.string.temp94B_1));

        }
        //95
        else if (tempName.equals(getString(R.string.tempName_95))) {
            editBinding.temp95A.setVisibility(View.VISIBLE);
            editBinding.temp95A.setText(getString(R.string.temp95B_1));

        }
        //96
        else if (tempName.equals(getString(R.string.tempName_96))) {
            editBinding.temp96A.setVisibility(View.VISIBLE);
            editBinding.temp96A.setText(getString(R.string.temp96B_1));

        }
        //97
        else if (tempName.equals(getString(R.string.tempName_97))) {
            editBinding.temp97A.setVisibility(View.VISIBLE);
            editBinding.temp97A.setText(getString(R.string.temp97B_1));

        }
        //98
        else if (tempName.equals(getString(R.string.tempName_98))) {
            editBinding.temp98A.setVisibility(View.VISIBLE);
            editBinding.temp98A.setText(getString(R.string.temp98A_1));

        }
        //99
        else if (tempName.equals(getString(R.string.tempName_99))) {
            editBinding.temp99A.setVisibility(View.VISIBLE);
            editBinding.temp99A.setText(getString(R.string.temp99A_1));

        }
        //100
        else if (tempName.equals(getString(R.string.tempName_100))) {
            editBinding.temp100A.setVisibility(View.VISIBLE);
            editBinding.temp100A.setText(getString(R.string.temp100A_1));

        }

        // Decent Meme 101 to 114
        else if (tempName.equals(getString(R.string.tempName_101))) {
            editBinding.temp101A.setVisibility(View.VISIBLE);
            editBinding.temp101A.setText(getString(R.string.temp101A_2));

        }
        //102
        else if (tempName.equals(getString(R.string.tempName_102))) {
            editBinding.temp102A.setVisibility(View.VISIBLE);
            editBinding.temp102B.setVisibility(View.VISIBLE);
            editBinding.temp102A.setText(getString(R.string.temp102A_2));
            editBinding.temp102B.setText(getString(R.string.temp102B_2));

        }
        //103
        else if (tempName.equals(getString(R.string.tempName_103))) {
            editBinding.temp103A.setVisibility(View.VISIBLE);
            editBinding.temp103A.setText(getString(R.string.temp103A_1));

        }
        //104
        else if (tempName.equals(getString(R.string.tempName_104))) {
            editBinding.temp104A.setVisibility(View.VISIBLE);
            editBinding.temp104A.setText(getString(R.string.temp104A_1));

        }
        //105
        else if (tempName.equals(getString(R.string.tempName_105))) {
            editBinding.temp105A.setVisibility(View.VISIBLE);
            editBinding.temp105A.setText(getString(R.string.temp105A_1));

        }
        //106
        else if (tempName.equals(getString(R.string.tempName_106))) {
            editBinding.temp106A.setVisibility(View.VISIBLE);
            editBinding.temp106A.setText(getString(R.string.temp106A_2));
        }
        //107
        else if (tempName.equals(getString(R.string.tempName_107))) {
            editBinding.temp107A.setVisibility(View.VISIBLE);
            editBinding.temp107B.setVisibility(View.VISIBLE);
            editBinding.temp107A.setText(getString(R.string.temp107A_2));
            editBinding.temp107B.setText(getString(R.string.temp107B_2));

        }
        //108
        else if (tempName.equals(getString(R.string.tempName_108))) {
            editBinding.temp108A.setVisibility(View.VISIBLE);
            editBinding.temp108B.setVisibility(View.VISIBLE);
            editBinding.temp108A.setText(getString(R.string.temp108A_2));
            editBinding.temp108B.setText(getString(R.string.temp108B_2));

        }
        //109
        else if (tempName.equals(getString(R.string.tempName_109))) {
            editBinding.temp109A.setVisibility(View.VISIBLE);
            editBinding.temp109A.setText(getString(R.string.temp109A_1));

        }
        //110
        else if (tempName.equals(getString(R.string.tempName_110))) {
            editBinding.temp110A.setVisibility(View.VISIBLE);
            editBinding.temp110A.setText(getString(R.string.temp110A_1));
        }
        //111
        else if (tempName.equals(getString(R.string.tempName_111))) {
            editBinding.temp111A.setVisibility(View.VISIBLE);
            editBinding.temp111B.setVisibility(View.VISIBLE);
            editBinding.temp111A.setText(getString(R.string.temp111A_2));
            editBinding.temp111B.setText(getString(R.string.temp111B_2));

        }
        //112
        else if (tempName.equals(getString(R.string.tempName_112))) {
            editBinding.temp112A.setVisibility(View.VISIBLE);
            editBinding.temp112B.setVisibility(View.VISIBLE);
            editBinding.temp112A.setText(getString(R.string.temp112A_2));
            editBinding.temp112B.setText(getString(R.string.temp112B_2));

        }
        //113
        else if (tempName.equals(getString(R.string.tempName_113))) {
            editBinding.temp113A.setVisibility(View.VISIBLE);
            editBinding.temp113B.setVisibility(View.VISIBLE);
            editBinding.temp113A.setText(getString(R.string.temp113A_2));
            editBinding.temp113B.setText(getString(R.string.temp113B_2));

        }
        //114
        else if (tempName.equals(getString(R.string.tempName_114))) {
            editBinding.temp114A.setVisibility(View.VISIBLE);
            editBinding.temp114A.setText(getString(R.string.temp114A_1));
        }


        // University Meme 115 to 129
        else if (tempName.equals(getString(R.string.tempName_115))) {
            editBinding.temp115A.setVisibility(View.VISIBLE);
            editBinding.temp115A.setText(getString(R.string.temp115A_2));

            Log.i("rafaqat", "newDefaultExampleShow---: "+tempName);
        }
        //116
        else if (tempName.equals(getString(R.string.tempName_116))) {
            editBinding.temp116A.setVisibility(View.VISIBLE);
            editBinding.temp116A.setText(getString(R.string.temp116A_2));
        }
        //117
        else if (tempName.equals(getString(R.string.tempName_117))) {
            editBinding.temp117A.setVisibility(View.VISIBLE);
            editBinding.temp117A.setText(getString(R.string.temp117A_2));
        }
        //118
        else if (tempName.equals(getString(R.string.tempName_118))) {
            editBinding.temp118A.setVisibility(View.VISIBLE);
            editBinding.temp118A.setText(getString(R.string.temp118A_2));
        }
        //119
        else if (tempName.equals(getString(R.string.tempName_119))) {
            editBinding.temp119A.setVisibility(View.VISIBLE);
            editBinding.temp119A.setText(getString(R.string.temp119A_2));
        }
        //120
        else if (tempName.equals(getString(R.string.tempName_120))) {
            editBinding.temp120A.setVisibility(View.VISIBLE);
            editBinding.temp120A.setText(getString(R.string.temp120A_2));
        }
        //121
        else if (tempName.equals(getString(R.string.tempName_121))) {
            editBinding.temp121A.setVisibility(View.VISIBLE);
            editBinding.temp121A.setText(getString(R.string.temp121A_2));
        }
        //122
        else if (tempName.equals(getString(R.string.tempName_122))) {
            editBinding.temp122A.setVisibility(View.VISIBLE);
            editBinding.temp122A.setText(getString(R.string.temp122A_2));
        }
        //123
        else if (tempName.equals(getString(R.string.tempName_123))) {
            editBinding.temp123A.setVisibility(View.VISIBLE);
            editBinding.temp123A.setText(getString(R.string.temp123A_2));
        }
        //124
        else if (tempName.equals(getString(R.string.tempName_124))) {
            editBinding.temp124A.setVisibility(View.VISIBLE);
            editBinding.temp124A.setText(getString(R.string.temp124A_2));
        }
        //125
        else if (tempName.equals(getString(R.string.tempName_125))) {
            editBinding.temp125A.setVisibility(View.VISIBLE);
            editBinding.temp125A.setText(getString(R.string.temp125A_2));
        }
        //126
        else if (tempName.equals(getString(R.string.tempName_126))) {
            editBinding.temp126A.setVisibility(View.VISIBLE);
            editBinding.temp126A.setText(getString(R.string.temp126A_2));
        }
        //127
        else if (tempName.equals(getString(R.string.tempName_127))) {
            editBinding.temp127A.setVisibility(View.VISIBLE);
            editBinding.temp127A.setText(getString(R.string.temp127A_2));
        }
        //128
        else if (tempName.equals(getString(R.string.tempName_128))) {
            editBinding.temp128A.setVisibility(View.VISIBLE);
            editBinding.temp128A.setText(getString(R.string.temp128A_2));
        }
        //129
        else if (tempName.equals(getString(R.string.tempName_129))) {
            editBinding.temp129A.setVisibility(View.VISIBLE);
            editBinding.temp129A.setText(getString(R.string.temp129A_2));
        }

        // Dank Memem 130 to 144
        else if (tempName.equals(getString(R.string.tempName_130))) {
            editBinding.temp130A.setVisibility(View.VISIBLE);
            editBinding.temp130A.setText(getString(R.string.temp130A_2));
        } else if (tempName.equals(getString(R.string.tempName_131))) {
            editBinding.temp131A.setVisibility(View.VISIBLE);
            editBinding.temp131A.setText(getString(R.string.temp131A_2));
        } else if (tempName.equals(getString(R.string.tempName_132))) {
            editBinding.temp132A.setVisibility(View.VISIBLE);
            editBinding.temp132A.setText(getString(R.string.temp132A_2));
        } else if (tempName.equals(getString(R.string.tempName_133))) {
            editBinding.temp133A.setVisibility(View.VISIBLE);
            editBinding.temp133A.setText(getString(R.string.temp133A_2));
        } else if (tempName.equals(getString(R.string.tempName_134))) {
            editBinding.temp134A.setVisibility(View.VISIBLE);
            editBinding.temp134A.setText(getString(R.string.temp134A_2));
        } else if (tempName.equals(getString(R.string.tempName_135))) {
            editBinding.temp135A.setVisibility(View.VISIBLE);
            editBinding.temp135A.setText(getString(R.string.temp135A_2));
        } else if (tempName.equals(getString(R.string.tempName_136))) {
            editBinding.temp136A.setVisibility(View.VISIBLE);
            editBinding.temp136A.setText(getString(R.string.temp136A_2));
        } else if (tempName.equals(getString(R.string.tempName_137))) {
            editBinding.temp137A.setVisibility(View.VISIBLE);
            editBinding.temp137A.setText(getString(R.string.temp137A_2));
        } else if (tempName.equals(getString(R.string.tempName_138))) {
            editBinding.temp138A.setVisibility(View.VISIBLE);
            editBinding.temp138A.setText(getString(R.string.temp138A_2));
        } else if (tempName.equals(getString(R.string.tempName_139))) {
            editBinding.temp139A.setVisibility(View.VISIBLE);
            editBinding.temp139A.setText(getString(R.string.temp139A_2));
        } else if (tempName.equals(getString(R.string.tempName_140))) {
            editBinding.temp140A.setVisibility(View.VISIBLE);
            editBinding.temp140A.setText(getString(R.string.temp140A_2));
        } else if (tempName.equals(getString(R.string.tempName_141))) {
            editBinding.temp141A.setVisibility(View.VISIBLE);
            editBinding.temp141A.setText(getString(R.string.temp141A_2));
        } else if (tempName.equals(getString(R.string.tempName_142))) {
            editBinding.temp142A.setVisibility(View.VISIBLE);
            editBinding.temp142A.setText(getString(R.string.temp142A_2));
        } else if (tempName.equals(getString(R.string.tempName_143))) {
            editBinding.temp143A.setVisibility(View.VISIBLE);
            editBinding.temp143A.setText(getString(R.string.temp143A_2));
        } else if (tempName.equals(getString(R.string.tempName_144))) {
            editBinding.temp144A.setVisibility(View.VISIBLE);
            editBinding.temp144A.setText(getString(R.string.temp144A_2));
        }

//
//        //84 temp
//        else if (retrieveImage == R.drawable.temp84) {
//            editBinding.temp84A.setVisibility(View.VISIBLE);
//            editBinding.temp84A.setText(getString(R.string.temp84A_2));
//
//        }
        //        //85 temp
//        else if (retrieveImage == R.drawable.temp85) {
//            editBinding.temp85A.setVisibility(View.VISIBLE);
//            editBinding.temp85B.setVisibility(View.VISIBLE);
//            editBinding.temp85A.setText(getString(R.string.temp85A_2));
//            editBinding.temp85B.setText(getString(R.string.temp85B_2));
//
//        }


//        else if (retrieveImage == R.drawable.temp29) {
//            editBinding.temp29A.setVisibility(View.VISIBLE);
//            editBinding.temp29B.setVisibility(View.VISIBLE);
//            editBinding.temp29A.setText(getString(R.string.temp29A_2));
//            editBinding.temp29B.setText(getString(R.string.temp29B_2));
//
//        } else if (retrieveImage == R.drawable.temp30) {
//            editBinding.temp30A.setVisibility(View.VISIBLE);
//            editBinding.temp30B.setVisibility(View.VISIBLE);
//            editBinding.temp30C.setVisibility(View.VISIBLE);
//            editBinding.temp30D.setVisibility(View.VISIBLE);
//            editBinding.temp30A.setText(getString(R.string.temp30A_2));
//            editBinding.temp30B.setText(getString(R.string.temp30B_2));
//            editBinding.temp30C.setText(getString(R.string.temp30C_2));
//            editBinding.temp30D.setText(getString(R.string.temp30D_2));
//
//        } else if (retrieveImage == R.drawable.temp31) {
//            editBinding.temp31A.setVisibility(View.VISIBLE);
//            editBinding.temp31B.setVisibility(View.VISIBLE);
//            editBinding.temp31C.setVisibility(View.VISIBLE);
//            editBinding.temp31A.setText(getString(R.string.temp31A_2));
//            editBinding.temp31C.setText(getString(R.string.temp31C_2));
//            editBinding.temp31B.setVisibility(View.GONE);
//
//        } else if (retrieveImage == R.drawable.temp32) {
//            editBinding.temp32A.setVisibility(View.VISIBLE);
//            editBinding.temp32B.setVisibility(View.VISIBLE);
//            editBinding.temp32A.setText(getString(R.string.temp32A_2));
//            editBinding.temp32B.setText(getString(R.string.temp32B_2));
//
//        } else if (retrieveImage == R.drawable.temp33) {
//            editBinding.temp33A.setVisibility(View.VISIBLE);
//            editBinding.temp33B.setVisibility(View.VISIBLE);
//            editBinding.temp33A.setText(getString(R.string.temp33A_2));
//            editBinding.temp33B.setText(getString(R.string.temp33B_2));
//
//        } else if (retrieveImage == R.drawable.temp34) {
//            editBinding.temp34A.setVisibility(View.VISIBLE);
//            editBinding.temp34B.setVisibility(View.VISIBLE);
//
//            editBinding.temp34A.setText(getString(R.string.temp34A_2));
//            editBinding.temp34B.setText(getString(R.string.temp34B_2));
//
//        } else if (retrieveImage == R.drawable.temp35) {
//            editBinding.temp35A.setVisibility(View.VISIBLE);
//            editBinding.temp35A.setText(getString(R.string.temp35A_2));
//
//        } else if (retrieveImage == R.drawable.temp36) {
//            editBinding.temp36A.setVisibility(View.VISIBLE);
//            editBinding.temp36A.setText(getString(R.string.temp36A_2));
//
//        } else if (retrieveImage == R.drawable.temp37) {
//            editBinding.temp37A.setVisibility(View.VISIBLE);
//            editBinding.temp37B.setVisibility(View.VISIBLE);
//            editBinding.temp37A.setText(getString(R.string.temp37A_2));
//            editBinding.temp37B.setText(getString(R.string.temp37B_2));
//
//        } else if (retrieveImage == R.drawable.temp38) {
//            editBinding.temp38A.setVisibility(View.VISIBLE);
//            editBinding.temp38B.setVisibility(View.VISIBLE);
//            editBinding.temp38A.setText(getString(R.string.temp38A_2));
//            editBinding.temp38B.setText(getString(R.string.temp38B_2));
//
//        } else if (retrieveImage == R.drawable.temp39) {
//            editBinding.temp39A.setVisibility(View.VISIBLE);
//            editBinding.temp39A.setText(getString(R.string.temp39A_2));
//
//        } else if (retrieveImage == R.drawable.temp40) {
//            editBinding.temp40A.setVisibility(View.VISIBLE);
//            editBinding.temp40A.setText(getString(R.string.temp40A_2));
//
//        } else if (retrieveImage == R.drawable.temp41) {
//            editBinding.temp41A.setVisibility(View.VISIBLE);
//            editBinding.temp41A.setText(getString(R.string.temp41A_2));
//
//        } else if (retrieveImage == R.drawable.temp42) {
//            editBinding.temp42A.setVisibility(View.VISIBLE);
//            editBinding.temp42B.setVisibility(View.VISIBLE);
//            editBinding.temp42A.setText(getString(R.string.temp42A_2));
//            editBinding.temp42B.setText(getString(R.string.temp42B_2));
//
//        } else if (retrieveImage == R.drawable.temp43) {
//            editBinding.temp43A.setVisibility(View.VISIBLE);
//            editBinding.temp43A.setText(getString(R.string.temp43A_2));
//
//        } else if (retrieveImage == R.drawable.temp44) {
//            editBinding.temp44A.setVisibility(View.VISIBLE);
//            editBinding.temp44B.setVisibility(View.VISIBLE);
//            editBinding.temp44A.setText(getString(R.string.temp44A_2));
//            editBinding.temp44B.setText(getString(R.string.temp44B_2));
//
//        } else if (retrieveImage == R.drawable.temp45) {
//            editBinding.temp45A.setVisibility(View.VISIBLE);
//            editBinding.temp45A.setText(getString(R.string.temp45A_2));
//
//        } else if (retrieveImage == R.drawable.temp46) {
//            editBinding.temp46A.setVisibility(View.VISIBLE);
//            editBinding.temp46B.setVisibility(View.VISIBLE);
//            editBinding.temp46A.setText(getString(R.string.temp46A_2));
//            editBinding.temp46B.setText(getString(R.string.temp46B_2));
//
//        } else if (retrieveImage == R.drawable.temp47) {
//            editBinding.temp47A.setVisibility(View.VISIBLE);
//            editBinding.temp47A.setText(getString(R.string.temp47A_2));
//
//        } else if (retrieveImage == R.drawable.temp48) {
//
//            editBinding.temp48A.setVisibility(View.VISIBLE);
//            editBinding.temp48B.setVisibility(View.VISIBLE);
//            editBinding.temp48C.setVisibility(View.VISIBLE);
//            editBinding.temp48A.setText(getString(R.string.temp48A_2));
//            editBinding.temp48B.setText(getString(R.string.temp48B_2));
//            editBinding.temp48C.setText(getString(R.string.temp48C_2));
//
//        } else if (retrieveImage == R.drawable.temp49) {
//            editBinding.temp49A.setVisibility(View.VISIBLE);
//            editBinding.temp49B.setVisibility(View.VISIBLE);
//            editBinding.temp49A.setText(getString(R.string.temp49A_2));
//            editBinding.temp49B.setText(getString(R.string.temp49B_2));
//
//        } else if (retrieveImage == R.drawable.temp50) {
//            editBinding.temp50A.setVisibility(View.VISIBLE);
//            editBinding.temp50B.setVisibility(View.VISIBLE);
//            editBinding.temp50A.setText(getString(R.string.temp50A_2));
//            editBinding.temp50B.setText(getString(R.string.temp50B_2));
//
//        } else if (retrieveImage == R.drawable.temp51) {
//            editBinding.temp51A.setVisibility(View.VISIBLE);
//            editBinding.temp51A.setText(getString(R.string.temp51A_2));
//
//        } else if (retrieveImage == R.drawable.temp52) {
//            editBinding.temp52A.setVisibility(View.VISIBLE);
//            editBinding.temp52A.setText(getString(R.string.temp52A_2));
//
//        } else if (retrieveImage == R.drawable.temp53) {
//            editBinding.temp53A.setVisibility(View.VISIBLE);
//            editBinding.temp53A.setText(getString(R.string.temp53A_2));
//
//        } else if (retrieveImage == R.drawable.temp54) {
//            editBinding.temp54A.setVisibility(View.VISIBLE);
//            editBinding.temp54B.setVisibility(View.VISIBLE);
//            editBinding.temp54A.setText(getString(R.string.temp54A_2));
//            editBinding.temp54B.setText(getString(R.string.temp54B_2));
//
//        } else if (retrieveImage == R.drawable.temp55) {
//            editBinding.temp55A.setVisibility(View.VISIBLE);
//            editBinding.temp55B.setVisibility(View.VISIBLE);
//            editBinding.temp55A.setText(getString(R.string.temp55A_2));
//            editBinding.temp55B.setText(getString(R.string.temp55B_2));
//
//        } else if (retrieveImage == R.drawable.temp56) {
//            editBinding.temp56A.setVisibility(View.VISIBLE);
//            editBinding.temp56A.setText(getString(R.string.temp56A_2));
//
//        } else if (retrieveImage == R.drawable.temp57) {
//            editBinding.temp57A.setVisibility(View.VISIBLE);
//            editBinding.temp57B.setVisibility(View.VISIBLE);
//            editBinding.temp57A.setText(getString(R.string.temp57A_2));
//            editBinding.temp57B.setText(getString(R.string.temp57B_2));
//
//        } else if (retrieveImage == R.drawable.temp58) {
//            editBinding.temp58A.setVisibility(View.VISIBLE);
//            editBinding.temp58A.setText(getString(R.string.temp58A_2));
//
//        } else if (retrieveImage == R.drawable.temp59) {
//            editBinding.temp59A.setVisibility(View.VISIBLE);
//            editBinding.temp59B.setVisibility(View.VISIBLE);
//            editBinding.temp59A.setText(getString(R.string.temp59A_2));
//            editBinding.temp59B.setText(getString(R.string.temp59B_2));
//
//        } else if (retrieveImage == R.drawable.temp60) {
//            editBinding.temp60A.setVisibility(View.VISIBLE);
//            editBinding.temp60A.setText(getString(R.string.temp60A_2));
//
//        } else if (retrieveImage == R.drawable.temp61) {
//            editBinding.temp61A.setVisibility(View.VISIBLE);
//            editBinding.temp61B.setVisibility(View.VISIBLE);
//            editBinding.temp61A.setText(getString(R.string.temp61A_2));
//            editBinding.temp61B.setText(getString(R.string.temp61B_2));
//
//        } else if (retrieveImage == R.drawable.temp62) {
//            editBinding.temp62A.setVisibility(View.VISIBLE);
//            editBinding.temp62B.setVisibility(View.VISIBLE);
//            editBinding.temp62A.setText(getString(R.string.temp62A_2));
//            editBinding.temp62B.setText(getString(R.string.temp62B_2));
//
//        } else if (retrieveImage == R.drawable.temp63) {
//            editBinding.temp63A.setVisibility(View.VISIBLE);
//            editBinding.temp63B.setVisibility(View.VISIBLE);
//            editBinding.temp63A.setText(getString(R.string.temp63A_2));
//            editBinding.temp63B.setVisibility(View.GONE);
//
//        } else if (retrieveImage == R.drawable.temp64) {
//            editBinding.temp64A.setVisibility(View.VISIBLE);
//            editBinding.temp64A.setText(getString(R.string.temp64A_2));
//
//        } else if (retrieveImage == R.drawable.temp65) {
//            editBinding.temp65A.setVisibility(View.VISIBLE);
//            editBinding.temp65A.setText(getString(R.string.temp65A_2));
//
//        } else if (retrieveImage == R.drawable.temp66) {
//            editBinding.temp66A.setVisibility(View.VISIBLE);
//            editBinding.temp66A.setText(getString(R.string.temp66A_2));
//
//        } else if (retrieveImage == R.drawable.temp67) {
//            editBinding.temp67A.setVisibility(View.VISIBLE);
//            editBinding.temp67B.setVisibility(View.VISIBLE);
//            editBinding.temp67A.setText(getString(R.string.temp67A_2));
//            editBinding.temp67B.setText(getString(R.string.temp67B_2));
//
//        } else if (retrieveImage == R.drawable.temp68) {
//            editBinding.temp68A.setVisibility(View.VISIBLE);
//            editBinding.temp68A.setText(getString(R.string.temp68A_2));
//
//        } else if (retrieveImage == R.drawable.temp69) {
//            editBinding.temp69A.setVisibility(View.VISIBLE);
//            editBinding.temp69B.setVisibility(View.VISIBLE);
//            editBinding.temp69A.setText(getString(R.string.temp69A_2));
//            editBinding.temp69B.setText(getString(R.string.temp69B_2));
//
//        } else if (retrieveImage == R.drawable.temp70) {
//            editBinding.temp70A.setVisibility(View.VISIBLE);
//            editBinding.temp70B.setVisibility(View.VISIBLE);
//            editBinding.temp70A.setText(getString(R.string.temp70A_2));
//            editBinding.temp70B.setText(getString(R.string.temp70B_2));
//
//        }
//


        //Call with in Method Default Method Example Click + Example Object Functionality
        exampleCompleteFunctionality();

    }

    private void exampleCompleteFunctionality() {

        editBinding.temp1A.setOnClickListener(view -> {
            String ex1A = editBinding.temp1A.getText().toString();
            fontName=editBinding.temp1A.getTag().toString();

            clipArt_example = fun(editBinding.temp1A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex1A, editBinding.temp1A);

        });
        editBinding.temp1B.setOnClickListener(view -> {
            fontName=editBinding.temp1B.getTag().toString();
            String ex2B = editBinding.temp1B.getText().toString();
            clipArt_example2 = fun(editBinding.temp1B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp1B);



        });

        // 71 Temp
        editBinding.temp71A.setOnClickListener(view -> {
            fontName=editBinding.temp71A.getTag().toString();

            String ex1A = editBinding.temp71A.getText().toString();
            clipArt_example = fun(editBinding.temp71A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex1A, editBinding.temp71A);
        });
        editBinding.temp71B.setOnClickListener(view -> {
            fontName=editBinding.temp71B.getTag().toString();

            String ex2B = editBinding.temp71B.getText().toString();
            clipArt_example2 = fun(editBinding.temp71B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp71B);

        });


        editBinding.temp2A.setOnClickListener(view -> {
            String ex2B = editBinding.temp2A.getText().toString();
            fontName=editBinding.temp2A.getTag().toString();

            clipArt_example = fun(editBinding.temp2A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp2A);

        });

        // 72 Temp
        editBinding.temp72A.setOnClickListener(view -> {
            fontName=editBinding.temp72A.getTag().toString();

            String ex1A = editBinding.temp72A.getText().toString();

            clipArt_example = fun(editBinding.temp72A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex1A, editBinding.temp72A);
        });
        editBinding.temp72B.setOnClickListener(view -> {
            fontName=editBinding.temp72B.getTag().toString();

            String ex2B = editBinding.temp72B.getText().toString();
            clipArt_example2 = fun(editBinding.temp72B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp72B);

        });


        editBinding.temp3A.setOnClickListener(view -> {
            fontName=editBinding.temp3A.getTag().toString();

            String ex2B = editBinding.temp3A.getText().toString();

            clipArt_example = fun(editBinding.temp3A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp3A);

        });

        // 73 Temp
        editBinding.temp73A.setOnClickListener(view -> {
            fontName=editBinding.temp73A.getTag().toString();

            String ex2B = editBinding.temp73A.getText().toString();

            clipArt_example = fun(editBinding.temp73A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp73A);

        });

        editBinding.temp4A.setOnClickListener(view -> {
            fontName=editBinding.temp4A.getTag().toString();

            String ex2B = editBinding.temp4A.getText().toString();
            clipArt_example = fun(editBinding.temp4A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp4A);

        });
        editBinding.temp4B.setOnClickListener(view -> {
            String ex2B = editBinding.temp4B.getText().toString();
            clipArt_example2 = fun(editBinding.temp4B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp4B);

        });

        // 74 Temp
        editBinding.temp74A.setOnClickListener(view -> {
            fontName=editBinding.temp74A.getTag().toString();

            String ex2B = editBinding.temp74A.getText().toString();

            clipArt_example = fun(editBinding.temp74A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp74A);

        });
        editBinding.temp74B.setOnClickListener(view -> {
            fontName=editBinding.temp74B.getTag().toString();

            String ex2B = editBinding.temp74B.getText().toString();
            clipArt_example2 = fun(editBinding.temp74B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp74B);

        });
        editBinding.temp74C.setOnClickListener(view -> {
            fontName=editBinding.temp74C.getTag().toString();

            String ex2B = editBinding.temp74C.getText().toString();

            clipArt_example3 = fun(editBinding.temp74C);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);
            exampleEditingFun(ex2B, editBinding.temp74C);

        });
        editBinding.temp74D.setOnClickListener(view -> {
            fontName=editBinding.temp74D.getTag().toString();

            String ex2B = editBinding.temp74D.getText().toString();

            clipArt_example4 = fun(editBinding.temp74D);
            //Tick and Cncel Functionality
            someFun(clipArt_example4);
            exampleEditingFun(ex2B, editBinding.temp74D);

        });
        editBinding.temp74E.setOnClickListener(view -> {
            fontName=editBinding.temp74E.getTag().toString();

            String ex2B = editBinding.temp74E.getText().toString();

            clipArt_example5 = fun(editBinding.temp74E);
            //Tick and Cncel Functionality
            someFun(clipArt_example5);
            exampleEditingFun(ex2B, editBinding.temp74E);

        });


        editBinding.temp5A.setOnClickListener(view -> {
            fontName=editBinding.temp5A.getTag().toString();

            String ex2B = editBinding.temp5A.getText().toString();

            clipArt_example = fun(editBinding.temp5A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp5A);

        });
        editBinding.temp5B.setOnClickListener(view -> {
            fontName=editBinding.temp5B.getTag().toString();

            String ex2B = editBinding.temp5B.getText().toString();

            clipArt_example2 = fun(editBinding.temp5B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp5B);

        });

        // 75 Temp
        editBinding.temp75A.setOnClickListener(view -> {
            fontName=editBinding.temp75A.getTag().toString();

            String ex2B = editBinding.temp75A.getText().toString();

            clipArt_example = fun(editBinding.temp75A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp75A);

        });
        editBinding.temp75B.setOnClickListener(view -> {
            fontName=editBinding.temp75B.getTag().toString();

            String ex2B = editBinding.temp75B.getText().toString();

            clipArt_example2 = fun(editBinding.temp75B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp75B);

        });
        editBinding.temp75C.setOnClickListener(view -> {
            fontName=editBinding.temp75C.getTag().toString();

            String ex2B = editBinding.temp75C.getText().toString();

            clipArt_example3 = fun(editBinding.temp75C);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);
            exampleEditingFun(ex2B, editBinding.temp75C);

        });
        editBinding.temp75D.setOnClickListener(view -> {
            fontName=editBinding.temp75D.getTag().toString();

            String ex2B = editBinding.temp75D.getText().toString();

            clipArt_example4 = fun(editBinding.temp75D);
            //Tick and Cncel Functionality
            someFun(clipArt_example4);
            exampleEditingFun(ex2B, editBinding.temp75D);

        });


        editBinding.temp6A.setOnClickListener(view -> {
            fontName=editBinding.temp6A.getTag().toString();

            String ex2B = editBinding.temp6A.getText().toString();
            clipArt_example = fun(editBinding.temp6A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp6A);

        });
        editBinding.temp6B.setOnClickListener(view -> {
            fontName=editBinding.temp6B.getTag().toString();

            String ex2B = editBinding.temp6B.getText().toString();

            clipArt_example2 = fun(editBinding.temp6B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp6B);

        });

        // 76 Temp
        editBinding.temp76A.setOnClickListener(view -> {
            fontName=editBinding.temp76A.getTag().toString();

            String ex2B = editBinding.temp76A.getText().toString();

            clipArt_example = fun(editBinding.temp76A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp76A);

        });
        editBinding.temp76B.setOnClickListener(view -> {
            fontName=editBinding.temp76B.getTag().toString();

            String ex2B = editBinding.temp76B.getText().toString();

            clipArt_example2 = fun(editBinding.temp76B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp76B);

        });

        editBinding.temp7A.setOnClickListener(view -> {
            fontName=editBinding.temp7A.getTag().toString();

            String ex2B = editBinding.temp7A.getText().toString();

            clipArt_example = fun(editBinding.temp7A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp7A);

        });

        // 77 Temp
        editBinding.temp77A.setOnClickListener(view -> {
            fontName=editBinding.temp77A.getTag().toString();

            String ex2B = editBinding.temp77A.getText().toString();

            clipArt_example = fun(editBinding.temp77A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp77A);

        });

        editBinding.temp8A.setOnClickListener(view -> {
            fontName=editBinding.temp8A.getTag().toString();

            String ex2B = editBinding.temp8A.getText().toString();

            clipArt_example = fun(editBinding.temp8A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp8A);

        });

        // 78 Temp
        editBinding.temp78A.setOnClickListener(view -> {
            fontName=editBinding.temp78A.getTag().toString();

            String ex2B = editBinding.temp78A.getText().toString();
            clipArt_example = fun(editBinding.temp78A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp78A);

        });
        editBinding.temp78B.setOnClickListener(view -> {
            fontName=editBinding.temp78B.getTag().toString();

            String ex2B = editBinding.temp78B.getText().toString();

            clipArt_example2 = fun(editBinding.temp78B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp78B);

        });


        editBinding.temp9A.setOnClickListener(view -> {
            fontName=editBinding.temp9A.getTag().toString();

            String ex2B = editBinding.temp9A.getText().toString();

            clipArt_example = fun(editBinding.temp9A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp9A);

        });

        // 79 Temp
        editBinding.temp79A.setOnClickListener(view -> {
            fontName=editBinding.temp79A.getTag().toString();

            String ex2B = editBinding.temp79A.getText().toString();

            clipArt_example = fun(editBinding.temp79A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp79A);

        });

        editBinding.temp10A.setOnClickListener(view -> {
            fontName=editBinding.temp10A.getTag().toString();

            String ex2B = editBinding.temp10A.getText().toString();

            clipArt_example = fun(editBinding.temp10A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp10A);

        });
        editBinding.temp10B.setOnClickListener(view -> {
            fontName=editBinding.temp10B.getTag().toString();
            String ex2B = editBinding.temp10B.getText().toString();

            clipArt_example2 = fun(editBinding.temp10B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp10B);

        });

        // 80 Temp
        editBinding.temp80A.setOnClickListener(view -> {
            fontName=editBinding.temp80A.getTag().toString();

            String ex2B = editBinding.temp80A.getText().toString();
            clipArt_example = fun(editBinding.temp80A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp80A);

        });

        editBinding.temp11A.setOnClickListener(view -> {
            fontName=editBinding.temp11A.getTag().toString();

            String ex2B = editBinding.temp11A.getText().toString();
            clipArt_example = fun(editBinding.temp11A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp11A);

        });
        editBinding.temp11B.setOnClickListener(view -> {
            fontName=editBinding.temp11B.getTag().toString();

            String ex2B = editBinding.temp11B.getText().toString();

            clipArt_example2 = fun(editBinding.temp11B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp11B);

        });
        // 81 Temp
        editBinding.temp81A.setOnClickListener(view -> {
            fontName=editBinding.temp81A.getTag().toString();

            String ex2B = editBinding.temp81A.getText().toString();

            clipArt_example = fun(editBinding.temp81A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp81A);

        });
        editBinding.temp81B.setOnClickListener(view -> {
            fontName=editBinding.temp81B.getTag().toString();

            String ex2B = editBinding.temp81B.getText().toString();
            clipArt_example2 = fun(editBinding.temp81B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp81B);

        });
        editBinding.temp81C.setOnClickListener(view -> {
            fontName=editBinding.temp81C.getTag().toString();

            String ex2B = editBinding.temp81C.getText().toString();
            clipArt_example3 = fun(editBinding.temp81C);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);

            exampleEditingFun(ex2B, editBinding.temp81C);

        });


        editBinding.temp12A.setOnClickListener(view -> {
            fontName=editBinding.temp12A.getTag().toString();

            String ex2B = editBinding.temp12A.getText().toString();
            clipArt_example = fun(editBinding.temp12A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp12A);

        });
        editBinding.temp12B.setOnClickListener(view -> {
            fontName=editBinding.temp12B.getTag().toString();

            String ex2B = editBinding.temp12B.getText().toString();

            clipArt_example2 = fun(editBinding.temp12B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp12B);

        });

        // 82 Temp
        editBinding.temp82A.setOnClickListener(view -> {
            fontName=editBinding.temp82A.getTag().toString();

            String ex2B = editBinding.temp82A.getText().toString();

            clipArt_example = fun(editBinding.temp82A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp82A);

        });

        editBinding.temp13A.setOnClickListener(view -> {
            fontName=editBinding.temp13A.getTag().toString();

            String ex2B = editBinding.temp13A.getText().toString();

            clipArt_example = fun(editBinding.temp13A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp13A);

        });
        editBinding.temp13B.setOnClickListener(view -> {
            fontName=editBinding.temp13B.getTag().toString();

            String ex2B = editBinding.temp13B.getText().toString();

            clipArt_example2 = fun(editBinding.temp13B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp13B);

        });

        // 83 Temp
        editBinding.temp83A.setOnClickListener(view -> {
            fontName=editBinding.temp83A.getTag().toString();

            String ex2B = editBinding.temp83A.getText().toString();

            clipArt_example = fun(editBinding.temp83A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp83A);

        });

        editBinding.temp14A.setOnClickListener(view -> {
            fontName=editBinding.temp14A.getTag().toString();

            String ex2B = editBinding.temp14A.getText().toString();
            clipArt_example = fun(editBinding.temp14A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp14A);

        });
        editBinding.temp14B.setOnClickListener(view -> {
            fontName=editBinding.temp14B.getTag().toString();

            String ex2B = editBinding.temp14B.getText().toString();
            clipArt_example2 = fun(editBinding.temp14B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp14B);

        });

        // 84 Temp
        editBinding.temp84A.setOnClickListener(view -> {
            fontName=editBinding.temp84A.getTag().toString();

            String ex2B = editBinding.temp84A.getText().toString();

            clipArt_example = fun(editBinding.temp84A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp84A);

        });

        editBinding.temp15A.setOnClickListener(view -> {
            fontName=editBinding.temp15A.getTag().toString();

            String ex2B = editBinding.temp15A.getText().toString();
            clipArt_example = fun(editBinding.temp15A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp15A);

        });
        editBinding.temp15B.setOnClickListener(view -> {
            fontName=editBinding.temp15B.getTag().toString();

            String ex2B = editBinding.temp15B.getText().toString();
            clipArt_example2 = fun(editBinding.temp15B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp15B);

        });

        // 85 Temp
        editBinding.temp85A.setOnClickListener(view -> {
            fontName=editBinding.temp85A.getTag().toString();

            String ex2B = editBinding.temp85A.getText().toString();
            clipArt_example = fun(editBinding.temp85A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp85A);

        });
        editBinding.temp85B.setOnClickListener(view -> {
            fontName=editBinding.temp85B.getTag().toString();

            String ex2B = editBinding.temp85B.getText().toString();

            clipArt_example2 = fun(editBinding.temp85B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp85B);

        });


        editBinding.temp16A.setOnClickListener(view -> {
            fontName=editBinding.temp16A.getTag().toString();

            String ex2B = editBinding.temp16A.getText().toString();
            clipArt_example = fun(editBinding.temp16A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp16A);

        });
        editBinding.temp17A.setOnClickListener(view -> {
            fontName=editBinding.temp17A.getTag().toString();

            String ex2B = editBinding.temp17A.getText().toString();
            clipArt_example = fun(editBinding.temp17A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp17A);

        });
        editBinding.temp18A.setOnClickListener(view -> {
            fontName=editBinding.temp18A.getTag().toString();

            String ex2B = editBinding.temp18A.getText().toString();
            clipArt_example = fun(editBinding.temp18A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp18A);

        });
        editBinding.temp18B.setOnClickListener(view -> {
            fontName=editBinding.temp18B.getTag().toString();

            String ex2B = editBinding.temp18B.getText().toString();

            clipArt_example2 = fun(editBinding.temp18B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp18B);

        });
        editBinding.temp19A.setOnClickListener(view -> {
            fontName=editBinding.temp19A.getTag().toString();

            String ex2B = editBinding.temp19A.getText().toString();
            clipArt_example = fun(editBinding.temp19A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp19A);

        });
        editBinding.temp20A.setOnClickListener(view -> {
            fontName=editBinding.temp20A.getTag().toString();

            String ex2B = editBinding.temp20A.getText().toString();

            clipArt_example = fun(editBinding.temp20A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp20A);

        });
        editBinding.temp20B.setOnClickListener(view -> {
            fontName=editBinding.temp20B.getTag().toString();

            String ex2B = editBinding.temp20B.getText().toString();

            clipArt_example2 = fun(editBinding.temp20B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp20B);

        });
//        editBinding.temp21A.setOnClickListener(view -> {
//            String ex2B = editBinding.temp21A.getText().toString();
//            clipArt_example = fun(editBinding.temp21A);
//            //Tick and Cncel Functionality
//            someFun(clipArt_example);
//            exampleEditingFun(ex2B, editBinding.temp21A);
//
//        });
//        editBinding.temp21B.setOnClickListener(view -> {
//            String ex2B = editBinding.temp21B.getText().toString();
//            clipArt_example2 = fun(editBinding.temp21B);
//            //Tick and Cncel Functionality
//            someFun(clipArt_example2);
//
//            exampleEditingFun(ex2B, editBinding.temp21B);
//
//        });

        editBinding.temp22A.setOnClickListener(view -> {
            fontName=editBinding.temp22A.getTag().toString();

            String ex2B = editBinding.temp22A.getText().toString();

            clipArt_example = fun(editBinding.temp22A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp22A);

        });
        editBinding.temp22B.setOnClickListener(view -> {
            fontName=editBinding.temp22B.getTag().toString();

            String ex2B = editBinding.temp22B.getText().toString();

            clipArt_example2 = fun(editBinding.temp22B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp22B);

        });
        editBinding.temp22C.setOnClickListener(view -> {
            fontName=editBinding.temp22C.getTag().toString();

            String ex2B = editBinding.temp22C.getText().toString();

            clipArt_example3 = fun(editBinding.temp22C);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);
            exampleEditingFun(ex2B, editBinding.temp22C);

        });
        editBinding.temp23A.setOnClickListener(view -> {
            fontName=editBinding.temp23A.getTag().toString();

            String ex2B = editBinding.temp23A.getText().toString();

            clipArt_example = fun(editBinding.temp23A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp23A);

        });
        editBinding.temp24A.setOnClickListener(view -> {
            fontName=editBinding.temp24A.getTag().toString();

            String ex2B = editBinding.temp24A.getText().toString();

            clipArt_example = fun(editBinding.temp24A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp24A);

        });
        editBinding.temp25A.setOnClickListener(view -> {
            fontName=editBinding.temp25A.getTag().toString();

            String ex2B = editBinding.temp25A.getText().toString();

            clipArt_example = fun(editBinding.temp25A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp25A);

        });
        editBinding.temp25B.setOnClickListener(view -> {
            fontName=editBinding.temp25B.getTag().toString();

            String ex2B = editBinding.temp25B.getText().toString();

            clipArt_example2 = fun(editBinding.temp25B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp25B);

        });
        editBinding.temp25C.setOnClickListener(view -> {
            fontName=editBinding.temp25C.getTag().toString();

            String ex2B = editBinding.temp25C.getText().toString();

            clipArt_example3 = fun(editBinding.temp25C);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);
            exampleEditingFun(ex2B, editBinding.temp25C);

        });
        editBinding.temp26A.setOnClickListener(view -> {
            fontName=editBinding.temp26A.getTag().toString();

            String ex2B = editBinding.temp26A.getText().toString();

            clipArt_example = fun(editBinding.temp26A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp26A);

        });
        editBinding.temp26B.setOnClickListener(view -> {
            fontName=editBinding.temp26B.getTag().toString();

            String ex2B = editBinding.temp26B.getText().toString();
            clipArt_example2 = fun(editBinding.temp26B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp26B);

        });
        editBinding.temp27A.setOnClickListener(view -> {
            fontName=editBinding.temp27A.getTag().toString();

            String ex2B = editBinding.temp27A.getText().toString();

            clipArt_example = fun(editBinding.temp27A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp27A);

        });
        editBinding.temp27B.setOnClickListener(view -> {
            fontName=editBinding.temp27B.getTag().toString();

            String ex2B = editBinding.temp27B.getText().toString();

            clipArt_example2 = fun(editBinding.temp27B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp27B);

        });
        editBinding.temp28A.setOnClickListener(view -> {
            fontName=editBinding.temp28A.getTag().toString();

            String ex2B = editBinding.temp28A.getText().toString();

            clipArt_example = fun(editBinding.temp28A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp28A);

        });
        editBinding.temp28B.setOnClickListener(view -> {
            fontName=editBinding.temp28B.getTag().toString();

            String ex2B = editBinding.temp28B.getText().toString();
            clipArt_example2 = fun(editBinding.temp28B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp28B);

        });


        editBinding.temp28D.setOnClickListener(view -> {
            fontName=editBinding.temp28D.getTag().toString();

            String ex2B = editBinding.temp28D.getText().toString();
            clipArt_example3 = fun(editBinding.temp28D);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);

            exampleEditingFun(ex2B, editBinding.temp28D);

        });
        editBinding.temp29A.setOnClickListener(view -> {
            fontName=editBinding.temp29A.getTag().toString();

            String ex2B = editBinding.temp29A.getText().toString();

            clipArt_example = fun(editBinding.temp29A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp29A);

        });
        editBinding.temp29B.setOnClickListener(view -> {
            fontName=editBinding.temp29B.getTag().toString();

            String ex2B = editBinding.temp29B.getText().toString();

            clipArt_example2 = fun(editBinding.temp29B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp29B);

        });
        editBinding.temp30A.setOnClickListener(view -> {
            fontName=editBinding.temp30A.getTag().toString();

            String ex2B = editBinding.temp30A.getText().toString();
            clipArt_example = fun(editBinding.temp30A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp30A);

        });
        editBinding.temp30B.setOnClickListener(view -> {
            fontName=editBinding.temp30B.getTag().toString();

            String ex2B = editBinding.temp30B.getText().toString();

            clipArt_example2 = fun(editBinding.temp30B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp30B);

        });
        editBinding.temp30C.setOnClickListener(view -> {
            fontName=editBinding.temp30C.getTag().toString();

            String ex2B = editBinding.temp30C.getText().toString();

            clipArt_example3 = fun(editBinding.temp30C);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);
            exampleEditingFun(ex2B, editBinding.temp30C);

        });
        editBinding.temp30D.setOnClickListener(view -> {
            fontName=editBinding.temp30D.getTag().toString();

            String ex2B = editBinding.temp30D.getText().toString();

            clipArt_example4 = fun(editBinding.temp30D);
            //Tick and Cncel Functionality
            someFun(clipArt_example4);
            exampleEditingFun(ex2B, editBinding.temp30D);

        });
        editBinding.temp31A.setOnClickListener(view -> {
            fontName=editBinding.temp31A.getTag().toString();

            String ex2B = editBinding.temp31A.getText().toString();

            clipArt_example = fun(editBinding.temp31A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp31A);

        });
        editBinding.temp31B.setOnClickListener(view -> {
            fontName=editBinding.temp31B.getTag().toString();

            String ex2B = editBinding.temp31B.getText().toString();

            clipArt_example2 = fun(editBinding.temp31B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp31B);

        });
        editBinding.temp31C.setOnClickListener(view -> {
            fontName=editBinding.temp31C.getTag().toString();

            String ex2B = editBinding.temp31C.getText().toString();

            clipArt_example3 = fun(editBinding.temp31C);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);
            exampleEditingFun(ex2B, editBinding.temp31C);

        });
        editBinding.temp32A.setOnClickListener(view -> {
            fontName=editBinding.temp32A.getTag().toString();

            String ex2B = editBinding.temp32A.getText().toString();

            clipArt_example = fun(editBinding.temp32A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp32A);

        });
        editBinding.temp32B.setOnClickListener(view -> {
            fontName=editBinding.temp32B.getTag().toString();

            String ex2B = editBinding.temp32B.getText().toString();
            clipArt_example2 = fun(editBinding.temp32B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp32B);

        });
        editBinding.temp33A.setOnClickListener(view -> {
            fontName=editBinding.temp33A.getTag().toString();

            String ex2B = editBinding.temp33A.getText().toString();

            clipArt_example = fun(editBinding.temp33A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp33A);

        });
        editBinding.temp33B.setOnClickListener(view -> {
            fontName=editBinding.temp33B.getTag().toString();

            String ex2B = editBinding.temp33B.getText().toString();
            clipArt_example2 = fun(editBinding.temp33B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp33B);

        });
        editBinding.temp34A.setOnClickListener(view -> {
            fontName=editBinding.temp34A.getTag().toString();

            String ex2B = editBinding.temp34A.getText().toString();

            clipArt_example = fun(editBinding.temp34A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp34A);

        });
        editBinding.temp34B.setOnClickListener(view -> {
            fontName=editBinding.temp34B.getTag().toString();

            String ex2B = editBinding.temp34B.getText().toString();
            clipArt_example2 = fun(editBinding.temp34B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp34B);

        });
        editBinding.temp35A.setOnClickListener(view -> {
            fontName=editBinding.temp35A.getTag().toString();

            String ex2B = editBinding.temp35A.getText().toString();
            clipArt_example = fun(editBinding.temp35A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp35A);

        });
        editBinding.temp36A.setOnClickListener(view -> {
            fontName=editBinding.temp36A.getTag().toString();

            String ex2B = editBinding.temp36A.getText().toString();
            clipArt_example = fun(editBinding.temp36A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp36A);

        });
        editBinding.temp37A.setOnClickListener(view -> {
            fontName=editBinding.temp37A.getTag().toString();

            String ex2B = editBinding.temp37A.getText().toString();

            clipArt_example = fun(editBinding.temp37A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp37A);

        });
        editBinding.temp37B.setOnClickListener(view -> {
            fontName=editBinding.temp37B.getTag().toString();

            String ex2B = editBinding.temp37B.getText().toString();
            clipArt_example2 = fun(editBinding.temp37B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp37B);

        });
        editBinding.temp38A.setOnClickListener(view -> {
            fontName=editBinding.temp38A.getTag().toString();

            String ex2B = editBinding.temp38A.getText().toString();

            clipArt_example = fun(editBinding.temp38A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp38A);

        });
        editBinding.temp38B.setOnClickListener(view -> {
            fontName=editBinding.temp38B.getTag().toString();

            String ex2B = editBinding.temp38B.getText().toString();
            clipArt_example2 = fun(editBinding.temp38B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp38B);

        });
        editBinding.temp39A.setOnClickListener(view -> {
            fontName=editBinding.temp39A.getTag().toString();

            String ex2B = editBinding.temp39A.getText().toString();

            clipArt_example = fun(editBinding.temp39A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp39A);

        });
        editBinding.temp40A.setOnClickListener(view -> {
            fontName=editBinding.temp40A.getTag().toString();

            String ex2B = editBinding.temp40A.getText().toString();

            clipArt_example = fun(editBinding.temp40A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp40A);

        });
        editBinding.temp41A.setOnClickListener(view -> {
            fontName=editBinding.temp41A.getTag().toString();

            String ex2B = editBinding.temp41A.getText().toString();

            clipArt_example = fun(editBinding.temp41A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp41A);

        });
        editBinding.temp42A.setOnClickListener(view -> {
            fontName=editBinding.temp42A.getTag().toString();

            String ex2B = editBinding.temp42A.getText().toString();

            clipArt_example = fun(editBinding.temp42A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp42A);

        });
        editBinding.temp42B.setOnClickListener(view -> {
            fontName=editBinding.temp42B.getTag().toString();

            String ex2B = editBinding.temp42B.getText().toString();

            clipArt_example2 = fun(editBinding.temp42B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp42B);

        });
        editBinding.temp43A.setOnClickListener(view -> {
            fontName=editBinding.temp43A.getTag().toString();

            String ex2B = editBinding.temp43A.getText().toString();

            clipArt_example = fun(editBinding.temp43A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp43A);

        });
        editBinding.temp44A.setOnClickListener(view -> {
            fontName=editBinding.temp44A.getTag().toString();

            String ex2B = editBinding.temp44A.getText().toString();
            clipArt_example = fun(editBinding.temp44A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp44A);

        });
        editBinding.temp44B.setOnClickListener(view -> {
            fontName=editBinding.temp44B.getTag().toString();

            String ex2B = editBinding.temp44B.getText().toString();

            clipArt_example2 = fun(editBinding.temp44B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp44B);

        });
        editBinding.temp45A.setOnClickListener(view -> {
            fontName=editBinding.temp45A.getTag().toString();

            String ex2B = editBinding.temp45A.getText().toString();

            clipArt_example = fun(editBinding.temp45A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp45A);

        });
        editBinding.temp46A.setOnClickListener(view -> {
            fontName=editBinding.temp46A.getTag().toString();

            String ex2B = editBinding.temp46A.getText().toString();
            clipArt_example = fun(editBinding.temp46A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp46A);

        });
        editBinding.temp46B.setOnClickListener(view -> {
            fontName=editBinding.temp46B.getTag().toString();

            String ex2B = editBinding.temp46B.getText().toString();
            clipArt_example2 = fun(editBinding.temp46B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp46B);

        });
        editBinding.temp47A.setOnClickListener(view -> {
            fontName=editBinding.temp47A.getTag().toString();

            String ex2B = editBinding.temp47A.getText().toString();

            clipArt_example = fun(editBinding.temp47A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp47A);

        });
        editBinding.temp48A.setOnClickListener(view -> {
            fontName=editBinding.temp48A.getTag().toString();

            String ex2B = editBinding.temp48A.getText().toString();
            clipArt_example = fun(editBinding.temp48A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp48A);

        });
        editBinding.temp48B.setOnClickListener(view -> {
            fontName=editBinding.temp48B.getTag().toString();

            String ex2B = editBinding.temp48B.getText().toString();

            clipArt_example2 = fun(editBinding.temp48B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp48B);

        });
        editBinding.temp48C.setOnClickListener(view -> {
            fontName=editBinding.temp48C.getTag().toString();

            String ex2B = editBinding.temp48C.getText().toString();
            clipArt_example3 = fun(editBinding.temp48C);
            //Tick and Cncel Functionality
            someFun(clipArt_example3);

            exampleEditingFun(ex2B, editBinding.temp48C);

        });
        editBinding.temp49A.setOnClickListener(view -> {
            fontName=editBinding.temp49A.getTag().toString();

            String ex2B = editBinding.temp49A.getText().toString();

            clipArt_example = fun(editBinding.temp49A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp49A);

        });
        editBinding.temp49B.setOnClickListener(view -> {
            fontName=editBinding.temp49B.getTag().toString();

            String ex2B = editBinding.temp49B.getText().toString();

            clipArt_example2 = fun(editBinding.temp49B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp49B);

        });
        editBinding.temp50A.setOnClickListener(view -> {
            fontName=editBinding.temp50A.getTag().toString();

            String ex2B = editBinding.temp50A.getText().toString();

            clipArt_example = fun(editBinding.temp50A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp50A);

        });
        editBinding.temp50B.setOnClickListener(view -> {
            fontName=editBinding.temp50B.getTag().toString();

            String ex2B = editBinding.temp50B.getText().toString();

            clipArt_example2 = fun(editBinding.temp50B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp50B);

        });
        editBinding.temp51A.setOnClickListener(view -> {
            fontName=editBinding.temp51A.getTag().toString();

            String ex2B = editBinding.temp51A.getText().toString();
            clipArt_example = fun(editBinding.temp51A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp51A);

        });
        editBinding.temp52A.setOnClickListener(view -> {
            fontName=editBinding.temp52A.getTag().toString();

            String ex2B = editBinding.temp52A.getText().toString();

            clipArt_example = fun(editBinding.temp52A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp52A);

        });
        editBinding.temp53A.setOnClickListener(view -> {
            fontName=editBinding.temp53A.getTag().toString();

            String ex2B = editBinding.temp53A.getText().toString();

            clipArt_example = fun(editBinding.temp53A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp53A);

        });
        editBinding.temp54A.setOnClickListener(view -> {
            fontName=editBinding.temp54A.getTag().toString();

            String ex2B = editBinding.temp54A.getText().toString();
            clipArt_example = fun(editBinding.temp54A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp54A);

        });
        editBinding.temp54B.setOnClickListener(view -> {
            fontName=editBinding.temp54B.getTag().toString();

            String ex2B = editBinding.temp54B.getText().toString();
            clipArt_example2 = fun(editBinding.temp54B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp54B);

        });
        editBinding.temp55A.setOnClickListener(view -> {
            fontName=editBinding.temp55A.getTag().toString();

            String ex2B = editBinding.temp55A.getText().toString();

            clipArt_example = fun(editBinding.temp55A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp55A);

        });
        editBinding.temp55B.setOnClickListener(view -> {
            fontName=editBinding.temp55B.getTag().toString();

            String ex2B = editBinding.temp55B.getText().toString();

            clipArt_example2 = fun(editBinding.temp55B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp55B);

        });
        editBinding.temp56A.setOnClickListener(view -> {
            fontName=editBinding.temp56A.getTag().toString();

            String ex2B = editBinding.temp56A.getText().toString();

            clipArt_example = fun(editBinding.temp56A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp56A);

        });
        editBinding.temp57A.setOnClickListener(view -> {
            fontName=editBinding.temp57A.getTag().toString();

            String ex2B = editBinding.temp57A.getText().toString();
            clipArt_example = fun(editBinding.temp57A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp57A);

        });
        editBinding.temp57B.setOnClickListener(view -> {
            fontName=editBinding.temp57B.getTag().toString();

            String ex2B = editBinding.temp57B.getText().toString();

            clipArt_example2 = fun(editBinding.temp57B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp57B);

        });
        editBinding.temp58A.setOnClickListener(view -> {
            fontName=editBinding.temp58A.getTag().toString();

            String ex2B = editBinding.temp58A.getText().toString();

            clipArt_example = fun(editBinding.temp58A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp58A);

        });
        editBinding.temp59A.setOnClickListener(view -> {
            fontName=editBinding.temp59A.getTag().toString();

            String ex2B = editBinding.temp59A.getText().toString();
            clipArt_example = fun(editBinding.temp59A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp59A);

        });
        editBinding.temp59B.setOnClickListener(view -> {
            fontName=editBinding.temp59B.getTag().toString();

            String ex2B = editBinding.temp59B.getText().toString();

            clipArt_example2 = fun(editBinding.temp59B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp59B);

        });
        editBinding.temp60A.setOnClickListener(view -> {
            fontName=editBinding.temp60A.getTag().toString();

            String ex2B = editBinding.temp60A.getText().toString();
            clipArt_example = fun(editBinding.temp60A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp60A);

        });
        editBinding.temp61A.setOnClickListener(view -> {
            fontName=editBinding.temp61A.getTag().toString();

            String ex2B = editBinding.temp61A.getText().toString();

            clipArt_example = fun(editBinding.temp61A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp61A);

        });
        editBinding.temp61B.setOnClickListener(view -> {
            fontName=editBinding.temp61B.getTag().toString();

            String ex2B = editBinding.temp61B.getText().toString();
            clipArt_example2 = fun(editBinding.temp61B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp61B);

        });
        editBinding.temp62A.setOnClickListener(view -> {
            fontName=editBinding.temp62A.getTag().toString();

            String ex2B = editBinding.temp62A.getText().toString();

            clipArt_example = fun(editBinding.temp62A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp62A);

        });
        editBinding.temp62B.setOnClickListener(view -> {
            fontName=editBinding.temp62B.getTag().toString();

            String ex2B = editBinding.temp62B.getText().toString();

            clipArt_example2 = fun(editBinding.temp62B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp62B);

        });
        editBinding.temp63A.setOnClickListener(view -> {
            fontName=editBinding.temp63A.getTag().toString();

            String ex2B = editBinding.temp63A.getText().toString();

            clipArt_example = fun(editBinding.temp63A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp63A);

        });
        editBinding.temp63B.setOnClickListener(view -> {
            fontName=editBinding.temp63B.getTag().toString();

            String ex2B = editBinding.temp63B.getText().toString();
            clipArt_example2 = fun(editBinding.temp63B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp63B);

        });
        editBinding.temp64A.setOnClickListener(view -> {
            fontName=editBinding.temp64A.getTag().toString();

            String ex2B = editBinding.temp64A.getText().toString();

            clipArt_example = fun(editBinding.temp64A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp64A);

        });
        editBinding.temp65A.setOnClickListener(view -> {
            fontName=editBinding.temp65A.getTag().toString();

            String ex2B = editBinding.temp65A.getText().toString();
            clipArt_example = fun(editBinding.temp65A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp65A);

        });
        editBinding.temp66A.setOnClickListener(view -> {
            fontName=editBinding.temp66A.getTag().toString();

            String ex2B = editBinding.temp66A.getText().toString();
            clipArt_example = fun(editBinding.temp66A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp66A);

        });
        editBinding.temp67A.setOnClickListener(view -> {
            fontName=editBinding.temp67A.getTag().toString();

            String ex2B = editBinding.temp67A.getText().toString();

            clipArt_example = fun(editBinding.temp67A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp67A);

        });
        editBinding.temp67B.setOnClickListener(view -> {
            fontName=editBinding.temp67B.getTag().toString();

            String ex2B = editBinding.temp67B.getText().toString();

            clipArt_example2 = fun(editBinding.temp67B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);
            exampleEditingFun(ex2B, editBinding.temp67B);

        });
        editBinding.temp68A.setOnClickListener(view -> {
            fontName=editBinding.temp68A.getTag().toString();

            String ex2B = editBinding.temp68A.getText().toString();
            clipArt_example = fun(editBinding.temp68A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp68A);

        });
        editBinding.temp69A.setOnClickListener(view -> {
            fontName=editBinding.temp69A.getTag().toString();

            String ex2B = editBinding.temp69A.getText().toString();
            clipArt_example = fun(editBinding.temp69A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp69A);

        });
        editBinding.temp69B.setOnClickListener(view -> {
            fontName=editBinding.temp69B.getTag().toString();

            String ex2B = editBinding.temp69B.getText().toString();
            clipArt_example2 = fun(editBinding.temp69B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp69B);

        });
        editBinding.temp70A.setOnClickListener(view -> {
            fontName=editBinding.temp70A.getTag().toString();

            String ex2B = editBinding.temp70A.getText().toString();

            clipArt_example = fun(editBinding.temp70A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp70A);

        });
        editBinding.temp70B.setOnClickListener(view -> {
            fontName=editBinding.temp70B.getTag().toString();

            String ex2B = editBinding.temp70B.getText().toString();
            clipArt_example2 = fun(editBinding.temp70B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp70B);

        });

        //86
        editBinding.temp86A.setOnClickListener(view -> {
            fontName=editBinding.temp86A.getTag().toString();

            String ex2B = editBinding.temp86A.getText().toString();
            clipArt_example = fun(editBinding.temp86A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp86A);

        });

        //87
        editBinding.temp87A.setOnClickListener(view -> {
            fontName=editBinding.temp87A.getTag().toString();

            String ex2B = editBinding.temp87A.getText().toString();
            clipArt_example = fun(editBinding.temp87A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp87A);

        });
        //88
        editBinding.temp88A.setOnClickListener(view -> {
            fontName=editBinding.temp88A.getTag().toString();

            String ex2B = editBinding.temp88A.getText().toString();
            clipArt_example = fun(editBinding.temp88A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp88A);

        });
        //89
        editBinding.temp89A.setOnClickListener(view -> {
            fontName=editBinding.temp89A.getTag().toString();

            String ex2B = editBinding.temp89A.getText().toString();
            clipArt_example = fun(editBinding.temp89A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp89A);

        });
        //90
        editBinding.temp90A.setOnClickListener(view -> {
            fontName=editBinding.temp90A.getTag().toString();

            String ex2B = editBinding.temp90A.getText().toString();
            clipArt_example = fun(editBinding.temp90A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp90A);

        });
        //91
        editBinding.temp91A.setOnClickListener(view -> {
            fontName=editBinding.temp91A.getTag().toString();

            String ex2B = editBinding.temp91A.getText().toString();
            clipArt_example = fun(editBinding.temp91A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp91A);

        });
        //92
        editBinding.temp92A.setOnClickListener(view -> {
            fontName=editBinding.temp92A.getTag().toString();

            String ex2B = editBinding.temp92A.getText().toString();
            clipArt_example = fun(editBinding.temp92A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp92A);

        });
        //93
        editBinding.temp93A.setOnClickListener(view -> {
            fontName=editBinding.temp93A.getTag().toString();

            String ex2B = editBinding.temp93A.getText().toString();
            clipArt_example = fun(editBinding.temp93A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp93A);

        });
        //94
        editBinding.temp94A.setOnClickListener(view -> {
            fontName=editBinding.temp94A.getTag().toString();

            String ex2B = editBinding.temp94A.getText().toString();
            clipArt_example = fun(editBinding.temp94A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp94A);

        });
        //95
        editBinding.temp95A.setOnClickListener(view -> {
            fontName=editBinding.temp95A.getTag().toString();

            String ex2B = editBinding.temp95A.getText().toString();
            clipArt_example = fun(editBinding.temp95A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp95A);

        });
        //96
        editBinding.temp96A.setOnClickListener(view -> {
            fontName=editBinding.temp96A.getTag().toString();
            String ex2B = editBinding.temp96A.getText().toString();

            clipArt_example = fun(editBinding.temp96A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp96A);

        });
        //97
        editBinding.temp97A.setOnClickListener(view -> {
            fontName=editBinding.temp97A.getTag().toString();

            String ex2B = editBinding.temp97A.getText().toString();
            clipArt_example = fun(editBinding.temp97A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp97A);

        });
        //98
        editBinding.temp98A.setOnClickListener(view -> {
            fontName=editBinding.temp98A.getTag().toString();

            String ex2B = editBinding.temp98A.getText().toString();
            clipArt_example = fun(editBinding.temp98A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp98A);

        });
        //99
        editBinding.temp99A.setOnClickListener(view -> {
            fontName=editBinding.temp99A.getTag().toString();

            String ex2B = editBinding.temp99A.getText().toString();
            clipArt_example = fun(editBinding.temp99A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp99A);

        });
        //100
        editBinding.temp100A.setOnClickListener(view -> {
            fontName=editBinding.temp100A.getTag().toString();

            String ex2B = editBinding.temp100A.getText().toString();
            clipArt_example = fun(editBinding.temp100A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp100A);

        });
        //101
        editBinding.temp101A.setOnClickListener(view -> {
            fontName=editBinding.temp101A.getTag().toString();

            String ex2B = editBinding.temp101A.getText().toString();
            clipArt_example = fun(editBinding.temp101A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp101A);

        });
        //102
        editBinding.temp102A.setOnClickListener(view -> {
            fontName=editBinding.temp102A.getTag().toString();

            String ex2B = editBinding.temp102A.getText().toString();
            clipArt_example = fun(editBinding.temp102A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp102A);

        });

        editBinding.temp102B.setOnClickListener(view -> {
            fontName=editBinding.temp102B.getTag().toString();

            String ex2B = editBinding.temp102B.getText().toString();
            clipArt_example2 = fun(editBinding.temp102B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp102B);

        });
        //103
        editBinding.temp103A.setOnClickListener(view -> {
            fontName=editBinding.temp103A.getTag().toString();

            String ex2B = editBinding.temp103A.getText().toString();
            clipArt_example = fun(editBinding.temp103A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp103A);

        });
        //104
        editBinding.temp104A.setOnClickListener(view -> {
            fontName=editBinding.temp104A.getTag().toString();

            String ex2B = editBinding.temp104A.getText().toString();
            clipArt_example = fun(editBinding.temp104A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp104A);

        });
        //105
        editBinding.temp105A.setOnClickListener(view -> {
            fontName=editBinding.temp105A.getTag().toString();

            String ex2B = editBinding.temp105A.getText().toString();
            clipArt_example = fun(editBinding.temp105A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp105A);

        });
        //106
        editBinding.temp106A.setOnClickListener(view -> {
            fontName=editBinding.temp106A.getTag().toString();

            String ex2B = editBinding.temp106A.getText().toString();
            clipArt_example = fun(editBinding.temp106A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp106A);

        });
        //107
        editBinding.temp107A.setOnClickListener(view -> {
            fontName=editBinding.temp107A.getTag().toString();

            String ex2B = editBinding.temp107A.getText().toString();
            clipArt_example = fun(editBinding.temp107A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp107A);

        });

        editBinding.temp107B.setOnClickListener(view -> {
            fontName=editBinding.temp107B.getTag().toString();

            String ex2B = editBinding.temp107B.getText().toString();
            clipArt_example2 = fun(editBinding.temp107B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp107B);

        });
        //108
        editBinding.temp108A.setOnClickListener(view -> {
            fontName=editBinding.temp108A.getTag().toString();

            String ex2B = editBinding.temp108A.getText().toString();
            clipArt_example = fun(editBinding.temp108A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp108A);

        });

        editBinding.temp108B.setOnClickListener(view -> {
            fontName=editBinding.temp108B.getTag().toString();

            String ex2B = editBinding.temp108B.getText().toString();
            clipArt_example2 = fun(editBinding.temp108B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp108B);

        });
        //109
        editBinding.temp109A.setOnClickListener(view -> {
            fontName=editBinding.temp109A.getTag().toString();

            String ex2B = editBinding.temp109A.getText().toString();
            clipArt_example = fun(editBinding.temp109A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp109A);

        });
        //110
        editBinding.temp110A.setOnClickListener(view -> {
            fontName=editBinding.temp110A.getTag().toString();

            String ex2B = editBinding.temp110A.getText().toString();
            clipArt_example = fun(editBinding.temp110A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp110A);

        });
        //111
        editBinding.temp111A.setOnClickListener(view -> {
            fontName=editBinding.temp111A.getTag().toString();

            String ex2B = editBinding.temp111A.getText().toString();
            clipArt_example = fun(editBinding.temp111A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp111A);

        });

        editBinding.temp111B.setOnClickListener(view -> {
            fontName=editBinding.temp111B.getTag().toString();

            String ex2B = editBinding.temp111B.getText().toString();
            clipArt_example2 = fun(editBinding.temp111B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp111B);

        });
        //112
        editBinding.temp112A.setOnClickListener(view -> {
            fontName=editBinding.temp112A.getTag().toString();

            String ex2B = editBinding.temp112A.getText().toString();
            clipArt_example = fun(editBinding.temp112A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp112A);

        });
        editBinding.temp112B.setOnClickListener(view -> {
            fontName=editBinding.temp112B.getTag().toString();

            String ex2B = editBinding.temp112B.getText().toString();
            clipArt_example2 = fun(editBinding.temp112B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp112B);

        });
        //113
        editBinding.temp113A.setOnClickListener(view -> {
            fontName=editBinding.temp113A.getTag().toString();

            String ex2B = editBinding.temp113A.getText().toString();
            clipArt_example = fun(editBinding.temp113A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);

            exampleEditingFun(ex2B, editBinding.temp113A);

        });
        editBinding.temp113B.setOnClickListener(view -> {
            fontName=editBinding.temp113B.getTag().toString();

            String ex2B = editBinding.temp113B.getText().toString();
            clipArt_example2 = fun(editBinding.temp113B);
            //Tick and Cncel Functionality
            someFun(clipArt_example2);

            exampleEditingFun(ex2B, editBinding.temp113B);

        });
        //114
        editBinding.temp114A.setOnClickListener(view -> {
            fontName=editBinding.temp114A.getTag().toString();

            String ex2B = editBinding.temp114A.getText().toString();
            clipArt_example = fun(editBinding.temp114A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp114A);

        });
        //115
        editBinding.temp115A.setOnClickListener(view -> {
            fontName=editBinding.temp115A.getTag().toString();

            String ex2B = editBinding.temp115A.getText().toString();
            clipArt_example = fun(editBinding.temp115A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp115A);

        });
        //116
        editBinding.temp116A.setOnClickListener(view -> {
            fontName=editBinding.temp116A.getTag().toString();

            String ex2B = editBinding.temp116A.getText().toString();
            clipArt_example = fun(editBinding.temp116A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp116A);

        });
        //117
        editBinding.temp117A.setOnClickListener(view -> {
            fontName=editBinding.temp117A.getTag().toString();

            String ex2B = editBinding.temp117A.getText().toString();
            clipArt_example = fun(editBinding.temp117A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp117A);

        });
        //118
        editBinding.temp118A.setOnClickListener(view -> {
            fontName=editBinding.temp118A.getTag().toString();

            String ex2B = editBinding.temp118A.getText().toString();
            clipArt_example = fun(editBinding.temp118A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp118A);

        });
        //119
        editBinding.temp119A.setOnClickListener(view -> {
            fontName=editBinding.temp119A.getTag().toString();

            String ex2B = editBinding.temp119A.getText().toString();
            clipArt_example = fun(editBinding.temp119A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp119A);

        });
        //120
        editBinding.temp120A.setOnClickListener(view -> {
            fontName=editBinding.temp120A.getTag().toString();

            String ex2B = editBinding.temp120A.getText().toString();
            clipArt_example = fun(editBinding.temp120A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp120A);

        });
        //121
        editBinding.temp121A.setOnClickListener(view -> {
            fontName=editBinding.temp121A.getTag().toString();

            String ex2B = editBinding.temp121A.getText().toString();
            clipArt_example = fun(editBinding.temp121A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp121A);

        });
        //122
        editBinding.temp122A.setOnClickListener(view -> {
            fontName=editBinding.temp122A.getTag().toString();

            String ex2B = editBinding.temp122A.getText().toString();
            clipArt_example = fun(editBinding.temp122A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp122A);

        });
        //123
        editBinding.temp123A.setOnClickListener(view -> {
            fontName=editBinding.temp123A.getTag().toString();

            String ex2B = editBinding.temp123A.getText().toString();
            clipArt_example = fun(editBinding.temp123A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp123A);

        });
        //124
        editBinding.temp124A.setOnClickListener(view -> {
            fontName=editBinding.temp124A.getTag().toString();

            String ex2B = editBinding.temp124A.getText().toString();
            clipArt_example = fun(editBinding.temp124A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp124A);

        });
        //125
        editBinding.temp125A.setOnClickListener(view -> {
            fontName=editBinding.temp125A.getTag().toString();

            String ex2B = editBinding.temp125A.getText().toString();
            clipArt_example = fun(editBinding.temp125A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp125A);

        });
        //126
        editBinding.temp126A.setOnClickListener(view -> {
            fontName=editBinding.temp126A.getTag().toString();

            String ex2B = editBinding.temp126A.getText().toString();
            clipArt_example = fun(editBinding.temp126A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp126A);

        });
        //127
        editBinding.temp127A.setOnClickListener(view -> {
            fontName=editBinding.temp127A.getTag().toString();

            String ex2B = editBinding.temp127A.getText().toString();
            clipArt_example = fun(editBinding.temp127A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp127A);

        });
        //128
        editBinding.temp128A.setOnClickListener(view -> {
            fontName=editBinding.temp128A.getTag().toString();

            String ex2B = editBinding.temp128A.getText().toString();
            clipArt_example = fun(editBinding.temp128A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp128A);

        });
        //129
        editBinding.temp129A.setOnClickListener(view -> {
            fontName=editBinding.temp129A.getTag().toString();

            String ex2B = editBinding.temp129A.getText().toString();
            clipArt_example = fun(editBinding.temp129A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp129A);

        });
        editBinding.temp130A.setOnClickListener(view -> {
            fontName=editBinding.temp130A.getTag().toString();

            String ex2B = editBinding.temp130A.getText().toString();
            clipArt_example = fun(editBinding.temp130A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp130A);

        });
        editBinding.temp131A.setOnClickListener(view -> {
            fontName=editBinding.temp131A.getTag().toString();

            String ex2B = editBinding.temp131A.getText().toString();
            clipArt_example = fun(editBinding.temp131A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp131A);

        });
        editBinding.temp132A.setOnClickListener(view -> {
            fontName=editBinding.temp132A.getTag().toString();

            String ex2B = editBinding.temp132A.getText().toString();
            clipArt_example = fun(editBinding.temp132A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp132A);

        });
        editBinding.temp133A.setOnClickListener(view -> {
            fontName=editBinding.temp133A.getTag().toString();

            String ex2B = editBinding.temp133A.getText().toString();
            clipArt_example = fun(editBinding.temp133A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp133A);

        });
        editBinding.temp134A.setOnClickListener(view -> {
            fontName=editBinding.temp134A.getTag().toString();

            String ex2B = editBinding.temp134A.getText().toString();
            clipArt_example = fun(editBinding.temp134A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp134A);

        });
        editBinding.temp135A.setOnClickListener(view -> {
            fontName=editBinding.temp135A.getTag().toString();

            String ex2B = editBinding.temp135A.getText().toString();
            clipArt_example = fun(editBinding.temp135A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp135A);

        });
        editBinding.temp136A.setOnClickListener(view -> {
            fontName=editBinding.temp136A.getTag().toString();

            String ex2B = editBinding.temp136A.getText().toString();
            clipArt_example = fun(editBinding.temp136A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp136A);

        });
        editBinding.temp137A.setOnClickListener(view -> {
            fontName=editBinding.temp137A.getTag().toString();

            String ex2B = editBinding.temp137A.getText().toString();
            clipArt_example = fun(editBinding.temp137A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp137A);

        });
        editBinding.temp138A.setOnClickListener(view -> {
            fontName=editBinding.temp138A.getTag().toString();

            String ex2B = editBinding.temp138A.getText().toString();
            clipArt_example = fun(editBinding.temp138A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp138A);

        });
        editBinding.temp139A.setOnClickListener(view -> {
            fontName=editBinding.temp139A.getTag().toString();

            String ex2B = editBinding.temp139A.getText().toString();
            clipArt_example = fun(editBinding.temp139A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp139A);

        });
        editBinding.temp140A.setOnClickListener(view -> {
            fontName=editBinding.temp140A.getTag().toString();

            String ex2B = editBinding.temp140A.getText().toString();
            clipArt_example = fun(editBinding.temp140A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp140A);

        });
        editBinding.temp141A.setOnClickListener(view -> {
            fontName=editBinding.temp141A.getTag().toString();

            String ex2B = editBinding.temp141A.getText().toString();
            clipArt_example = fun(editBinding.temp141A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp141A);

        });
        editBinding.temp142A.setOnClickListener(view -> {
            fontName=editBinding.temp142A.getTag().toString();

            String ex2B = editBinding.temp142A.getText().toString();
            clipArt_example = fun(editBinding.temp142A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp142A);

        });
        editBinding.temp143A.setOnClickListener(view -> {
            fontName=editBinding.temp143A.getTag().toString();

            String ex2B = editBinding.temp143A.getText().toString();
            clipArt_example = fun(editBinding.temp143A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp143A);

        });
        editBinding.temp144A.setOnClickListener(view -> {
            fontName=editBinding.temp144A.getTag().toString();

            String ex2B = editBinding.temp144A.getText().toString();
            clipArt_example = fun(editBinding.temp144A);
            //Tick and Cncel Functionality
            someFun(clipArt_example);
            exampleEditingFun(ex2B, editBinding.temp144A);

        });

    }

    private void exampleEditingFun(String ex, TextView temp) {

        if (ex.equals(getString(R.string.temp1A_1)) || ex.equals(getString(R.string.temp1A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp1B_1)) || ex.equals(getString(R.string.temp1B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //71 temp Text
        else if (ex.equals(getString(R.string.temp71A_1)) || ex.equals(getString(R.string.temp71A_2))) {

            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp71B_1)) || ex.equals(getString(R.string.temp71B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp2A_1)) || ex.equals(getString(R.string.temp2A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }

        //72 temp Text
        else if (ex.equals(getString(R.string.temp72A_1)) || ex.equals(getString(R.string.temp72A_2))) {

            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp72B_1)) || ex.equals(getString(R.string.temp72B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);

        } else if (ex.equals(getString(R.string.temp3A_1)) || ex.equals(getString(R.string.temp3A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }

        //73 temp Text
        else if (ex.equals(getString(R.string.temp73A_1)) || ex.equals(getString(R.string.temp73A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp4A_1)) || ex.equals(getString(R.string.temp4A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp4B_1)) || ex.equals(getString(R.string.temp4B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //74 temp Text
        else if (ex.equals(getString(R.string.temp74A_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp74B_1)) || ex.equals(getString(R.string.temp74B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp74C_1)) || ex.equals(getString(R.string.temp74C_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp74D_1))) {
            exampleClickListener(ex, temp, clipArt_example4);
        } else if (ex.equals(getString(R.string.temp74E_1))) {
            exampleClickListener(ex, temp, clipArt_example5);
        } else if (ex.equals(getString(R.string.temp5A_1)) || ex.equals(getString(R.string.temp5A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp5B_1)) || ex.equals(getString(R.string.temp5B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //75 temp Text
        else if (ex.equals(getString(R.string.temp75A_1)) || ex.equals(getString(R.string.temp75A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp75B_1)) || ex.equals(getString(R.string.temp75B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp75C_1)) || ex.equals(getString(R.string.temp75C_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp75D_1)) || ex.equals(getString(R.string.temp75D_2))) {
            exampleClickListener(ex, temp, clipArt_example4);
        } else if (ex.equals(getString(R.string.temp6A_1)) || ex.equals(getString(R.string.temp6A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp6B_1)) || ex.equals(getString(R.string.temp6B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //76 temp Text
        else if (ex.equals(getString(R.string.temp76A_1)) || ex.equals(getString(R.string.temp76A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp76B_1)) || ex.equals(getString(R.string.temp76B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp7A_1)) || ex.equals(getString(R.string.temp7A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }

        //77 temp Text
        else if (ex.equals(getString(R.string.temp77A_1)) || ex.equals(getString(R.string.temp77A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp8A_1)) || ex.equals(getString(R.string.temp8A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }

        //78 temp Text
        else if (ex.equals(getString(R.string.temp78A_1)) || ex.equals(getString(R.string.temp78A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp78B_1)) || ex.equals(getString(R.string.temp78B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp9A_1)) || ex.equals(getString(R.string.temp9A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }

        //79 temp Text
        else if (ex.equals(getString(R.string.temp79A_1)) || ex.equals(getString(R.string.temp79A_2))) {

            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp10A_1)) || ex.equals(getString(R.string.temp10A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp10B_1)) || ex.equals(getString(R.string.temp10B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //80 temp Text
        else if (ex.equals(getString(R.string.temp80A_1)) || ex.equals(getString(R.string.temp80A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp11A_1)) || ex.equals(getString(R.string.temp11A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp11B_1)) || ex.equals(getString(R.string.temp11B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //81 temp Text
        else if (ex.equals(getString(R.string.temp81A_1)) || ex.equals(getString(R.string.temp81A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp81B_1)) || ex.equals(getString(R.string.temp81B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp81C_1)) || ex.equals(getString(R.string.temp81C_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp12A_1)) || ex.equals(getString(R.string.temp12A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp12B_1)) || ex.equals(getString(R.string.temp12B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //82 temp Text
        else if (ex.equals(getString(R.string.temp82A_1)) || ex.equals(getString(R.string.temp82A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp13A_1)) || ex.equals(getString(R.string.temp13A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }

        //83 temp Text
        else if (ex.equals(getString(R.string.temp83A_1)) || ex.equals(getString(R.string.temp83A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp13B_1)) || ex.equals(getString(R.string.temp13B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp14A_1)) || ex.equals(getString(R.string.temp14A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp14B_1)) || ex.equals(getString(R.string.temp14B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //84 temp Text
        else if (ex.equals(getString(R.string.temp84A_1)) || ex.equals(getString(R.string.temp84A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp15A_1)) || ex.equals(getString(R.string.temp15A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp15B_1)) || ex.equals(getString(R.string.temp15B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }

        //85 temp Text
        else if (ex.equals(getString(R.string.temp85A_1)) || ex.equals(getString(R.string.temp85A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp85B_1)) || ex.equals(getString(R.string.temp85B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp16A_1)) || ex.equals(getString(R.string.temp16A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp17A_1)) || ex.equals(getString(R.string.temp17A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp18A_1)) || ex.equals(getString(R.string.temp18A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp18B_1)) || ex.equals(getString(R.string.temp18B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp19A_1)) || ex.equals(getString(R.string.temp19A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp20A_1)) || ex.equals(getString(R.string.temp20A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp20B_1)) || ex.equals(getString(R.string.temp20B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp21A_1)) || ex.equals(getString(R.string.temp21A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp22A_1)) || ex.equals(getString(R.string.temp22A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp22B_1)) || ex.equals(getString(R.string.temp22B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp22C_1)) || ex.equals(getString(R.string.temp22C_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp23A_1)) || ex.equals(getString(R.string.temp23A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp24A_1)) || ex.equals(getString(R.string.temp24A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp25A_1)) || ex.equals(getString(R.string.temp25A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp25B_1)) || ex.equals(getString(R.string.temp25B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp25C_1)) || ex.equals(getString(R.string.temp25C_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp26A_1)) || ex.equals(getString(R.string.temp26A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp26B_1)) || ex.equals(getString(R.string.temp26B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp27A_1)) || ex.equals(getString(R.string.temp27A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp27B_1)) || ex.equals(getString(R.string.temp27B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp28A_1)) || ex.equals(getString(R.string.temp28A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp28B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp28D_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp29A_1)) || ex.equals(getString(R.string.temp29A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp29B_1)) || ex.equals(getString(R.string.temp29B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp30A_1)) || ex.equals(getString(R.string.temp30A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp30B_1)) || ex.equals(getString(R.string.temp30B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp30C_1)) || ex.equals(getString(R.string.temp30C_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp30D_1)) || ex.equals(getString(R.string.temp30D_2))) {
            exampleClickListener(ex, temp, clipArt_example4);
        } else if (ex.equals(getString(R.string.temp31A_1)) || ex.equals(getString(R.string.temp31A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp31B_1))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp31C_1)) || ex.equals(getString(R.string.temp31C_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp32A_1)) || ex.equals(getString(R.string.temp32A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp32B_1)) || ex.equals(getString(R.string.temp32B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp33A_1)) || ex.equals(getString(R.string.temp33A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp33B_1)) || ex.equals(getString(R.string.temp33B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp34A_1)) || ex.equals(getString(R.string.temp34A_2))) {

            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp34B_1)) || ex.equals(getString(R.string.temp34B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp35A_1)) || ex.equals(getString(R.string.temp35A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp36A_1)) || ex.equals(getString(R.string.temp36A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp37A_1)) || ex.equals(getString(R.string.temp37A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp37B_1)) || ex.equals(getString(R.string.temp37B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp38A_1)) || ex.equals(getString(R.string.temp38A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp38B_1)) || ex.equals(getString(R.string.temp38B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp39A_1)) || ex.equals(getString(R.string.temp39A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp40A_1)) || ex.equals(getString(R.string.temp40A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp41A_1)) || ex.equals(getString(R.string.temp41A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp42A_1)) || ex.equals(getString(R.string.temp42A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp42B_1)) || ex.equals(getString(R.string.temp42B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp43A_1)) || ex.equals(getString(R.string.temp43A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp44A_1)) || ex.equals(getString(R.string.temp44A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp44B_1)) || ex.equals(getString(R.string.temp44B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp45A_1)) || ex.equals(getString(R.string.temp45A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp46A_1)) || ex.equals(getString(R.string.temp46A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp46B_1)) || ex.equals(getString(R.string.temp46B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp47A_1)) || ex.equals(getString(R.string.temp47A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp48A_1)) || ex.equals(getString(R.string.temp48A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp48B_1)) || ex.equals(getString(R.string.temp48B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp48C_1)) || ex.equals(getString(R.string.temp48C_2))) {
            exampleClickListener(ex, temp, clipArt_example3);
        } else if (ex.equals(getString(R.string.temp49A_1)) || ex.equals(getString(R.string.temp49A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp49B_1)) || ex.equals(getString(R.string.temp49B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp50A_1)) || ex.equals(getString(R.string.temp50A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp50B_1)) || ex.equals(getString(R.string.temp50B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp51A_1)) || ex.equals(getString(R.string.temp51A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp52A_1)) || ex.equals(getString(R.string.temp52A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp53A_1)) || ex.equals(getString(R.string.temp53A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp54A_1)) || ex.equals(getString(R.string.temp54A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp54B_1)) || ex.equals(getString(R.string.temp54B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp55A_1)) || ex.equals(getString(R.string.temp55A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp55B_1)) || ex.equals(getString(R.string.temp55B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp56A_1)) || ex.equals(getString(R.string.temp56A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp57A_1)) || ex.equals(getString(R.string.temp57A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp57B_1)) || ex.equals(getString(R.string.temp57B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp58A_1)) || ex.equals(getString(R.string.temp58A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp59A_1)) || ex.equals(getString(R.string.temp59A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp59B_1)) || ex.equals(getString(R.string.temp59B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp60A_1)) || ex.equals(getString(R.string.temp60A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp61A_1)) || ex.equals(getString(R.string.temp61A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp61B_1)) || ex.equals(getString(R.string.temp61B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp62A_1)) || ex.equals(getString(R.string.temp62A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp62B_1)) || ex.equals(getString(R.string.temp62B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp63A_1)) || ex.equals(getString(R.string.temp63A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp63B_1))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp64A_1)) || ex.equals(getString(R.string.temp64A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp65A_1)) || ex.equals(getString(R.string.temp65A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp66A_1)) || ex.equals(getString(R.string.temp66A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp67A_1)) || ex.equals(getString(R.string.temp67A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp67B_1)) || ex.equals(getString(R.string.temp67B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp68A_1)) || ex.equals(getString(R.string.temp68A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp69A_1)) || ex.equals(getString(R.string.temp69A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp69B_1)) || ex.equals(getString(R.string.temp69B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        } else if (ex.equals(getString(R.string.temp70A_1)) || ex.equals(getString(R.string.temp70A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp70B_1)) || ex.equals(getString(R.string.temp70B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);

        }
        //86
        else if (ex.equals(getString(R.string.temp86A_1)) || ex.equals(getString(R.string.temp86B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //87
        else if (ex.equals(getString(R.string.temp87A_1)) || ex.equals(getString(R.string.temp87B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //88
        else if (ex.equals(getString(R.string.temp88A_1)) || ex.equals(getString(R.string.temp88B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //89
        else if (ex.equals(getString(R.string.temp89A_1)) || ex.equals(getString(R.string.temp89B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //90
        else if (ex.equals(getString(R.string.temp90A_1)) || ex.equals(getString(R.string.temp90B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //91
        else if (ex.equals(getString(R.string.temp91A_1)) || ex.equals(getString(R.string.temp91B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //92
        else if (ex.equals(getString(R.string.temp92A_1)) || ex.equals(getString(R.string.temp92B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //93
        else if (ex.equals(getString(R.string.temp93A_1)) || ex.equals(getString(R.string.temp93B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //94
        else if (ex.equals(getString(R.string.temp94A_1)) || ex.equals(getString(R.string.temp94B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //95
        else if (ex.equals(getString(R.string.temp95A_1)) || ex.equals(getString(R.string.temp95B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //96
        else if (ex.equals(getString(R.string.temp96A_1)) || ex.equals(getString(R.string.temp96B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //97
        else if (ex.equals(getString(R.string.temp97A_1)) || ex.equals(getString(R.string.temp97B_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //98
        else if (ex.equals(getString(R.string.temp98A_1)) || ex.equals(getString(R.string.temp98A_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //99
        else if (ex.equals(getString(R.string.temp99A_1)) || ex.equals(getString(R.string.temp99A_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //100
        else if (ex.equals(getString(R.string.temp100A_1)) || ex.equals(getString(R.string.temp100A_1))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //101
        else if (ex.equals(getString(R.string.temp101A_1)) || ex.equals(getString(R.string.temp101A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //102
        else if (ex.equals(getString(R.string.temp102A_1)) || ex.equals(getString(R.string.temp102A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp102B_1)) || ex.equals(getString(R.string.temp102B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }
        //103
        else if (ex.equals(getString(R.string.temp103A_1)) || ex.equals(getString(R.string.temp103A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //104
        else if (ex.equals(getString(R.string.temp104A_1)) || ex.equals(getString(R.string.temp104A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //105
        else if (ex.equals(getString(R.string.temp105A_1)) || ex.equals(getString(R.string.temp105A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //106
        else if (ex.equals(getString(R.string.temp106A_1)) || ex.equals(getString(R.string.temp106A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //107
        else if (ex.equals(getString(R.string.temp107A_1)) || ex.equals(getString(R.string.temp107A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp107B_1)) || ex.equals(getString(R.string.temp107B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }
        //108
        else if (ex.equals(getString(R.string.temp108A_1)) || ex.equals(getString(R.string.temp108A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp108B_1)) || ex.equals(getString(R.string.temp108B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }
        //109
        else if (ex.equals(getString(R.string.temp109A_1)) || ex.equals(getString(R.string.temp109A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //110
        else if (ex.equals(getString(R.string.temp110A_1)) || ex.equals(getString(R.string.temp110A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
//111
        else if (ex.equals(getString(R.string.temp111A_1)) || ex.equals(getString(R.string.temp111A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp111B_1)) || ex.equals(getString(R.string.temp111B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }
        //112
        else if (ex.equals(getString(R.string.temp112A_1)) || ex.equals(getString(R.string.temp112A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp112B_1)) || ex.equals(getString(R.string.temp112B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }
        //113
        else if (ex.equals(getString(R.string.temp113A_1)) || ex.equals(getString(R.string.temp113A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        } else if (ex.equals(getString(R.string.temp113B_1)) || ex.equals(getString(R.string.temp113B_2))) {
            exampleClickListener(ex, temp, clipArt_example2);
        }
        //114
        else if (ex.equals(getString(R.string.temp114A_1)) || ex.equals(getString(R.string.temp114A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //115
        else if (ex.equals(getString(R.string.temp115A_1)) || ex.equals(getString(R.string.temp115A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //116
        else if (ex.equals(getString(R.string.temp116A_1)) || ex.equals(getString(R.string.temp116A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //117
        else if (ex.equals(getString(R.string.temp117A_1)) || ex.equals(getString(R.string.temp117A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //118
        else if (ex.equals(getString(R.string.temp118A_1)) || ex.equals(getString(R.string.temp118A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //119
        else if (ex.equals(getString(R.string.temp119A_1)) || ex.equals(getString(R.string.temp119A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //120
        else if (ex.equals(getString(R.string.temp120A_1)) || ex.equals(getString(R.string.temp120A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //121
        else if (ex.equals(getString(R.string.temp121A_1)) || ex.equals(getString(R.string.temp121A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //122
        else if (ex.equals(getString(R.string.temp122A_1)) || ex.equals(getString(R.string.temp122A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //123
        else if (ex.equals(getString(R.string.temp123A_1)) || ex.equals(getString(R.string.temp123A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //124
        else if (ex.equals(getString(R.string.temp124A_1)) || ex.equals(getString(R.string.temp124A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //125
        else if (ex.equals(getString(R.string.temp125A_1)) || ex.equals(getString(R.string.temp125A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //126
        else if (ex.equals(getString(R.string.temp126A_1)) || ex.equals(getString(R.string.temp126A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //127
        else if (ex.equals(getString(R.string.temp127A_1)) || ex.equals(getString(R.string.temp127A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //128
        else if (ex.equals(getString(R.string.temp128A_1)) || ex.equals(getString(R.string.temp128A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //129
        else if (ex.equals(getString(R.string.temp129A_1)) || ex.equals(getString(R.string.temp129A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //130
        else if (ex.equals(getString(R.string.temp130A_1)) || ex.equals(getString(R.string.temp130A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //131
        else if (ex.equals(getString(R.string.temp131A_1)) || ex.equals(getString(R.string.temp131A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //132
        else if (ex.equals(getString(R.string.temp132A_1)) || ex.equals(getString(R.string.temp132A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //133
        else if (ex.equals(getString(R.string.temp133A_1)) || ex.equals(getString(R.string.temp133A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //134
        else if (ex.equals(getString(R.string.temp134A_1)) || ex.equals(getString(R.string.temp134A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //135
        else if (ex.equals(getString(R.string.temp135A_1)) || ex.equals(getString(R.string.temp135A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //136
        else if (ex.equals(getString(R.string.temp136A_1)) || ex.equals(getString(R.string.temp136A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //137
        else if (ex.equals(getString(R.string.temp137A_1)) || ex.equals(getString(R.string.temp137A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //138
        else if (ex.equals(getString(R.string.temp138A_1)) || ex.equals(getString(R.string.temp138A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //139
        else if (ex.equals(getString(R.string.temp139A_1)) || ex.equals(getString(R.string.temp139A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //140
        else if (ex.equals(getString(R.string.temp140A_1)) || ex.equals(getString(R.string.temp140A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //141
        else if (ex.equals(getString(R.string.temp141A_1)) || ex.equals(getString(R.string.temp141A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //142
        else if (ex.equals(getString(R.string.temp142A_1)) || ex.equals(getString(R.string.temp142A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //143
        else if (ex.equals(getString(R.string.temp143A_1)) || ex.equals(getString(R.string.temp143A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
        //144
        else if (ex.equals(getString(R.string.temp144A_1)) || ex.equals(getString(R.string.temp144A_2))) {
            exampleClickListener(ex, temp, clipArt_example);
        }
    }

    private void exampleTextFun() {

        if (tempName==null)
        {

        }
        else {
            //Example Button Click Show Change Polpular 1 to 14
            if (tempName.equals(getString(R.string.tempName_1))) {
                example_2_Show(editBinding.temp1A, editBinding.temp1B,
                        getString(R.string.temp1A_1), getString(R.string.temp1B_1),
                        getString(R.string.temp1A_2), getString(R.string.temp1B_2));
            }

            //71 temp
//        else if (retrieveImage == R.drawable.temp71) {
//
//            example_2_Show(editBinding.temp71A, editBinding.temp71B,
//                    getString(R.string.temp71A_1), getString(R.string.temp71B_1),
//                    getString(R.string.temp71A_2), getString(R.string.temp71B_2));
//
//        }
            else if (tempName.equals(getString(R.string.tempName_2))) {

                example_1_Show(editBinding.temp2A, getString(R.string.temp2A_1), getString(R.string.temp2A_2));

            }

            //72 temp
//        else if (retrieveImage == R.drawable.temp72) {
//
//            example_2_Show(editBinding.temp72A, editBinding.temp72B,
//                    getString(R.string.temp72A_1), getString(R.string.temp72B_1),
//                    getString(R.string.temp72A_2), getString(R.string.temp72B_2));
//        }
            else if (tempName.equals(getString(R.string.tempName_3))) {

                example_1_Show(editBinding.temp3A, getString(R.string.temp3A_1), getString(R.string.temp3A_2));
            }

            //73 temp
//        else if (retrieveImage == R.drawable.temp73) {
//
//            example_1_Show(editBinding.temp73A, getString(R.string.temp73A_1), getString(R.string.temp73A_2));
//
//        }
            else if (tempName.equals(getString(R.string.tempName_4))) {

                example_2_Show(editBinding.temp4A, editBinding.temp4B,
                        getString(R.string.temp4A_1), getString(R.string.temp4B_1),
                        getString(R.string.temp4A_2), getString(R.string.temp4B_2));
            }

            //74 temp
//        else if (retrieveImage == R.drawable.temp74) {
//
//            example_5_Show(editBinding.temp74A, editBinding.temp74B, editBinding.temp74C, editBinding.temp74D, editBinding.temp74E,
//                    getString(R.string.temp74A_1), getString(R.string.temp74B_1),
//                    getString(R.string.temp74C_1), getString(R.string.temp74D_1), getString(R.string.temp74E_1),
//                    getString(R.string.temp74B_2), getString(R.string.temp74C_2));
//        }
            else if (tempName.equals(getString(R.string.tempName_5))) {

                example_2_Show(editBinding.temp5A, editBinding.temp5B,
                        getString(R.string.temp5A_1), getString(R.string.temp5B_1),
                        getString(R.string.temp5A_2), getString(R.string.temp5B_2));
            }

            //75 temp
//        else if (retrieveImage == R.drawable.temp75) {
//
//            example_4_Show(editBinding.temp75A, editBinding.temp75B, editBinding.temp75C, editBinding.temp75D,
//                    getString(R.string.temp75A_1), getString(R.string.temp75B_1),
//                    getString(R.string.temp75C_1), getString(R.string.temp75D_1), getString(R.string.temp75A_2),
//                    getString(R.string.temp75B_2), getString(R.string.temp75C_2), getString(R.string.temp75D_2));
//        }
            else if (tempName.equals(getString(R.string.tempName_6))) {

                example_2_Show(editBinding.temp6A, editBinding.temp6B,
                        getString(R.string.temp6A_1), getString(R.string.temp6B_1),
                        getString(R.string.temp6A_2), getString(R.string.temp6B_2));
            }
            //76 temp
//        else if (retrieveImage == R.drawable.temp76) {
//
//            example_2_Show(editBinding.temp76A, editBinding.temp76B,
//                    getString(R.string.temp76A_1), getString(R.string.temp76B_1),
//                    getString(R.string.temp76A_2), getString(R.string.temp76B_2));
//        }
            else if (tempName.equals(getString(R.string.tempName_7))) {

                example_1_Show(editBinding.temp7A, getString(R.string.temp7A_1), getString(R.string.temp7A_2));
            }
            //77 temp
//        else if (retrieveImage == R.drawable.temp77) {
//
//            example_1_Show(editBinding.temp77A, getString(R.string.temp77A_1), getString(R.string.temp77A_2));
//        }
            else if (tempName.equals(getString(R.string.tempName_8))) {

                example_1_Show(editBinding.temp8A, getString(R.string.temp8A_1), getString(R.string.temp8A_2));

            }

            //78 temp
//        else if (retrieveImage == R.drawable.temp78) {
//
//            example_2_Show(editBinding.temp78A, editBinding.temp78B,
//                    getString(R.string.temp78A_1), getString(R.string.temp78B_1),
//                    getString(R.string.temp78A_2), getString(R.string.temp78B_2));
//        }
            else if (tempName.equals(getString(R.string.tempName_9))) {

                example_1_Show(editBinding.temp9A, getString(R.string.temp9A_1), getString(R.string.temp9A_2));

            }

            //79 temp
//        else if (retrieveImage == R.drawable.temp79) {
//            example_1_Show(editBinding.temp79A, getString(R.string.temp79A_1), getString(R.string.temp79A_2));
//        }
            else if (tempName.equals(getString(R.string.tempName_10))) {

                example_2_Show(editBinding.temp10A, editBinding.temp10B,
                        getString(R.string.temp10A_1), getString(R.string.temp10B_1),
                        getString(R.string.temp10A_2), getString(R.string.temp10B_2));
            }

            //80 temp
//        else if (retrieveImage == R.drawable.temp80) {
//
//            example_1_Show(editBinding.temp80A, getString(R.string.temp80A_1), getString(R.string.temp80A_2));
//
//        }
            else if (tempName.equals(getString(R.string.tempName_11))) {

                example_2_Show(editBinding.temp11A, editBinding.temp11B,
                        getString(R.string.temp11A_1), getString(R.string.temp11B_1),
                        getString(R.string.temp11A_2), getString(R.string.temp11B_2));
            }

            //81 temp
//        else if (retrieveImage == R.drawable.temp81) {
//
//            example_3_Show(editBinding.temp81A, editBinding.temp81B, editBinding.temp81C,
//                    getString(R.string.temp81A_1), getString(R.string.temp81B_1),
//                    getString(R.string.temp81C_1), getString(R.string.temp81A_2),
//                    getString(R.string.temp81B_2), getString(R.string.temp81C_2));
//        }
            else if (tempName.equals(getString(R.string.tempName_12))) {

                example_2_Show(editBinding.temp12A, editBinding.temp12B,
                        getString(R.string.temp12A_1), getString(R.string.temp12B_1),
                        getString(R.string.temp12A_2), getString(R.string.temp12B_2));
            }

            //82 temp
//        else if (retrieveImage == R.drawable.temp82) {
//
//            example_1_Show(editBinding.temp82A, getString(R.string.temp82A_1), getString(R.string.temp82A_2));
//
//        }
            else if (tempName.equals(getString(R.string.tempName_13))) {

                example_2_Show(editBinding.temp13A, editBinding.temp13B,
                        getString(R.string.temp13A_1), getString(R.string.temp13B_1),
                        getString(R.string.temp13A_2), getString(R.string.temp13B_2));
            }

            //83 temp
//        else if (retrieveImage == R.drawable.temp83) {
//            example_1_Show(editBinding.temp83A, getString(R.string.temp83A_1), getString(R.string.temp83A_2));
//
//        }
            else if (tempName.equals(getString(R.string.tempName_14))) {
                example_2_Show(editBinding.temp14A, editBinding.temp14B,
                        getString(R.string.temp14A_1), getString(R.string.temp14B_1),
                        getString(R.string.temp14A_2), getString(R.string.temp14B_2));
            }

            //Example Button Click Show Change Polpular 15 to 28
            else if (tempName.equals(getString(R.string.tempName_15))) {

                example_2_Show(editBinding.temp15A, editBinding.temp15B,
                        getString(R.string.temp15A_1), getString(R.string.temp15B_1),
                        getString(R.string.temp15A_2), getString(R.string.temp15B_2));
            } else if (tempName.equals(getString(R.string.tempName_16))) {
                example_1_Show(editBinding.temp16A, getString(R.string.temp16A_1), getString(R.string.temp16A_2));

            } else if (tempName.equals(getString(R.string.tempName_17))) {

                example_1_Show(editBinding.temp17A, getString(R.string.temp17A_1), getString(R.string.temp17A_2));
            } else if (tempName.equals(getString(R.string.tempName_18))) {

                example_2_Show(editBinding.temp18A, editBinding.temp18B,
                        getString(R.string.temp18A_1), getString(R.string.temp18B_1),
                        getString(R.string.temp18A_2), getString(R.string.temp18B_2));
            } else if (tempName.equals(getString(R.string.tempName_19))) {

                example_1_Show(editBinding.temp19A, getString(R.string.temp19A_1), getString(R.string.temp19A_2));
            } else if (tempName.equals(getString(R.string.tempName_20))) {

                example_2_Show(editBinding.temp20A, editBinding.temp20B,
                        getString(R.string.temp20A_1), getString(R.string.temp20B_1),
                        getString(R.string.temp20A_2), getString(R.string.temp20B_2));
            }

            //Without Method
//        else if (tempName.equals(getString(R.string.tempName_21))) {
//            editBinding.temp21A.setVisibility(View.VISIBLE);
//            editBinding.temp21B.setVisibility(View.VISIBLE);
//            if (which_text.equals("example1")) {
//
//                editBinding.temp21A.setText(getString(R.string.temp21A_1));
//                editBinding.temp21A.setVisibility(View.VISIBLE);
//                editBinding.temp21B.setVisibility(View.GONE);
//                which_text = "example2";
//                remove2ExampleBox();
//            } else if (which_text.equals("example2")) {
//                editBinding.temp21B.setText(getString(R.string.temp21A_2));
//                editBinding.temp21A.setVisibility(View.GONE);
//                which_text = "example1";
//                remove2ExampleBox();
//            }
//
//        }
            else if (tempName.equals(getString(R.string.tempName_22))) {

                example_3_Show(editBinding.temp22A, editBinding.temp22B, editBinding.temp22C,
                        getString(R.string.temp22A_1), getString(R.string.temp22B_1),
                        getString(R.string.temp22C_1), getString(R.string.temp22A_2),
                        getString(R.string.temp22B_2), getString(R.string.temp22C_2));
            } else if (tempName.equals(getString(R.string.tempName_23))) {

                example_1_Show(editBinding.temp23A, getString(R.string.temp23A_1), getString(R.string.temp23A_2));

            } else if (tempName.equals(getString(R.string.tempName_24))) {
                example_1_Show(editBinding.temp24A, getString(R.string.temp24A_1), getString(R.string.temp24A_2));
            } else if (tempName.equals(getString(R.string.tempName_25))) {

                example_3_Show(editBinding.temp25A, editBinding.temp25B, editBinding.temp25C,
                        getString(R.string.temp25A_1), getString(R.string.temp25B_1),
                        getString(R.string.temp25C_1), getString(R.string.temp25A_2),
                        getString(R.string.temp25B_2), getString(R.string.temp25C_2));
            } else if (tempName.equals(getString(R.string.tempName_26))) {

                example_2_Show(editBinding.temp26A, editBinding.temp26B,
                        getString(R.string.temp26A_1), getString(R.string.temp26B_1),
                        getString(R.string.temp26A_2), getString(R.string.temp26B_2));
            } else if (tempName.equals(getString(R.string.tempName_27))) {
                example_2_Show(editBinding.temp27A, editBinding.temp27B,
                        getString(R.string.temp27A_1), getString(R.string.temp27B_1),
                        getString(R.string.temp27A_2), getString(R.string.temp27B_2));
            }
            //Without Method
            else if (tempName.equals(getString(R.string.tempName_28))) {
                editBinding.temp28A.setVisibility(View.VISIBLE);
                editBinding.temp28B.setVisibility(View.VISIBLE);
                editBinding.temp28C.setVisibility(View.VISIBLE);
                editBinding.temp28D.setVisibility(View.VISIBLE);
                if (which_text.equals("example1")) {

                    editBinding.temp28A.setText(getString(R.string.temp28A_1));
                    editBinding.temp28A.setVisibility(View.VISIBLE);
                    editBinding.temp28B.setVisibility(View.GONE);
                    editBinding.temp28C.setVisibility(View.GONE);
                    editBinding.temp28D.setVisibility(View.GONE);

                    which_text = "example2";
                    remove3ExampleBox();
                } else if (which_text.equals("example2")) {
                    editBinding.temp28A.setText(getString(R.string.temp28A_2));
                    editBinding.temp28B.setText(getString(R.string.temp28B_2));
                    editBinding.temp28D.setText(getString(R.string.temp28D_2));
                    which_text = "example1";
                    remove3ExampleBox();
                }
            }


            // Islamic Meme 86 to 100
            else if (tempName.equals(getString(R.string.tempName_86))) {
                example_1_Show(editBinding.temp86A, getString(R.string.temp86A_1), getString(R.string.temp86B_1));

            }
            //87
            else if (tempName.equals(getString(R.string.tempName_87))) {
                example_1_Show(editBinding.temp87A, getString(R.string.temp87A_1), getString(R.string.temp87B_1));

            }
            //88
            else if (tempName.equals(getString(R.string.tempName_88))) {
                example_1_Show(editBinding.temp88A, getString(R.string.temp88A_1), getString(R.string.temp88B_1));

            }
            //89
            else if (tempName.equals(getString(R.string.tempName_89))) {
                example_1_Show(editBinding.temp89A, getString(R.string.temp89A_1), getString(R.string.temp89B_1));

            }
            //90
            else if (tempName.equals(getString(R.string.tempName_90))) {
                example_1_Show(editBinding.temp90A, getString(R.string.temp90A_1), getString(R.string.temp90B_1));

            }
            //91
            else if (tempName.equals(getString(R.string.tempName_91))) {
                example_1_Show(editBinding.temp91A, getString(R.string.temp91A_1), getString(R.string.temp91B_1));

            }
            //92
            else if (tempName.equals(getString(R.string.tempName_92))) {
                example_1_Show(editBinding.temp92A, getString(R.string.temp92A_1), getString(R.string.temp92B_1));

            }
            //93
            else if (tempName.equals(getString(R.string.tempName_93))) {
                example_1_Show(editBinding.temp93A, getString(R.string.temp93A_1), getString(R.string.temp93B_1));

            }
            //94
            else if (tempName.equals(getString(R.string.tempName_94))) {
                example_1_Show(editBinding.temp94A, getString(R.string.temp94A_1), getString(R.string.temp94B_1));

            }
            //95
            else if (tempName.equals(getString(R.string.tempName_95))) {
                example_1_Show(editBinding.temp95A, getString(R.string.temp95A_1), getString(R.string.temp95B_1));

            }
            //96
            else if (tempName.equals(getString(R.string.tempName_96))) {
                example_1_Show(editBinding.temp96A, getString(R.string.temp96A_1), getString(R.string.temp96B_1));

            }
            //97
            else if (tempName.equals(getString(R.string.tempName_97))) {
                example_1_Show(editBinding.temp97A, getString(R.string.temp97A_1), getString(R.string.temp97B_1));

            }
            //98
            else if (tempName.equals(getString(R.string.tempName_98))) {
                example_1_Show(editBinding.temp98A, getString(R.string.temp98A_1), getString(R.string.temp98A_1));

            }
            //99
            else if (tempName.equals(getString(R.string.tempName_99))) {
                example_1_Show(editBinding.temp99A, getString(R.string.temp99A_1), getString(R.string.temp99A_1));

            }
            //100
            else if (tempName.equals(getString(R.string.tempName_100))) {
                example_1_Show(editBinding.temp100A, getString(R.string.temp100A_1), getString(R.string.temp100A_1));

            }

            // Decent Meme 101 to 114
            else if (tempName.equals(getString(R.string.tempName_101))) {
                example_1_Show(editBinding.temp101A, getString(R.string.temp101A_1), getString(R.string.temp101A_2));

            }
            //102
            else if (tempName.equals(getString(R.string.tempName_102))) {

                example_2_Show(editBinding.temp102A, editBinding.temp102B,
                        getString(R.string.temp102A_1), getString(R.string.temp102B_1),
                        getString(R.string.temp102A_2), getString(R.string.temp102B_2));
            }
            //103
            else if (tempName.equals(getString(R.string.tempName_103))) {
                example_1_Show(editBinding.temp103A, getString(R.string.temp103A_1), getString(R.string.temp103A_2));

            }
            //104
            else if (tempName.equals(getString(R.string.tempName_104))) {
                example_1_Show(editBinding.temp104A, getString(R.string.temp104A_1), getString(R.string.temp104A_2));

            }
            //105
            else if (tempName.equals(getString(R.string.tempName_105))) {
                example_1_Show(editBinding.temp105A, getString(R.string.temp105A_1), getString(R.string.temp105A_2));

            }
            //106
            else if (tempName.equals(getString(R.string.tempName_106))) {
                example_1_Show(editBinding.temp106A, getString(R.string.temp106A_1), getString(R.string.temp106A_2));

            }
            //107
            else if (tempName.equals(getString(R.string.tempName_107))) {

                example_2_Show(editBinding.temp107A, editBinding.temp107B,
                        getString(R.string.temp107A_1), getString(R.string.temp107B_1),
                        getString(R.string.temp107A_2), getString(R.string.temp107B_2));
            }
            //108
            else if (tempName.equals(getString(R.string.tempName_108))) {

                example_2_Show(editBinding.temp108A, editBinding.temp108B,
                        getString(R.string.temp108A_1), getString(R.string.temp102B_1),
                        getString(R.string.temp108A_2), getString(R.string.temp108B_2));
            }
            //109
            else if (tempName.equals(getString(R.string.tempName_109))) {
                example_1_Show(editBinding.temp109A, getString(R.string.temp109A_1), getString(R.string.temp109A_2));

            }
            //110
            else if (tempName.equals(getString(R.string.tempName_110))) {
                example_1_Show(editBinding.temp110A, getString(R.string.temp110A_1), getString(R.string.temp110A_2));

            }
            //111
            else if (tempName.equals(getString(R.string.tempName_111))) {

                example_2_Show(editBinding.temp111A, editBinding.temp111B,
                        getString(R.string.temp111A_1), getString(R.string.temp111B_1),
                        getString(R.string.temp111A_2), getString(R.string.temp111B_2));
            }
            //112
            else if (tempName.equals(getString(R.string.tempName_112))) {

                example_2_Show(editBinding.temp112A, editBinding.temp112B,
                        getString(R.string.temp112A_1), getString(R.string.temp112B_1),
                        getString(R.string.temp112A_2), getString(R.string.temp112B_2));
            }
            //113
            else if (tempName.equals(getString(R.string.tempName_113))) {

                example_2_Show(editBinding.temp113A, editBinding.temp113B,
                        getString(R.string.temp113A_1), getString(R.string.temp113B_1),
                        getString(R.string.temp113A_2), getString(R.string.temp113B_2));
            }
            //114
            else if (tempName.equals(getString(R.string.tempName_114))) {
                example_1_Show(editBinding.temp114A, getString(R.string.temp114A_1), getString(R.string.temp114A_2));

            }


            // University Meme 115 to 129
            else if (tempName.equals(getString(R.string.tempName_115))) {
                example_1_Show(editBinding.temp115A, getString(R.string.temp115A_1), getString(R.string.temp115A_2));

            }
            //116
            else if (tempName.equals(getString(R.string.tempName_116))) {
                example_1_Show(editBinding.temp116A, getString(R.string.temp116A_1), getString(R.string.temp116A_2));

            }
            //117
            else if (tempName.equals(getString(R.string.tempName_117))) {
                example_1_Show(editBinding.temp117A, getString(R.string.temp117A_1), getString(R.string.temp117A_2));

            }
            //118
            else if (tempName.equals(getString(R.string.tempName_118))) {
                example_1_Show(editBinding.temp118A, getString(R.string.temp118A_1), getString(R.string.temp118A_2));

            }
            //119
            else if (tempName.equals(getString(R.string.tempName_119))) {
                example_1_Show(editBinding.temp119A, getString(R.string.temp119A_1), getString(R.string.temp119A_2));

            }
            //120
            else if (tempName.equals(getString(R.string.tempName_120))) {
                example_1_Show(editBinding.temp120A, getString(R.string.temp120A_1), getString(R.string.temp120A_2));

            }
            //121
            else if (tempName.equals(getString(R.string.tempName_121))) {
                example_1_Show(editBinding.temp121A, getString(R.string.temp121A_1), getString(R.string.temp121A_2));

            }
            //122
            else if (tempName.equals(getString(R.string.tempName_122))) {
                example_1_Show(editBinding.temp122A, getString(R.string.temp122A_1), getString(R.string.temp122A_2));

            }
            //123
            else if (tempName.equals(getString(R.string.tempName_123))) {
                example_1_Show(editBinding.temp123A, getString(R.string.temp123A_1), getString(R.string.temp123A_2));

            }
            //124
            else if (tempName.equals(getString(R.string.tempName_124))) {
                example_1_Show(editBinding.temp124A, getString(R.string.temp124A_1), getString(R.string.temp124A_2));

            }
            //125
            else if (tempName.equals(getString(R.string.tempName_125))) {
                example_1_Show(editBinding.temp125A, getString(R.string.temp125A_1), getString(R.string.temp125A_2));

            }
            //126
            else if (tempName.equals(getString(R.string.tempName_126))) {
                example_1_Show(editBinding.temp126A, getString(R.string.temp126A_1), getString(R.string.temp126A_2));

            }
            //127
            else if (tempName.equals(getString(R.string.tempName_127))) {
                example_1_Show(editBinding.temp127A, getString(R.string.temp127A_1), getString(R.string.temp127A_2));

            }
            //128
            else if (tempName.equals(getString(R.string.tempName_128))) {
                example_1_Show(editBinding.temp128A, getString(R.string.temp128A_1), getString(R.string.temp128A_2));

            }
            //129
            else if (tempName.equals(getString(R.string.tempName_129))) {
                example_1_Show(editBinding.temp129A, getString(R.string.temp129A_1), getString(R.string.temp129A_2));

            }
            //130
            else if (tempName.equals(getString(R.string.tempName_130))) {
                example_1_Show(editBinding.temp130A, getString(R.string.temp130A_1), getString(R.string.temp130A_2));
            }
            //131
            else if (tempName.equals(getString(R.string.tempName_131))) {
                example_1_Show(editBinding.temp131A, getString(R.string.temp131A_1), getString(R.string.temp131A_2));
            }
            //132
            else if (tempName.equals(getString(R.string.tempName_132))) {
                example_1_Show(editBinding.temp132A, getString(R.string.temp132A_1), getString(R.string.temp132A_2));
            }
            //133
            else if (tempName.equals(getString(R.string.tempName_133))) {
                example_1_Show(editBinding.temp133A, getString(R.string.temp133A_1), getString(R.string.temp133A_2));
            }
            //134
            else if (tempName.equals(getString(R.string.tempName_134))) {
                example_1_Show(editBinding.temp134A, getString(R.string.temp134A_1), getString(R.string.temp134A_2));
            }
            //135
            else if (tempName.equals(getString(R.string.tempName_135))) {
                example_1_Show(editBinding.temp135A, getString(R.string.temp135A_1), getString(R.string.temp135A_2));
            }
            //136
            else if (tempName.equals(getString(R.string.tempName_136))) {
                example_1_Show(editBinding.temp136A, getString(R.string.temp136A_1), getString(R.string.temp136A_2));
            }
            //137
            else if (tempName.equals(getString(R.string.tempName_137))) {
                example_1_Show(editBinding.temp137A, getString(R.string.temp137A_1), getString(R.string.temp137A_2));
            }
            //138
            else if (tempName.equals(getString(R.string.tempName_138))) {
                example_1_Show(editBinding.temp138A, getString(R.string.temp138A_1), getString(R.string.temp138A_2));
            }
            //139
            else if (tempName.equals(getString(R.string.tempName_139))) {
                example_1_Show(editBinding.temp139A, getString(R.string.temp139A_1), getString(R.string.temp139A_2));
            }
            //140
            else if (tempName.equals(getString(R.string.tempName_140))) {
                example_1_Show(editBinding.temp140A, getString(R.string.temp140A_1), getString(R.string.temp140A_2));
            }
            //141
            else if (tempName.equals(getString(R.string.tempName_141))) {
                example_1_Show(editBinding.temp141A, getString(R.string.temp141A_1), getString(R.string.temp141A_2));
            }
            //142
            else if (tempName.equals(getString(R.string.tempName_142))) {
                example_1_Show(editBinding.temp142A, getString(R.string.temp142A_1), getString(R.string.temp142A_2));
            }
            //143
            else if (tempName.equals(getString(R.string.tempName_143))) {
                example_1_Show(editBinding.temp143A, getString(R.string.temp143A_1), getString(R.string.temp143A_2));
            }
            //144
            else if (tempName.equals(getString(R.string.tempName_144))) {
                example_1_Show(editBinding.temp144A, getString(R.string.temp144A_1), getString(R.string.temp144A_2));
            }
//
//        //84 temp
//        else if (retrieveImage == R.drawable.temp84) {
//
//            example_1_Show(editBinding.temp84A, getString(R.string.temp84A_1), getString(R.string.temp84A_2));
//
//        }
            //        //85 temp
//        else if (retrieveImage == R.drawable.temp85) {
//
//            example_2_Show(editBinding.temp85A, editBinding.temp85B,
//                    getString(R.string.temp85A_1), getString(R.string.temp85B_1),
//                    getString(R.string.temp85A_2), getString(R.string.temp85B_2));
//        }

//        else if (retrieveImage == R.drawable.temp29) {
//
//            example_2_Show(editBinding.temp29A, editBinding.temp29B,
//                    getString(R.string.temp29A_1), getString(R.string.temp29B_1),
//                    getString(R.string.temp29A_2), getString(R.string.temp29B_2));
//        } else if (retrieveImage == R.drawable.temp30) {
//
//            example_4_Show(editBinding.temp30A, editBinding.temp30B, editBinding.temp30C, editBinding.temp30D,
//                    getString(R.string.temp30A_1), getString(R.string.temp30B_1),
//                    getString(R.string.temp30C_1), getString(R.string.temp30D_1),
//                    getString(R.string.temp30A_2), getString(R.string.temp30B_2),
//                    getString(R.string.temp30C_2), getString(R.string.temp30D_2));
//        }
//        //Without Method
//        else if (retrieveImage == R.drawable.temp31) {
//            editBinding.temp31A.setVisibility(View.VISIBLE);
//            editBinding.temp31B.setVisibility(View.VISIBLE);
//            editBinding.temp31C.setVisibility(View.VISIBLE);
//            if (which_text.equals("example1")) {
//
//                editBinding.temp31A.setText(getString(R.string.temp31A_1));
//                editBinding.temp31B.setText(getString(R.string.temp31B_1));
//                editBinding.temp31C.setText(getString(R.string.temp31C_1));
//                which_text = "example2";
//                remove3ExampleBox();
//            } else if (which_text.equals("example2")) {
//                editBinding.temp31A.setText(getString(R.string.temp31A_2));
//                editBinding.temp31C.setText(getString(R.string.temp31C_2));
//                editBinding.temp31B.setVisibility(View.GONE);
//                which_text = "example1";
//                remove3ExampleBox();
//            }
//
//        } else if (retrieveImage == R.drawable.temp32) {
//
//            example_2_Show(editBinding.temp32A, editBinding.temp32B,
//                    getString(R.string.temp32A_1), getString(R.string.temp32B_1),
//                    getString(R.string.temp32A_2), getString(R.string.temp32B_2));
//        } else if (retrieveImage == R.drawable.temp33) {
//
//            example_2_Show(editBinding.temp33A, editBinding.temp33B,
//                    getString(R.string.temp33A_1), getString(R.string.temp33B_1),
//                    getString(R.string.temp33A_2), getString(R.string.temp33B_2));
//        } else if (retrieveImage == R.drawable.temp34) {
//
//            example_2_Show(editBinding.temp34A, editBinding.temp34B,
//                    getString(R.string.temp34A_1), getString(R.string.temp34B_1),
//                    getString(R.string.temp34B_1), getString(R.string.temp34B_2));
//        } else if (retrieveImage == R.drawable.temp35) {
//
//            example_1_Show(editBinding.temp35A, getString(R.string.temp35A_1), getString(R.string.temp35A_2));
//
//        } else if (retrieveImage == R.drawable.temp36) {
//
//            example_1_Show(editBinding.temp36A, getString(R.string.temp36A_1), getString(R.string.temp36A_2));
//
//        } else if (retrieveImage == R.drawable.temp37) {
//
//            example_2_Show(editBinding.temp37A, editBinding.temp37B,
//                    getString(R.string.temp37A_1), getString(R.string.temp37B_1),
//                    getString(R.string.temp37A_2), getString(R.string.temp37B_2));
//        } else if (retrieveImage == R.drawable.temp38) {
//
//            example_2_Show(editBinding.temp38A, editBinding.temp38B,
//                    getString(R.string.temp38A_1), getString(R.string.temp38B_1),
//                    getString(R.string.temp38A_2), getString(R.string.temp38B_2));
//        } else if (retrieveImage == R.drawable.temp39) {
//
//            example_1_Show(editBinding.temp39A, getString(R.string.temp39A_1), getString(R.string.temp39A_2));
//
//        } else if (retrieveImage == R.drawable.temp40) {
//
//            example_1_Show(editBinding.temp40A, getString(R.string.temp40A_1), getString(R.string.temp40A_2));
//        } else if (retrieveImage == R.drawable.temp41) {
//
//            example_1_Show(editBinding.temp41A, getString(R.string.temp41A_1), getString(R.string.temp41A_2));
//        } else if (retrieveImage == R.drawable.temp42) {
//
//            example_2_Show(editBinding.temp42A, editBinding.temp42B,
//                    getString(R.string.temp42A_1), getString(R.string.temp42B_1),
//                    getString(R.string.temp42A_2), getString(R.string.temp42B_2));
//        } else if (retrieveImage == R.drawable.temp43) {
//            example_1_Show(editBinding.temp43A, getString(R.string.temp43A_1), getString(R.string.temp43A_2));
//
//        } else if (retrieveImage == R.drawable.temp44) {
//
//            example_2_Show(editBinding.temp44A, editBinding.temp44B,
//                    getString(R.string.temp44A_1), getString(R.string.temp44B_1),
//                    getString(R.string.temp44A_2), getString(R.string.temp44B_2));
//        } else if (retrieveImage == R.drawable.temp45) {
//
//            example_1_Show(editBinding.temp45A, getString(R.string.temp45A_1), getString(R.string.temp45A_2));
//
//        } else if (retrieveImage == R.drawable.temp46) {
//
//            example_2_Show(editBinding.temp46A, editBinding.temp46B,
//                    getString(R.string.temp46A_1), getString(R.string.temp46B_1),
//                    getString(R.string.temp46A_2), getString(R.string.temp46B_2));
//        } else if (retrieveImage == R.drawable.temp47) {
//
//            example_1_Show(editBinding.temp47A, getString(R.string.temp47A_1), getString(R.string.temp47A_2));
//
//        } else if (retrieveImage == R.drawable.temp48) {
//
//            example_3_Show(editBinding.temp48A, editBinding.temp48B, editBinding.temp48C,
//                    getString(R.string.temp48A_1), getString(R.string.temp48B_1),
//                    getString(R.string.temp48C_1), getString(R.string.temp48A_2),
//                    getString(R.string.temp48B_2), getString(R.string.temp48C_2));
//        } else if (retrieveImage == R.drawable.temp49) {
//
//            example_2_Show(editBinding.temp49A, editBinding.temp49B,
//                    getString(R.string.temp49A_1), getString(R.string.temp49B_1),
//                    getString(R.string.temp49A_2), getString(R.string.temp49B_2));
//        } else if (retrieveImage == R.drawable.temp50) {
//
//            example_2_Show(editBinding.temp50A, editBinding.temp50B,
//                    getString(R.string.temp50A_1), getString(R.string.temp50B_1),
//                    getString(R.string.temp50A_2), getString(R.string.temp50B_2));
//        } else if (retrieveImage == R.drawable.temp51) {
//
//            example_1_Show(editBinding.temp51A, getString(R.string.temp51A_1), getString(R.string.temp51A_2));
//
//        } else if (retrieveImage == R.drawable.temp52) {
//
//            example_1_Show(editBinding.temp52A, getString(R.string.temp52A_1), getString(R.string.temp52A_2));
//
//        } else if (retrieveImage == R.drawable.temp53) {
//
//            example_1_Show(editBinding.temp53A, getString(R.string.temp53A_1), getString(R.string.temp53A_2));
//
//        } else if (retrieveImage == R.drawable.temp54) {
//
//            example_2_Show(editBinding.temp54A, editBinding.temp54B,
//                    getString(R.string.temp54A_1), getString(R.string.temp54B_1),
//                    getString(R.string.temp54A_2), getString(R.string.temp54B_2));
//        } else if (retrieveImage == R.drawable.temp55) {
//
//            example_2_Show(editBinding.temp55A, editBinding.temp55B,
//                    getString(R.string.temp55A_1), getString(R.string.temp55B_1),
//                    getString(R.string.temp55A_2), getString(R.string.temp55B_2));
//        } else if (retrieveImage == R.drawable.temp56) {
//
//            example_1_Show(editBinding.temp56A, getString(R.string.temp56A_1), getString(R.string.temp56A_2));
//
//        } else if (retrieveImage == R.drawable.temp57) {
//
//            example_2_Show(editBinding.temp57A, editBinding.temp57B,
//                    getString(R.string.temp57A_1), getString(R.string.temp57B_1),
//                    getString(R.string.temp57A_2), getString(R.string.temp57B_2));
//        } else if (retrieveImage == R.drawable.temp58) {
//
//            example_1_Show(editBinding.temp58A, getString(R.string.temp58A_1), getString(R.string.temp58A_2));
//
//        } else if (retrieveImage == R.drawable.temp59) {
//
//            example_2_Show(editBinding.temp59A, editBinding.temp59B,
//                    getString(R.string.temp59A_1), getString(R.string.temp59B_1),
//                    getString(R.string.temp59A_2), getString(R.string.temp59A_2));
//        } else if (retrieveImage == R.drawable.temp60) {
//
//            example_1_Show(editBinding.temp60A, getString(R.string.temp60A_1), getString(R.string.temp60A_2));
//
//        } else if (retrieveImage == R.drawable.temp61) {
//
//            example_2_Show(editBinding.temp61A, editBinding.temp61B,
//                    getString(R.string.temp61A_1), getString(R.string.temp61B_1),
//                    getString(R.string.temp61A_2), getString(R.string.temp61B_2));
//        } else if (retrieveImage == R.drawable.temp62) {
//
//            example_2_Show(editBinding.temp62A, editBinding.temp62B,
//                    getString(R.string.temp62A_1), getString(R.string.temp62B_1),
//                    getString(R.string.temp62A_2), getString(R.string.temp62B_2));
//        }
//        //Without Method
//        else if (retrieveImage == R.drawable.temp63) {
//            editBinding.temp63A.setVisibility(View.VISIBLE);
//            editBinding.temp63B.setVisibility(View.VISIBLE);
//
//            if (which_text.equals("example1")) {
//                editBinding.temp63A.setText(getString(R.string.temp63A_1));
//                editBinding.temp63B.setText(getString(R.string.temp63B_1));
//                which_text = "example2";
//                remove3ExampleBox();
//            } else if (which_text.equals("example2")) {
//                editBinding.temp63A.setText(getString(R.string.temp63A_2));
//                editBinding.temp63B.setVisibility(View.GONE);
//                which_text = "example1";
//                remove3ExampleBox();
//            }
//
//        } else if (retrieveImage == R.drawable.temp64) {
//
//            example_1_Show(editBinding.temp64A, getString(R.string.temp64A_1), getString(R.string.temp64A_2));
//
//        } else if (retrieveImage == R.drawable.temp65) {
//
//            example_1_Show(editBinding.temp65A, getString(R.string.temp65A_1), getString(R.string.temp65A_2));
//
//        } else if (retrieveImage == R.drawable.temp66) {
//            example_1_Show(editBinding.temp66A, getString(R.string.temp66A_1), getString(R.string.temp66A_2));
//
//        } else if (retrieveImage == R.drawable.temp67) {
//
//            example_2_Show(editBinding.temp67A, editBinding.temp67B,
//                    getString(R.string.temp67A_1), getString(R.string.temp67B_1),
//                    getString(R.string.temp67A_2), getString(R.string.temp67B_2));
//        } else if (retrieveImage == R.drawable.temp68) {
//
//            example_1_Show(editBinding.temp68A, getString(R.string.temp68A_1), getString(R.string.temp68A_2));
//        } else if (retrieveImage == R.drawable.temp69) {
//
//            example_2_Show(editBinding.temp69A, editBinding.temp69B,
//                    getString(R.string.temp69A_1), getString(R.string.temp69B_1),
//                    getString(R.string.temp69A_2), getString(R.string.temp69B_2));
//        }
//        else if (retrieveImage == R.drawable.temp70) {
//
//            example_2_Show(editBinding.temp70A, editBinding.temp70B,
//                    getString(R.string.temp70A_1), getString(R.string.temp70B_1),
//                    getString(R.string.temp70A_2), getString(R.string.temp70B_2));
//        }
        }

    }


    private ClipArt_Example fun(TextView v) {
        x = v.getX();
        y = v.getY();
        return new ClipArt_Example(this, x, y);
    }


    //This method call in this Method exampleEditingFun
    private void exampleClickListener(String ex, TextView temp, ClipArt_Example cl) {
        editBinding.framelayout.addView(cl);
        cl.setText(ex);
        temp.setText("");
        ClipArt_Example.setSelectedText(cl);
        visibleAndInvisibleView();
        storeExValue = "ExampleText";
        editBinding.edittext.setText(ClipArt_Example.getSelectedText().getTextView().getText().toString());

        cl.setOnClickListener(view1 ->
        {
            ClipArt_Example.setSelectedText(cl);
            storeExValue = "ExampleText";
            editBinding.edittext.setText(ClipArt_Example.getSelectedText().getTextView().getText().toString());
            visibleAndInvisibleView();
            disableall();
            editBinding.fontSettingCardView.setVisibility(View.INVISIBLE);
        });


    }


    private void example_1_Show(TextView temp1, String ex1A, String ex2A) {
        temp1.setVisibility(View.VISIBLE);
        if (which_text.equals("example1")) {
            temp1.setText(ex1A);
            which_text = "example2";
            //Example Logic
            remove3ExampleBox();
        } else if (which_text.equals("example2")) {
            temp1.setText(ex2A);
            which_text = "example1";
            //Example Logic
            remove3ExampleBox();
        }
    }

    private void example_2_Show(TextView temp1, TextView temp2, String ex1A, String ex1B, String ex2A, String ex2B) {
        temp1.setVisibility(View.VISIBLE);
        temp2.setVisibility(View.VISIBLE);
        if (which_text.equals("example1")) {
            temp1.setText(ex1A);
            temp2.setText(ex1B);
            which_text = "example2";
            //Example Logic
            remove3ExampleBox();


        } else if (which_text.equals("example2")) {
            temp1.setText(ex2A);
            temp2.setText(ex2B);
            which_text = "example1";
            //Example Logic
            remove3ExampleBox();

        }


    }

    private void example_3_Show(TextView temp1, TextView temp2, TextView temp3,
                                String ex1A, String ex1B, String ex1C, String ex2A, String ex2B, String ex2C) {
        temp1.setVisibility(View.VISIBLE);
        temp2.setVisibility(View.VISIBLE);
        temp3.setVisibility(View.VISIBLE);


        if (which_text.equals("example1")) {
            temp1.setText(ex1A);
            temp2.setText(ex1B);
            temp3.setText(ex1C);
            which_text = "example2";
            remove3ExampleBox();

        } else if (which_text.equals("example2")) {
            temp1.setText(ex2A);
            temp2.setText(ex2B);
            temp3.setText(ex2C);
            which_text = "example1";
            remove3ExampleBox();
        }

    }

    private void example_4_Show(TextView temp1, TextView temp2, TextView temp3, TextView temp4,
                                String ex1A, String ex1B, String ex1C, String ex1D, String ex2A, String ex2B, String ex2C, String ex2D) {
        temp1.setVisibility(View.VISIBLE);
        temp2.setVisibility(View.VISIBLE);
        temp3.setVisibility(View.VISIBLE);
        temp4.setVisibility(View.VISIBLE);

        if (which_text.equals("example1")) {
            temp1.setText(ex1A);
            temp2.setText(ex1B);
            temp3.setText(ex1C);
            temp4.setText(ex1D);
            which_text = "example2";
            remove3ExampleBox();

        } else if (which_text.equals("example2")) {
            temp1.setText(ex2A);
            temp2.setText(ex2B);
            temp3.setText(ex2C);
            temp4.setText(ex2D);

            which_text = "example1";
            remove3ExampleBox();
        }
    }

    private void example_5_Show(TextView temp1, TextView temp2, TextView temp3, TextView temp4, TextView temp5,
                                String ex1A, String ex1B, String ex1C, String ex1D, String ex1E, String ex2B, String ex2C) {
        temp1.setVisibility(View.VISIBLE);
        temp2.setVisibility(View.VISIBLE);
        temp3.setVisibility(View.VISIBLE);
        temp4.setVisibility(View.VISIBLE);
        temp5.setVisibility(View.VISIBLE);
        if (which_text.equals("example1")) {
            temp1.setVisibility(View.VISIBLE);
            temp4.setVisibility(View.VISIBLE);
            temp5.setVisibility(View.VISIBLE);

            temp1.setText(ex1A);
            temp2.setText(ex1B);
            temp3.setText(ex1C);
            temp4.setText(ex1D);
            temp5.setText(ex1E);
            which_text = "example2";
            //Example Logic
            remove3ExampleBox();
        } else if (which_text.equals("example2")) {
            temp1.setVisibility(View.INVISIBLE);
            temp2.setText(ex2B);
            temp3.setText(ex2C);
            temp4.setVisibility(View.INVISIBLE);
            temp5.setVisibility(View.INVISIBLE);
            which_text = "example1";
            remove3ExampleBox();

        }
    }


    private void runTimePermission() {
        ActivityCompat.requestPermissions(EditActivity.this, permission, permissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionCode) {
            if (grantResults.length > 0) {
                boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean CAMERA = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE && CAMERA) {
                    permissionCheck=true;
                } else {
//                    Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    public void adapterInit(Context context, MenuAdapter adapter, RecyclerView recyclerView, ArrayList<MenuItem> list) {
        adapter = new MenuAdapter(list, context, this);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.HORIZONTAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void adapterInitForSticker(Context context, StickerAdapter adapter, RecyclerView recyclerView, ArrayList<StickerItem> list) {
        adapter = new StickerAdapter(context, list, this);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.setAdapter(adapter);
    }

    public void adapterInitForShape(Context context, StickerAdapter adapter, RecyclerView recyclerView, ArrayList<StickerItem> list) {
        adapter = new StickerAdapter(context, list, this);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.setAdapter(adapter);

    }

    //Main Menu List Undo,Example,Save,Share
    public ArrayList<MenuItem> populateData() {
        ArrayList<MenuItem> dataModelList = new ArrayList<>();
        dataModelList.add(new MenuItem("Undo", R.drawable.undo));
        dataModelList.add(new MenuItem("Example", R.drawable.example));
        dataModelList.add(new MenuItem("Shape", R.drawable.shape_fun));
        dataModelList.add(new MenuItem("Save", R.drawable.save__));
        dataModelList.add(new MenuItem("Share", R.drawable.share_ss));
        return dataModelList;
    }

    //Sticker Ad List
    public ArrayList<StickerItem> stickerItems() {
        ArrayList<StickerItem> dataModelList = new ArrayList<>();
        dataModelList.add(new StickerItem("a", R.drawable.a));
        dataModelList.add(new StickerItem("b", R.drawable.b));
        dataModelList.add(new StickerItem("c", R.drawable.c));
        dataModelList.add(new StickerItem("d", R.drawable.d));
        dataModelList.add(new StickerItem("e", R.drawable.e));
        dataModelList.add(new StickerItem("f", R.drawable.f));
        dataModelList.add(new StickerItem("g", R.drawable.g));
        dataModelList.add(new StickerItem("h", R.drawable.h));
        dataModelList.add(new StickerItem("i", R.drawable.i));
        dataModelList.add(new StickerItem("j", R.drawable.j));
        dataModelList.add(new StickerItem("k", R.drawable.k));
        dataModelList.add(new StickerItem("l", R.drawable.l));
        dataModelList.add(new StickerItem("m", R.drawable.m));
        dataModelList.add(new StickerItem("n", R.drawable.n));
        dataModelList.add(new StickerItem("o", R.drawable.o));
        dataModelList.add(new StickerItem("p", R.drawable.p));
        return dataModelList;
    }

    //Shape Ad List
    public ArrayList<StickerItem> shapeItems() {
        ArrayList<StickerItem> dataModelList = new ArrayList<>();
        dataModelList.add(new StickerItem("a", R.drawable.s_1));
        dataModelList.add(new StickerItem("b", R.drawable.s_2));
        dataModelList.add(new StickerItem("c", R.drawable.s_3));
        dataModelList.add(new StickerItem("d", R.drawable.s_4));
        dataModelList.add(new StickerItem("e", R.drawable.s_7));
        dataModelList.add(new StickerItem("f", R.drawable.s_11));
        dataModelList.add(new StickerItem("g", R.drawable.s_14));
        dataModelList.add(new StickerItem("h", R.drawable.s_5));
        dataModelList.add(new StickerItem("i", R.drawable.s_20));
        dataModelList.add(new StickerItem("j", R.drawable.s_21));
        dataModelList.add(new StickerItem("k", R.drawable.s_22));
        dataModelList.add(new StickerItem("l", R.drawable.s_23));
        dataModelList.add(new StickerItem("m", R.drawable.s_24));
        dataModelList.add(new StickerItem("n", R.drawable.s_9));
        dataModelList.add(new StickerItem("o", R.drawable.s_17));
        dataModelList.add(new StickerItem("p", R.drawable.s_15));
        return dataModelList;
    }

    //BackPress
    @Override
    public void onBackPressed() {
        LayoutInflater factory = LayoutInflater.from(this);
        final AlertDialog exitDialog = new AlertDialog.Builder(this).create();
        final View deleteDialogView = factory.inflate(R.layout.exit_editing, null);
        exitDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.exitApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog.dismiss();
                finish();

            }
        });
        deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

        exitDialog.show();
    }


    //Main Menu Item Click Undo Share Save
    @Override
    public void itemPos(MenuItem model, int newName) {
        String s = model.getTitle();
        switch (s) {
            case "Undo":
                if (packageCheck) {
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String key = dataSnapshot.getKey();
                                    String status, adId;
                                    if (key!=null&&key.equals(EDIT_ACT)) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            String key2 = dataSnapshot1.getKey();
                                            if (key2 != null && key2.equals(INTERSTITIAL)) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    AdModel model = dataSnapshot2.getValue(AdModel.class);

                                                    if (dataSnapshot2.getKey().equals(UNDO)) {
                                                        if (model != null) {
                                                            status = model.getStatus();
                                                            adId = model.getAdId();
                                                            if (status.equals("true")) {
                                                                if (BuildConfig.DEBUG) {
                                                                    adId = AdId.AD_MOB_INTERSTITIAL_TEST;
                                                                } else {
                                                                    adId = model.getAdId();
                                                                }

                                                                if (categoriesDisplayAct > 0) {
                                                                    progressDialog.dismiss();
                                                                    if (categoriesDisplayAct == 3) {
                                                                        if (interstitialBuilder != null) {
                                                                            interstitialBuilder.show(EditActivity.this);

                                                                        }
                                                                        interstitialBuilder = InterstitialBuilder.create().setAdId(com.appbrain.AdId.EXIT)
                                                                                .setFinishOnExit(EditActivity.this).preload(EditActivity.this);
                                                                    }
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        editBinding.framelayout.removeView(list.remove(i));
                                                                        Log.i("rafaqat", "itemPos: " + load);
                                                                    }
                                                                    hideView();
                                                                    categoriesDisplayAct--;
                                                                } else {
                                                                    progressDialog.show();
                                                                    load_IA(EditActivity.this, adId, "Undo");
                                                                }
                                                            } else {
                                                                progressDialog.dismiss();
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    editBinding.framelayout.removeView(list.remove(i));
                                                                    Log.i("rafaqat", "itemPos: " + load);
                                                                }
                                                                hideView();
                                                            }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    progressDialog.dismiss();
                    for (int i = 0; i < list.size(); i++) {
                        editBinding.framelayout.removeView(list.remove(i));
                        Log.i("rafaqat", "itemPos: " + load);
                    }
                    hideView();
                }


                break;
            case "Example":

                if (packageCheck) {
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String key = dataSnapshot.getKey();
                                    String status, adId;
                                    if (key.equals(EDIT_ACT)) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            String key2 = dataSnapshot1.getKey();
                                            if (key2.equals(INTERSTITIAL)) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    AdModel model = dataSnapshot2.getValue(AdModel.class);

                                                    if (dataSnapshot2.getKey().equals(EXAMPLE)) {
                                                        if (model != null && key2 != null) {
                                                            status = model.getStatus();
                                                            adId = model.getAdId();
                                                            if (status.equals("true")) {
                                                                if (BuildConfig.DEBUG)
                                                                {
                                                                    adId=AdId.AD_MOB_INTERSTITIAL_TEST;
                                                                }
                                                                else
                                                                {
                                                                    adId=model.getAdId();
                                                                }

                                                                if (categoriesDisplayAct > 0) {
                                                                    progressDialog.dismiss();
                                                                    if (categoriesDisplayAct==3){
                                                                        if (interstitialBuilder!=null) {
                                                                            interstitialBuilder.show(EditActivity.this);
                                                                        }
                                                                        interstitialBuilder = InterstitialBuilder.create().setAdId(com.appbrain.AdId.EXIT)
                                                                                .setFinishOnExit(EditActivity.this).preload(EditActivity.this);
                                                                    }
                                                                    exampleTextFun();

                                                                    categoriesDisplayAct--;
                                                                } else {
                                                                    progressDialog.show();
                                                                    load_IA(EditActivity.this, adId, "Example");
                                                                }
                                                            } else {
                                                                progressDialog.dismiss();
                                                                exampleTextFun();

                                                            }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    exampleTextFun();

                }


                break;
            case "Save":

                if (packageCheck) {
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String key = dataSnapshot.getKey();
                                    String status, adId;
                                    if (key.equals(EDIT_ACT)) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            String key2 = dataSnapshot1.getKey();
                                            if (key2.equals(INTERSTITIAL)) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    AdModel model = dataSnapshot2.getValue(AdModel.class);

                                                    if (dataSnapshot2.getKey().equals(SAVE)) {
                                                        if (model != null && key2 != null) {
                                                            status = model.getStatus();
                                                            adId = model.getAdId();
                                                            if (status.equals("true")) {
                                                                if (BuildConfig.DEBUG)
                                                                {
                                                                    adId=AdId.AD_MOB_INTERSTITIAL_TEST;
                                                                }
                                                                else
                                                                {
                                                                    adId=model.getAdId();
                                                                }
                                                                if (categoriesDisplayAct > 0) {
                                                                    progressDialog.dismiss();
                                                                    if (categoriesDisplayAct==3){
                                                                        if (interstitialBuilder!=null) {
                                                                            interstitialBuilder.show(EditActivity.this);
                                                                        }
                                                                        interstitialBuilder = InterstitialBuilder.create().setAdId(com.appbrain.AdId.EXIT)
                                                                                .setFinishOnExit(EditActivity.this).preload(EditActivity.this);
                                                                    }
                                                                    disableall();
                                                                    saveImage();
                                                                    categoriesDisplayAct--;
                                                                } else {
                                                                    progressDialog.show();
                                                                    load_IA(EditActivity.this, adId, "save");
                                                                }
                                                            } else {
                                                                progressDialog.dismiss();
                                                                disableall();
                                                                saveImage();

                                                            }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    disableall();
                    saveImage();
                }

                break;

            case "Shape":

                if (packageCheck) {
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String key = dataSnapshot.getKey();
                                    String status, adId;
                                    if (key != null && key.equals(EDIT_ACT)) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            String key2 = dataSnapshot1.getKey();
                                            if (key2 != null && key2.equals(INTERSTITIAL)) {
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    AdModel model = dataSnapshot2.getValue(AdModel.class);

                                                    if (dataSnapshot2.getKey().equals(SHAPE)) {
                                                        if (model != null && key2 != null) {
                                                            status = model.getStatus();
                                                            adId = model.getAdId();
                                                            if (status.equals("true")) {
                                                                if (BuildConfig.DEBUG) {
                                                                    adId = AdId.AD_MOB_INTERSTITIAL_TEST;
                                                                } else {
                                                                    adId = model.getAdId();
                                                                }
                                                                if (categoriesDisplayAct > 0) {
                                                                    progressDialog.dismiss();
                                                                    if (categoriesDisplayAct == 3) {
                                                                        if (interstitialBuilder != null) {
                                                                            interstitialBuilder.show(EditActivity.this);

                                                                        }
                                                                        interstitialBuilder = InterstitialBuilder.create().setAdId(com.appbrain.AdId.EXIT)
                                                                                .setFinishOnExit(EditActivity.this).preload(EditActivity.this);
                                                                    }
                                                                    shapeShow();
                                                                    categoriesDisplayAct--;
                                                                } else {
                                                                    progressDialog.show();
                                                                    load_IA(EditActivity.this, adId, "shape");
                                                                }
                                                            } else {
                                                                progressDialog.dismiss();
                                                                shapeShow();

                                                            }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    shapeShow();
                }
                break;
            case "Share":
                editBinding.pbr.setVisibility(View.VISIBLE);
                editBinding.framelayout.setDrawingCacheEnabled(true);
                editBinding.framelayout.buildDrawingCache();
                Bitmap bitmap = editBinding.framelayout.getDrawingCache();
                OutputStream fileOutputStream;
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        File file = new File(Environment.DIRECTORY_DCIM + File.separator + "Meme Maker");
                        ContentResolver contentResolver = getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, file.toString());
                        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        fileOutputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        Objects.requireNonNull(fileOutputStream);
                        editBinding.pbr.setVisibility(View.INVISIBLE);

                        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                        final Intent sendImage = new Intent(Intent.ACTION_SEND);
                        sendImage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendImage.putExtra(Intent.EXTRA_STREAM, Uri.parse(pathofBmp));
                        sendImage.setType("image/*");
                        startActivity(sendImage);

                    } else {
                        String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Meme Maker";
                        File file = new File(imagesDir);
                        if (!file.exists()) {
                            file.mkdir();

                        }
                        File image = new File(file + "/" + System.currentTimeMillis() + ".jpg");
                        fileOutputStream = new FileOutputStream(image);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        editBinding.pbr.setVisibility(View.INVISIBLE);

                        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                        new ShareCompat.IntentBuilder(this).
                                setStream(Uri.parse(pathofBmp)).
                                setType("image/*").
                                setChooserTitle("Share Image").
                                startChooser();
                    }

                } catch (Exception e) {
                    Toast.makeText(EditActivity.this, "Not Saved" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                break;
        }
    }

    //Sticker Item Click Interface
    @Override
    public void itemPos(StickerItem model) {
        String s = model.getTitle();
        switch (s) {
            case "a":
            case "b":
            case "c":
            case "d":
            case "e":
            case "f":
            case "g":
            case "h":
            case "i":
            case "j":
            case "k":
            case "l":
            case "m":
            case "n":
            case "o":
            case "p":
                editBinding.stickerRecycleView.setVisibility(View.GONE);
                editBinding.cancelStckerRecycelView.setVisibility(View.GONE);
                addSticker(model.getIcon());
                break;

        }
    }


    //Save Functionality
    private void saveImage() {
        LayoutInflater factory = LayoutInflater.from(this);
        final AlertDialog saveDialog = new AlertDialog.Builder(this).create();
        final View deleteDialogView = factory.inflate(R.layout.save_dialog, null);
        saveDialog.setView(deleteDialogView);
        EditText name = deleteDialogView.findViewById(R.id.saveMemeName);

        editBinding.framelayout.setDrawingCacheEnabled(true);
        editBinding.framelayout.buildDrawingCache();
        Bitmap bitmap = editBinding.framelayout.getDrawingCache();
        deleteDialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                editBinding.pbr.setVisibility(View.VISIBLE);
                String s_name = name.getText().toString();
                OutputStream fileOutputStream;
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        File file = new File(Environment.DIRECTORY_DCIM + File.separator + "Meme Maker");
                        ContentResolver contentResolver = getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, s_name + ".jpg");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, file.toString());
                        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        fileOutputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        Objects.requireNonNull(fileOutputStream);
                        Toast.makeText(EditActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        editBinding.pbr.setVisibility(View.INVISIBLE);


                    } else {
                        String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Meme Maker";
                        File file = new File(imagesDir);
                        if (!file.exists()) {
                            file.mkdir();

                        }
                        File image = new File(file + "/" + s_name + ".jpg");
                        fileOutputStream = new FileOutputStream(image);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        Toast.makeText(EditActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        editBinding.pbr.setVisibility(View.INVISIBLE);
                        disableall();
                    }

                } catch (Exception e) {
                    Toast.makeText(EditActivity.this, "Not Saved" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
                saveDialog.dismiss();
                Intent intent = new Intent(EditActivity.this, SavedImages.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
        deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDialog.dismiss();
            }
        });

        saveDialog.show();
    }

    //Sticker RecycleView Show Functionality
    public void stickerShow() {
        shapeAndStickerValueChecker = "sticker";
        Animation slideup = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        editBinding.stickerRecycleView.setVisibility(View.VISIBLE);
        editBinding.cancelStckerRecycelView.setVisibility(View.VISIBLE);
        editBinding.fontSettingCardView.setVisibility(View.INVISIBLE);

        editBinding.stickerRecycleView.setAnimation(slideup);
        editBinding.cancelStckerRecycelView.setAnimation(slideup);

        editBinding.cancelStckerRecycelView.setOnClickListener(view -> {
            editBinding.stickerRecycleView.setVisibility(View.GONE);
            editBinding.cancelStckerRecycelView.setVisibility(View.GONE);
        });

        editBinding.shapeRecycleView.setVisibility(View.GONE);
        editBinding.cancelShapeRecycelView.setVisibility(View.GONE);
    }

    //Shape RecycleView Show Functionality
    public void shapeShow() {
        shapeAndStickerValueChecker = "shape";
        Animation slideup = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        editBinding.shapeRecycleView.setVisibility(View.VISIBLE);
        editBinding.cancelShapeRecycelView.setVisibility(View.VISIBLE);

        editBinding.fontSettingCardView.setVisibility(View.INVISIBLE);
        editBinding.shapeRecycleView.setAnimation(slideup);
        editBinding.cancelShapeRecycelView.setAnimation(slideup);

        editBinding.cancelShapeRecycelView.setOnClickListener(view -> {
            editBinding.shapeRecycleView.setVisibility(View.GONE);
            editBinding.cancelShapeRecycelView.setVisibility(View.GONE);
            editBinding.shapeColorPicker.setVisibility(View.GONE);
            editBinding.sendToFront.setVisibility(View.GONE);
        });

        editBinding.stickerRecycleView.setVisibility(View.GONE);
        editBinding.cancelStckerRecycelView.setVisibility(View.GONE);
    }


    //Set Text Font
    private void setTextFont(TextView textView, int font) {
        Typeface typeface = ResourcesCompat.getFont(this, font);
        textView.setTypeface(typeface);
    }


    //Set Text Size
    public void setTextSize(TextView textView, int size) {
        textView.setTextSize((float) size);
    }


    private void fontsettings_popup() {
        //Font Spinner
        spinnerFontShowFun();
        editBinding.stickerRecycleView.setVisibility(View.GONE);
        editBinding.cancelStckerRecycelView.setVisibility(View.GONE);
        editBinding.edittext.setEnabled(false);
        editBinding.edittext.setEnabled(true);
        Animation slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        editBinding.fontSettingCardView.setVisibility(View.VISIBLE);
        editBinding.fontSettingCardView.setAnimation(slideup);

    }

    //Ad Sticker Method
    private void addSticker(int i) {
        if (shapeAndStickerValueChecker.equals("shape")) {
            shapeAndStickerValueChecker = "shape";
//            editBinding.shapeColorPicker.setVisibility(View.VISIBLE);
//            editBinding.sendToFront.setVisibility(View.VISIBLE);
            editBinding.mid.setVisibility(View.INVISIBLE);
            ClipArt_Image ca = new ClipArt_Image(this);
            editBinding.framelayout.addView(ca);

            ca.setId(count++);
            list.add(ca);
            int ii = ClipArt_Image.setImageView(i);
            ImageView img2 = ClipArt_Image.getImageView(ii);

            ca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shapeAndStickerValueChecker = "shape";
                    disableall();
                    editBinding.shapeRecycleView.setVisibility(View.VISIBLE);
                    editBinding.cancelShapeRecycelView.setVisibility(View.VISIBLE);
                    editBinding.shapeColorPicker.setVisibility(View.VISIBLE);
                    editBinding.sendToFront.setVisibility(View.VISIBLE);
                    editBinding.mid.setVisibility(View.INVISIBLE);

                    defaultColor = ContextCompat.getColor(EditActivity.this, R.color.orange);
                    editBinding.shapeColorPicker.setOnClickListener(view2 -> {
                        AmbilWarnaDialog dialog = new AmbilWarnaDialog(EditActivity.this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                            @Override
                            public void onCancel(AmbilWarnaDialog dialog) {

                            }

                            @Override
                            public void onOk(AmbilWarnaDialog dialog, int color) {
                                defaultColor = color;
                                DrawableCompat.setTint(img2.getDrawable(), defaultColor);

                            }
                        });
                        dialog.show();
                    });


                }
            });

        } else if (shapeAndStickerValueChecker.equals("sticker")) {
            shapeAndStickerValueChecker = "sticker";
            editBinding.mid.setVisibility(View.INVISIBLE);
            editBinding.sendToFront.setVisibility(View.INVISIBLE);
            ClipArt_Image ca = new ClipArt_Image(this);
            editBinding.framelayout.addView(ca);

            ca.setId(count++);
            list.add(ca);
            int ii = ClipArt_Image.setImageView(i);
            ImageView img2 = ClipArt_Image.getImageView(ii);

            ca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shapeAndStickerValueChecker = "sticker";
                    disableall();
                    editBinding.mid.setVisibility(View.INVISIBLE);
                    editBinding.shapeColorPicker.setVisibility(View.INVISIBLE);
                    editBinding.shapeRecycleView.setVisibility(View.INVISIBLE);
                    editBinding.cancelShapeRecycelView.setVisibility(View.INVISIBLE);
                    editBinding.sendToFront.setVisibility(View.INVISIBLE);
                    editBinding.stickerRecycleView.setVisibility(View.VISIBLE);
                    editBinding.cancelStckerRecycelView.setVisibility(View.VISIBLE);

                }
            });
        }

    }

    private void spinnerFontShowFun() {

        String[] fonts = getResources().getStringArray(R.array.fonts);
        ArrayAdapter spinneradapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fonts);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editBinding.fontspinner.setAdapter(spinneradapter);
//        adapter = new Spinner_Adapter(this, font_list);
//        editBinding.fontspinner.setAdapter(adapter);

        if (storeExValue.equals("SimpleText")) {
            fonts[0] = "Selected Font Name" + "-" + ClipArt.getSelectedText().getTextView().getTag().toString();
        } else {
            fonts[0] = "Selected Font Name" + "-" + ClipArt_Example.getSelectedText().getTextView().getTag().toString();

        }
        editBinding.fontspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String name = font_list.get(i).getName();
                spinnerSelectedItemName = spinneradapter.getItem(i).toString();
                ((TextView) view).setTextColor(getColor(R.color.white));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


    }

    //Change Text Size
    private void changeTextSize() {
        editBinding.seekfontsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChangedValue = i;
                if (storeExValue.equals("SimpleText")) {
                    setTextSize(ClipArt.getSelectedText().getTextView(), i);
                } else if (storeExValue.equals("ExampleText")) {
                    setTextSize(ClipArt_Example.getSelectedText().getTextView(), i);

                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //  Toast(context, progressChangedValue + "");
            }
        });
    }

    //Text Style
    private void textStyleFun() {

        editBinding.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.normalstyle:
                        if (storeExValue.equals("SimpleText")) {
                            ClipArt.getSelectedText().getTextView().setTypeface(null, Typeface.NORMAL);
                        } else if (storeExValue.equals("ExampleText")) {
                            ClipArt_Example.getSelectedText().getTextView().setTypeface(null, Typeface.NORMAL);
                        }

                        break;
                    case R.id.boldstyle:
                        if (storeExValue.equals("SimpleText")) {
                            ClipArt.getSelectedText().getTextView().setTypeface(null, Typeface.BOLD);

                        } else if (storeExValue.equals("ExampleText")) {
                            ClipArt_Example.getSelectedText().getTextView().setTypeface(null, Typeface.BOLD);

                        }

                        break;
                    case R.id.italicstyle:
                        if (storeExValue.equals("SimpleText")) {
                            ClipArt.getSelectedText().getTextView().setTypeface(null, Typeface.ITALIC);

                        } else if (storeExValue.equals("ExampleText")) {
                            ClipArt_Example.getSelectedText().getTextView().setTypeface(null, Typeface.ITALIC);
                        }

                        break;


                }
            }
        });

    }


    //Hide All View
    private void hideView() {
        editBinding.temp1A.setVisibility(View.GONE);
        editBinding.temp1B.setVisibility(View.GONE);
        //Temp 71
        editBinding.temp71A.setVisibility(View.GONE);
        editBinding.temp71B.setVisibility(View.GONE);

        editBinding.temp2A.setVisibility(View.GONE);
        //Temp 72
        editBinding.temp72A.setVisibility(View.GONE);
        editBinding.temp72B.setVisibility(View.GONE);

        editBinding.temp3A.setVisibility(View.GONE);
        //Temp 73
        editBinding.temp73A.setVisibility(View.GONE);

        editBinding.temp4A.setVisibility(View.GONE);
        editBinding.temp4B.setVisibility(View.GONE);

        //Temp 74
        editBinding.temp74A.setVisibility(View.GONE);
        editBinding.temp74B.setVisibility(View.GONE);
        editBinding.temp74C.setVisibility(View.GONE);
        editBinding.temp74D.setVisibility(View.GONE);
        editBinding.temp74E.setVisibility(View.GONE);

        editBinding.temp5A.setVisibility(View.GONE);
        editBinding.temp5B.setVisibility(View.GONE);

        //Temp 75
        editBinding.temp75A.setVisibility(View.GONE);
        editBinding.temp75B.setVisibility(View.GONE);
        editBinding.temp75C.setVisibility(View.GONE);
        editBinding.temp75D.setVisibility(View.GONE);

        editBinding.temp6A.setVisibility(View.GONE);
        editBinding.temp6B.setVisibility(View.GONE);

        //Temp 76
        editBinding.temp76A.setVisibility(View.GONE);
        editBinding.temp76B.setVisibility(View.GONE);

        editBinding.temp7A.setVisibility(View.GONE);

        //Temp 77
        editBinding.temp77A.setVisibility(View.INVISIBLE);

        editBinding.temp8A.setVisibility(View.GONE);

        //Temp 78
        editBinding.temp78A.setVisibility(View.GONE);
        editBinding.temp78B.setVisibility(View.GONE);

        editBinding.temp9A.setVisibility(View.GONE);

        //Temp 79
        editBinding.temp79A.setVisibility(View.GONE);

        editBinding.temp10A.setVisibility(View.GONE);
        editBinding.temp10B.setVisibility(View.GONE);

        //Temp 80
        editBinding.temp80A.setVisibility(View.GONE);

        editBinding.temp11A.setVisibility(View.GONE);
        editBinding.temp11B.setVisibility(View.GONE);

        //Temp 81
        editBinding.temp81A.setVisibility(View.GONE);
        editBinding.temp81B.setVisibility(View.GONE);
        editBinding.temp81C.setVisibility(View.GONE);


        editBinding.temp12A.setVisibility(View.GONE);
        editBinding.temp12B.setVisibility(View.GONE);

        //Temp 82
        editBinding.temp82A.setVisibility(View.GONE);

        editBinding.temp13A.setVisibility(View.GONE);
        editBinding.temp13B.setVisibility(View.GONE);

        //Temp 83
        editBinding.temp83A.setVisibility(View.GONE);

        editBinding.temp14A.setVisibility(View.GONE);
        editBinding.temp14B.setVisibility(View.GONE);

        //Temp 84
        editBinding.temp84A.setVisibility(View.GONE);

        editBinding.temp15A.setVisibility(View.GONE);
        editBinding.temp15B.setVisibility(View.GONE);

        //Temp 85
        editBinding.temp85A.setVisibility(View.GONE);
        editBinding.temp85B.setVisibility(View.GONE);

        editBinding.temp16A.setVisibility(View.GONE);
        editBinding.temp17A.setVisibility(View.GONE);
        editBinding.temp18A.setVisibility(View.GONE);
        editBinding.temp18B.setVisibility(View.GONE);
        editBinding.temp19A.setVisibility(View.GONE);
        editBinding.temp20A.setVisibility(View.GONE);
        editBinding.temp20B.setVisibility(View.GONE);
//        editBinding.temp21A.setVisibility(View.GONE);
//        editBinding.temp21B.setVisibility(View.GONE);
        editBinding.temp22A.setVisibility(View.GONE);
        editBinding.temp22B.setVisibility(View.GONE);
        editBinding.temp22C.setVisibility(View.GONE);
        editBinding.temp23A.setVisibility(View.GONE);
        editBinding.temp24A.setVisibility(View.GONE);
        editBinding.temp25A.setVisibility(View.GONE);
        editBinding.temp25B.setVisibility(View.GONE);
        editBinding.temp25C.setVisibility(View.GONE);
        editBinding.temp26A.setVisibility(View.GONE);
        editBinding.temp26B.setVisibility(View.GONE);
        editBinding.temp27A.setVisibility(View.GONE);
        editBinding.temp27B.setVisibility(View.GONE);
        editBinding.temp28A.setVisibility(View.GONE);
        editBinding.temp28B.setVisibility(View.GONE);
        editBinding.temp28C.setVisibility(View.GONE);
        editBinding.temp28D.setVisibility(View.GONE);
        editBinding.temp29A.setVisibility(View.GONE);
        editBinding.temp29B.setVisibility(View.GONE);
        editBinding.temp30A.setVisibility(View.GONE);
        editBinding.temp30B.setVisibility(View.GONE);
        editBinding.temp30C.setVisibility(View.GONE);
        editBinding.temp30D.setVisibility(View.GONE);
        editBinding.temp31A.setVisibility(View.GONE);
        editBinding.temp31B.setVisibility(View.GONE);
        editBinding.temp31C.setVisibility(View.GONE);
        editBinding.temp32A.setVisibility(View.GONE);
        editBinding.temp32B.setVisibility(View.GONE);
        editBinding.temp33A.setVisibility(View.GONE);
        editBinding.temp33B.setVisibility(View.GONE);
        editBinding.temp34A.setVisibility(View.GONE);
        editBinding.temp34B.setVisibility(View.GONE);
        editBinding.temp35A.setVisibility(View.GONE);
        editBinding.temp36A.setVisibility(View.GONE);
        editBinding.temp37A.setVisibility(View.GONE);
        editBinding.temp37B.setVisibility(View.GONE);
        editBinding.temp38A.setVisibility(View.GONE);
        editBinding.temp38B.setVisibility(View.GONE);
        editBinding.temp39A.setVisibility(View.GONE);
        editBinding.temp40A.setVisibility(View.GONE);
        editBinding.temp41A.setVisibility(View.GONE);
        editBinding.temp42A.setVisibility(View.GONE);
        editBinding.temp42B.setVisibility(View.GONE);
        editBinding.temp43A.setVisibility(View.GONE);
        editBinding.temp44A.setVisibility(View.GONE);
        editBinding.temp44B.setVisibility(View.GONE);
        editBinding.temp45A.setVisibility(View.GONE);
        editBinding.temp46A.setVisibility(View.GONE);
        editBinding.temp46B.setVisibility(View.GONE);
        editBinding.temp47A.setVisibility(View.GONE);
        editBinding.temp48A.setVisibility(View.GONE);
        editBinding.temp48B.setVisibility(View.GONE);
        editBinding.temp48C.setVisibility(View.GONE);
        editBinding.temp49A.setVisibility(View.GONE);
        editBinding.temp49B.setVisibility(View.GONE);
        editBinding.temp50A.setVisibility(View.GONE);
        editBinding.temp50B.setVisibility(View.GONE);
        editBinding.temp51A.setVisibility(View.GONE);
        editBinding.temp52A.setVisibility(View.GONE);
        editBinding.temp53A.setVisibility(View.GONE);
        editBinding.temp54A.setVisibility(View.GONE);
        editBinding.temp54B.setVisibility(View.GONE);
        editBinding.temp55A.setVisibility(View.GONE);
        editBinding.temp55B.setVisibility(View.GONE);
        editBinding.temp56A.setVisibility(View.GONE);
        editBinding.temp57A.setVisibility(View.GONE);
        editBinding.temp57B.setVisibility(View.GONE);
        editBinding.temp58A.setVisibility(View.GONE);
        editBinding.temp59A.setVisibility(View.GONE);
        editBinding.temp59B.setVisibility(View.GONE);
        editBinding.temp60A.setVisibility(View.GONE);
        editBinding.temp61A.setVisibility(View.GONE);
        editBinding.temp61B.setVisibility(View.GONE);
        editBinding.temp62A.setVisibility(View.GONE);
        editBinding.temp62B.setVisibility(View.GONE);
        editBinding.temp63A.setVisibility(View.GONE);
        editBinding.temp63B.setVisibility(View.GONE);
        editBinding.temp64A.setVisibility(View.GONE);
        editBinding.temp65A.setVisibility(View.GONE);
        editBinding.temp66A.setVisibility(View.GONE);
        editBinding.temp67A.setVisibility(View.GONE);
        editBinding.temp67B.setVisibility(View.GONE);
        editBinding.temp68A.setVisibility(View.GONE);
        editBinding.temp69A.setVisibility(View.GONE);
        editBinding.temp69B.setVisibility(View.GONE);
        editBinding.temp70A.setVisibility(View.GONE);
        editBinding.temp70B.setVisibility(View.GONE);

        //86
        editBinding.temp86A.setVisibility(View.GONE);
        editBinding.temp87A.setVisibility(View.GONE);
        editBinding.temp88A.setVisibility(View.GONE);
        editBinding.temp89A.setVisibility(View.GONE);
        editBinding.temp90A.setVisibility(View.GONE);
        editBinding.temp91A.setVisibility(View.GONE);
        editBinding.temp92A.setVisibility(View.GONE);
        editBinding.temp93A.setVisibility(View.GONE);
        editBinding.temp94A.setVisibility(View.GONE);
        editBinding.temp95A.setVisibility(View.GONE);
        editBinding.temp96A.setVisibility(View.GONE);
        editBinding.temp97A.setVisibility(View.GONE);
        editBinding.temp98A.setVisibility(View.GONE);
        editBinding.temp99A.setVisibility(View.GONE);
        editBinding.temp100A.setVisibility(View.GONE);
        editBinding.temp101A.setVisibility(View.GONE);
        editBinding.temp102A.setVisibility(View.GONE);
        editBinding.temp102B.setVisibility(View.GONE);
        editBinding.temp103A.setVisibility(View.GONE);
        editBinding.temp104A.setVisibility(View.GONE);
        editBinding.temp105A.setVisibility(View.GONE);
        editBinding.temp106A.setVisibility(View.GONE);
        editBinding.temp107A.setVisibility(View.GONE);
        editBinding.temp107B.setVisibility(View.GONE);
        editBinding.temp108A.setVisibility(View.GONE);
        editBinding.temp108B.setVisibility(View.GONE);
        editBinding.temp109A.setVisibility(View.GONE);
        editBinding.temp110A.setVisibility(View.GONE);
        editBinding.temp111A.setVisibility(View.GONE);
        editBinding.temp111B.setVisibility(View.GONE);
        editBinding.temp113A.setVisibility(View.GONE);
        editBinding.temp113B.setVisibility(View.GONE);
        editBinding.temp114A.setVisibility(View.GONE);
        editBinding.temp115A.setVisibility(View.GONE);
        editBinding.temp116A.setVisibility(View.GONE);
        editBinding.temp117A.setVisibility(View.GONE);
        editBinding.temp118A.setVisibility(View.GONE);
        editBinding.temp119A.setVisibility(View.GONE);
        editBinding.temp120A.setVisibility(View.GONE);
        editBinding.temp121A.setVisibility(View.GONE);
        editBinding.temp122A.setVisibility(View.GONE);
        editBinding.temp123A.setVisibility(View.GONE);
        editBinding.temp124A.setVisibility(View.GONE);
        editBinding.temp125A.setVisibility(View.GONE);
        editBinding.temp126A.setVisibility(View.GONE);
        editBinding.temp127A.setVisibility(View.GONE);
        editBinding.temp128A.setVisibility(View.GONE);
        editBinding.temp129A.setVisibility(View.GONE);
        editBinding.temp130A.setVisibility(View.GONE);
        editBinding.temp131A.setVisibility(View.GONE);
        editBinding.temp132A.setVisibility(View.GONE);
        editBinding.temp133A.setVisibility(View.GONE);
        editBinding.temp134A.setVisibility(View.GONE);
        editBinding.temp135A.setVisibility(View.GONE);
        editBinding.temp136A.setVisibility(View.GONE);
        editBinding.temp137A.setVisibility(View.GONE);
        editBinding.temp138A.setVisibility(View.GONE);
        editBinding.temp139A.setVisibility(View.GONE);
        editBinding.temp140A.setVisibility(View.GONE);
        editBinding.temp141A.setVisibility(View.GONE);
        editBinding.temp142A.setVisibility(View.GONE);
        editBinding.temp143A.setVisibility(View.GONE);
        editBinding.temp144A.setVisibility(View.GONE);


    }


    //Disable All Frame
    public void disableall() {

        for (int i = 0; i < editBinding.framelayout.getChildCount(); i++) {

            if (editBinding.framelayout.getChildAt(i) instanceof ClipArt) {

                ((ClipArt) editBinding.framelayout.getChildAt(i)).disableAll();

            }

            if (editBinding.framelayout.getChildAt(i) instanceof ClipArt_Image) {

                ((ClipArt_Image) editBinding.framelayout.getChildAt(i)).disableAll();

            }
            if (editBinding.framelayout.getChildAt(i) instanceof ClipArt_Example) {

                ((ClipArt_Example) editBinding.framelayout.getChildAt(i)).disableAll();


            }

        }
    }

    //Ad Text Method
    public void addText() {
        ClipArt ca = new ClipArt(this);
        editBinding.framelayout.addView(ca);
        ca.setId(count++);
        list.add(ca);
        hideView();
        ca.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                storeExValue = "SimpleText";
                ClipArt.setSelectedText(ca);//
                editBinding.mid.setVisibility(View.VISIBLE);
                editBinding.shapeRecycleView.setVisibility(View.GONE);
                editBinding.cancelShapeRecycelView.setVisibility(View.GONE);
                editBinding.shapeColorPicker.setVisibility(View.GONE);
                editBinding.edittext.setText(ClipArt.getSelectedText().getTextView().getText().toString());
                editBinding.fontSettingCardView.setVisibility(View.INVISIBLE);
                disableall();


            }
        });


    }


    //Remove Boxes
    private void remove3ExampleBox() {
        editBinding.framelayout.removeView(clipArt_example);
        editBinding.framelayout.removeView(clipArt_example2);
        editBinding.framelayout.removeView(clipArt_example3);
        editBinding.framelayout.removeView(clipArt_example4);
        editBinding.framelayout.removeView(clipArt_example5);
        editBinding.mid.setVisibility(View.INVISIBLE);
    }


    private void someFun(ClipArt_Example clipArt_example) {

        clipArt_example.setMyClickListenerTick(new ClearEditText() {
            @Override
            public void clearMyText() {
                editBinding.mid.setVisibility(View.INVISIBLE);
                disableall();
            }
        });
        clipArt_example.setMyClickListenerCancell(new ClearEditText() {
            @Override
            public void clearMyText() {
                editBinding.mid.setVisibility(View.INVISIBLE);
                disableall();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        valueChecker();

    }

    private String CMD_PING_GOOGLE = "ping -c 1 google.com";

    private boolean isInternetAvailable(Context context) {
        return isConnected(context) && checkInternetPingGoogle();
    }

    private boolean checkInternetPingGoogle() {
        try {
            int a = Runtime.getRuntime().exec(CMD_PING_GOOGLE).waitFor();
            return a == 0x0;
        } catch (IOException | InterruptedException ex) {
        }
        return false;
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } else {
            return false;
        }
    }


    private void valueCheckBackground() {
        ExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        ex.execute(new Runnable() {
            @Override
            public void run() {
                packageCheck = isInternetAvailable(EditActivity.this);
            }
        });
    }

    private void visibleAndInvisibleView() {
        editBinding.mid.setVisibility(View.VISIBLE);
        editBinding.shapeColorPicker.setVisibility(View.INVISIBLE);
        editBinding.shapeRecycleView.setVisibility(View.INVISIBLE);
        editBinding.cancelShapeRecycelView.setVisibility(View.INVISIBLE);
        editBinding.stickerRecycleView.setVisibility(View.INVISIBLE);
        editBinding.cancelStckerRecycelView.setVisibility(View.INVISIBLE);
        editBinding.sendToFront.setVisibility(View.INVISIBLE);
    }

    private void selectedExampleFontFun(int font) {
        setTextFont(ClipArt_Example.getSelectedText().getTextView(), font);
        ClipArt_Example.getSelectedText().setTextFont(spinnerSelectedItemName);
    }

    private void selectedTextFontFun(int font) {
        setTextFont(ClipArt.getSelectedText().getTextView(), font);
        ClipArt.getSelectedText().setTextFont(spinnerSelectedItemName);
    }

    private void valueChecker() {
        ExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        ex.execute(new Runnable() {
            @Override
            public void run() {
                packageCheck = isInternetAvailable(EditActivity.this);
            }
        });
    }


    public void load_IA(Activity context, String adID, String check) {
        String str2;
        if (mInterstitialAd != null) {
            mInterstitialAd = null;
        }
        AdRequest adRequest = new AdRequest.Builder().build();

        if (BuildConfig.DEBUG) {
            str2 = adID;
        } else {
            str2 = adID;
        }


        InterstitialAd.load(context, str2, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                Log.e(com.mememaker.mememakerpro.creatememe.constant.Constant.MY_TAG, "onInterstitial_Ad_Loaded:");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        show_IA(context, check);
                    }
                }, COUNTER_TIME_AD);


            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                progressDialog.dismiss();

                mInterstitialAd = null;
                show_IA(context,check);
            }
        });


    }

    public void show_IA(Activity context, String check) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    mInterstitialAd = null;
                    if (check.equals("Undo")) {
                        for (int i = 0; i < list.size(); i++) {
                            editBinding.framelayout.removeView(list.remove(i));
                            Log.i("rafaqat", "itemPos: " + load);
                        }
                        hideView();
                    } else if (check.equals("Example")) {
                        exampleTextFun();
                    } else if (check.equals("save")) {
                        disableall();
                        saveImage();
                    } else if (check.equals("shape")) {
                        shapeShow();
                    }
                    categoriesDisplayAct = 6;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    mInterstitialAd = null;
                }
            });
        }else {
            interstitialBuilder.show(this);
            interstitialBuilder = InterstitialBuilder.create().setAdId(com.appbrain.AdId.EXIT)
                    .setFinishOnExit(EditActivity.this).preload(EditActivity.this);
            if (check.equals("Undo")) {
                for (int i = 0; i < list.size(); i++) {
                    editBinding.framelayout.removeView(list.remove(i));
                    Log.i("rafaqat", "itemPos: " + load);
                }
                hideView();
            } else if (check.equals("Example")) {
                exampleTextFun();
            } else if (check.equals("save")) {
                disableall();
                saveImage();
            } else if (check.equals("shape")) {
                shapeShow();
            }
            categoriesDisplayAct = 6;
        }
    }
}