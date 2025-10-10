package com.asphalt.commonui.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asphalt.commonui.constants.Constants

@Composable
fun CircularNetworkImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String? = null,
    size: Dp = Constants.AVATAR_DEFAULT_SIZE,
    borderWidth: Dp = Constants.AVATAR_DEFAULT_BORDER_WIDTH,
    borderColor: Color = Color.Transparent,
    placeholderPainter: Painter? = null,
    contentScale: ContentScale = ContentScale.Crop,
    onClick: (() -> Unit)? = null
) {
    val ctx = LocalContext.current
    Surface(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = CircleShape,
        color = Color.Transparent) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(BorderStroke(borderWidth, borderColor), CircleShape)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(ctx).data(imageUrl).crossfade(true).build(),
                placeholder = placeholderPainter,
                error = placeholderPainter,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        }
    }
}