package com.example.suryashakthi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suryashakthi.domain.model.EnergyRecord
import com.example.suryashakthi.domain.repository.EnergyRepository
import com.example.suryashakthi.domain.usecase.GetSmartInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Enterprise state management for the Dashboard.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: EnergyRepository,
    private val getSmartInsightsUseCase: GetSmartInsightsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        repository.getHistoricalRecords()
            .onEach { records ->
                _state.update { it.copy(records = records, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun requestAiInsight() {
        viewModelScope.launch {
            _state.update { it.copy(isAiLoading = true) }
            getSmartInsightsUseCase().fold(
                onSuccess = { insight ->
                    _state.update { it.copy(aiInsight = insight, isAiLoading = false) }
                },
                onFailure = { error ->
                    _state.update { it.copy(error = error.message, isAiLoading = false) }
                }
            )
        }
    }

    fun saveEnergyRecord(solar: Float, consumption: Float, weather: String = "Sunny") {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.saveRecord(
                EnergyRecord(
                    solarProduction = solar,
                    gridConsumption = consumption,
                    batteryStorage = 0f,
                    weatherCondition = weather
                )
            )
            // No need to manually update state as loadData() listens to the Flow
        }
    }

    fun deleteEnergyRecord(record: EnergyRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }
}

data class DashboardState(
    val records: List<EnergyRecord> = emptyList(),
    val aiInsight: String? = null,
    val isLoading: Boolean = true,
    val isAiLoading: Boolean = false,
    val error: String? = null
)
