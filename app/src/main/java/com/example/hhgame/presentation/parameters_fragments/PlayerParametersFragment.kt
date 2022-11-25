package com.example.hhgame.presentation.parameters_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hhgame.R
import com.example.hhgame.data.Player
import com.example.hhgame.databinding.FragmentPlayerParametersBinding
import com.example.hhgame.presentation.parameters_fragments.viewmodel.ParametersViewModel
import com.example.hhgame.utils.AppConstants
import com.example.hhgame.utils.extensions.handleInputText
import kotlinx.coroutines.launch


class PlayerParametersFragment : Fragment() {

    private var _binding: FragmentPlayerParametersBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var viewModel: ParametersViewModel

    private fun createPlayer() = with(binding) {
        createPlayerButton.setOnClickListener {
            setFragmentResult(
                AppConstants.REQUEST_KEY_PLAYER, bundleOf(
                    AppConstants.BUNDLE_KEY_PLAYER to Player(
                        attack = attackEditText.text.toString().toInt(),
                        protection = protectionEditText.text.toString().toInt(),
                        health = healthEditText.text.toString().toInt(),
                        maxHealth = healthEditText.text.toString().toInt(),
                        minDamage = minDamageEditText.text.toString().toInt(),
                        maxDamage = maxDamageEditText.text.toString().toInt(),
                    )
                )
            )
            findNavController().navigateUp()
        }
    }

    private fun returnBack() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initAppBar() {
        binding.tvToolbar.text = resources.getString(R.string.player_parameters)
    }

    private fun initInputs() = with(binding) {
        healthEditText.doAfterTextChanged { health ->
            viewModel.healthChanged(health.toString())
        }
        protectionEditText.doAfterTextChanged { protection ->
            viewModel.protectionChanged(protection.toString())
        }
        attackEditText.doAfterTextChanged { attack ->
            viewModel.attackChanged(attack.toString())
        }
        minDamageEditText.doAfterTextChanged { minDamage ->
            viewModel.minDamageChanged(minDamage.toString())
        }
        maxDamageEditText.doAfterTextChanged { maxDamage ->
            viewModel.maxDamageChanged(maxDamage.toString())
        }
    }

    private fun initObservers() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                createPlayerButton.isEnabled = state.isAcceptable
                healthInputLayout.handleInputText(state.health)
                protectionInputLayout.handleInputText(state.protection)
                attackInputLayout.handleInputText(state.attack)
                minDamageInputLayout.handleInputText(state.minDamage)
                maxDamageInputLayout.handleInputText(state.maxDamage)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerParametersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ParametersViewModel::class.java]
        initAppBar()
        initInputs()
        initObservers()
        createPlayer()
        returnBack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}