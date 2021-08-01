package com.sirius.sample.base.dagger;

import android.app.Application
import com.sirius.sample.ui.activities.auth.AuthActivity
import com.sirius.sample.ui.activities.loader.LoaderActivity
import com.sirius.sample.ui.activities.main.MainActivity
import com.sirius.sample.ui.activities.splash.SplashActivity
import com.sirius.sample.ui.auth.auth_first.AuthFirstFragment
import com.sirius.sample.ui.auth.auth_fourth.AuthFourthFragment
import com.sirius.sample.ui.auth.auth_second.AuthSecondFragment
import com.sirius.sample.ui.auth.auth_third.AuthThirdFragment
import com.sirius.sample.ui.auth.auth_third_identity.AuthThirdIChooseIdFragment
import com.sirius.sample.ui.auth.auth_third_identity.AuthThirdIdentityFragment
import com.sirius.sample.ui.auth.auth_zero.AuthZeroFragment
import com.sirius.sample.ui.chats.ChatsFragment
import com.sirius.sample.ui.contacts.ContactsFragment
import com.sirius.sample.ui.credentials.CredentialsFragment
import com.sirius.sample.ui.menu.MenuFragment
import com.sirius.sample.ui.qrcode.ScanQrFragment
import com.sirius.sample.ui.qrcode.ShowQrFragment
import com.sirius.sample.ui.validating.ErrorFragment
import com.sirius.sample.ui.validating.ValidatingFragment


import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class])
interface AppComponent {

    @dagger.Component.Builder
    interface Builder {
        @BindsInstance
        fun withApplication(application: Application?): Builder?
        fun build(): AppComponent?
    }



    /**
     * Add all fragment with ViewModel Here
     */
    //Activities
    fun inject(activity: MainActivity)
    fun inject(activity: AuthActivity)
    fun inject(activity: SplashActivity)
    fun inject(activity: LoaderActivity)


    //Fragments
    fun inject(fragment: AuthZeroFragment)
    fun inject(fragment: AuthFirstFragment)
    fun inject(fragment: AuthSecondFragment)
    fun inject(fragment: AuthThirdFragment)
    fun inject(fragment: AuthThirdIdentityFragment)
    fun inject(fragment: AuthThirdIChooseIdFragment)
    fun inject(fragment: AuthFourthFragment)

    fun inject(fragment: CredentialsFragment)
    fun inject(fragment: ContactsFragment)
    fun inject(fragment: MenuFragment)
    fun inject(fragment: ScanQrFragment)
    fun inject(fragment: ShowQrFragment)
    fun inject(fragment: ChatsFragment)
    fun inject(fragment: ValidatingFragment)
    fun inject(fragment: ErrorFragment)

}