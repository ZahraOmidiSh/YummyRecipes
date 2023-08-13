package com.zahra.yummyrecipes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.zahra.yummyrecipes.utils.Constants.REGISTER_USER_INFO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, REGISTER_USER_INFO)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(REGISTER_USER_INFO) }
        )
}
//corruptionHandler (optional) — invoked if a CorruptionException is thrown by the serializer when the data cannot be de-serialized which instructs DataStore how to replace the corrupted data
//migrations (optional) — a list of DataMigration for moving previous data into DataStore
//scope (optional) — the scope in which IO operations and transform functions will execute; in this case, we’re reusing the same scope as the DataStore API default one
//produceFile — generates the File object for Preferences DataStore based on the provided Context and name, stored in this.applicationContext.filesDir + datastore/ subdirectory
