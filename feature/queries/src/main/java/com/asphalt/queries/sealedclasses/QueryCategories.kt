package com.asphalt.queries.sealedclasses

import com.asphalt.commonui.R
import com.asphalt.queries.constants.QueryConstants


sealed class QueryCategories(val id: Int, val nameRes: Int) {
    object Maintenance :
        QueryCategories(QueryConstants.CATEGORY_MAINTENANCE_ID, R.string.maintenance)

    object Routes : QueryCategories(QueryConstants.CATEGORY_ROUTES_ID, R.string.routes)
    object Gear : QueryCategories(QueryConstants.CATEGORY_GEAR_ID, R.string.gear)
    object Safety : QueryCategories(QueryConstants.CATEGORY_SAFETY_ID, R.string.safety)
    object Other : QueryCategories(QueryConstants.CATEGORY_OTHER_ID, R.string.other)

    companion object {
        fun getAllCategories(): List<QueryCategories> {
            return listOf(
                Maintenance, Routes, Gear, Safety, Other
            )
        }

        fun getCategoryNameById(id: Int): Int? {
            getAllCategories().forEach { categories ->
                if (categories.id == id) {
                    return categories.nameRes
                }
            }
            return null
        }
    }
}