package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentSettingsBinding
import com.daniil.halushka.telegram.ui.screens.activities.authorization.AuthorizationActivity
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AUTH
import com.daniil.halushka.telegram.util.CURRENT_UID
import com.daniil.halushka.telegram.util.FOLDER_PROFILE_IMAGE
import com.daniil.halushka.telegram.util.REF_STORAGE_ROOT
import com.daniil.halushka.telegram.util.USER
import com.daniil.halushka.telegram.util.getUrlFromStorage
import com.daniil.halushka.telegram.util.putImageToStorage
import com.daniil.halushka.telegram.util.putUrlToDB
import com.daniil.halushka.telegram.util.replaceActivity
import com.daniil.halushka.telegram.util.replaceParentFragment
import com.daniil.halushka.telegram.util.showFragmentToast

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private lateinit var settingsBinding: FragmentSettingsBinding
    private val customCropImage = registerForActivityResult(CropImageContract()) {
        if (it !is CropImage.CancelledResult) {
            handleCropImageResult(it.uriContent.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return settingsBinding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initializeFields()
    }

    private fun initializeFields() {
        settingsBinding.settingsFullName.text = USER.fullname
        settingsBinding.settingsStatus.text = USER.status
        settingsBinding.settingsPhoneNumber.text = USER.phone
        settingsBinding.settingsUsername.text = USER.username
        settingsBinding.settingsInformation.text = USER.information
        settingsBinding.settingsButtonChangeUsername.setOnClickListener {
            replaceParentFragment(ChangeUsernameFragment())
        }
        settingsBinding.settingsButtonChangeInformation.setOnClickListener {
            replaceParentFragment(ChangeInformationFragment())
        }
        settingsBinding.settingsChangeUserAvatar.setOnClickListener {
            changeUserAvatar()
        }
    }

    private fun changeUserAvatar() {
        fun startCameraWithoutUri(includeCamera: Boolean, includeGallery: Boolean) {
            customCropImage.launch(
                CropImageContractOptions(
                    uri = null,
                    cropImageOptions = CropImageOptions(
                        imageSourceIncludeCamera = includeCamera,
                        imageSourceIncludeGallery = includeGallery,
                        cropShape = CropImageView.CropShape.OVAL,
                        maxCropResultHeight = 600,
                        maxCropResultWidth = 600
                    ),
                ),
            )
        }
        startCameraWithoutUri(includeCamera = true, includeGallery = true)
    }

    private fun handleCropImageResult(uri: String) {
        val uriLocal = Uri.parse(uri.replace("file:", ""))
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
            .child(CURRENT_UID)
        putImageToStorage(uriLocal, path) {
            getUrlFromStorage(path) {
                putUrlToDB(it) {
                    //settingsBinding.settingsUserPhoto.downloadAndSetImage(it)
                    showFragmentToast(getString(R.string.toast_details_update))
                    USER.photoUrl = it
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                (APP_ACTIVITY).replaceActivity(AuthorizationActivity())
            }

            R.id.settings_menu_change_name -> replaceParentFragment(ChangeNameFragment())
        }
        return true
    }
}