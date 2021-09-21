package com.calamity.rtuitlabclicker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.calamity.rtuitlabclicker.common.Constants
import com.calamity.rtuitlabclicker.data.local.UsersDatabase
import com.calamity.rtuitlabclicker.data.local.dao.UserDao
import com.calamity.rtuitlabclicker.data.remote.GithubApi
import com.calamity.rtuitlabclicker.data.remote.GithubAuthApi
import com.calamity.rtuitlabclicker.data.repository.UserRepositoryImpl
import com.calamity.rtuitlabclicker.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: UsersDatabase.Callback
    ) = Room.databaseBuilder(app, UsersDatabase::class.java, "users_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    @Singleton
    fun provideUserDao(db: UsersDatabase) = db.userDao()

    @Provides
    @Singleton
    fun provideGithubApi(): GithubApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(GithubApi::class.java)

    @Provides
    @Singleton
    fun provideGithubAuthApi(): GithubAuthApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.OAUTH_URL)
            .build()
            .create(GithubAuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao, api: GithubApi, authApi: GithubAuthApi, prefs: SharedPreferences) : UserRepository =
        UserRepositoryImpl(dao, api, authApi, prefs)

    @Provides
    @Singleton
    fun provideSharedPrefs(
        @ApplicationContext context: Context
    ) = context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE)!!


    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope