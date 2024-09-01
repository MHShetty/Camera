package app.grapheneos.camera.ui.composable.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import app.grapheneos.camera.ui.theme.AppColor

enum class GalleryAction {
    EDIT_MEDIA,
    DELETE_MEDIA,
    SHOW_MEDIA_INFO,
    SHARE_MEDIA,
    EDIT_MEDIA_WITH_APP,
}

private val GALLERY_ACTIONS = listOf(
    TopBarAction(
        id = GalleryAction.EDIT_MEDIA,
        title = "Edit Media",
        icon = Icons.Filled.Edit
    ),
    TopBarAction(
        id = GalleryAction.DELETE_MEDIA,
        title = "Delete Media",
        icon = Icons.Filled.Delete
    ),
    TopBarAction(
        id = GalleryAction.SHOW_MEDIA_INFO,
        title = "Show media info",
        icon = Icons.Filled.Info
    ),
    TopBarAction(
        id = GalleryAction.SHARE_MEDIA,
        title = "Share Media",
        icon = Icons.Filled.Share
    ),
    TopBarAction(
        id = GalleryAction.EDIT_MEDIA_WITH_APP,
        title = "Edit with",
        icon = Icons.Filled.Edit,
        alwaysInMoreOptions = true
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryTopBar(
    visible: Boolean,
    onCloseAction: () -> Unit,
    onEditAction: (chooseApp: Boolean) -> Unit,
    onDeleteAction: () -> Unit,
    onInfoAction: () -> Unit,
    onShareAction: () -> Unit
) {

    AnimatedVisibility(
        visible = visible,

        enter = slideInVertically(
            initialOffsetY = { height -> -height },
            animationSpec = tween(durationMillis = 300, easing = EaseIn),
        ),
        exit = slideOutVertically(
            targetOffsetY = { height -> -height },
            animationSpec = tween(durationMillis = 300, easing = EaseIn),
        ),
    ) {
        TopAppBar(
            title = {},

            colors = TopAppBarColors(
                containerColor = AppColor.AppBarColor,
                scrolledContainerColor = AppColor.AppBarColor,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
            ),

            navigationIcon = {
                IconButton(onClick = onCloseAction) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },

            actions = {
                TopBarActions(
                    actions = GALLERY_ACTIONS,
                    onActionClicked = { id ->
                        when (id) {
                            GalleryAction.EDIT_MEDIA -> {
                                onEditAction(false)
                            }
                            GalleryAction.DELETE_MEDIA -> {
                                onDeleteAction()
                            }
                            GalleryAction.SHOW_MEDIA_INFO -> {
                                onInfoAction()
                            }
                            GalleryAction.SHARE_MEDIA -> {
                                onShareAction()
                            }
                            GalleryAction.EDIT_MEDIA_WITH_APP -> {
                                onEditAction(true)
                            }
                        }
                    },
                    // To avoid overlapping leading back arrow and some spacing
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
            },
        )
    }
}