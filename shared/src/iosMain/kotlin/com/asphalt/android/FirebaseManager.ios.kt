@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
package com.asphalt.android

import cocoapods.FirebaseDatabase.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import platform.Foundation.numberWithLongLong
import cocoapods.FirebaseDatabase.FIRServerValue

@OptIn(ExperimentalForeignApi::class)
class IosDatabaseReference(
    private val nativeRef: FIRDatabaseReference
) : IDatabaseReference {

    override fun push(): IDatabaseReference {
        return IosDatabaseReference(nativeRef.childByAutoId())
    }

    override val key: String?
        get() = nativeRef.key

    override fun setValue(value: Any?) {
       val finalValue = when (value) {
            is Boolean -> value
            is Number -> value.toInt() == 1
            is String -> value.lowercase() == "true" || value == "1"
            else -> false
        }
        nativeRef.setValue(finalValue)
    }

    override fun child(path: String): IDatabaseReference {
        return IosDatabaseReference(nativeRef.child(path))
    }

    override fun updateChildren(updates: Map<String, Any?>) {
        nativeRef.updateChildValues(updates as Map<Any?, *>)
    }

    override fun runTransaction(updateFunction: (DataSnapshot) -> TransactionResult) {

        nativeRef.runTransactionBlock { mutableData ->

            val safeData = mutableData!!

            val snapshot = DataSnapshot(
                value = safeData.value,
                _key = safeData.key,
                nativeChildren = null
            )

            val result = updateFunction(snapshot)

            if (result.isSuccess) {
                safeData.value = result.newData
                FIRTransactionResult.successWithValue(safeData)
            } else {
                FIRTransactionResult.abort()
            }
        }
    }


    override fun observeValue(): Flow<DataSnapshot> = callbackFlow {

        val handle = nativeRef.observeEventType(
            FIRDataEventType.FIRDataEventTypeValue
        ) { snapshot ->

            trySend(DataSnapshot(snapshot))
        }

        awaitClose {
            nativeRef.removeObserverWithHandle(handle)
        }
    }



}
actual class PlatformDatabase : IFirebaseDatabase {

    @OptIn(ExperimentalForeignApi::class)
    actual override fun getReference(path: String?): IDatabaseReference {

        val nativeRef = if (path == null) {
            FIRDatabase.database().reference()
        } else {
            FIRDatabase.database().referenceWithPath(path)
        }

        return IosDatabaseReference(nativeRef)
    }
}

actual class DataSnapshot constructor(
    private val value: Any?,
    private val _key: String?,
    private val nativeChildren: List<FIRDataSnapshot>? = null
) {

    constructor(native: FIRDataSnapshot?) : this(
        value = native?.value,
        _key = native?.key,
        nativeChildren = buildList {
            val children = native?.children
            while (true) {
                val next = children?.nextObject() as? FIRDataSnapshot ?: break
                add(next)
            }
        }
    )

    actual fun getValue(): Any? = value

    actual val key: String?
        get() = _key

    actual val children: List<DataSnapshot>
        get() = nativeChildren?.map { DataSnapshot(it) } ?: emptyList()
}
actual class TransactionResult(
    val isSuccess: Boolean,
    val newData: Any? = null
) {
    actual companion object {
        actual fun success(value: Any?): TransactionResult =
            TransactionResult(true, value)

        actual fun abort(): TransactionResult =
            TransactionResult(false, null)
    }
}


actual object FirebaseServerValue {
    actual val TIMESTAMP: Any
        get() = FIRServerValue.timestamp()  // Returns NSDictionary, fine

    actual fun increment(value: Int): Any {
        // Use NSNumber factory
        return FIRServerValue.increment(NSNumber.numberWithLongLong(value.toLong()))
    }
}



