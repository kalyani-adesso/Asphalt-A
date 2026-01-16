package com.asphalt.android

import kotlinx.coroutines.flow.Flow

interface IFirebaseDatabase {
    fun getReference(path: String? = null): IDatabaseReference
}
expect object FirebaseServerValue {
    val TIMESTAMP: Any
}

interface IDatabaseReference {
    fun push(): IDatabaseReference
    val key: String?
    fun setValue(value: Any?)
    fun child(path: String): IDatabaseReference
    fun runTransaction(updateFunction: (DataSnapshot) -> TransactionResult)
    fun updateChildren(updates: Map<String, Any?>)
    fun observeValue(): Flow<DataSnapshot>
}



expect class DataSnapshot {
    fun getValue(): Any?
    val key: String?
    val children: List<DataSnapshot>
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