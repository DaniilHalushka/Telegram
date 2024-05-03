package com.daniil.halushka.telegram.ui.screens.fragments.chat.singleChat

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.data.models.UserModel
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.FOLDER_MESSAGE_IMAGE
import com.daniil.halushka.telegram.database.NODE_MESSAGES
import com.daniil.halushka.telegram.database.NODE_USERS
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.database.REF_STORAGE_ROOT
import com.daniil.halushka.telegram.database.TYPE_TEXT
import com.daniil.halushka.telegram.database.getCommonModel
import com.daniil.halushka.telegram.database.getUrlFromStorage
import com.daniil.halushka.telegram.database.getUserModel
import com.daniil.halushka.telegram.database.putImageToStorage
import com.daniil.halushka.telegram.database.sendMessage
import com.daniil.halushka.telegram.database.sendMessageAsImage
import com.daniil.halushka.telegram.databinding.FragmentSingleChatBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppChildEventListener
import com.daniil.halushka.telegram.util.AppTextWatcher
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.RECORD_AUDIO
import com.daniil.halushka.telegram.util.checkPermission
import com.daniil.halushka.telegram.util.downloadAndSetImage
import com.daniil.halushka.telegram.util.showToast
import com.google.firebase.database.DatabaseReference

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var moduleListenerInfoToolbar: AppValueEventListener
    private lateinit var moduleReceivingUser: UserModel
    private lateinit var moduleToolbarInfo: View
    private lateinit var singleChatBinding: FragmentSingleChatBinding
    private lateinit var moduleRefUsers: DatabaseReference
    private lateinit var moduleRefMessages: DatabaseReference
    private lateinit var moduleAdapter: SingleChatAdapter
    private lateinit var moduleRecyclerView: RecyclerView
    private lateinit var moduleMessagesListener: AppChildEventListener
    private var moduleCountMessages = 10
    private var moduleIsScrolling = false
    private var moduleSmoothScrollToPosition = true
    private lateinit var moduleSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var moduleLayoutManager: LinearLayoutManager
    private val customCropImage = registerForActivityResult(CropImageContract()) {
        if (it !is CropImage.CancelledResult) {
            handleCropImageResult(it.uriContent.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        singleChatBinding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return singleChatBinding.root
    }

    override fun onResume() {
        super.onResume()
        initializeFields()
        initializeToolbar()
        initializeRecyclerView()
    }

    private fun initializeFields() {
        moduleSwipeRefreshLayout = singleChatBinding.chatSwipeRefreshLayout
        moduleLayoutManager = LinearLayoutManager(this.context)
        singleChatBinding.chatInputMessage.addTextChangedListener(AppTextWatcher {
            val text = singleChatBinding.chatInputMessage.text.toString()
            if (text.isEmpty()) {
                singleChatBinding.sendMessageButton.visibility = View.GONE
                singleChatBinding.attachButton.visibility = View.VISIBLE
            } else {
                singleChatBinding.sendMessageButton.visibility = View.VISIBLE
                singleChatBinding.attachButton.visibility = View.GONE
            }
        })

        singleChatBinding.attachButton.setOnClickListener { attachFile() }

        singleChatBinding.voiceMessageButton.setOnTouchListener { view, event ->
            if (checkPermission(RECORD_AUDIO)) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    //TODO record
                } else if (event.action == MotionEvent.ACTION_UP){
                    //TODO stop record
                }
            }
            true
        }
    }

    private fun attachFile() {
        fun startCameraWithoutUri(includeCamera: Boolean, includeGallery: Boolean) {
            customCropImage.launch(
                CropImageContractOptions(
                    uri = null,
                    cropImageOptions = CropImageOptions(
                        imageSourceIncludeCamera = includeCamera,
                        imageSourceIncludeGallery = includeGallery,
                    ),
                ),
            )
        }
        startCameraWithoutUri(includeCamera = true, includeGallery = true)
    }

    private fun handleCropImageResult(uri: String) {
        val uriLocal = Uri.parse(uri.replace("file:", ""))
        val messageKey = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id).push().key.toString()
        val path = REF_STORAGE_ROOT.child(FOLDER_MESSAGE_IMAGE)
            .child(messageKey)
        putImageToStorage(uriLocal, path) {
            getUrlFromStorage(path) { url ->
                sendMessageAsImage(contact.id, url, messageKey)
                moduleSmoothScrollToPosition = true
            }
        }
    }

    private fun initializeRecyclerView() {
        moduleRecyclerView = singleChatBinding.chatRecyclerView
        moduleAdapter = SingleChatAdapter()
        moduleRecyclerView.adapter = moduleAdapter
        moduleRecyclerView.setHasFixedSize(true)
        moduleRecyclerView.isNestedScrollingEnabled = false
        moduleRecyclerView.layoutManager = moduleLayoutManager
        moduleRefMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)

        moduleMessagesListener = AppChildEventListener { snapshot ->
            val message = snapshot.getCommonModel()
            if (moduleSmoothScrollToPosition) {
                moduleAdapter.addItemToBottom(message) {
                    moduleRecyclerView.smoothScrollToPosition(moduleAdapter.itemCount)
                }
            } else {
                moduleAdapter.addItemToTop(message) {
                    moduleSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        moduleRefMessages.limitToLast(moduleCountMessages)
            .addChildEventListener(moduleMessagesListener)

        moduleRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    moduleIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (moduleIsScrolling && dy < 0 && moduleLayoutManager.findFirstVisibleItemPosition() <= 3) {
                    updateData()
                }
            }
        })

        moduleSwipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        moduleSmoothScrollToPosition = false
        moduleIsScrolling = false
        moduleCountMessages += 10
        moduleRefMessages.removeEventListener(moduleMessagesListener)
        moduleRefMessages.limitToLast(moduleCountMessages)
            .addChildEventListener(moduleMessagesListener)

    }

    private fun initializeToolbar() {
        moduleToolbarInfo =
            APP_ACTIVITY.moduleToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info)
        moduleToolbarInfo.visibility = View.VISIBLE
        moduleListenerInfoToolbar = AppValueEventListener {
            moduleReceivingUser = it.getUserModel()
            initializeInfoToolbar()
        }

        moduleRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        moduleRefUsers.addValueEventListener(moduleListenerInfoToolbar)

        singleChatBinding.sendMessageButton.setOnClickListener {
            moduleSmoothScrollToPosition = true
            val message = singleChatBinding.chatInputMessage.text.toString()
            if (message.isEmpty()) {
                showToast(getString(R.string.enter_message))
            } else sendMessage(message, contact.id, TYPE_TEXT) {
                singleChatBinding.chatInputMessage.setText("")
            }
        }
    }

    private fun initializeInfoToolbar() {
        if (moduleReceivingUser.fullname.isEmpty()) {
            moduleToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text =
                contact.fullname
        } else moduleToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text =
            moduleReceivingUser.fullname

        moduleToolbarInfo.findViewById<ImageView>(R.id.toolbar_chat_image)
            .downloadAndSetImage(moduleReceivingUser.photoURL)

        moduleToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_status).text =
            moduleReceivingUser.state
    }

    override fun onPause() {
        super.onPause()
        moduleToolbarInfo.visibility = View.GONE
        moduleRefUsers.removeEventListener(moduleListenerInfoToolbar)
        moduleRefMessages.removeEventListener(moduleMessagesListener)
    }
}