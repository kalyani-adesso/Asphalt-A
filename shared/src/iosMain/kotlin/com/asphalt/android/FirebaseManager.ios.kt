package com.asphalt.android

import kotlinx.coroutines.flow.Flow
import cocoapods.FirebaseDatabase.FIRDatabase
import kotlinx.cinterop.ExperimentalForeignApi


class IosDatabaseReference(
    private val nativeRef: IDatabaseReference
) : IDatabaseReference {
    override fun push(): IDatabaseReference {
        TODO("Not yet implemented")
    }

    override val key: String?
        get() = TODO("Not yet implemented")

    override fun setValue(value: Any?) {
        TODO("Not yet implemented")
    }

    override fun child(path: String): IDatabaseReference {
        TODO("Not yet implemented")
    }

    override fun runTransaction(updateFunction: (DataSnapshot) -> TransactionResult) {
        TODO("Not yet implemented")
    }

    override fun updateChildren(updates: Map<String, Any?>) {
        TODO("Not yet implemented")
    }

    override fun observeValue(): Flow<DataSnapshot> {
        TODO("Not yet implemented")
    }
}



actual class TransactionResult {
    actual companion object {
        actual fun success(value: Any?): TransactionResult {
            TODO("Not yet implemented")
        }

        actual fun abort(): TransactionResult {
            TODO("Not yet implemented")
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
        return IosDatabaseReference(nativeRef as IDatabaseReference)
    }
}

actual object FirebaseServerValue {
    actual val TIMESTAMP: Any
        get() = TODO("Not yet implemented")

    actual fun increment(value: Int): Any {
        TODO("Not yet implemented")
    }
}

actual class DataSnapshot {
    actual fun getValue(): Any? {
        TODO("Not yet implemented")
    }

    actual val key: String?
        get() = TODO("Not yet implemented")
    actual val children: List<DataSnapshot>
        get() = TODO("Not yet implemented")
}