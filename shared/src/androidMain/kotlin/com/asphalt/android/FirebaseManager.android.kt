package com.asphalt.android


import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot as AndroidNativeSnapshot
import com.google.firebase.database.DatabaseReference as NativeAndroidRef
import com.google.firebase.database.Transaction as AndroidNativeTransaction

class AndroidDatabaseReference(
    private val nativeRef: NativeAndroidRef
) : IDatabaseReference {

    override fun child(path: String): IDatabaseReference {

        val nativeChild = nativeRef.child(path)
        return AndroidDatabaseReference(nativeChild)
    }

    override fun runTransaction(updateFunction: (DataSnapshot) -> TransactionResult) {
        nativeRef.runTransaction(object : AndroidNativeTransaction.Handler {
            override fun doTransaction(mutableData: com.google.firebase.database.MutableData): AndroidNativeTransaction.Result {

                val commonSnapshot = DataSnapshot(mutableData.value, mutableData.key)

                val commonResult: TransactionResult = updateFunction(commonSnapshot)

                return if (commonResult.isSuccess) {
                    mutableData.value = commonResult.newData
                    AndroidNativeTransaction.success(mutableData)
                } else {
                    AndroidNativeTransaction.abort()
                }
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: AndroidNativeSnapshot?) {
            }
        })
    }

    override fun updateChildren(updates: Map<String, Any?>) {
        nativeRef.updateChildren(updates)
    }

}

actual class PlatformDatabase : IFirebaseDatabase {
    actual override fun getReference(path: String?): IDatabaseReference {
        val nativeRef = if (path == null) {
            FirebaseDatabase.getInstance().reference
        } else {
            FirebaseDatabase.getInstance().getReference(path)
        }
        return AndroidDatabaseReference(nativeRef)
    }
}

actual class FirebaseManager actual constructor() {
    actual fun getDatabase(): IFirebaseDatabase = PlatformDatabase()
}

actual class DataSnapshot(private val rawValue: Any?, actual val key: String? = null) {
    constructor(native: AndroidNativeSnapshot) : this(native.value, native.key)

    actual fun getValue(): Any? = rawValue
    actual fun exists(): Boolean = rawValue != null
}


actual class TransactionResult(
    val isSuccess: Boolean,
    val newData: Any? = null
) {
    actual companion object {
        actual fun success(value: Any?): TransactionResult = TransactionResult(true, value)
        actual fun abort(): TransactionResult = TransactionResult(false, null)
    }
}