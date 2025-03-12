package com.example.blinknotes.speedTest

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HiFPTDetailSpeedTestScreenModel (
    val idContract : String = "",
    val supplierName : String = "",
    val wifiName: String = "",
    val ipAddress: String = "",
    val ping: String = "",
    val jitter: String = "",
    val downloadSpeed: String = "",
    val uploadSpeed: String = "",
    val deviceName: String = "",
    val testDate: String = "",
    @DrawableRes val painter: Int = 0
    )
class HiFPTDetailSpeedTestScreenViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HiFPTDetailSpeedTestScreenViewModel::class.java)) {
            HiFPTDetailSpeedTestScreenViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
class HiFPTDetailSpeedTestScreenViewModel : ViewModel() {
    private val viewModelState = MutableStateFlow(HiFPTDetailSpeedTestScreenModel())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )
    init {
        viewModelScope.launch {
            viewModelState.update { state ->
                state.copy(
                    idContract  = "SGAP10345",
                 supplierName  = "FPT Telecom",
                 wifiName = "wifi nhà ba mẹ",
                 ipAddress = "192.6.8.12",
                 ping = "5.6 ms",
                 jitter = "7.7 ms",
                 downloadSpeed = "110.2 Mbps",
                 uploadSpeed = "120.4 Mbps",
                 deviceName = "Samsung Galaxy S24 Ultra",
                 testDate = "17:56-02/01/2025",
                 painter = 0
                )
            }
        }
    }
}