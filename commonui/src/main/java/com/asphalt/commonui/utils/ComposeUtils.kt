package com.asphalt.commonui.utils

import android.annotation.SuppressLint
import androidx.annotation.Px
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenDark
import com.asphalt.commonui.theme.LightGray28
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralGrey
import com.asphalt.commonui.theme.NeutralGrey30
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralMidGrey
import com.asphalt.commonui.theme.NeutralRed
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.NeutralWhite25
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.RoundedBox

object ComposeUtils {
    @Composable
    fun getScreenHeight(): Int {
        val windowInfo = LocalWindowInfo.current
        val screenHeight = windowInfo.containerSize.height
        return screenHeight
    }

    @Composable
    fun getScreenWidth(): Int {
        val windowInfo = LocalWindowInfo.current
        val screenWidth = windowInfo.containerSize.width
        return screenWidth
    }
    @Composable
    fun Float.toDp(): Dp{
        val density = LocalDensity.current.density
        return  (this / density).dp
    }

    @Composable
    fun getDpForScreenRatio(percentage: Float, screenSize: Int): Dp {

        val density = LocalResources.current.displayMetrics.density
        return ((screenSize * percentage) / density).dp
    }

    @Composable
    fun DefaultButtonContent(buttonText: String, color: Color = NeutralWhite) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            Text(
                buttonText,
                color = color,
                fontSize = Dimensions.textSize18,
                style = TypographyBold.labelLarge,
            )
        }
    }

    @Composable
    fun DefaultColumnRoot(
        topPadding: Dp,
        bottomPadding: Dp,
        isScrollable: Boolean = true,
        content: @Composable ColumnScope.() -> Unit
    ) {
        val modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(
                top = topPadding,
                bottom = bottomPadding,
                start = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                end = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING
            )
        Column(
            modifier = if (isScrollable) modifier
                .verticalScroll(rememberScrollState()) else modifier,
            verticalArrangement = Arrangement.spacedBy(Dimensions.size20, Alignment.Top)
        ) {
            content()
        }
    }

    @Composable
    fun CommonContentBox(
        modifier: Modifier = Modifier,
        isBordered: Boolean = false,
        radius: Dp = Dimensions.size10,
        content: @Composable BoxScope.() -> Unit
    ) {
        RoundedBox(
            modifier = modifier,
            borderColor = if (isBordered) NeutralGrey30 else null,
            backgroundColor = NeutralLightPaper,
            cornerRadius = radius,
            borderStroke = if (isBordered) Constants.DEFAULT_BORDER_STROKE else null,
        ) {
            content()
        }
    }

    @Composable
    fun ColorIconRounded(
        modifier: Modifier = Modifier,
        backColor: Color,
        size: Dp = Dimensions.size30,
        radius: Dp = Dimensions.size5,
        resId: Int,
        tint: Color = NeutralWhite
    ) {
        RoundedBox(
            backgroundColor = backColor,
            modifier = modifier
                .width(size)
                .height(size),
            cornerRadius = radius,
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(resId), null, tint = tint)
        }
    }

    @Composable
    fun RoundedIconWithHeaderComponent(
        iconColor: Color,
        iconRes: Int,
        title: String,
        subtitle: String,
        @SuppressLint("ModifierParameter") titleModifier: Modifier = Modifier,
        subtitleModifier: Modifier = Modifier,
        titleColor: Color = NeutralBlack,
        subtitleColor: Color = NeutralDarkGrey
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                Dimensions.spacing10
            )
        ) {
            ColorIconRounded(backColor = iconColor, resId = iconRes)
            Column(verticalArrangement = Arrangement.spacedBy(Dimensions.size3)) {
                SectionTitle(title, titleModifier, color = titleColor)
                SectionSubtitle(subtitle, subtitleModifier, color = subtitleColor)
            }
        }
    }

    @Composable
    fun SectionTitle(text: String, modifier: Modifier = Modifier, color: Color = NeutralBlack) {
        Text(
            style = TypographyBold.bodyMedium,
            text = text, modifier = modifier, color = color
        )
    }

    @Composable
    fun SectionSubtitle(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = NeutralDarkGrey,
    ) {
        Text(
            text = text, style = Typography.bodyMedium,
            fontSize = Dimensions.textSize12,
            color = color, modifier = modifier
        )
    }

    @Composable
    fun CustomTextField(
        value: String,
        onValueChanged: (String) -> Unit,
        placeHolderText: String,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        backColor: Color = NeutralWhite,
        borderColor: Color? = null,
        borderStroke: Dp? = null,
        isSingleLine: Boolean = true,
        heightMin: Dp = Dimensions.size50,
        textStyle: TextStyle = Typography.bodySmall,
        placeHolderTextStyle: TextStyle = TypographyMedium.bodySmall,
        readOnly: Boolean = false

    ) {
        RoundedBox(
            modifier = Modifier
                .heightIn(min = heightMin, max = Dimensions.padding100),
            cornerRadius = Dimensions.size10,
            backgroundColor = backColor,
            borderColor = borderColor,
            borderStroke = borderStroke
        ) {
            TextField(
                readOnly = readOnly,
                keyboardActions = keyboardActions,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                value = value,
                onValueChange = { onValueChanged(it) },
                placeholder = {
                    Text(
                        placeHolderText,
                        style = placeHolderTextStyle,
                        color = NeutralDarkGrey
                    )
                },

                textStyle = textStyle,

                singleLine = isSingleLine,
                keyboardOptions = keyboardOptions,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
            )
        }
    }

    @Composable
    fun ArrowView(
        modifier: Modifier = Modifier,
        backColor: Color = NeutralLightPaper,
        boxContent: @Composable BoxScope.() -> Unit
    ) {
        RoundedBox(
            contentAlignment = Alignment.Center,
            modifier = modifier.size(Dimensions.size32),
            backgroundColor = backColor, cornerRadius = Dimensions.size8
        ) {
            Box(
                modifier = Modifier.arrowPadding()
            ) {
                boxContent()
            }
        }
    }

    @Composable
    fun TexFieldError(error: Boolean, errorText: String) {
        if (error)
            Text(
                text = errorText,
                Modifier.padding(top = Dimensions.size4),
                style = Typography.bodySmall,
                color = NeutralRed
            )
    }

    @Composable
    fun HeaderWithInputField(
        title: String,
        value: String,
        onValueChanged: (String) -> Unit,
        placeholder: String,
        isError: Boolean,
        errorText: String,
        keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        isSingleLine: Boolean = true,
        readOnly: Boolean = false,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)) {
            SectionTitle(title)
            CustomTextField(
                value,
                { onValueChanged(it) },
                placeHolderText = placeholder,
                keyboardOptions = keyboardOptions,
                isSingleLine = isSingleLine,
                readOnly = readOnly,
                backColor = if (readOnly) LightGray28 else NeutralWhite

            )
            TexFieldError(
                isError,
                errorText
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomSwitch(switchValue: Boolean, onSwitch: (Boolean) -> Unit) {

        Switch(
            thumbContent = {
                RoundedBox(
                    modifier = Modifier.size(Dimensions.padding21),
                    cornerRadius = Dimensions.padding21
                ) {
                }
            },
            checked = switchValue,
            onCheckedChange = { onSwitch(it) },

            modifier = Modifier
                .width(Dimensions.size38)
                .height(Dimensions.padding24),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = GreenDark,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = NeutralGrey,
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent
            )
        )
    }

    private fun Modifier.arrowPadding(): Modifier = this.then(
        Modifier.padding(
            start = Dimensions.padding13,
            end = Dimensions.padding9pt25,
        )
    )
}

