package com.doubleclick.pizzastation.android

import android.app.Application
import com.myfatoorah.sdk.enums.MFCountry
import com.myfatoorah.sdk.enums.MFEnvironment
import com.myfatoorah.sdk.views.MFSDK

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MFSDK.init(
            "Aga4TqRVSkeoscxlOVIxufSbUi_2gjoUblJv-WUmFhi83RS8uvPbM5MibHYAAAujgu8C8IfbP4bBi0J5yU1X1WJE5ThfnBlaF448LXftMojYoaYrcHJY0LT43UHHRbZpkEfSn5jAwP95IndBgfgZ_T8uw3ANqZgsulXqJQDflYmxe6b-jr_o9odEvPMOd5X6s6LJTgydQmIN0evI6bjrUczUgQXEFOoxxmMcQK1t4mM1xTNzKYMz2VdQUzziG2N2gzHHXSanu85_DEllmtHKdSFUexLmS6CDgm_BRO6h5NBQg2UYz_g-2LRZN4JR4XPbswciorKYLI629jsVaHcLGRgl8M-VtXSsvKevjx_PERNwjNkY4QDeMhpP45HmjxOvUwkHUVEFocEUKqJHQz-1oJ2xwXkjcqFeCYV7-tE92CPLRDdhr6lUxsiYxa5jwx63LkvZYcPGaJ5WsPCfNz1AJevUQiN0-9jLkX7kpJv0NWjx4J7SqhdXuodz4o76_So1vNA3Ei849rq-3KMjHBKs8fZkXChVEOUqQ3J5KG8WDAAc2TgixXaps7-DKCgvvd2iMHoOM6B47l8X6c4F-TeJF9jofQFJBgQw6YrSH3H3gf9jHDZPbi6M_kzcMWux8imr2yuNkctTlWlSTVLm0F3JGJ3AuM9aEEacsihdEzesIllmJzclH_UyiTFyJWlQQ2tiF6NrNw",
            MFCountry.KUWAIT,
            MFEnvironment.TEST
        );
    }

}