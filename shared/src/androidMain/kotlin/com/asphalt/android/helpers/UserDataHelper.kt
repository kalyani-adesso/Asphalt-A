package com.asphalt.android.helpers

import com.asphalt.android.model.UserDomain

class UserDataHelper {
    companion object{
        fun getUserDataFromCurrentList(usersList:List<UserDomain>, uid:String): UserDomain?{
            return usersList.find {
                it.uid == uid
            }
        }
    }
}