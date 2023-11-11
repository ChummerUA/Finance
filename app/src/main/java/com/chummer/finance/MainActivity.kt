package com.chummer.finance

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.chummer.preferences.mono.lastInfoFetchTime.GetLastMonoAccountsFetchTimeUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var getLastMonoFetchTime: GetLastMonoAccountsFetchTimeUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            Navigation(navController = navController, start = AccountNode.SelectAccount)
        }
    }

}
