package app.grapheneos.camera.ui.composable.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.grapheneos.camera.ui.theme.AppColor

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

    var dropDownMenuExpanded by remember {
        mutableStateOf(false)
    }

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
                IconButton(onClick = {
                    onEditAction(false)
                }) {
                    Icon(
                        Icons.Filled.Edit,
                        "Edit"
                    )
                }

                IconButton(onClick = onDeleteAction) {
                    Icon(Icons.Filled.Delete, null)
                }

                IconButton(onClick = onInfoAction) {
                    Icon(Icons.Filled.Info, null)
                }

                IconButton(onClick = onShareAction) {
                    Icon(Icons.Filled.Share, null)
                }

                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = {
                        dropDownMenuExpanded = !dropDownMenuExpanded
                    }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            "More Options"
                        )
                    }

                    DropdownMenu(
                        expanded = dropDownMenuExpanded,
                        onDismissRequest = {
                            dropDownMenuExpanded = false
                        },
                        modifier = Modifier.width(200.dp)
                    ) {
                        DropdownMenuItem(
                            contentPadding = PaddingValues(vertical = 0.dp, horizontal = 12.dp),
                            modifier = Modifier
                                .requiredSizeIn(maxHeight = 42.dp),

                            text = {
                                Text(
                                    text = "Edit With",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        .padding(0.dp)
                                )
                            },

                            onClick = {
                                onEditAction(true)
                                dropDownMenuExpanded = false
                            },
                        )
                    }
                }
            },
        )
    }
}