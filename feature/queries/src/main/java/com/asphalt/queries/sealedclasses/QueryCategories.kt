package com.asphalt.queries.sealedclasses

sealed class QueryCategories(val id: Int, val name: String) {
    object Maintenance : QueryCategories(1, "Maintenance")
    object Routes : QueryCategories(2, "Routes")
    object Gear : QueryCategories(3, "Gear")
    object Safety : QueryCategories(4, "Safety")
    object Other : QueryCategories(5, "Other")

    companion object {
        fun getAllCategories(): List<QueryCategories> {
            return listOf(
                Maintenance, Routes, Gear, Safety, Other
            )
        }

        fun getCategoryNameById(id: Int): String? {
            getAllCategories().forEach { categories ->
                if (categories.id == id) {
                    return categories.name
                }
            }
            return null
        }
    }
}