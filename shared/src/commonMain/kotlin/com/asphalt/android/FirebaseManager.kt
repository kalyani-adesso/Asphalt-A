package com.asphalt.android

interface IFirebaseDatabase {
    fun getReference(path: String? = null): IDatabaseReference
}

interface IDatabaseReference {
    fun child(path: String): IDatabaseReference
    fun runTransaction(updateFunction: (DataSnapshot) -> TransactionResult)
    fun updateChildren(updates: Map<String, Any?>)
}

expect class FirebaseManager() {
    fun getDatabase(): IFirebaseDatabase
}

expect class DataSnapshot {
    fun getValue(): Any?
    fun exists(): Boolean
    val key: String?
}

expect class TransactionResult {
    companion object {
        fun success(value: Any?): TransactionResult
        fun abort(): TransactionResult
    }
}

expect class PlatformDatabase() : IFirebaseDatabase {
    override fun getReference(path: String?): IDatabaseReference
}