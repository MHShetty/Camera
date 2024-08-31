package app.grapheneos.camera.ui.composable.screen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.grapheneos.camera.CapturedItem
import app.grapheneos.camera.R
import app.grapheneos.camera.ktx.showOrReplaceSnackbar
import app.grapheneos.camera.ui.activities.VideoPlayer
import app.grapheneos.camera.ui.composable.components.FileDeletionDialog
import app.grapheneos.camera.ui.composable.components.GalleryImage
import app.grapheneos.camera.ui.composable.components.GalleryTopBar
import app.grapheneos.camera.ui.composable.components.MediaInfoDialog
import app.grapheneos.camera.ui.composable.components.MediaItemDetails
import app.grapheneos.camera.ui.theme.AppColor
import kotlinx.coroutines.launch
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

import app.grapheneos.camera.ITEM_TYPE_IMAGE
import app.grapheneos.camera.ITEM_TYPE_VIDEO

private const val TAG = "GalleryScreen"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(
    isSecureMode: Boolean,
    mediaItemsLoader: () -> List<CapturedItem>,
    latestMediaItemLoader: () -> CapturedItem?,
    onExit: () -> Unit = {},
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val zoomableState = rememberZoomableState()

    var inFocusMode by remember {
        mutableStateOf(false)
    }

    val backgroundColor by animateColorAsState(
        label = "background_color_animation",
        targetValue = if (inFocusMode) Color.Black else AppColor.BackgroundColor,
        animationSpec = tween(durationMillis = 300, easing = EaseIn),
    )

    val capturedItems = remember {
        mutableStateListOf<CapturedItem>()
    }

    val pagerState = rememberPagerState(pageCount = {
        capturedItems.size
    })

    val displayedMediaInfo = rememberSaveable(saver = MediaItemDetails.Saver) {
        mutableStateOf(null)
    }

    var itemToBeDeleted by rememberSaveable {
        mutableStateOf<CapturedItem?>(null)
    }

    fun getCurrentMediaItem(): CapturedItem {
        return capturedItems[pagerState.currentPage]
    }

    LaunchedEffect(mediaItemsLoader) {
        coroutineScope.launch {
            val latestItem = latestMediaItemLoader()
            synchronized(capturedItems) {
                if (latestItem != null && capturedItems.isEmpty()) {
                    capturedItems.add(latestItem)
                }
            }
        }

        coroutineScope.launch {
            val items = mediaItemsLoader()

            if (items.isEmpty()) {
                Toast.makeText(context, R.string.empty_gallery, Toast.LENGTH_LONG).show()
                onExit()
            }

            capturedItems.clear()
            capturedItems.addAll(items)
        }
    }


    // Displays media info dialog when displayedMediaInfo is not null
    MediaInfoDialog(
        mediaItemDetails = displayedMediaInfo.value,
        onFinish = {
            displayedMediaInfo.value = null
        },
    )

    // Displays item deletion dialog when deletionItem is not null
    FileDeletionDialog(
        deletionItem = itemToBeDeleted,
        onDeleteAction = { item ->
            coroutineScope.launch {
                val result = item.delete(context)

                if (result) {
                    capturedItems.remove(item)

                    if (capturedItems.isEmpty()) {
                        Toast.makeText(context, R.string.empty_gallery, Toast.LENGTH_LONG)
                            .show()
                        onExit()
                    } else {
                        snackbarHostState.showOrReplaceSnackbar(
                            context.getString(R.string.deleted_successfully)
                        )
                    }
                } else {

                    snackbarHostState.showOrReplaceSnackbar(
                        context.getString(R.string.deleting_unexpected_error)
                    )
                }
            }
        },
        dismissHandler = {
            itemToBeDeleted = null
        }
    )

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(surface = Color.DarkGray)
    ) {
        Scaffold(
            containerColor = backgroundColor,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },

            topBar = {
                GalleryTopBar(
                    visible = !inFocusMode,
                    onCloseAction = {
                        onExit()
                    },
                    onEditAction = { chooseApp ->
                        coroutineScope.launch {
                            if (isSecureMode) {
                                snackbarHostState.showOrReplaceSnackbar(
                                    context.getString(R.string.edit_not_allowed)
                                )
                                return@launch
                            }

                            val currentItem = getCurrentMediaItem()

                            val editIntent = Intent(Intent.ACTION_EDIT).apply {
                                setDataAndType(currentItem.uri, currentItem.mimeType())
                                putExtra(Intent.EXTRA_STREAM, currentItem.uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                            }

                            if (chooseApp) {
                                val chooser = Intent.createChooser(
                                    editIntent,
                                    context.getString(R.string.edit_image)
                                ).apply {
                                    putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE, false)
                                }
                                context.startActivity(chooser)
                            } else {
                                try {
                                    context.startActivity(editIntent)
                                } catch (ignored: ActivityNotFoundException) {
                                    snackbarHostState.showOrReplaceSnackbar(
                                        context.getString(R.string.no_editor_app_error)
                                    )
                                }
                            }
                        }
                    },

                    onDeleteAction = {
                        itemToBeDeleted = getCurrentMediaItem()
                    },

                    onInfoAction = {
                        coroutineScope.launch {
                            if (capturedItems.isEmpty()) {
                                snackbarHostState.showOrReplaceSnackbar(
                                    context.getString(R.string.unable_to_obtain_file_details)
                                )
                                return@launch
                            }

                            val currentItem = getCurrentMediaItem()

                            try {
                                val currentItemInfo =
                                    MediaItemDetails.forCapturedItem(context, currentItem)
                                displayedMediaInfo.value = currentItemInfo
                            } catch (e: Exception) {
                                Log.i(TAG, "Unable to obtain file details for MediaInfoDialog")
                                e.printStackTrace()
                                snackbarHostState.showOrReplaceSnackbar(
                                    context.getString(R.string.unable_to_obtain_file_details)
                                )
                            }
                        }
                    },
                    onShareAction = {
                        coroutineScope.launch {
                            if (isSecureMode) {
                                snackbarHostState.showOrReplaceSnackbar(
                                    context.getString(R.string.sharing_not_allowed)
                                )
                                return@launch
                            }

                            val curItem = getCurrentMediaItem()

                            val share = Intent(Intent.ACTION_SEND)
                            share.putExtra(Intent.EXTRA_STREAM, curItem.uri)
                            share.setDataAndType(curItem.uri, curItem.mimeType())
                            share.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                            context.startActivity(
                                Intent.createChooser(
                                    share,
                                    context.getString(R.string.share_image)
                                )
                            )
                        }
                    },
                )
            },

            content = { innerPadding ->

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = !inFocusMode,
                    beyondBoundsPageCount = 1,
                    modifier = Modifier
                        .padding(
                            innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            // the fixed padding of 56.dp has been added to GalleryImage to
                            // avoid having a fixed black bar on the top (56dp comes from
                            // material guidelines)
                            0.dp,
                            innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                            innerPadding.calculateBottomPadding(),
                        )
                        .fillMaxSize()
                ) { page ->
                    val capturedItem = capturedItems[page]

                    var modifier: Modifier = Modifier

                    if (capturedItem.type == ITEM_TYPE_IMAGE) {
                        modifier = modifier.zoomable(zoomableState)
                    }

                    GalleryImage(
                        capturedItem = capturedItem,
                        modifier = modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (capturedItem.type == ITEM_TYPE_VIDEO) {
                                    val intent =
                                        Intent(context, VideoPlayer::class.java)
                                    intent.putExtra(
                                        VideoPlayer.VIDEO_URI,
                                        capturedItem.uri
                                    )
                                    intent.putExtra(
                                        VideoPlayer.IN_SECURE_MODE,
                                        isSecureMode
                                    )

                                    context.startActivity(intent)
                                } else {
                                    inFocusMode = !inFocusMode
                                }
                            }
                            .graphicsLayer {
                                val pageOffset = pagerState.getOffsetFractionForPage(page)
                                alpha = 1f - (pageOffset / .5f) * .7f
                            }
                    )

                    LaunchedEffect(zoomableState.zoomFraction) {
                        // inFocusMode is a state variable, therefore check value before updating it repeatedly
                        zoomableState.zoomFraction?.let { zoomFraction ->
                            if (zoomFraction >= 0.01f) {
                                if (!inFocusMode) {
                                    inFocusMode = true
                                }
                            } else {
                                if (inFocusMode) {
                                    inFocusMode = false
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}