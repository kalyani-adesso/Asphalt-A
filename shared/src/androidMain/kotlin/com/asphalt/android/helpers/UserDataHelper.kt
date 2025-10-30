package com.asphalt.android.helpers

import com.asphalt.android.model.UserData

class UserDataHelper {
    companion object{
        fun getUserDataFromCurrentList(usersList:List<UserData>,uid:String): UserData?{
            return usersList.find {
                it.uid == uid
            }
        }
    }
}