package com.yusufbek.runtrackingapp.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufbek.runtrackingapp.db.RunEntity
import com.yusufbek.runtrackingapp.other.SortType
import com.yusufbek.runtrackingapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runsSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMillis()
    private val runsSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()

    val runs = MediatorLiveData<List<RunEntity>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runsSortedByDate) {
            if (sortType == SortType.DATE) {
                it.let {
                    runs.value = it
                }
            }
        }
        runs.addSource(runsSortedByDistance) {
            if (sortType == SortType.DISTANCE) {
                runs.value = it
            }
        }
        runs.addSource(runsSortedByTimeInMillis) {
            if (sortType == SortType.RUNNING_TIME) {
                runs.value = it
            }
        }
        runs.addSource(runsSortedByAvgSpeed) {
            if (sortType == SortType.AVG_SPEED) {
                runs.value = it
            }
        }
        runs.addSource(runsSortedByCaloriesBurned) {
            if (sortType == SortType.CALORIES_BURNED) {
                runs.value = it
            }
        }
    }

    fun sortRuns(sortType: SortType) {
        when (sortType) {
            SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
            SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
            SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
            SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
            SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
        }.also {
            this.sortType = sortType
        }
    }

    fun saveRun(run: RunEntity) {
        viewModelScope.launch {
            mainRepository.insertRun(run)
        }
    }

}