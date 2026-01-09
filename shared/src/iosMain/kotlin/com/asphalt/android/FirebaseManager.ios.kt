package com.asphalt.android

actual class FirebaseManager actual constructor() {
    actual fun getDatabase(): IFirebaseDatabase {
        TODO("Not yet implemented")
    }
}

actual class DataSnapshot {
    actual fun getValue(): Any? {
        TODO("Not yet implemented")
    }

    actual fun exists(): Boolean {
        TODO("Not yet implemented")
    }

    actual val key: String?
        get() = TODO("Not yet implemented")
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

actual class PlatformDatabase actual constructor() : IFirebaseDatabase {
    actual override fun getReference(path: String?): IDatabaseReference {
        TODO("Not yet implemented")
    }
}