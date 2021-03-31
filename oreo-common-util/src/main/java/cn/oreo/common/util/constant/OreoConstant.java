package cn.oreo.common.util.constant;

public class OreoConstant {

    public static final String MESSAGE_STRING_INVALIDATE = "字符串不合法！请不要传入空字符串！";
    public static final String MESSAGE_LOGIN_FAILED = "账号或密码错误！请重新输入！";
    public static final String MESSAGE_LOGIN_PASSWORD_FAILED = "密码错误，请重新输入！";
    public static final String MESSAGE_LOGIN_CAPTCHA_FAILED = "验证码错误，请重新输入！";
    public static final String MESSAGE_LOGIN_PHONE_FAILED = "手机号码错误，请重新输入！";
    public static final String MESSAGE_LOGIN_UN_PASSWORD = "未设置密码，请用手机验证码登录！";
    public static final String MESSAGE_CAPTCHA_EXISTED = "120秒内已发送过，获取验证码失败";
    public static final String MESSAGE_CAPTCHA_UN_EXISTED = "验证码不存在或已过期，请重新获取验证码";
    public static final String MESSAGE_CAPTCHA_SUCCESS = "手机验证码发送成功";
    public static final String MESSAGE_CAPTCHA_FAILURE = "手机验证码发送失败";
    public static final String MESSAGE_LOGIN_UN_ACCOUNT = "亲为新用户，正在自动创建用户~";
    public static final String MESSAGE_UN_LOGIN = "请登录后再访问！";
    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "系统错误：登录账号不唯一！";
    public static final String MESSAGE_NAME_ALREADY_EXIST = "该名称太受欢迎了，请更换一个!";
    public static final String MESSAGE_AJAX_OPER_FAILED = "请重新尝试！";
    public static final String MESSAGE_LOGIN_ACCT_ALREADY_EXIST = "该昵称太受欢迎！请更换新的昵称！";
    public static final String MESSAGE_REGISTER_SUCCESS = "注册成功！请返回登陆！";
    public static final String MESSAGE_REGISTER_FAILED = "注册失败！请重新注册！";
    public static final String MESSAGE_ADD_COMMENT = "添加评论成功！";
    public static final String MESSAGE_ADD_COMMENT_FAILED = "添加评论失败，请重新评论！";
    public static final String MESSAGE_ADD_COMMENT_FAILED_UNLOGIN = "请登录后再评论！";
    public static final String MESSAGE_SYSTEM_INTERNAL_FAILED = "系统内部逻辑错误";
    public static final String MESSAGE_CUSTOMIZE_EXCEPTION = "自定义异常";
    public static final String MESSAGE_OPERATE_SUCCESS = "操作成功";
    public static final String MESSAGE_OPERATE_FAILED = "操作失败";

    public static final String MESSAGE_REQUEST_REQUIRED = "参数不可以为空";

    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_USER = "user";
    public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
    public static final String ATTR_NAME_MESSAGE = "message";

    public static final String ATTR_REDIS_PHONE_CAPTCHA_PREFIX = "phone_captcha_";

    public static final String MESSAGE_PARAMETER_EXCEPTION = "参数异常,请联系管理员!";
    public static final String MESSAGE_MYSQL_UNIKEY_EXCEPTION = "主键是唯一值，重复主键错误！";
    public static final String RESERVE_TIME_EXCEPTION = "预约失败，预约进入时间小于当前时间";
    public static final String ATTR_REDIS_USERS_RESERVE_PRE = "users_reserve";

    public static final String MESSAGE_CAR_EXISTED = "车主这台车已经录入到系统中了哦！";
    public static final String MESSAGE_CAR_NULL = "该用户暂无录入车辆！";
    public static final String MESSAGE_CAR_DELETE_SUCCESS = "车辆解绑成功！";
    public static final String MESSAGE_CAR_DELETE_FAILURE = "车辆解绑失败！";
    public static final String RESERVE_SUCCESS = "预约成功";
    public static final String RESERVE_FAILURE = "预约失败";
    public static final String RESERVE_CANCEL_SUCCESS = "取消预约成功";
    public static final String RESERVE_CANCEL_FAILURE = "取消预约失败";
    public static final String RESERVE_UPDATE_SUCCESS = "更新预约成功";
    public static final String RESERVE_UPDATE_FAILURE = "更新预约失败";
    public static final String MESSAGE_ORDER_NOT_RESERVE = "未查到预约信息";
    public static final String MESSAGE_QUERY_RESULT_EMPTY = "查询结果为空";
    public static final String RESERVE_STATUS_INVALID = "预约已失效";

    public static final String DELAYED_QUEUE_NAME = "delay.queue.reserve.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delay.queue.reserve.exchange";
    public static final String DELAYED_ROUTING_KEY = "delay.queue.reserve.routing.key.cancel";
    public static final Integer RESERVE_CANCEL_TIME = 6000;
    public static final String MESSAGE_ORDER_SEARCH_FAILURE_PARAM = "小区名称或用户id参数异常";
    public static final String MESSAGE_VERIFY_EXIST_PARKING = "该停车位已存在，请联系小区管理员";
    public static final String MESSAGE_PARAM_MISS = "参数缺失";
    public static final String MESSAGE_COMMUNITY_SPACE_TOTAL_NUMBER_SHORTAGE = "该小区可用停车位剩余量不足，请重新预约";
    public static final String MESSAGE_COMMUNITY_SPACE_AVAILABLE_NUMBER_SHORTAGE = "该小区总停车位剩余量异常，删除操作失败";

    public static final String MESAAGE_ADD_PARKING_IS_NOT_OWNER = "该用户不是业主类型，请先认证为业主类型后新增";

    public static final String MESSAGE_PARKING_IS_NULL = "当前位置附近没有可用停车位！";
}
