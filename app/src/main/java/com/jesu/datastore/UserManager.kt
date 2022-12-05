package com.jesu.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class UserManager(context: Context) {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("users")
    private val mDataStore = context.datastore

    companion object {

        val USER_NAME_KEY = stringPreferencesKey(name = "USER_NAME")
        val USER_AGE_KEY = intPreferencesKey(name = "USER_AGE")
        val USER_REGNO = longPreferencesKey("USER_REGNO")

    }

    suspend fun storeUserData(age: Int, name: String, userRegNo: Long) {

        mDataStore.edit { preferences ->
            preferences[USER_AGE_KEY] = age
            preferences[USER_NAME_KEY] = name
            preferences[USER_REGNO] = userRegNo

        }
    }

    val userAgeFlow: Flow<Int> = mDataStore.data.map {
        it[USER_AGE_KEY] ?: 0
    }.catch { exception ->
        if (exception is IOException) {
            emit(0)
        }
    }

    val userNameFlow: Flow<String> = mDataStore.data.map {
        it[USER_NAME_KEY] ?: ""
    }.catch { exception ->
        if (exception is IOException)
            emit("")

    }

    val userNoFlow: Flow<Long> = mDataStore.data.map {
        it[USER_REGNO] ?: 0L
    }.catch { exception ->
        if (exception is IOException) {
            emit(0L)
        }
    }


}