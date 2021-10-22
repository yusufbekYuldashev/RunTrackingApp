package com.yusufbek.runtrackingapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.yusufbek.runtrackingapp.db.RunDatabase
import com.yusufbek.runtrackingapp.other.Constants.KEY_FIRST_RUN
import com.yusufbek.runtrackingapp.other.Constants.KEY_NAME
import com.yusufbek.runtrackingapp.other.Constants.KEY_WEIGHT
import com.yusufbek.runtrackingapp.other.Constants.RUN_DATABASE_NAME
import com.yusufbek.runtrackingapp.other.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, RunDatabase::class.java, RUN_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunDatabase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideWeight(sharedPreferences: SharedPreferences) =
        sharedPreferences.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun provideFirstRun(sharedPreferences: SharedPreferences) = sharedPreferences.getBoolean(
        KEY_FIRST_RUN, true
    )
}