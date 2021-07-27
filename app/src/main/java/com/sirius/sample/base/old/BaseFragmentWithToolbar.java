package com.sirius.sample.base.old;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;

import com.sirius.sample.R;
import com.sirius.sample.base.App;
import com.sirius.sample.base.ui.BaseActivity;
import com.sirius.sample.base.ui.BaseFragment;
import com.sirius.sample.ui.activities.main.MainActivity;


/**
 * Created by REIGOR on 12.10.2015.
 */
public abstract class BaseFragmentWithToolbar<VB extends ViewDataBinding> extends Fragment {

    public View rootView;
    public LayoutInflater inflater;


    public abstract void subscribe();



    public boolean isHideBottomNavigationView() {
        return true;
    }

    public ActionBar getActionBar() {
        if (getActivity() != null) {
            return ((BaseActivity) getActivity()).getSupportActionBar();
        }
        return null;
    }


    public void hideBottomNavigationView() {
        if (getActivity() != null) {
            // ((MainActivity) getActivity()).hideShowBottomNavigation(isHideBottomNavigationView());
        }
    }

    public void lockUnlockDrawerLayout() {
       /* if (getMainActivity() != null) {
            if (isDrawerLayoutLocked()) {
                getMainActivity().drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            } else {
                getMainActivity().drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

        }*/
    }

    public BaseActivityOld getPageManagerActivity() {
        if (getActivity() != null) {
            return ((BaseActivityOld) getActivity());
        }
        return null;
    }



    public MainActivity getMainActivity() {
        if (getActivity() != null) {
            if (getActivity() instanceof MainActivity) {
                return ((MainActivity) getActivity());
            }
        }
        return null;
    }

    public Toolbar getToolBar() {
        if (getActivity() != null) {
            return ((BaseActivityOld) getActivity()).getToolBar();
        }
        return null;
    }


    public void setToolBarTitle() {
        setToolBarTitleVisible();
        setToolBarImageTitleGone();
        if (getActivity() != null) {
            Toolbar mActionToolbar = getToolBar();
        //    TextView mTitle = (TextView) mActionToolbar.findViewById(R.id.toolbar_title);
         //   mTitle.setText(getTitle());
        }
    }

    public void setToolBarTitleGone() {
        if (getActivity() != null) {
            Toolbar mActionToolbar = getToolBar();
        //    TextView mTitle = (TextView) mActionToolbar.findViewById(R.id.toolbar_title);
         //   mTitle.setVisibility(View.GONE);
        }
    }

    public void setToolBarTitleVisible() {
        if (getActivity() != null) {
            Toolbar mActionToolbar = getToolBar();
        //    TextView mTitle = (TextView) mActionToolbar.findViewById(R.id.toolbar_title);
        //    mTitle.setVisibility(View.VISIBLE);
        }
    }


    public void setToolBarImageTitleGone() {
        if (getActivity() != null) {
            Toolbar mActionToolbar = getToolBar();
         //   ImageView imageView = (ImageView) mActionToolbar.findViewById(R.id.mainToolbarImage);
        //    imageView.setVisibility(View.GONE);
        }
    }


    public void setToolBarImageTitle() {
        setToolBarTitleGone();
        if (getActivity() != null) {
            Toolbar mActionToolbar = getToolBar();
         //   ImageView imageView = (ImageView) mActionToolbar.findViewById(R.id.mainToolbarImage);
         //   imageView.setVisibility(View.VISIBLE);
         //   imageView.setImageResource(getToolbarImage());
        }
    }

    public AppBarLayout getAppBarLayout() {
        if (getActivity() != null) {
            return ((BaseActivityOld) getActivity()).getAppBarLayout();
        }
        return null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.dummy_menu, menu);
        //  MenuItem actionDummy = menu.findItem(R.id.action_dummy);
        //   actionDummy.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public boolean isUseDataBinding() {
        return true;
    }

    public boolean isHideToolbar() {
        return false;
    }

    public void hideShowToolbar() {
        if (getActivity() != null) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                if (isHideToolbar()) {
                    actionBar.hide();
                } else {
                    actionBar.show();
                }
            }
        }

    }

    public boolean isUseMakeToolbar() {
        return true;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    private boolean isBack;

    public VB getDataBinding() {
        if (dataBinding != null) {
            return (VB) dataBinding;
        } else {
            return null;
        }

    }

    public ViewDataBinding dataBinding;

    public boolean isBack() {
        return isBack;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title = "Utils.getTranslation(R.string.app_name)";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    public boolean isDrawerLayoutLocked() {
        return false;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        try {
            rootView = inflater.inflate(getRootViewRes(), null, false);
            if (isUseDataBinding()) {
                dataBinding = DataBindingUtil.bind(rootView);
            }
            hideShowToolbar();
            if (isUseMakeToolbar()) {
                makeToolbars();
            }
            setupViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        subscribe();
        return rootView;
    }

    public abstract void setupViews();

    @LayoutRes
    public abstract int getRootViewRes();


    public void onBackPressed() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).onBackPressed();
        }
    }


    @DrawableRes
    public int getToolbarImage() {
        return 0;
    }

    public boolean isWhiteToolbar() {
        return false;
    }

    public int getColorForToolbar() {
        return App.getContext().getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public void onResume() {
        //  getMainActivity().setOnBackPressedListener(getOnBackPressedListener());
        super.onResume();
        lockUnlockDrawerLayout();
        hideBottomNavigationView();
        if (isUseMakeToolbar()) {
            AppBarLayout actionBar = getAppBarLayout();
            if (actionBar != null) {
                actionBar.setBackgroundColor(getColorForToolbar());
            }
            Toolbar toolbar = getToolBar();
            if (toolbar != null) {

                toolbar.setTitle("");
                if (getTitle() != null) {
                    setToolBarTitle();
                }
                if (getToolbarImage() != 0) {
                    setToolBarImageTitle();
                }
                if (isBack()) {
                    if (isWhiteToolbar()) {
                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                    } else {
                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                    }

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                } else {
                    toolbar.setNavigationIcon(null);

                 /*   if (isWhiteToolbar()) {
                        toolbar.setNavigationIcon(R.drawable.ic_drawer_white);
                    } else {
                        toolbar.setNavigationIcon(R.drawable.ic_drawer);
                    }
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPageManager().openMenu();
                        }
                    });*/
                }
               /* if (isHideActionBar()) {
                    actionBar.hide();
                } else {
                    actionBar.show();
                }*/

            }
        }

    }

    @Override
    public void onPause() {
        //  getMainActivity().setOnBackPressedListener(null);
        super.onPause();
    }

    public void makeToolbars() {

       /* mActionToolbar = getPageManager().getToolbar();
        setupActionBar(false, false);
        if (mActionToolbar != null) {

            if (mActionToolbar.getMenu() != null) {
                mActionToolbar.getMenu().clear();
            }
            if (getInfoBlock() != null) {
                if (getInfoBlock().getTitle() != null) {
                    mActionToolbar.setTitle(getInfoBlock().getTitle());
                }
            }
            if (getTitle() != null) {
                mActionToolbar.setTitle(getTitle());
            }
            mActionToolbar.inflateMenu(R.menu.menu_infos_page_fragment);
            MenuItem menuButton = (MenuItem) mActionToolbar.getMenu().findItem(R.id.action_back_menu);
            if (isFirst) {
                mActionToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
                mActionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).showMenu(true);
                    }
                });
                menuButton.setVisible(false);

            } else {
                mActionToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                mActionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBack();
                    }
                });
                menuButton.setVisible(false);
                menuButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_back_menu:
                                ((MainActivity) getActivity()).showMenu(true);
                                break;
                        }

                        return true;
                    }
                });
            }
        }*/

    }

    public void setRequest(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    int requestCode;

    public void onFragmentResult(int requestCode, int resultCode, Intent data) {

    }

    public void pushPageInside(BaseFragment pageFragment, @IdRes int frameId) {
    //    pageFragment.setPageManager(getPageManager());
        getChildFragmentManager().beginTransaction().replace(frameId, pageFragment).commit();
    }

}
