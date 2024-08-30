package app.grapheneos.camera.ui.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.grapheneos.camera.CapturedItem
import app.grapheneos.camera.R
import app.grapheneos.camera.ui.theme.AppColor

@Composable
fun FileDeletionDialog(
    deletionItem : CapturedItem?,
    onDeleteAction: (item: CapturedItem) -> Unit,
    onDismissAction: () -> Unit,

    ) {
    if (deletionItem != null) {
        Dialog(
            content = {
                ProvideTextStyle(
                    value = TextStyle(color = Color(0xFFEEEEEE))
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .background(
                                shape = RoundedCornerShape(24.dp),
                                color = AppColor.BackgroundColor
                            )
                            .padding(
                                start = 24.dp,
                                end = 12.dp,
                                top = 20.dp,
                                bottom = 10.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete_title),
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            fontSize = 20.sp,
                        )

                        Text(
                            modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
                            fontSize = 16.sp,
                            text = stringResource(
                                id = R.string.delete_description,
                                deletionItem.uiName()
                            )
                        )

                        Row (
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            TextButton(
                                onClick = onDismissAction) {
                                Text("Cancel")
                            }

                            TextButton(onClick = {
                                onDeleteAction(deletionItem)
                            }) {
                                Text("Delete")
                            }


                        }
                    }
                }
            },

            onDismissRequest = onDismissAction
        )
    }
}