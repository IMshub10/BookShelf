package com.summer.bookshelf.networking

object APIConstants {

    //region COUNTRIES
    const val COUNTRIES_BASE_URL = "https://api.first.org/"

    /**
     * GET
     */
    const val COUNTRIES = "data/v1/countries"
    //endregion

    //region GEO DATA
    const val GEO_LOCATION_BASE_URL = "http://ip-api.com/"

    /**
     * GET
     */
    const val MY_GEO_DATA = "json"
    //endregion

    //region BOOK LIST
    const val BOOK_KEEPER_BASE_URL = "https://jsonkeeper.com/"

    /**
     * GET
     */
    const val BOOK_LIST = "b/CNGI"
    //endregion

}