/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  zhangyoumao <zhangyoumao@tp-link.net>
 * Created: 2014-3-20

 * updated: 2014-5-21
 * wenyong <wenyong@tp-link.net>
 * Reason: code migration
 *  
 * updated:2014-8-12
 * yangyushi<yangyushi@tp-link.net>
 * Reason: change ErrorCode for interface v1.5
 * 
 */

package com.tplink.cloud.api;

/**
 * The class provides error type, Definitions in the designed file <<
 * 浜戣澶囦笌浜戠閫氫俊鎺ュ彛璁捐 _v1.6.0 >>
 */
public enum ErrorCode {

    ERROR_SUCCESS(0, "Success"), ERROR_UNKNOWN(-1, "Unknown error"),

    ERROR_GENERIC(-10000, "Generic error"), ERROR_PARSE_JSON(-10100, "JSON format error"), ERROR_PARSE_JSON_NULL(
            -10101, "JSON body is NULL"),

    ERROR_IP_REQS_OUT_OF_LIMIT(-10200, "IP requests out of rate limit"), ERROR_USER_REQS_OUT_OF_LIMIT(-10201,
            "User requests out of rate limit"),

    ERROR_SERVER_INTERNAL_ERROR(-20000, "Server internal error"), ERROR_PERMISSION_DENIED(-20001, "Permission denied"), ERROR_REQUEST_TIMEOUT(
            -20002, "Request timeout"), ERROR_SERVER_BUSY(-20003, "Server busy"),

    ERROR_PARSE_JSON_ID(-20100, "JSON ID parse failed"), ERROR_METHOD_NOT_FOUND(-20103,
            "The method does not exist or is not available"), ERROR_PARAMS_NOT_FOUND(-20104, "Parameter doesn't exist"), ERROR_PARAMS_WRONG_TYPE(
            -20105, "Parameter has different type with value"), ERROR_PARAMS_WRONG_RANGE(-20106,
            "The value is out of the range for parameter"), ERROR_INVALID_PARAMS(-20107, "Parameter is invalid"),

    ERROR_MAIL_FORMAT_ERROR(-20200, "E-mail format error"), ERROR_PHONE_NUM_FORMAT_ERROR(-20201,
            "Phone number format error"), ERROR_USERNAME_FORMAT_ERROR(-20202, "Username format error"), ERROR_REGTIME_FORMAT_ERROR(
            -20203, "Regtime format error"),

    ERROR_DEVICE_ID_FORMAT_ERROR(-20500, "Device id format error"), ERROR_DEVICE_ID_NOT_FOUND(-20501,
            "Device id not found"), ERROR_BIND_DEVICE_ERROR(-20502, "Bind device error"), ERROR_UNBIND_DEVICE_ERROR(
            -20503, "Unbind device error"), ERROR_HW_ID_NOT_FOUND(-20504, "Hardware id not found"), ERROR_FW_ID_NOT_FOUND(
            -20505, "Firmware id not found"), ERROR_DEVICE_HAS_BIND_OTHER_ACCOUNT(-20506,
            "Device has bind other account"), ERROR_DEVICE_HAS_UNBIND(-20507, "Device has unbinded"), ERROR_USER_NUM_OUT_OF_RANGE(
            -20508, "User number out of range"), ERROR_ACCOUNT_PERMISSION_DENIED(-20509, "Account permission denied"), ERROR_OEM_ID_NOT_EXIST(
            -20510, "Oem id not exist"), ERROR_HW_ID_INVALID(-20520, "Hardware id is invalid"), ERROR_FW_ID_INVALID(
            -20521, "Firmware id is invalid"), ERROR_DEVICE_ID_INVALID(-20522, "Device id is invalid"), ERROR_MODEL_NOT_FOUND(
            -20523, "Model is not found"),
    // Device not found by Mac and SN
    ERROR_DEVICE_NOT_FOUND(-20530, "Device not found"),
    // Multiple device id found by Mac and SN
    ERROR_DEVICE_DUPLICATED(-20531, "Device duplicated"), ERROR_DEVICE_OFFLINE(-20571, "Device is offline"), ERROR_DEVICE_ALIAS_FORMAT_ERROR(
            -20572, "Device alias format error"), ERROR_ACCOUNT_NOT_BIND_TO_DEVICE(-20580,
            "Account is not binded to the device"),

    ERROR_ACCOUNT_NOT_FOUND(-20600, "Account not found"), ERROR_PASSWORD_INCORRECT(-20601, "Password incorrect"), ERROR_ACCOUNT_INACTIVE(
            -20602, "Account inactive"), ERROR_ACCOUNT_DUPLICATED(-20603, "Account duplicated"), ERROR_ACCOUNT_USERNAME_FORMAT_ERROR(
            -20604, "Account username format error"), ERROR_ACCOUNT_ACTIVE_MAIL_SEND_FAIL(-20606,
            "Send active mail failed"), ERROR_RESET_MAIL_SEND_FAIL(-20609, "Send reset mail failed"), ERROR_ACCOUNT_WRONG_RANG(
            -20608, "Account is out of the range"), ERROR_VERI_CODE_ERROR(-20607, "Verification code error"), ERROR_ACCOUNT_TYPE_ERROR(
            -20610, "Account type error"), ERROR_PASSWORD_FORMAT_ERROR(-20615, "Password format error"), ERROR_NEW_PASSWORD_FORMAT_ERROR(
            -20616, "New password format error"), ERROR_OWNER_NOT_EXIST(-20617, "Owner not exist"), ERROR_USER_NOT_EXIST(
            -20618, "User not exist"), ERROR_PHONE_DUPLICATED(-20619, "Phone duplicated"), ERROR_NICKNAME_FORMAT_ERROR(
            -20620, "Nickname format error"), ERROR_EMAIL_DUPLICATED(-20621, "Email duplicated"), ERROR_USERNAME_DUPLICATED(
            -20622, "Username duplicated"), ERROR_ACCOUNT_ACTIVATED(-20623, "Account has been activated"), ERROR_TOKEN_EXPIRED(
            -20651, "Token expired"), ERROR_TOKEN_INCORRECT(-20652, "Token incorrect"), ERROR_REFRESH_TOKEN_EXPIRED(
            -20655, "Refresh token expired"), ERROR_REFRESH_TOKEN_INCORRECT(-20656, "Refresh token incorrect"), ERROR_ACCOUNT_LOCKED(
            -20661, "Account locked"), ERROR_DEVICE_LOCKED_BY_GET_VERI_CODE(-20662, "Device locked"), ERROR_ACCOUNT_ACTIVE_FAIL(
            -20671, "Verify account failed"), ERROR_ACCOUNT_ACTIVE_TIMEOUT(-20672, "Active mail timeout"), ERROR_RESET_PWD_TIMEOUT(
            -20673, "Reset password mail timeout"), ERROR_RESET_PWD_FAIL(-20674, "Reset password fail"), ERROR_ACCOUNT_LOGIN_IN_OTHER_PLACES(
            -20675, "Account login in other places"), ERROR_VERI_CODE_INEFFECTIVE(-20676,
            "Verification code ineffective"),

    ERROR_FW_DOWNLOAD_STATUS_ERROR(-20701, "Firmware download status error"), ERROR_FW_DOWNLOAD_PROGRESS_ERROR(-20702,
            "Firmware download progress error"), ERROR_FW_ID_NOT_SUPPORT_DEVICE(-20703,
            "Firmware id not support this device"), ERROR_HWID_OEMID_FIT_ERROR(-20704, "Hwid not fit with oemid"),

    ERROR_DEVICE_HAS_NO_APP_ACCOUNT(-20801, "device has no app account"), ERROR_APP_OFFLINE(-20802, "app is offline"), ERROR_APP_ACCOUNT_LOGIN_IN_OTHER_PLACE(
            -20803, "app account login in other place"), ERROR_MOBILE_TYPE_NOT_SUPPORT(-20811,
            "Mobile type not support"), ERROR_DEVICE_TOKEN_FORMAT_ERROR(-20812, "Device token format error"), ERROR_BADGE_SIZE_ERROR(
            -20813, "Badge size error"),

    ERROR_PLUGIN_INDEX_ERROR(-20901, "Plugin index error"), ERROR_PLUGIN_ID_NOT_EXIST(-20902, "Plugin id not exist"), ERROR_PLUGIN_VERSION_NOT_EXIST(
            -20903, "Plugin version not exist"), ERROR_PLUGIN_PRE_INSTALLED_LIST_SIZE_ERROR(-20904,
            "Plugin pre installed list size not right"), ERROR_PLUGIN_MANUAL_INSTALLED_LIST_SIZE_ERROR(-20905,
            "Plugin manual installed list size not right"), ERROR_PLUGIN_LIST_ITEMS_DUPLICATE(-20906,
            "Plugin list items are duplicate"), ERROR_PLUGIN_FIT_ERROR(-20907, "Plugin fit error"),

    ERROR_IP_VC_REQ_HOURLY_LIMITED(-21001, "Get veriCode request too much from this ip hourly"),

    ERROR_CONFIG_VERSION_NOT_EXIST(-21101, "config version not exist"),

    ERROR_APP_NOT_FOUND(-23001, "app not found"),

    ERROR_P2P_SESSIONID_INCORRECT(-24000, "P2P session ID incorrect"), ERROR_P2P_NOT_READY(-24001,
            "P2P connect not ready"), ERROR_RELAY_NOT_READY(-24002, "Relay connect not ready"),

    ERROR_DEVICE_TOKEN_EXPIRED(-25001, "Device token expired"), ERROR_DEVICE_TOKEN_INCORRECT(-25002,
            "Device token incorrect"),

    ERROR_VA_PREDEDUCT_EXISTED(-26000, "Prededuct Info already existed"), ERROR_VA_BALANCE_NOT_FOUND(-26001,
            "Balance Info not found"), ERROR_VA_PREDEDUCT_NOT_FOUND(-26002, "Prededuct Info not found"), ERROR_VA_BALANCE_NOT_ENOUGH(
            -26003, "Balance not enough"),

    INTERNAL_ERROR_MODULE_INTERNAL_ERROR(-30000, "Module internal error"),

    INTERNAL_ERROR_PARAMS_NOT_FOUND(-30104, "Parameter doesn't exist"),

    INTERNAL_ERROR_DEVICE_INFO_NOT_MATCH(-30501, "Device info is not match"), INTERNAL_ERROR_DEVICE_ID_DUPLICATED(
            -30502, "Device id duplicated"), INTERNAL_ERROR_SERVER_IS_STOPPING(-30503, "Server is stopping"),

    /*
     * SMS error code
     */
    INTERNAL_ERROR_SMS_INVALID_MOBILE(-31200, "Invalid mobile"), INTERNAL_ERROR_SMS_MOBILE_IN_BLACKLIST(-31201,
            "Mobile is in blacklist"), INTERNAL_ERROR_SMS_MSG_CONTAINS_ILLEGAL_KEYWORD(-31202,
            "Message contains illegal keyword"), INTERNAL_ERROR_SMS_MSG_OUT_OF_LIMIT(-31203,
            "Message length out of limit"), INTERNAL_ERROR_SMS_MSG_NEED_AUDIT(-31204, "Message need audit"), INTERNAL_ERROR_SMS_SENDING_IN_PROCESS(
            -31205, "Sending request is in process"), INTERNAL_ERROR_SMS_NO_REPLY(-31206, "Phone has no replys"), INTERNAL_ERROR_SMS_FREQUENT_VERIFY_CODE_SUBMISSION(
            -31207, "Frequent verify code submission"), INTERNAL_ERROR_SMS_MSG_IMCOMPATIBLE_WITH_MOULD(-31208,
            "Message is imcompatible with mould"), INTERNAL_ERROR_SMS_EXCEED_MOBILE_SEND_LIMIT(-31209,
            "Exceed mobile 24 hours sending limit"),

    DEVICE_ERROR_EMAIL_FORMAT_ERROR(-51201, "Email format error"), DEVICE_ERROR_EMAIL_LENGTH_ERROR(-51202,
            "Email length error"), DEVICE_ERROR_EMAIL_PASSWORD_LENGTH_ERROR(-51203, "Email password length error"), DEVICE_ERROR_CLIENT_INTERNAL_ERROR(
            -51204, "Cloud client internal error"), DEVICE_ERROR_REQUEST_ID_NOT_FOUND(-51205, "Request id not found"), DEVICE_ERROR_METHOD_NOT_FOUND(
            -51206, "The method does not exist or is not available"), DEVICE_ERROR_PARAMETER_INVALID(-51207,
            "Method parameter invalid"), DEVICE_ERROR_GET_DATA_FAILED(-51208, "Get data failed"), DEVICE_ERROR_URL_INVALID(
            -51209, "URL invalid"), DEVICE_ERROR_PASSWORD_INVALID(-51210, "Password invalid"), DEVICE_ERROR_DOWNLOAD_FW_FAILED(
            -51211, "Download firmware failed"), DEVICE_ERROR_UPGRADE_FW_FAILED(-51212, "Upgrade firmware failed"), DEVICE_ERROR_CONFIGURATE_FAILED(
            -51213, "Configurate failed"), DEVICE_ERROR_PERMISSION_DENIED(-51214, "Permission denied"), DEVICE_ERROR_REQUEST_TIMEOUT(
            -51215, "Timeout"), DEVICE_ERROR_MEMORY_OUT(-51216, "Memory out"), DEVICE_ERROR_SEND_REQ_MSG_FAILED(-51217,
            "Send request message to cloud failed"), DEVICE_ERROR_CONNECTING_TO_CLOUD_SERVER(-51218,
            "Connecting to cloud server"), DEVICE_ERROR_WAITTING_LAST_REQUEST(-51219, "Waiting last request"), DEVICE_ERROR_ACCOUNT_FORMAT_ERROR(
            -51220, "Account format error"), DEVICE_ERROR_VERIFY_CODE_FORMAT_ERROR(-51221, "Verify code format error"), DEVICE_ERROR_NEW_PASSWORD_INVALID(
            -51222, "New password invalid"), DEVICE_ERROR_ACCOUNT_NAME_ERROR(-51223, "Account name error"), DEVICE_ERROR_DEVICE_ID_ERROR(
            -51224, "Device id error"), DEVICE_ERROR_SYNC_VERSION_OLD(-51225,
            "Old version larger than received version"),

    /*
     * DDNS error code
     */
    DDNS_PARAM_ERROR(-22000, "parameter error"), DDNS_DOMAIN_REGISTERED(-22001, "domain was registered"), DDNS_DOMAIN_TO_ACCOUNT_LIMITED(
            -22002, "domain registered is too much"), DDNS_DOMAIN_TO_DEVICE_LIMITED(-22003,
            "domain binded to the device is too much"), DDNS_DOMAIN_BINDED(-22004, "domain was binded to other device"), DDNS_NO_DOMAIN_BIND_TO_DEVICE(
            -22005, "no domain binded to device"), DDNS_DB_ERROR(-22006, "db operate fail"), DDNS_DIRTY_WORD(-22007,
            "domain contains dirty-word"), DDNS_DOMAIN_NOT_EXISTS(-22008, "domain doesn't exist"), DDNS_DOMAIN_AND_DEVICE_MISMATCH(
            -22009, "domain and device mismatch");

    // error code
    private int code;
    private String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorCode getErrorCode(int code) {
        ErrorCode[] errorCodes = ErrorCode.values();
        for (ErrorCode errorCode : errorCodes) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        ErrorCode errorCode = ErrorCode.ERROR_UNKNOWN;
        errorCode.code = code;
        return errorCode;
    }
}