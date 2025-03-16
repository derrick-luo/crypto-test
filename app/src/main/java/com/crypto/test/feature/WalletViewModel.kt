package com.crypto.test.feature

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WalletViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<WalletUiState> =
        MutableStateFlow(WalletUiState.Uninitialized)
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()


}
