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
import com.daniil.halushka.telegram.database.AUTH
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.FOLDER_PROFILE_IMAGE
import com.daniil.halushka.telegram.database.REF_STORAGE_ROOT
import com.daniil.halushka.telegram.database.USER
import com.daniil.halushka.telegram.database.getUrlFromStorage
import com.daniil.halushka.telegram.database.putImageToStorage
import com.daniil.halushka.telegram.database.putUrlToDB
import com.daniil.halushka.telegram.databinding.FragmentSettingsBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppStates
import com.daniil.halushka.telegram.util.downloadAndSetImage
import com.daniil.halushka.telegram.util.replaceFragment
import com.daniil.halushka.telegram.util.restartActivity
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
        settingsBinding.settingsStatus.text = USER.state
        settingsBinding.settingsPhoneNumber.text = USER.phone
        settingsBinding.settingsUsername.text = USER.username
        settingsBinding.settingsInformation.text = USER.information
        settingsBinding.settingsButtonChangeUsername.setOnClickListener {
            replaceFragment(ChangeUsernameFragment(), R.id.main_data_container)
        }
        settingsBinding.settingsButtonChangeInformation.setOnClickListener {
            replaceFragment(ChangeInformationFragment(), R.id.main_data_container)
        }
        settingsBinding.settingsChangeUserAvatar.setOnClickListener {
            changeUserAvatar()
        }
        settingsBinding.settingsUserPhoto.downloadAndSetImage(USER.photoURL)
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
            getUrlFromStorage(path) { url ->
                putUrlToDB(url) {
                    settingsBinding.settingsUserPhoto.downloadAndSetImage(url)
                    showFragmentToast(getString(R.string.toast_details_update))
                    USER.photoURL = url
                    APP_ACTIVITY.moduleAppDrawer.updateHeader()
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
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }

            R.id.settings_menu_change_name -> replaceFragment(
                ChangeNameFragment(),
                R.id.main_data_container
            )
        }
        return true
    }
}