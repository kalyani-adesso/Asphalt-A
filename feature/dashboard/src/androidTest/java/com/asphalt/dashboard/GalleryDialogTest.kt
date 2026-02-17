package com.asphalt.dashboard

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.asphalt.dashboard.composables.screens.gallery.GalleryDialog
import com.asphalt.dashboard.data.GalleryModel
import org.junit.Rule
import org.junit.Test

class GalleryDialogTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun setContent(
        images: ArrayList<GalleryModel> = arrayListOf(),
        isShowUpload: Boolean = true,
        onDismiss: () -> Unit = {},
        onUpload: (ArrayList<GalleryModel>) -> Unit = {}
    ) {
        composeRule.setContent {
            GalleryDialog(
                images = images,
                isShowUpload = isShowUpload,
                onDismiss = onDismiss,
                onUpload = onUpload
            )
        }
    }

    @Test
    fun dialog_shows_title() {
        setContent()

        composeRule
            .onNodeWithTag("gallery_title")
            .assertIsDisplayed()
    }

    @Test
    fun empty_state_is_shown_when_no_images() {
        setContent(images = arrayListOf())

        composeRule
            .onNodeWithText("Upload Photos from Gallery")
            .assertIsDisplayed()
    }

    @Test
    fun cancel_button_calls_onDismiss() {
        var dismissed = false

        setContent(onDismiss = { dismissed = true })

        composeRule
            .onNodeWithTag("cancel_button")
            .performClick()

        assert(dismissed)
    }

    @Test
    fun select_photos_button_visible_when_no_images() {
        setContent(images = arrayListOf())

        composeRule
            .onNodeWithTag("upload_button")
            .assertIsDisplayed()
            .assert(hasText("SELECT PHOTOS"))
    }

    @Test
    fun upload_button_text_changes_when_images_exist() {
        val images = arrayListOf(
            GalleryModel(uri = "file://image1", isFromLocal = true)
        )

        setContent(images = images)

        composeRule
            .onNodeWithTag("upload_button")
            .assert(hasText("UPLOAD"))
    }

    @Test
    fun images_are_rendered_in_grid() {
        val images = arrayListOf(
            GalleryModel(uri = "file://image1", isFromLocal = true),
            GalleryModel(uri = "file://image2", isFromLocal = true)
        )

        setContent(images = images)

        composeRule
            .onAllNodesWithTag("gallery_image")
            .assertCountEquals(2)
    }

    @Test
    fun remove_image_removes_item_from_grid() {
        val images = arrayListOf(
            GalleryModel(uri = "file://image1", isFromLocal = true)
        )

        setContent(images = images)

        composeRule
            .onNodeWithTag("remove_image")
            .performClick()

        composeRule
            .onAllNodesWithTag("gallery_image")
            .assertCountEquals(0)
    }

    @Test
    fun upload_returns_selected_images() {
        val images = arrayListOf(
            GalleryModel(uri = "file://image1", isFromLocal = true)
        )

        var uploadedImages: ArrayList<GalleryModel>? = null

        setContent(
            images = images,
            onUpload = { image -> uploadedImages = image }
        )

        composeRule
            .onNodeWithTag("upload_button")
            .performClick()

        assert(uploadedImages?.size == 1)
        assert(uploadedImages?.first()?.uri == "file://image1")
    }
}