package com.sirius.sample.base.old;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;


/**
 * Created by IGOR on 20.03.2018.
 */

public abstract class PageManagerActivity extends BaseActivityOld  {



    private static PageManagerActivity instance;

    boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        instance = this;
        super.onCreate(savedInstanceState);
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void onBack();

        boolean callSuper();

        boolean callSuperBefore();

    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            if (onBackPressedListener.callSuper()) {
                if (onBackPressedListener.callSuperBefore()) {
                    super.onBackPressed();
                }
            }
            onBackPressedListener.onBack();
            if (onBackPressedListener.callSuper()) {
                if (!onBackPressedListener.callSuperBefore()) {
                    super.onBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }

    }

    public static PageManagerActivity getInstance() {
        return instance;
    }

    public View getRootView() {
        return getWindow().getDecorView().getRootView();
    }


    @IdRes
    public abstract int getRootFragmentId();



    public void pushPage(BaseFragmentWithToolbar page) {
        pushPage(page, false);
    }


    public void pushPage(BaseFragmentWithToolbar page, boolean withAnimation) {
     //   page.setPageManager(getPageManager());
        page.setBack(true);
        isBack = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (withAnimation) {
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).replace(getRootFragmentId(), page).commitAllowingStateLoss();
    }


    public void pushPage(BaseFragmentWithToolbar page, boolean withAnimation, boolean isBack) {
      //  page.setPageManager(getPageManager());
        page.setBack(isBack);
        this.isBack = isBack;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (withAnimation) {
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).replace(getRootFragmentId(), page).commitAllowingStateLoss();
    }




    public void pushPageAdd(BaseFragmentWithToolbar page, boolean withAnimation) {
       // page.setPageManager(getPageManager());
        page.setBack(true);
        isBack = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (withAnimation) {
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).add(getRootFragmentId(), page).commit();
    }


    public void showPage(BaseFragmentWithToolbar page) {

        try {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        page.setBack(false);
        isBack = false;
     //   page.setPageManager(getPageManager());
        getSupportFragmentManager().beginTransaction().replace(getRootFragmentId(), page).commitAllowingStateLoss();
    }


    public void showPage(BaseFragmentWithToolbar page, boolean isBack) {

        try {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        page.setBack(isBack);
        this.isBack = isBack;
      //  page.setPageManager(getPageManager());
        getSupportFragmentManager().beginTransaction().replace(getRootFragmentId(), page).commitAllowingStateLoss();
    }


    public void popPage(BaseFragmentWithToolbar page) {
        try {
            getSupportFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pushPage(page);
        // page.setBack(false);
        // page.setPageManager(getPageManager());
        //getSupportFragmentManager().beginTransaction().replace(getRootFragmentId(), page).commit();
    }


    public boolean isBack() {
        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        if (listFragment != null) {
            if (listFragment.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public void popPage() {
        onBackPressed();
    }


    public static void setData(int requestCode, int result, Intent data) {
      //  getInstance().getPageManager().popPage();
        getInstance().onFragmentResult(requestCode, result, data);
    }

    public static void setDataWithoutPopPage(int requestCode, int result, Intent data) {
        getInstance().onFragmentResult(requestCode, result, data);

    }



    public void pushPageForResult(BaseFragmentWithToolbar page, int requestCode, Intent data) {
        page.setRequest(requestCode);
        pushPage(page, false);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        if (listFragment != null) {
            for (Fragment fragmnet : listFragment) {
                fragmnet.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        if (listFragment != null) {
            for (Fragment fragmnet : listFragment) {
                fragmnet.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onFragmentResult(int requestCode, int resultCode, @Nullable Intent data) {
        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        if (listFragment != null) {
            for (Fragment fragmnet : listFragment) {
                if (fragmnet instanceof BaseFragmentWithToolbar) {
                    ((BaseFragmentWithToolbar) fragmnet).onFragmentResult(requestCode, resultCode, data);
                }

            }
        }
    }
}
