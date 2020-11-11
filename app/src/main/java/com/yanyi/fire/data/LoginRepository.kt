package com.yanyi.fire.data

import com.yanyi.fire.data.model.LoggedInUser
import com.yanyi.fire.util.RespLiveData
import com.yanyi.fire.ui.login.LoginResult

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    init {
        user = null
    }


    fun login(account: String, password: String, livaData: RespLiveData<LoginResult>) {
        dataSource.login(account, password, livaData)
    }

    fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}