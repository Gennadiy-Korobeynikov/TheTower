package com.tpu.thetower.fragments.lvl5

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5ChestBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.PermissionManager
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.managers.devicemanagers.ChestCodeReceiver
import com.tpu.thetower.managers.devicemanagers.ChestManager
import com.tpu.thetower.utils.SoundEffect
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5PuzzleChestFragment : Fragment(R.layout.fragment_lvl5_chest), Hintable {

    private var _binding: FragmentLvl5ChestBinding? = null
    private val binding get() = _binding!!

    private lateinit var hintManager: HintManager

    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var soundManager : SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    @Inject lateinit var chestManager: ChestManager

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_lvl5)

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "chest_input"

        const val KEY_CHEST_CODE = "KEY_CHEST_CODE"
        const val ACTION_CHEST_CODE = "com.tpu.thetower.action.CHEST_CODE"
    }

    private lateinit var notificationManager: NotificationManager
    private lateinit var permissionManager: PermissionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl5ChestBinding.bind(view)
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        permissionManager = PermissionManager(this, requireActivity())

        ensureNotificationChannel()

        maybeShowChestNotification()

        setListeners()

        val levelSnapshot = blurVM.getBlur(Lvl5Fragment.KEY_LVL5_SNAPSHOT)
            ?: error("Snapshot must be set before opening puzzle")

        val blur = getOrCreateBlur(
            blurVM = blurVM,
            blurKey = Lvl5Fragment.KEY_LVL5_BLUR,
            sourceBitmap = levelSnapshot,
            radius = 220f,
            context = requireContext()
        )

        binding.ivBg.setImageBitmap(blur)
    }

    fun setListeners() {
        // Открыт ли сундук
        viewLifecycleOwner.lifecycleScope.launch {
            chestManager.chestState.collect { isOpen ->
                if (isOpen) {
                    binding.ivAccessCard.visibility = View.VISIBLE
                    binding.ivChest.visibility = View.GONE
                    UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
                    soundManager.playSound(SoundEffect.CHEST_OPENING)
                    notificationManager.cancel(NOTIFICATION_ID)
                }
            }
        }

        binding.ivAccessCard.setOnClickListener {
            FragmentNavigation.goBack(this)
            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
            loadManager.changeAccessCardNumber(6)
        }
    }

    private fun maybeShowChestNotification() {
        val canPost =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                permissionManager.isPermissionGranted(android.Manifest.permission.POST_NOTIFICATIONS)

        if (!canPost) return
        showChestNotification()
    }

    private fun ensureNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Ввод кода сундука",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Уведомления для ввода кода сундука"
            setSound(null, null)
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(channel)
    }


    private fun showChestNotification() {
        val context = requireContext()

        val remoteInput = RemoteInput.Builder(KEY_CHEST_CODE)
            .setLabel("Введите код")
            .build()

        val intent = Intent(context, ChestCodeReceiver::class.java).apply {
            action = ACTION_CHEST_CODE
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
             NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val action = NotificationCompat.Action.Builder(
            R.drawable.lvl5_map_icon,  // иконка кнопки
            "Код", // текст кнопки
            pendingIntent  // что выполнить
        )
            .addRemoteInput(remoteInput)
            .build()

        val large = BitmapFactory.decodeResource(context.resources, R.drawable.lvl5_map_icon)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.lvl5_chest_small_icon)
            .setLargeIcon(large)
            .setContentTitle("Запертый сундук")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(action)
            .setAutoCancel(false)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }


    override fun useHint() {
        // todo
    }

    override fun skipPuzzle() {
        chestManager.tryOpen("37")
    }

    override fun onPause() {
        super.onPause()
        notificationManager.cancel(NOTIFICATION_ID)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}