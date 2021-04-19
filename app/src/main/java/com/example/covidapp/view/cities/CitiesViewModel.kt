package com.example.covidapp.view.cities

import android.content.Context
import android.graphics.Color.argb
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.example.covidapp.BR
import com.example.covidapp.R
import com.example.covidapp.data.city.CitiesRepository
import com.example.covidapp.data.city.City
import com.example.covidapp.util.Colors
import com.example.covidapp.util.Numbers
import com.example.covidapp.view.util.EventLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import timber.log.Timber


private const val BOLD_TEST_FROM_CASES_NO = 1_000

class CitiesViewModel(
    private val context: Context,
    private val citiesRepository: CitiesRepository,
    private val numbers: Numbers,
    private val colors: Colors
) : ViewModel() {

    private val whiteColor = ContextCompat.getColor(context, R.color.white2)
    private val whiteColorInt = colors.getIntFromColor(whiteColor.red, whiteColor.green, whiteColor.blue)
    private val pinkColor = ContextCompat.getColor(context, R.color.pink)
    private val handler = CoroutineExceptionHandler { _, exception -> Timber.e(exception) }

    val uiEvents = EventLiveData<CitiesUiEvent>()

    val items: LiveData<List<CityUiModel>> = citiesRepository.items
        .map { cities ->
            val calculatedSteps = calculateSteps(pinkColor, whiteColor, steps = cities.size - 1)
            cities.sortedWith(compareBy({ it.totalCases * -1 }, { if (it.totalCases == 0) it.name else -1 }))
                .mapIndexed { index, city ->
                    val calculatedColor = if (cities.size == 1 || city.totalCases == 0) {
                        whiteColorInt
                    } else {
                        calculateColor(pinkColor, calculatedSteps, index)
                    }
                    val shouldBoldText = city.totalCases > BOLD_TEST_FROM_CASES_NO && numbers.isPrimeNo(city.totalCases)
                    city.toUiModel(shouldBoldText = shouldBoldText, backgroundColor = calculatedColor)
                }
        }
        .flowOn(Dispatchers.IO + handler)
        .asLiveData()

    fun onListItemClicked(uiModel: CityUiModel) {
        uiEvents.postAsEvent(CitiesUiEvent.OpenCityDetails(uiModel.id, uiModel.name))
    }

    fun onRemoveClicked(uiModel: CityUiModel) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            citiesRepository.remove(uiModel.id)
        }
    }

    fun addRandomCity() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val newCity = City(name = citiesRepository.getRandomCity(), totalCases = 0)
            citiesRepository.add(newCity)
        }
    }

    private fun calculateSteps(startColor: Int, endColor: Int, steps: Int): Triple<Float, Float, Float> {
        val r1 = startColor.red
        val g1 = startColor.green
        val b1 = startColor.blue

        val r2 = endColor.red
        val g2 = endColor.green
        val b2 = endColor.blue

        val redStep = (r2 - r1) / steps.toFloat()
        val greenStep = (g2 - g1) / steps.toFloat()
        val blueStep = (b2 - b1) / steps.toFloat()
        return Triple(redStep, greenStep, blueStep)
    }

    private fun calculateColor(startColor: Int, rgbStep: Triple<Float, Float, Float>, currentStep: Int): Int {
        val r1 = startColor.red
        val g1 = startColor.green
        val b1 = startColor.blue

        val e1 = (r1 + (rgbStep.first * currentStep)).toInt()
        val e2 = (g1 + (rgbStep.second * currentStep)).toInt()
        val e3 = (b1 + (rgbStep.third * currentStep)).toInt()

        return argb(255, e1, e2, e3)
    }
}

sealed class CitiesUiEvent {
    data class OpenCityDetails(
        val cityId: Int,
        val cityName: String
    ) : CitiesUiEvent()
}

private fun City.toUiModel(shouldBoldText: Boolean, backgroundColor: Int) = CityUiModel(
    id = id,
    name = name,
    totalCases = totalCases,
    shouldBoldText = shouldBoldText,
    backgroundColor = backgroundColor
)

fun itemBinding(viewModel: CitiesViewModel) = OnItemBindClass<CityUiModel>()
    .map(CityUiModel::class.java) { itemBinding, _, _ ->
        itemBinding.set(BR.uiModel, R.layout.city_list_item)
            .bindExtra(BR.viewModel, viewModel)
    }

fun diffConfig(): AsyncDifferConfig<CityUiModel> = AsyncDifferConfig.Builder(
    object : DiffUtil.ItemCallback<CityUiModel>() {
        override fun areItemsTheSame(oldItem: CityUiModel, newItem: CityUiModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CityUiModel, newItem: CityUiModel): Boolean =
            oldItem == newItem
    }
).build()
