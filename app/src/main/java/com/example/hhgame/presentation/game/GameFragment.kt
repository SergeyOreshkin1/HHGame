package com.example.hhgame.presentation.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hhgame.R
import com.example.hhgame.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val args by navArgs<GameFragmentArgs>()

    /** Инициализация начальных значений **/

    private fun initPlayerParameters() = with(binding) {
        args.player.apply {
            tvAttackPlayer.text = String.format(resources.getString(R.string.attack), attack)
            tvProtectionPlayer.text =
                String.format(resources.getString(R.string.protection), protection)
            tvHealthPlayerValue.text = health.toString()
            tvAttackModifierPlayer.text = String.format(
                resources.getString(R.string.attack_modifier), args.player.calculateAttackModifier(
                    attackersAttack = attack,
                    defenderDefense = args.bug.protection
                )
            )
            tvDamagePlayer.text =
                String.format(resources.getString(R.string.damage), minDamage, maxDamage)
            countCups.text = COUNT_CUP_DEFAULT_VALUE
        }
    }

    private fun initMonsterParameters() = with(binding) {
        args.bug.apply {
            tvAttackMonster.text = String.format(resources.getString(R.string.attack), attack)
            tvProtectionMonster.text =
                String.format(resources.getString(R.string.protection), protection)
            tvHealthMonsterValue.text = health.toString()
            tvAttackModifierMonster.text = String.format(
                resources.getString(R.string.attack_modifier), args.bug.calculateAttackModifier(
                    attackersAttack = attack,
                    defenderDefense = args.player.protection
                )
            )
            tvDamageMonster.text =
                String.format(resources.getString(R.string.damage), minDamage, maxDamage)
        }
    }

    /** Функция исцеления игрока (Использование умения - выпить кофе). При использовании тратится одна единциа.
     * Если после использования умения здоровья получается больше, чем было в начале игры,
     * то утсанавливается максимально допустимое здоровье (начальное). **/

    private fun healUser() = with(binding) {
        actionHealCardView.setOnClickListener {
            args.player.apply {
                val currentCupsCount = countCups.text.toString().toInt()
                countCups.text = (currentCupsCount - ONE_CUP).toString()
                val healResult: Int
                val healthAfterTreatment =
                    tvHealthPlayerValue.text.toString().toInt() + maxHealth / 2
                healResult =
                    if (healthAfterTreatment > maxHealth) maxHealth else healthAfterTreatment
                tvHealthPlayerValue.text = healResult.toString()
                healAvailable()
            }
        }
    }

    /** Проверка возможно ли использовать умение восстановления здоровья. **/

    private fun healAvailable() = with(binding) {
        actionHealCardView.isEnabled = countCups.text.toString().toInt() > CUP_RAN_OUT_VALUE
    }

    /** Функция проверки успешен ли удар и нанесения урона. В цикле от 1 до модификатора атаки атакующего
     *  происходит имитация броска игральных костей (res - один бросок кубика, после которого может выпасть цифра от 1 до 6.
     *  Результаты бросков помещаюися в массив. Если массив содержит кубик с числом 5 или 6 - высчитывается нанесенный урон,
     *  т.е. берется произвольное число из промежутка минимального и максимального урона атакующего, напр. промежуток 4-7, берем
     *  одну цифру 4, 5, 6 или 7, которая и будет являться нанесенным уроном,
     *  иначе урон равен нулю, т.е. ход закончился неудачей. **/

    private fun strike(
        minAttackerDamage: Int,
        maxAttackerDamage: Int,
        attackerAttackModifier: Int
    ): Int {
        val result: Int
        val list: ArrayList<Int> = ArrayList()
        for (dice in ONE..attackerAttackModifier) {
            val res = (MIN_DICE_VALUE..MAX_DICE_VALUE).random()
            list.add(res)
        }
        result = if (list.contains(FIVE_POINTS) || list.contains(SIX_POINTS)) {
            (minAttackerDamage..maxAttackerDamage).random()
        } else FAILURE
        return result
    }

    /** Функция совершения хода. Считаем, что первый ход делает игрок, после этого, если монстр остался жив
     * автоматически срабатывает код, имитирующий ход противника.
     * При нажатии на кнопку "Бросить кубик" высчитывается урон, который будет нанесен монстру
     *  и отнимается от его текущего здоровья. Под кнопкой выводится результат хода - успех или неудача.
     *  Для монстра (бага) логика аналогична **/

    private fun throwDiceAction() = with(binding) {
        throwDiceBtn.setOnClickListener {

            /** Сколько урона нанес игрок **/

            val playerRollResult = strike(
                minAttackerDamage = args.player.minDamage,
                maxAttackerDamage = args.player.maxDamage,
                args.player.calculateAttackModifier(
                    attackersAttack = args.player.attack,
                    defenderDefense = args.bug.protection
                )
            )

            tvDamagePlayerAfterThrow.text = playerRollResult.toString()

            /** Обновление значения здоровья игрока **/

            tvHealthMonsterValue.text =
                (tvHealthMonsterValue.text.toString().toInt() - playerRollResult).toString()

            /** Был ли успешным ход игрока **/

            if (playerRollResult != FAILURE) tvRollResultPlayerValue.text =
                resources.getString(R.string.lucky) else tvRollResultPlayerValue.text =
                resources.getString(R.string.failure)

            /** Сколько урона нанес монстр **/

            if (tvHealthMonsterValue.text.toString().toInt() > ZERO_HP_POINTS) {
                val monsterRollResult = strike(
                    minAttackerDamage = args.bug.minDamage,
                    maxAttackerDamage = args.bug.maxDamage,
                    args.bug.calculateAttackModifier(
                        attackersAttack = args.bug.attack,
                        defenderDefense = args.player.protection
                    )
                )

                tvDamageMonsterAfterThrow.text = monsterRollResult.toString()

                /** Обновление значения здоровья монстра **/

                tvHealthPlayerValue.text =
                    (tvHealthPlayerValue.text.toString().toInt() - monsterRollResult).toString()

                /** Был ли успешным ход монстра **/

                if (monsterRollResult != FAILURE) tvRollResultMonsterValue.text =
                    resources.getString(R.string.lucky) else tvRollResultMonsterValue.text =
                    resources.getString(R.string.failure)
            }

            /** закончилась ли игра **/

            monsterDiedCheck()
            playerDiedCheck()
        }
    }

    /** После хода проверяем осталось ли здоровье у игрока и его противника **/

    private fun monsterDiedCheck() = with(binding) {
        if (tvHealthMonsterValue.text.toString().toInt() <= ZERO_HP_POINTS) {
            fragmentVictory.root.isVisible = true
            fragmentDefeat.root.isVisible = false
            gameContent.isVisible = false
            fragmentVictory.newGameButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun playerDiedCheck() = with(binding) {
        if (tvHealthPlayerValue.text.toString().toInt() <= ZERO_HP_POINTS) {
            fragmentVictory.root.isVisible = false
            fragmentDefeat.root.isVisible = true
            gameContent.isVisible = false
            fragmentDefeat.newGameButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlayerParameters()
        initMonsterParameters()
        healUser()
        throwDiceAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val COUNT_CUP_DEFAULT_VALUE = "3"
        private const val ONE_CUP = 1
        private const val CUP_RAN_OUT_VALUE = 0
        private const val ONE = 1
        private const val FAILURE = 0
        private const val FIVE_POINTS = 5
        private const val SIX_POINTS = 6
        private const val MIN_DICE_VALUE = 1
        private const val MAX_DICE_VALUE = 6
        private const val ZERO_HP_POINTS = 0
    }
}