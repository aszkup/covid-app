package com.example.covidapp.view.cases

import androidx.lifecycle.*
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.example.covidapp.BR
import com.example.covidapp.R
import com.example.covidapp.data.cases.Cases
import com.example.covidapp.data.cases.CasesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber

class NewCasesViewModel(
    val cityId: Int,
    cityName: String,
    private val casesRepository: CasesRepository
) : ViewModel() {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.from(ZoneOffset.UTC))
    private val handler = CoroutineExceptionHandler { _, exception -> Timber.e(exception) }

    val cityName = MutableLiveData(cityName)
    val totalCases: LiveData<Int> = casesRepository.getTotalCases(cityId)
        .flowOn(Dispatchers.IO + handler)
        .asLiveData()

    val items: LiveData<List<CasesUiModel>> = casesRepository.getItems(cityId)
        .map { cases -> cases.map { it.toUiModel(dateTimeFormatter) } }
        .flowOn(Dispatchers.IO + handler)
        .asLiveData()

    fun addRandomCases() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val newCasesNumber = (0..10000).random()
            val dateTime = getCreationDate()
            val newCases = Cases(cityId = cityId, number = newCasesNumber, date = dateTime)
            casesRepository.add(newCases, totalCases = totalCases.value ?: 0 + newCasesNumber)
        }
    }

    fun removeAllClicked() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            casesRepository.removeAll()
        }
    }

    fun onRemoveClicked(uiModel: CasesUiModel) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            casesRepository.remove(cityId, uiModel.date)
        }
    }

    private fun getCreationDate() = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)
        .minus((0..7).random().toLong(), ChronoUnit.DAYS)
}

fun Cases.toUiModel(dateTimeFormatter: DateTimeFormatter) = CasesUiModel(
    id = id,
    number = number,
    date = date,
    dateText = dateTimeFormatter.format(date)
)

fun itemBinding(viewModel: NewCasesViewModel) = OnItemBindClass<CasesUiModel>()
    .map(CasesUiModel::class.java) { itemBinding, _, _ ->
        itemBinding.set(BR.uiModel, R.layout.cases_list_item)
            .bindExtra(BR.viewModel, viewModel)
    }

fun diffConfig(): AsyncDifferConfig<CasesUiModel> = AsyncDifferConfig.Builder(
    object : DiffUtil.ItemCallback<CasesUiModel>() {
        override fun areItemsTheSame(oldItem: CasesUiModel, newItem: CasesUiModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CasesUiModel, newItem: CasesUiModel): Boolean =
            oldItem == newItem
    }
).build()
