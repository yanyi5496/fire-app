package com.yanyi.fire.data

import androidx.lifecycle.LiveData
import com.yanyi.fire.data.model.LoggedInUser
import com.yanyi.fire.net.RespLiveData
import com.yanyi.fire.ui.login.LoginResult

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }


    fun login(account: String, password: String, livaData: RespLiveData<LoginResult>) {
        // handle login

        dataSource.login(account, password, livaData)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}