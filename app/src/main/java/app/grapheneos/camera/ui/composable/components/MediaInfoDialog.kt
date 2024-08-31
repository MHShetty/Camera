package app.grapheneos.camera.ui.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

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
import app.grapheneos.camera.R
import app.grapheneos.camera.ui.composable.data.MediaItemDetails
import app.grapheneos.camera.ui.theme.AppColor

@Composable
fun MediaInfoDialog(
    mediaItemDetails: MediaItemDetails?,
    onFinish : () -> Unit,
) {
    if (mediaItemDetails != null) {
        Dialog(
            onDismissRequest = onFinish,

            content = @Composable {
                ProvideTextStyle(value = TextStyle(
                    color = Color(0xFFEEEEEE)
                )
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(24.dp),
                                color = AppColor.BackgroundColor,
                            )
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                top = 20.dp,
                                bottom = 8.dp
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.file_details),
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                        )

                        Box (
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                fontSize = 16.sp,
                                text = """
                                ${stringResource(R.string.file_name_generic)}
                                ${mediaItemDetails.fileName ?: stringResource(id = R.string.not_found_generic)}
                                
                                ${stringResource(R.string.file_path)}
                                ${mediaItemDetails.filePath ?: stringResource(id = R.string.not_found_generic)}
                                
                                ${stringResource(R.string.file_size)}
                                ${mediaItemDetails.size ?: stringResource(id = R.string.not_found_generic)}
                                
                                ${stringResource(R.string.file_created_on)}
                                ${mediaItemDetails.dateAdded ?: stringResource(id = R.string.not_found_generic)}
                                
                                ${stringResource(R.string.last_modified_on)}
                                ${mediaItemDetails.dateModified ?: stringResource(id = R.string.not_found_generic)}                        
                                """.trimIndent(),
                            )
                        }

                        TextButton(
                            onClick = onFinish,
                            modifier = Modifier
                                .align(alignment = Alignment.End)
                        ) {
                            Text(text = "OK")
                        }



                    }
                }
            }
        )
    }
}