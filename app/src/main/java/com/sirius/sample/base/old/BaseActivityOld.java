package com.sirius.sample.base.old;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import java.io.Serializable;


public abstract class BaseActivityOld extends AppCompatActivity {

    public static final String EXTRA_INTENT_BASE = "extra";
    public static final String EXTRA_INTENT_IS_BACK = "is_back";
    public static final String EXTRA_INTENT_SERIALIZEBLE_BASE = "extra_serializeble";
    public static final String EXTRA_INTENT_PARCELABLE_BASE = "extra_parcelablee";
    public static final String EXTRA_INTENT_PARCELABLE_BASE_BOOLEAN = "extra_parcelablee_boolean";
    public static final String EXTRA_INTENT_PARCELABLE_BASE_INTEGER = "extra_parcelablee_integer";
    public static final String EXTRA_INTENT_PARCELABLE_BASE_LIST = "extra_parcelablee_list";

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setExtraSer(Serializable extraSer) {
        this.extraSer = extraSer;
    }

    public void setExtraPar(Parcelable extraPar) {
        this.extraPar = extraPar;
    }

    private String extra;

    public boolean getExtraBool() {
        return extraBool;
    }

    public void setExtraBool(boolean extraBool) {
        this.extraBool = extraBool;
    }

    private boolean extraBool;

    public int getExtraInt() {
        return extraInt;
    }

    public void setExtraInt(int extraInt) {
        this.extraInt = extraInt;
    }

    private int extraInt;

    public boolean isExtraIsback() {
        return extraIsback;
    }

    private boolean extraIsback;
    private Serializable extraSer;

    public Serializable getExtraSer() {
        return extraSer;
    }

    public Parcelable getExtraPar() {
        return extraPar;
    }

    private Parcelable extraPar;




    @LayoutRes
    public abstract int getRootViewId();


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getRootViewId());
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra(EXTRA_INTENT_BASE) != null) {
                extra = intent.getStringExtra(EXTRA_INTENT_BASE);
            }
            extraBool = intent.getBooleanExtra(EXTRA_INTENT_PARCELABLE_BASE_BOOLEAN, false);
            extraInt = intent.getIntExtra(EXTRA_INTENT_PARCELABLE_BASE_INTEGER, 0);


            extraIsback = intent.getBooleanExtra(EXTRA_INTENT_IS_BACK, true);
            if (intent.getSerializableExtra(EXTRA_INTENT_SERIALIZEBLE_BASE) != null) {
                extraSer = intent.getSerializableExtra(EXTRA_INTENT_SERIALIZEBLE_BASE);
            }
            if (intent.getParcelableExtra(EXTRA_INTENT_PARCELABLE_BASE) != null) {
                extraPar = intent.getParcelableExtra(EXTRA_INTENT_PARCELABLE_BASE);
            }

        }
        setupViews();
    }


    public abstract void setupViews();

    public abstract Toolbar getToolBar();

    public abstract AppBarLayout getAppBarLayout();

    public void showActivity(Class classActivity) {
        showActivity(classActivity, false);
    }


    public void showActivity(Class classActivity, boolean onTop) {
        Intent intent = new Intent(this, classActivity);
        if (onTop) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }

    public Intent getIntent(Class classActivity, boolean onTop) {
        Intent intent = new Intent(this, classActivity);
        if (onTop) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return intent;
    }


    public void showActivity(Class classActivity, Serializable extra) {
        Intent intent = new Intent(this, classActivity);
        intent.putExtra(EXTRA_INTENT_SERIALIZEBLE_BASE, extra);
        startActivity(intent);
    }

    public void showActivity(Class classActivity, Parcelable extra, boolean isBack) {
        Intent intent = new Intent(this, classActivity);
        intent.putExtra(EXTRA_INTENT_PARCELABLE_BASE, extra);
        intent.putExtra(EXTRA_INTENT_IS_BACK, isBack);
        startActivity(intent);
    }

    public void showActivity(Class classActivity, Parcelable extra) {
        Intent intent = new Intent(this, classActivity);
        intent.putExtra(EXTRA_INTENT_PARCELABLE_BASE, extra);
        startActivity(intent);
    }



    public void showActivity(Class classActivity, String extra) {
        Intent intent = new Intent(this, classActivity);
        intent.putExtra(EXTRA_INTENT_BASE, extra);
        startActivity(intent);
    }

    public void showActivity(Class classActivity, String extra, boolean onTop) {
        Intent intent = getIntent(classActivity, onTop);
        intent.putExtra(EXTRA_INTENT_BASE, extra);
        startActivity(intent);
    }

}
