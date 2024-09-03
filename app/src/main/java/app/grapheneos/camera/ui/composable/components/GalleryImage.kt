package app.grapheneos.camera.ui.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.grapheneos.camera.CapturedItem
import app.grapheneos.camera.ITEM_TYPE_VIDEO
import app.grapheneos.camera.R
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.VideoFrameDecoder

private const val TAG = "GalleryImage"

@Composable
fun GalleryImage(
    capturedItem: CapturedItem,
    modifier: Modifier,
) {

    val imageLoaderBuilder = ImageLoader.Builder(LocalContext.current)

    if (capturedItem.type == ITEM_TYPE_VIDEO) {
        imageLoaderBuilder.components { add(VideoFrameDecoder.Factory()) }
    }

    val imageLoader = imageLoaderBuilder.build()

    SubcomposeAsyncImage(
        model = capturedItem.uri,
        imageLoader = imageLoader,
        modifier = Modifier
            .then(modifier)
//            .padding(top = 56.dp)
            .fillMaxSize(),
        alignment = Alignment.Center,
        contentDescription = "Video Thumbnail",
    ) {
        val state = painter.state

        if (state is AsyncImagePainter.State.Loading) {
            Text(
                text = "…",
                color = Color.White,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically),
            )
        } else if (state is AsyncImagePainter.State.Error || state is AsyncImagePainter.State.Empty) {
            Text(
                text = "Unable to load media ${capturedItem.uiName()}",
                color = Color.Gray,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        } else if (state is AsyncImagePainter.State.Success) {
            SubcomposeAsyncImageContent()

            if (capturedItem.type == ITEM_TYPE_VIDEO) {
                Icon(
                    painter = painterResource(R.drawable.play),
                    tint = Color.White,
                    contentDescription = "Play Video",
                    modifier = Modifier
                        .background(
                            color = Color(0x99000000),
                            shape = CircleShape
                        )
                        .border(
                            width = 0.5.dp,
                            color = Color(0x50ffffff),
                            shape = CircleShape
                        )
                        .requiredSize(56.dp)
                        .padding(12.dp)
                        .align(Alignment.Center)
                )
            }

        }
    }
}