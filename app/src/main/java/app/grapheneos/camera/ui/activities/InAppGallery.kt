package app.grapheneos.camera.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import app.grapheneos.camera.CapturedItem

import app.grapheneos.camera.ui.composable.CameraApp
import app.grapheneos.camera.ui.composable.screen.routes.GalleryRoute
import app.grapheneos.camera.ui.composable.screen.viewmodel.CapturedItemsRepository

import app.grapheneos.camera.util.getParcelableArrayListExtra
import app.grapheneos.camera.util.getParcelableExtra

class InAppGallery : AppCompatActivity() {

    companion object {
        const val INTENT_KEY_SECURE_MODE = "is_secure_mode"
        const val INTENT_KEY_VIDEO_ONLY_MODE = "video_only_mode"
        const val INTENT_KEY_LIST_OF_SECURE_MODE_CAPTURED_ITEMS = "secure_mode_items"
        const val INTENT_KEY_LAST_CAPTURED_ITEM = "last_captured_item"
    }

    lateinit var capturedItemsRepository : CapturedItemsRepository

    private val deviceUnlockStateListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            capturedItemsRepository.loadCapturedItems()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isSecureMode = intent.getBooleanExtra(INTENT_KEY_SECURE_MODE, false)
        val showVideosOnly = intent.getBooleanExtra(INTENT_KEY_VIDEO_ONLY_MODE, false)

        if (isSecureMode) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        val mediaItems = getParcelableArrayListExtra<CapturedItem>(
            intent,
            INTENT_KEY_LIST_OF_SECURE_MODE_CAPTURED_ITEMS
        )

        val lastCapturedItem = getParcelableExtra<CapturedItem>(
            intent,
            INTENT_KEY_LAST_CAPTURED_ITEM
        )

        // Initial and start loading captured items in background
        capturedItemsRepository = CapturedItemsRepository(
            context = this,
            scope = lifecycleScope,
            mediaItems = mediaItems,
            showVideosOnly = showVideosOnly,
            placeholderItem = lastCapturedItem,
        )

        setContent {
            CameraApp(
                initialRoute = GalleryRoute,
                onExitAction = this::finish,
            )
        }
    }

    override fun onStart() {
        super.onStart()

        registerReceiver(
            deviceUnlockStateListener,
            IntentFilter().apply {
                addAction(Intent.ACTION_USER_PRESENT)
            }
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(deviceUnlockStateListener)
    }
}