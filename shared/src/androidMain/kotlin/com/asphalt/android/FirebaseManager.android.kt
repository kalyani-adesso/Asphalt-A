package com.asphalt.android


import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.google.firebase.database.DataSnapshot as AndroidNativeSnapshot
import com.google.firebase.database.DatabaseReference as NativeAndroidRef
import com.google.firebase.database.Transaction as AndroidNativeTransaction

class AndroidDatabaseReference(
    private val nativeRef: NativeAndroidRef
) : IDatabaseReference {
    override fun push(): IDatabaseReference {
        return AndroidDatabaseReference(nativeRef.push())
    }

    override val key: String?
        get() = nativeRef.key

    override fun setValue(value: Any?) {
        nativeRef.setValue(value)
    }

    override fun child(path: String): IDatabaseReference {

        val nativeChild = nativeRef.child(path)
        return AndroidDatabaseReference(nativeChild)
    }
    override fun runTransaction(updateFunction: (DataSnapshot) -> TransactionResult) {
        nativeRef.runTransaction(object : com.google.firebase.database.Transaction.Handler {
            override fun doTransaction(mutableData: com.google.firebase.database.MutableData): com.google.firebase.database.Transaction.Result {

                val commonSnapshot = DataSnapshot(mutableData.value, mutableData.key)

                val commonResult = updateFunction(commonSnapshot)

                return if (commonResult.isSuccess) {
                    mutableData.value = commonResult.newData
                    com.google.firebase.database.Transaction.success(mutableData)
                } else {
                    com.google.firebase.database.Transaction.abort()
                }
            }

            override fun onComplete(error: com.google.firebase.database.DatabaseError?, committed: Boolean, currentData: com.google.firebase.database.DataSnapshot?) {
                // Transaction finished
            }
        })
    }

    override fun updateChildren(updates: Map<String, Any?>) {
        nativeRef.updateChildren(updates)
    }

    override fun observeValue(): Flow<DataSnapshot> = callbackFlow {
        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {

                trySend(DataSnapshot(snapshot))
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                close(error.toException())
            }
        }

        nativeRef.addValueEventListener(listener)

        awaitClose { nativeRef.removeEventListener(listener) }
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



actual class DataSnapshot(
    private val value: Any?,
    private val _key: String?,
    private val nativeChildren: Iterable<com.google.firebase.database.DataSnapshot>? = null
) {
    constructor(native: com.google.firebase.database.DataSnapshot) : this(
        value = native.value,
        _key = native.key,
        nativeChildren = native.children
    )

    actual fun getValue(): Any? = value
    actual val key: String? get() = _key

    actual val children: List<DataSnapshot>
        get() = nativeChildren?.map { DataSnapshot(it) } ?: emptyList()
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

actual object FirebaseServerValue {
    actual val TIMESTAMP: Any = ServerValue.TIMESTAMP
}