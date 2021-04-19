package com.example.covidapp.view.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.covidapp.R
import com.example.covidapp.databinding.CitiesFragmentBinding
import com.example.covidapp.view.cases.NewCasesArgs
import com.example.covidapp.view.main.MAIN_UI_EVENTS
import com.example.covidapp.view.main.MainUiEvents
import com.example.covidapp.view.util.EventLiveData
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named


class CitiesFragment : Fragment() {

    private val viewModel: CitiesViewModel by viewModel()
    private val mainUiEvents: EventLiveData<MainUiEvents> by inject(qualifier = named(MAIN_UI_EVENTS))
    lateinit var binding: CitiesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<CitiesFragmentBinding>(
            inflater,
            R.layout.cities_fragment,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@CitiesFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addDivider()
        observeMainUiEvents()
        observeUiEvents()
    }

    private fun observeUiEvents() {
        viewModel.uiEvents.observeEvents(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is CitiesUiEvent.OpenCityDetails -> {
                    val args = NewCasesArgs(uiEvent.cityId, uiEvent.cityName)
                    val action = CitiesFragmentDirections.actionCitiesFragmentToNewCasesFragment(args)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun observeMainUiEvents() {
        mainUiEvents.observeEvents(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                MainUiEvents.OnFabClicked -> viewModel.addRandomCity()
            }
        }
    }

    private fun addDivider() {
        val dividerItemDecoration = DividerItemDecoration(context, VERTICAL)
        binding.citiesRecycler.addItemDecoration(dividerItemDecoration)
    }
}
