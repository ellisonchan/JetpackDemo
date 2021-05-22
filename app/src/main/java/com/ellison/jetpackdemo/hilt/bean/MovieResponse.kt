package com.ellison.jetpackdemo.hilt.bean

data class MovieResponse<T>(
        var TotalResults: String = "0",
        var Response: String = "false",
        var Error: String = "null",
        var Search: T
) {
    override fun toString(): String {
        return "(TotalResults=$TotalResults,\nResponse=$Response,\nError=$Error,\nSearch=$Search)"
    }
}