package app.grapheneos.camera.ui.activities

import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

import app.grapheneos.camera.CapturedItem
import app.grapheneos.camera.CapturedItems

import app.grapheneos.camera.ITEM_TYPE_VIDEO
import app.grapheneos.camera.ui.composable.screen.GalleryScreen

import app.grapheneos.camera.util.getParcelableArrayListExtra
import app.grapheneos.camera.util.getParcelableExtra

class InAppGallery : AppCompatActivity() {

    companion object {
        const val INTENT_KEY_SECURE_MODE = "is_secure_mode"
        const val INTENT_KEY_VIDEO_ONLY_MODE = "video_only_mode"
        const val INTENT_KEY_LIST_OF_SECURE_MODE_CAPTURED_ITEMS = "secure_mode_items"
        const val INTENT_KEY_LAST_CAPTURED_ITEM = "last_captured_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isSecureMode = intent.getBooleanExtra(INTENT_KEY_SECURE_MODE, false)
        val showVideosOnly = intent.getBooleanExtra(INTENT_KEY_VIDEO_ONLY_MODE, false)

        if (isSecureMode) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        setContent {
            GalleryScreen(
                isSecureMode = isSecureMode,

                mediaItemsLoader = {
                    val mediaItems = if (isSecureMode) {
                        getParcelableArrayListExtra<CapturedItem>(
                            intent,
                            INTENT_KEY_LIST_OF_SECURE_MODE_CAPTURED_ITEMS
                        )!!
                    } else {
                        CapturedItems.get(this)
                    }

                    val relevantMediaItems = if (showVideosOnly) {
                        mediaItems.filter { capturedItem -> capturedItem.type == ITEM_TYPE_VIDEO }
                    } else {
                        mediaItems
                    }

                    val sortedMediaItems = relevantMediaItems.sortedByDescending { it.dateString }
                    sortedMediaItems
                },

                latestMediaItemLoader = {
                    val lastCapturedItem =
                        getParcelableExtra<CapturedItem>(intent, INTENT_KEY_LAST_CAPTURED_ITEM)
                    lastCapturedItem
                },

                onExit = this::finish
            )
        }
    }
}