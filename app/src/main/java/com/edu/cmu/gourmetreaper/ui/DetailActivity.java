package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dbLayout.CuisineDAO;
import com.edu.cmu.gourmetreaper.dbLayout.CuisineReviewDAO;
import com.edu.cmu.gourmetreaper.dbLayout.daoImplementation.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.dbLayout.daoImplementation.CuisineReviewDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;
import com.edu.cmu.gourmetreaper.entities.CuisineReview;
import com.edu.cmu.gourmetreaper.entities.RestaurantReview;
import com.edu.cmu.gourmetreaper.service.FacebookService;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends Activity {
    private CuisineDAO cd;
    private CuisineReviewDAO crd;
    private Cuisine c;
    private List<CuisineReview> crList;
    private TextView dishTitle, disText;
    private Button addButton;
    private ImageView dishImg1, dishImg2;
    private EditText dishComInput;
    private TextView dishComText1, dishComText2, reviewNum;
    private ArrayList<String> chosenList;
    private CallbackManager callbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private ImageButton postButton;
    private ImageView selectButton, fromGallery;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private FacebookService facebookService;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            postButton.setVisibility(View.VISIBLE);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        showDish();
        initReview();
        initFacebook();
        showReview();
        addListenerOnRatingBar();
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                txtRatingValue.setText(String.valueOf(rating));
            }
        });
    }


    public void initFacebook() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

                if (newToken == null) {

                }
            }
        };
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                if (newProfile == null) {

                }
            }
        };
        facebookService = new FacebookService(mTokenTracker, mProfileTracker);
        this.startService(new Intent(this, FacebookService.class));

    }

    public void showDish() {
        dishTitle = (TextView) findViewById(R.id.dishTitle);
        disText = (TextView) findViewById(R.id.disText);
        dishImg1 = (ImageView) findViewById(R.id.dishImg1);
        cd = new CuisineDAOImpl(this);
        Bundle dishMsg = getIntent().getExtras();

        String dishName = dishMsg.getString("dishName");
        c = cd.getCuisineByName(dishName);
        dishTitle.setText(c.getCuisineName());
        disText.setText(c.getCuisineDescription());
        dishImg1.setImageBitmap(c.getImage());

        addButton = (Button) findViewById(R.id.addButton);
        chosenList = getIntent().getStringArrayListExtra("chosenList");
        if (chosenList != null && chosenList.size() > 0) {
            for (String dish : chosenList) {
                if (dish.equals(dishName)) {
                    addButton.setText("Added");
                    break;
                }
            }
        }

    }

    public void initReview() {
        crd = new CuisineReviewDAOImpl(this);
        crList = crd.getAllCuisineReviewWithCuisine(c.getCuisineID());
        dishComText1 = (TextView) findViewById(R.id.dishComText1);
        dishComText2 = (TextView) findViewById(R.id.dishComText2);
        reviewNum = (TextView) findViewById(R.id.reviewNum);
        dishComInput = (EditText) findViewById(R.id.dishComInput);

        updateReviewNum();
        postButton = (ImageButton) findViewById(R.id.fbShareButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePhotoToFacebook();
            }
        });

        fromGallery = (ImageView) findViewById(R.id.showImg);

        selectButton = (ImageView) findViewById(R.id.uploadImg);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void updateReviewNum() {
        reviewNum.setText(crList.size() + " Reviews");
    }

    public void addToOrder(View view) {
        if (addButton.getText().toString().equals("Added")) {
            addButton.setText("Add to order");
        } else {
            addButton.setText("Added");
        }
    }


    private void sharePhotoToFacebook() {
        if (Profile.getCurrentProfile() == null) {
            Toast.makeText(this, "You are not logged in. Please login before posting", Toast.LENGTH_LONG).show();
            return;
        }
        Bitmap bitmap = null;
        String postContent = "";
        try {
            bitmap = ((BitmapDrawable) fromGallery.getDrawable()).getBitmap();
            postContent = dishComInput.getText().toString();
        } catch (Exception e) {
            Toast.makeText(this, "Please choose photo that you want to post.", Toast.LENGTH_LONG).show();
            return;
        }
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption(postContent)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
        Toast.makeText(this, "Thanks for using Lightening Order. Your post has been released.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fos;
        try {
            destination.createNewFile();
            fos = new FileOutputStream(destination);
            fos.write(bytes.toByteArray());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fromGallery.setImageBitmap(bm);
    }


    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = this.getContentResolver().query(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        fromGallery.setImageBitmap(bm);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();

    }

    @Override
    public void onStop() {
        super.onStop();
        this.stopService(new Intent(this, FacebookService.class));

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo with Camera", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo that You Want to Post");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo with Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void showReview() {
        crList = crd.getAllCuisineReviewWithCuisine(c.getCuisineID());
        if (crList != null && crList.size() > 0) {
            if (crList.size() > 0 && crList.get(0) != null) {
                dishComText1.setText(crList.get(0).getComment() + " rating: " + crList.get(0).getRating());
            } else {
                dishComText1.setText("There is no comments yet");
            }
            if (crList.size() > 1 && crList.get(1) != null) {
                dishComText2.setText(crList.get(1).getComment() + " rating: " + crList.get(1).getRating());
            } else {
                dishComText2.setText("There is no comments yet");
            }
        }
    }

    public void submitReview(View view) {
        CuisineReview cr = new CuisineReview();
        cr.setComment(dishComInput.getText().toString());
        if (txtRatingValue.getText() == null || txtRatingValue.getText().toString().length() == 0) {
            cr.setRating(0);
        } else {
            cr.setRating((int) Double.parseDouble(txtRatingValue.getText().toString()));
        }
        List<Bitmap> imgs = new ArrayList<>();
        fromGallery.buildDrawingCache();
        Bitmap bmap = fromGallery.getDrawingCache();
        imgs.add(bmap);
        cr.setImages(imgs);
        crd.insertCuisineReview(cr, c.getCuisineID());
        showReview();
        updateReviewNum();
    }
}
