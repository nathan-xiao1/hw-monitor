package com.destack.hwmonitor.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

// Model representing an individual CPU core item on UI
class CPUCore(val processorID: Int) {

    private val _usage = MutableLiveData(-1)
    private val _usageMin = MutableLiveData(Integer.MAX_VALUE)
    private val _usageMax = MutableLiveData(-1)

    private val _temperature = MutableLiveData(Random.nextInt(30, 70)) // TODO Replace
    private val _temperatureMin = MutableLiveData(Integer.MAX_VALUE)
    private val _temperatureMax = MutableLiveData(0)

    val usage: LiveData<Int> = _usage
    val usageMin: LiveData<Int> = _usageMin
    val usageMax: LiveData<Int> = _usageMax
    val temperature: LiveData<Int> = _temperature
    val temperatureMin: LiveData<Int> = _temperatureMin
    val temperatureMax: LiveData<Int> = _temperatureMax

    fun update(usage: Int, temperature: Int) {
        _usage.postValue(usage)
        _usageMin.postValue(min(_usageMin.value!!, usage))
        _usageMax.postValue(max(_usageMax.value!!, usage))
        _temperature.postValue(temperature)
        _temperatureMin.postValue(min(_temperatureMin.value!!, temperature))
        _temperatureMax.postValue(max(_temperatureMax.value!!, temperature))
    }

}