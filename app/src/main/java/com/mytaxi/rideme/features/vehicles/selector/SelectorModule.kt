package com.mytaxi.rideme.features.vehicles.selector

import androidx.lifecycle.ViewModel
import com.mytaxi.rideme.app.ActivityScope
import com.mytaxi.rideme.archextensions.LifecycleViewModelModule
import com.mytaxi.rideme.archextensions.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module(includes = [LifecycleViewModelModule::class])
abstract class SelectorModule {

    @Module
    companion object {
        private const val SELECTOR_VIEW_MODEL = "SELECTOR_VIEW_MODEL"

        @Provides
        @ActivityScope
        @JvmStatic
        @Named(SELECTOR_VIEW_MODEL)
        fun provideSelectorViewModel(selectorViewModel: SelectorViewModel)
                : SelectorViewModel {
            return selectorViewModel
        }
    }

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SelectorViewModel::class)
    abstract fun provideViewModel(@Named(SELECTOR_VIEW_MODEL) selectorViewModel: SelectorViewModel): ViewModel

    @Binds
    @ActivityScope
    abstract fun provideListFragmentListener(
        @Named(SELECTOR_VIEW_MODEL) selectorViewModel: SelectorViewModel): FragmentsListener

}