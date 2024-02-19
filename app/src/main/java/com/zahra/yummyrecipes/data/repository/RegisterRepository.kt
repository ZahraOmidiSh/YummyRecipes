package com.zahra.yummyrecipes.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.zahra.yummyrecipes.data.source.RemoteDataSource
import com.zahra.yummyrecipes.models.register.BodyRegister
import com.zahra.yummyrecipes.models.register.RegisterStoredModel
import com.zahra.yummyrecipes.utils.Constants.REGISTER_HASH
import com.zahra.yummyrecipes.utils.Constants.REGISTER_USERNAME
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(
    private val remote: RemoteDataSource,
    private val dataStore: DataStore<Preferences>
) {
    //Store user info
    private object StoreKeys {
        val username = stringPreferencesKey(REGISTER_USERNAME)
        val hash = stringPreferencesKey(REGISTER_HASH)
    }

    suspend fun saveRegisterData(username: String, hash: String) {
        dataStore.edit {
            it[StoreKeys.username] = username
            it[StoreKeys.hash] = hash
        }
    }

    val readRegisterData: Flow<RegisterStoredModel> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val username = it[StoreKeys.username] ?: ""
            val hash = it[StoreKeys.hash] ?: ""
            RegisterStoredModel(username, hash)
        }

    // API
    suspend fun postRegister(apiKey: String, body: BodyRegister) = remote.postRegister(apiKey, body)
}