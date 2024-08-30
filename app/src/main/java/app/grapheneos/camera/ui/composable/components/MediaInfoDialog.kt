package app.grapheneos.camera.ui.composable.components

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore.MediaColumns
import android.provider.OpenableColumns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidxc.exifinterface.media.ExifInterface
import app.grapheneos.camera.CapturedItem
import app.grapheneos.camera.ITEM_TYPE_VIDEO
import app.grapheneos.camera.R
import app.grapheneos.camera.ui.theme.AppColor
import app.grapheneos.camera.util.storageLocationToUiString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MediaItemDetails private constructor(
    val filePath: String?,
    val fileName: String?,
    val size: String?,
    var dateAdded: String?,
    var dateModified: String?,
) {

    companion object {


        val Saver = Saver<MutableState<MediaItemDetails?>, Array<String?>>(
            save = {
                val item = it.value ?: return@Saver arrayOf()
                arrayOf(item.filePath, item.fileName, item.size, item.dateAdded, item.dateModified)
            },
            restore = {
                if (it.isEmpty()) return@Saver null
                mutableStateOf(MediaItemDetails(it[0], it[1], it[2], it[3], it[4]))
            },
        )

        fun forCapturedItem(context: Context, item: CapturedItem) : MediaItemDetails {

            var relativePath: String? = null
            var fileName: String? = null
            var size: String? = null

            var dateAdded: String? = null
            var dateModified: String? = null

            val projection = arrayOf(MediaColumns.RELATIVE_PATH, OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE)

            context.contentResolver.query(item.uri, projection, null,null)?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(1)

                    if (fileName == null) {
                        throw Exception("File name not found for file")
                    }

                    relativePath = getRelativePath(context, item.uri, it.getString(0), fileName!!)
                    size = String.format(Locale.ROOT, "%.2f MB", (it.getLong(2) / (1000f * 1000f)))
                }
            }

            if (item.type == ITEM_TYPE_VIDEO) {
                MediaMetadataRetriever().use {
                    it.setDataSource(context, item.uri)
                    dateAdded = convertTimeForVideo(it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)!!)
                    dateModified = dateAdded

                }
            } else {
                context.contentResolver.openInputStream(item.uri)?.use { stream ->
                    val eInterface = ExifInterface(stream)

                    val offset = eInterface.getAttribute(ExifInterface.TAG_OFFSET_TIME)

                    if (eInterface.hasAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)) {
                        dateAdded = convertTimeForPhoto(
                            eInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)!!,
                            offset
                        )
                    }

                    if (eInterface.hasAttribute(ExifInterface.TAG_DATETIME)) {
                        dateModified = convertTimeForPhoto(
                            eInterface.getAttribute(ExifInterface.TAG_DATETIME)!!,
                            offset
                        )
                    }
                }
            }

            return MediaItemDetails(
                relativePath,
                fileName,
                size,
                dateAdded,
                dateModified,
            )
        }

        @SuppressLint("SimpleDateFormat")
        fun convertTime(time: Long, showTimeZone: Boolean = true): String {
            val date = Date(time)
            val format = SimpleDateFormat(
                if (showTimeZone) {
                    "yyyy-MM-dd HH:mm:ss z"
                } else {
                    "yyyy-MM-dd HH:mm:ss"
                }
            )
            format.timeZone = TimeZone.getDefault()
            return format.format(date)
        }

        private fun convertTimeForVideo(time: String): String {
            val dateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsedDate = dateFormat.parse(time)
            return convertTime(parsedDate?.time ?: 0)
        }

        private fun convertTimeForPhoto(time: String, offset: String? = null): String {
            val timestamp = if (offset != null) {
                "$time $offset"
            } else {
                time
            }

            val dateFormat = SimpleDateFormat(
                if (offset == null) {
                    "yyyy:MM:dd HH:mm:ss"
                } else {
                    "yyyy:MM:dd HH:mm:ss Z"
                }, Locale.US
            )

            if (offset == null) {
                dateFormat.timeZone = TimeZone.getDefault()
            }
            val parsedDate = dateFormat.parse(timestamp)
            return convertTime(parsedDate?.time ?: 0, offset != null)
        }

        private fun getRelativePath(ctx: Context, uri: Uri, path: String?, fileName: String): String {
            if (path == null) {
                return storageLocationToUiString(ctx, uri.toString())
            }

            return "${ctx.getString(R.string.main_storage)}/$path$fileName"
        }
    }
}

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