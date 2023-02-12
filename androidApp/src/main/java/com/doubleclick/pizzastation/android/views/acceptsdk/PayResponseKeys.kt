package com.doubleclick.pizzastation.android.views.acceptsdk

object PayResponseKeys {
    const val AMOUNT_CENTS = "amount_cents"
    const val IS_REFUNDED = "is_refunded"
    const val IS_CAPTURE = "is_capture"
    const val CAPTURED_AMOUNT = "captured_amount"
    const val SOURCE_DATA_TYPE = "source_data.type"
    const val PENDING = "pending"
    const val MERCHANT_ORDER_ID = "merchant_order_id"
    const val IS_3D_SECURE = "is_3d_secure"
    const val ID = "od"
    const val IS_VOID = "is_void"
    const val CURRENCY = "currency"
    const val IS_AUTH = "is_auth"
    const val IS_REFUND = "is_refund"
    const val OWNER = "owner"
    const val IS_VOIDED = "is_voided"
    const val SOURCE_DATA_PAN = "source_data.pan"
    const val PROFILE_ID = "profile_id"
    const val SUCCESS = "success"
    const val DATA_MESSAGE = "data.message"
    const val SOURCE_DATA_SUB_TYPE = "source_data.sub_type"
    const val ERROR_OCCURED = "error_occured"
    const val IS_STANDALONE_PAYMENT = "is_standalone_oayment"
    const val CREATED_AT = "created_at"
    const val REFUNDED_AMOUNT_CENTS = "refunded_amount_cents"
    const val INTEGRATION_ID = "integration_id"
    const val ORDER = "order"
    const val REDIRECTION_URL = "redirection_url"
    val PAY_DICT_KEYS = arrayOf(
        "amount_cents",
        "is_refunded",
        "is_capture",
        "captured_amount",
        "source_data.type",
        "pending",
        "merchant_order_id",
        "is_3d_secure",
        "id",
        "is_void",
        "currency",
        "is_auth",
        "is_refund",
        "owner",
        "is_voided",
        "source_data.pan",
        "profile_id",
        "success",
        "data.message",
        "source_data.sub_type",
        "error_occured",
        "is_standalone_payment",
        "created_at",
        "refunded_amount_cents",
        "integration_id",
        "order"
    )
}
