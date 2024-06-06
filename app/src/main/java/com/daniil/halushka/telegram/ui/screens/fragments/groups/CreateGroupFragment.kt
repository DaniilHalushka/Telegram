package com.daniil.halushka.telegram.ui.screens.fragments.groups

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.database.createGroupInDatabase
import com.daniil.halushka.telegram.databinding.FragmentCreateGroupBinding
import com.daniil.halushka.telegram.ui.screens.fragments.base.BaseFragment
import com.daniil.halushka.telegram.ui.screens.fragments.mainList.MainListFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.getPlurals
import com.daniil.halushka.telegram.util.hideKeyboard
import com.daniil.halushka.telegram.util.replaceFragment
import com.daniil.halushka.telegram.util.showToast

class CreateGroupFragment(private var listContacts: List<CommonModel>) :
    BaseFragment(R.layout.fragment_create_group) {

    private lateinit var createGroupBinding: FragmentCreateGroupBinding

    private lateinit var moduleRecyclerView: RecyclerView
    private lateinit var moduleAdapter: AddContactsAdapter
    private var moduleUri = Uri.EMPTY

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
        createGroupBinding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return createGroupBinding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initializeRecyclerView()
        createGroupBinding.createGroupPhoto.setOnClickListener {
            addPhoto()
        }
        createGroupBinding.createGroupButtonDone.setOnClickListener {
            val nameGroup = createGroupBinding.createGroupInputName.text.toString()
            if (nameGroup.isEmpty()) showToast(getString(R.string.enter_name_of_group))
            else {
                createGroupInDatabase(nameGroup, moduleUri, listContacts) {
                    replaceFragment(MainListFragment(), R.id.main_data_container)
                }
            }
        }
        createGroupBinding.createGroupInputName.requestFocus()
        createGroupBinding.createGroupCounts.text = getPlurals(listContacts.size)
    }

    private fun addPhoto() {
        fun startCameraWithoutUri(includeCamera: Boolean, includeGallery: Boolean) {
            customCropImage.launch(
                CropImageContractOptions(
                    uri = null,
                    cropImageOptions = CropImageOptions(
                        imageSourceIncludeCamera = includeCamera,
                        imageSourceIncludeGallery = includeGallery,
                        cropShape = CropImageView.CropShape.OVAL
                    ),
                ),
            )
        }
        startCameraWithoutUri(includeCamera = true, includeGallery = true)
    }


    private fun handleCropImageResult(uri: String) {
        moduleUri = Uri.parse(uri.replace("file:", ""))
        createGroupBinding.createGroupPhoto.setImageURI(moduleUri)
    }

    private fun initializeRecyclerView() {
        moduleRecyclerView = createGroupBinding.createGroupRecyclerView
        moduleAdapter = AddContactsAdapter()

        moduleRecyclerView.adapter = moduleAdapter

        listContacts.forEach {
            moduleAdapter.updateListItems(it)
        }
    }
}