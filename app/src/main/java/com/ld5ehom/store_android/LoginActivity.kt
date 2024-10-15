package com.ld5ehom.store_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.ld5ehom.store_android.ui.theme.StoreTheme
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class LoginActivity : ComponentActivity() {

    // Retrofit instance creation for API calls
    // API 호출을 위한 Retrofit 인스턴스 생성
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/api/")
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    // Login service for remote API calls
    // 원격 API 호출을 위한 로그인 서비스
    private val loginRetrofitService = retrofit.create(LoginRetrofitService::class.java)

    // Local and remote data sources for user data
    // 사용자 데이터를 위한 로컬 및 원격 데이터 소스
    private val localDataSource = UserLocalDataSource(this)
    private val remoteDataSource = UserRemoteDataSource(loginRetrofitService)

    // User data repository to manage local and remote data
    // 로컬 및 원격 데이터를 관리하는 사용자 데이터 저장소
    private val userDataRepository = UserDataRepository(localDataSource, remoteDataSource)

    // ViewModel initialization with a factory for saved state
    // 저장된 상태를 위한 ViewModel 초기화
    private val viewModel: LoginViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory() {
            override fun <T : ViewModel> create(
                key: String, modelClass: Class<T>, handle: SavedStateHandle
            ): T {
                return LoginViewModel(userDataRepository) as T
            }

        }
    }

    // Called when the activity is first created
    // 엑티비티가 처음 생성될 때 호출됨
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observes UI state and reacts to user state changes
        // UI 상태를 관찰하고 사용자 상태 변경에 반응함
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.userState) {
                        UserState.NONE -> {
                            // nothing to do
                        }

                        UserState.FAILED -> {
                            // Show login failed message
                            Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다", Toast.LENGTH_SHORT)
                                .show()
                        }

                        UserState.LOGGED_IN -> {
                            // Start the UserInfoActivity and finish LoginActivity
                            // UserInfoActivity 시작하고 LoginActivity 종료
                            startActivity(Intent(this@LoginActivity, UserInfoActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }

        // Compose UI and bind ViewModel to the login screen
        // Compose UI를 사용하여 ViewModel과 로그인 화면을 바인딩함
        setContent {
            StoreTheme {
                val uiState = viewModel.uiState.collectAsState().value
                LoginScreen(
                    id = uiState.id,
                    pw = uiState.pw,
                    onIdChange = viewModel::onIdChange,
                    onPwChange = viewModel::onPwChange,
                    onLoginClick = viewModel::login
                )
            }
        }
    }
}

