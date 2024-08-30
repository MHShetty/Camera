package app.grapheneos.camera.ui.activities

import android.net.Uri
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

import app.grapheneos.camera.ui.composable.screen.VideoPlayerScreen
import app.grapheneos.camera.util.getParcelableExtra

class VideoPlayer : AppCompatActivity() {

    companion object {
        const val IN_SECURE_MODE = "isInSecureMode"
        const val VIDEO_URI = "videoUri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = this.intent

        if (intent.getBooleanExtra(IN_SECURE_MODE, false)) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        val uri = getParcelableExtra<Uri>(intent, VIDEO_URI)!!

        setContent {
            VideoPlayerScreen(
                mediaUri = uri,
                omExitAction = this::finish
            )
        }
    }
}
