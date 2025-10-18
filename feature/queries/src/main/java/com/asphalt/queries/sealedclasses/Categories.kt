package com.asphalt.queries.sealedclasses

sealed class Categories(val id: Int, val name: String) {
    object Maintenance : Categories(1, "Maintenance")
    object Routes : Categories(2, "Routes")
    object Gear : Categories(3, "Gear")
    object Safety : Categories(4, "Safety")
    object Other : Categories(5, "Other")

    companion object {
        fun getAllCategories(): List<Categories> {
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