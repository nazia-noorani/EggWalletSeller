package com.egneese.sellers.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.egneese.sellers.R;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.ui.TypefaceSpan;
import com.egneese.sellers.util.NetworkCheck;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.Comparator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adityaagrawal on 11/01/16.
 */
public class QRCodeDisplayActivity extends ActionBarActivity {

    @InjectView(R.id.imgQRCode)
    ImageView imgQRCode;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.bottomsheet)
    BottomSheetLayout bottomSheetLayout;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_display);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SpannableString s = new SpannableString("QR Code");
        s.setSpan(new TypefaceSpan(this, "roboto_light.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getImage();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!NetworkCheck.isNetworkAvailable(this))
            SnackBar.noInternet(this);
    }


    private void getImage() {
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            imgQRCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
            messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
            messageCustomDialogDTO.setMessage(getResources().getString(R.string.error_message));
            messageCustomDialogDTO.setContext(this);
            SnackBar.show(this, messageCustomDialogDTO);
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share){
            String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
            Uri bmpUri = Uri.parse(pathofBmp);

            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"TEXT TO BE FILLLEDD");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/jpeg");
            IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(QRCodeDisplayActivity.this, shareIntent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
                @Override
                public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
                    bottomSheetLayout.dismissSheet();
                    startActivity(activityInfo.getConcreteIntent(shareIntent));
                }
            });
            intentPickerSheet.setFilter(new IntentPickerSheetView.Filter() {
                @Override
                public boolean include(IntentPickerSheetView.ActivityInfo info) {
                    return !info.componentName.getPackageName().startsWith("com.android");
                }
            });
            intentPickerSheet.setSortMethod(new Comparator<IntentPickerSheetView.ActivityInfo>() {
                @Override
                public int compare(IntentPickerSheetView.ActivityInfo lhs, IntentPickerSheetView.ActivityInfo rhs) {
                    return rhs.label.compareTo(lhs.label);
                }
            });

            Drawable customDrawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null);
            IntentPickerSheetView.ActivityInfo customInfo = new IntentPickerSheetView.ActivityInfo(customDrawable, "Custom mix-in", QRCodeDisplayActivity.this, DashboardActivity.class);
            intentPickerSheet.setMixins(Collections.singletonList(customInfo));

            bottomSheetLayout.showWithSheetView(intentPickerSheet);
        }
        return super.onOptionsItemSelected(item);
    }
}
