package com.example.covidapp.view.cases

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covidapp.R
import com.example.covidapp.databinding.NewCasesFragmentBinding
import com.example.covidapp.view.main.MAIN_UI_EVENTS
import com.example.covidapp.view.main.MainUiEvents
import com.example.covidapp.view.util.EventLiveData
import kotlinx.android.parcel.Parcelize
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class NewCasesFragment : Fragment() {

    private val args: NewCasesFragmentArgs by navArgs()
    private val viewModel: NewCasesViewModel by viewModel {
        parametersOf(args.cityInfo.cityId, args.cityInfo.cityName)
    }
    private val mainUiEvents: EventLiveData<MainUiEvents> by inject(qualifier = named(MAIN_UI_EVENTS))
    lateinit var binding: NewCasesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<NewCasesFragmentBinding>(
            inflater,
            R.layout.new_cases_fragment,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@NewCasesFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addDivider()
        observeMainUiEvents()
    }

    private fun observeMainUiEvents() {
        mainUiEvents.observeEvents(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                MainUiEvents.OnFabClicked -> viewModel.addRandomCases()
            }
        }
    }

    private fun addDivider() {
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        binding.casesRecycler.addItemDecoration(dividerItemDecoration)
    }
}

@Parcelize
data class NewCasesArgs(
    val cityId: Int,
    val cityName: String
) : Parcelable
