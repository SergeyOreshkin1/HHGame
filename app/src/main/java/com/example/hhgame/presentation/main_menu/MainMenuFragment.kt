package com.example.hhgame.presentation.main_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.hhgame.R
import com.example.hhgame.data.Bug
import com.example.hhgame.data.Player
import com.example.hhgame.databinding.FragmentMainMenuBinding
import com.example.hhgame.utils.AppConstants

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var player: Player? = null
    private var monster: Bug? = null

    private fun moveToPlayerCreation() = with(binding) {
        createPlayerButton.setOnClickListener {
            findNavController().navigate(R.id.action_main_menu_fragment_to_player_parameters_fragment)
        }
    }

    private fun moveToMonsterCreation() = with(binding) {
        createMonsterButton.setOnClickListener {
            findNavController().navigate(R.id.action_main_menu_fragment_to_bug_parameters_fragment)
        }
    }

    private fun startGame() = with(binding) {
        startGameBtn.setOnClickListener {
            findNavController().navigate(
                MainMenuFragmentDirections.actionMainMenuFragmentToGameFragment(
                    requireNotNull(monster),
                    requireNotNull(player)
                )
            )
        }
    }

    private fun setFragmentListenerPlayerParameters() {
        setFragmentResultListener(AppConstants.REQUEST_KEY_PLAYER) { _, bundle ->
            player = bundle.getParcelable(AppConstants.BUNDLE_KEY_PLAYER)
        }
    }

    private fun setFragmentListenerMonsterParameters() {
        setFragmentResultListener(AppConstants.REQUEST_KEY_MONSTER) { _, bundle ->
            monster = bundle.getParcelable(AppConstants.BUNDLE_KEY_MONSTER)
        }
    }

    private fun aboutApp() = with(binding) {
        aboutAppBtn.setOnClickListener {
            findNavController().navigate(R.id.action_main_menu_fragment_to_about_app_fragment)
        }
    }

    private fun checkStartGameEnabled() = with(binding) {
        player?.let {
            imageCheckPlayerParameters.setImageResource(R.drawable.ic_check_mark_apply)
        } ?: imageCheckPlayerParameters.setImageResource(R.drawable.ic_check_mark_default)
        monster?.let {
            imageCheckMonsterParameters.setImageResource(R.drawable.ic_check_mark_apply)
        } ?: imageCheckMonsterParameters.setImageResource(R.drawable.ic_check_mark_default)
        startGameBtn.isEnabled = (player != null && monster != null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moveToPlayerCreation()
        moveToMonsterCreation()
        startGame()
        aboutApp()
        setFragmentListenerPlayerParameters()
        setFragmentListenerMonsterParameters()
    }

    override fun onResume() {
        super.onResume()
        checkStartGameEnabled()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}