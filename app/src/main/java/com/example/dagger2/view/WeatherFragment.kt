package com.example.dagger2.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.fragment.app.Fragment
import com.example.dagger2.databinding.FragmentWeatherBinding
import com.example.dagger2.viewmodel.WeatherViewModel
import com.example.dagger2.viewmodel.WeatherViewModelFactory
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dagger2.R
import com.example.dagger2.WeatherApp
import com.example.dagger2.model.repository.location.LocationResult
import com.example.dagger2.model.repository.weather.WeatherResult
import com.example.dagger2.model.repository.weather.retrofit.Weather
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            (requireActivity().application as WeatherApp).weatherRepository,
            (requireActivity().application as WeatherApp).locationRepository)
    }
    private lateinit var gpsLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gpsLauncher = registerForActivityResult(StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getLocation()
            } else {
                showErrorScreen(R.string.turn_on_your_gps)
            }
        }
        locationPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.getLocation()
            } else {
                showErrorScreen(R.string.no_permission)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGetLocation.setOnClickListener {
            viewModel.getLocation()
        }

        viewModel.location.observe(viewLifecycleOwner) { location: LocationResult ->
            when(location) {
                is LocationResult.NoPermission -> showPermissionDialog()
                is LocationResult.GpsOff -> showGpsDialog()
                is LocationResult.NotAvailable -> showErrorScreen(R.string.turn_on_your_gps)
                is LocationResult.Success -> {
                    viewModel.getWeather("${location.location.latitude},${location.location.longitude}")
                }
            }
        }

        viewModel.currentWeather.observe(viewLifecycleOwner) { weather: WeatherResult ->
            when(weather) {
                is WeatherResult.TechnicalError -> showErrorScreen(R.string.technical_problems)
                is WeatherResult.Success -> showWeatherUi(weather.data)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showErrorScreen(idString: Int) {
        binding.containerSuccess.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.errorLocationContainer.visibility = View.VISIBLE
        binding.tvError.text = getString(idString)
    }

    private fun showGpsDialog() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 500L).build()
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()

        LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(settingsRequest)
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    gpsLauncher.launch(intentSenderRequest)
                }
            }
    }

    private fun showPermissionDialog() {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun showWeatherUi(weather: Weather) {
        binding.progressBar.visibility = View.GONE
        binding.errorLocationContainer.visibility = View.GONE
        binding.containerSuccess.visibility = View.VISIBLE
        with(binding) {
            tvTemp.text = getString(R.string.temp_c, weather.current.tempC.toInt())
            tvCity.text = weather.location.city
            tvWindKmp.text = getString(R.string.wind_kmp, weather.current.wind.toInt())
            tvDescriptionOfWeather.text = weather.current.condition.description
            Glide
                .with(requireContext())
                .load("https:" + weather.current.condition.icon)
                .into(imgIconWeather)
        }
    }
}