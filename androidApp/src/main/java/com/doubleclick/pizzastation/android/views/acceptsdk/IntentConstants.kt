package com.doubleclick.pizzastation.android.views.acceptsdk

object IntentConstants {
    const val CARD_IO_SCAN_REQUEST = 16
    const val THREE_D_SECURE_VERIFICATION_REQUEST = 32
    const val USER_CANCELED = 1
    const val MISSING_ARGUMENT = 2
    const val TRANSACTION_ERROR = 3
    const val TRANSACTION_REJECTED = 4
    const val TRANSACTION_REJECTED_PARSING_ISSUE = 5
    const val TRANSACTION_SUCCESSFUL = 6
    const val TRANSACTION_SUCCESSFUL_PARSING_ISSUE = 7
    const val TRANSACTION_SUCCESSFUL_CARD_SAVED = 8
    const val USER_CANCELED_3D_SECURE_VERIFICATION = 9
    const val USER_CANCELED_3D_SECURE_VERIFICATION_PARSING_ISSUE = 10
    const val USER_FINISHED_3D_VERIFICATION = 17
    const val TRANSACTION_ERROR_REASON = "transaction_error_reason"
    const val RAW_PAY_RESPONSE = "raw_pay_response"
    const val MISSING_ARGUMENT_VALUE = "missing_argument_value"
}
