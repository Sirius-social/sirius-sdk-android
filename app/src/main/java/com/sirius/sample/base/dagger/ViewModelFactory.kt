package com.sirius.sample.base.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sirius.sample.ui.activities.auth.AuthActivityModel
import com.sirius.sample.ui.activities.loader.LoaderActivityModel
import com.sirius.sample.ui.activities.main.MainActivityModel
import com.sirius.sample.ui.activities.splash.SplashActivityModel
import com.sirius.sample.ui.auth.auth_first.AuthFirstViewModel
import com.sirius.sample.ui.auth.auth_fourth.AuthFourthViewModel
import com.sirius.sample.ui.auth.auth_second.AuthSecondViewModel
import com.sirius.sample.ui.auth.auth_third.AuthThirdViewModel
import com.sirius.sample.ui.auth.auth_third_identity.AuthThirdChooseIdViewModel
import com.sirius.sample.ui.auth.auth_third_identity.AuthThirdIdentityViewModel
import com.sirius.sample.ui.auth.auth_zero.AuthZeroViewModel
import com.sirius.sample.ui.chats.ChatsViewModel
import com.sirius.sample.ui.contacts.ContactsViewModel
import com.sirius.sample.ui.credentials.CredentialsViewModel
import com.sirius.sample.ui.menu.MenuViewModel
import com.sirius.sample.ui.qrcode.ScanQrViewModel
import com.sirius.sample.ui.qrcode.ShowQrViewModel
import com.sirius.sample.ui.validating.ErrorViewModel
import com.sirius.sample.ui.validating.ValidatingViewModel


import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * Add all viewModel Here
     */


    /**
     * Activity viewModel Here
     */


    @Binds
    @IntoMap
    @ViewModelKey(MainActivityModel::class)
    internal abstract fun bindMainActivityModel(viewModel: MainActivityModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthActivityModel::class)
    internal abstract fun bindAuthActivityModel(viewModel: AuthActivityModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityModel::class)
    internal abstract fun bindSplashActivityModel(viewModel: SplashActivityModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoaderActivityModel::class)
    internal abstract fun bindLoaderActivityModel(viewModel: LoaderActivityModel): ViewModel


    /**
     * Fragments viewModel Here
     */

    @Binds
    @IntoMap
    @ViewModelKey(AuthZeroViewModel::class)
    internal abstract fun bindAuthZeroViewModel(viewModel: AuthZeroViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(AuthFirstViewModel::class)
    internal abstract fun bindAuthFirstViewModel(viewModel: AuthFirstViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthSecondViewModel::class)
    internal abstract fun bindAuthSecondViewModel(viewModel: AuthSecondViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthThirdViewModel::class)
    internal abstract fun bindAuthThirdViewModel(viewModel: AuthThirdViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthThirdIdentityViewModel::class)
    internal abstract fun bindAuthThirdIdentityViewModel(viewModel: AuthThirdIdentityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthThirdChooseIdViewModel::class)
    internal abstract fun bindAuthThirdChooseIdViewModel(viewModel: AuthThirdChooseIdViewModel): ViewModel



    @Binds
    @IntoMap
    @ViewModelKey(AuthFourthViewModel::class)
    internal abstract fun bindAuthFourthViewModel(viewModel: AuthFourthViewModel): ViewModel



    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    internal abstract fun bindMenuViewModel(viewModel: MenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel::class)
    internal abstract fun bindContactsViewModel(viewModel: ContactsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CredentialsViewModel::class)
    internal abstract fun bindCredentialsViewModel(viewModel: CredentialsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanQrViewModel::class)
    internal abstract fun bindScanQrViewModel(viewModel: ScanQrViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShowQrViewModel::class)
    internal abstract fun bindShowQrViewModel(viewModel: ShowQrViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatsViewModel::class)
    internal abstract fun bindChatsViewModel(viewModel: ChatsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ValidatingViewModel::class)
    internal abstract fun bindValidatingViewModel(viewModel: ValidatingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ErrorViewModel::class)
    internal abstract fun bindErrorViewModel(viewModel: ErrorViewModel): ViewModel

}