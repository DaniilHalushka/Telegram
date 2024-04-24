package com.daniil.halushka.telegram.ui.screens.fragments.chat.singleChat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.data.models.UserModel
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.NODE_MESSAGES
import com.daniil.halushka.telegram.database.NODE_USERS
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.database.TYPE_TEXT
import com.daniil.halushka.telegram.database.getCommonModel
import com.daniil.halushka.telegram.database.getUserModel
import com.daniil.halushka.telegram.database.sendMessage
import com.daniil.halushka.telegram.databinding.FragmentSingleChatBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppChildEventListener
import com.daniil.halushka.telegram.util.AppValueEventListener
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        singleChatBinding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return singleChatBinding.root
    }

    override fun onResume() {
        super.onResume()
        moduleSwipeRefreshLayout = singleChatBinding.chatSwipeRefreshLayout
        initializeToolbar()
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        moduleRecyclerView = singleChatBinding.chatRecyclerView
        moduleAdapter = SingleChatAdapter()
        moduleRecyclerView.adapter = moduleAdapter
        moduleRefMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)

        moduleMessagesListener = AppChildEventListener { snapshot ->
            moduleAdapter.addItem(snapshot.getCommonModel(), moduleSmoothScrollToPosition) {
                if (moduleSmoothScrollToPosition) {
                    moduleRecyclerView.smoothScrollToPosition(moduleAdapter.itemCount)
                }
                moduleSwipeRefreshLayout.isRefreshing = false
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
                if (moduleIsScrolling && dy < 0) {
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