package bus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ApiUtil {
	public static final String API_COMPLAINT_DETAIL = "getComplaintDetail";
	public static final String API_METHOD_ARTICLECATES = "articleCates";
	public static final String API_METHOD_ARTICLE_BYCAT = "articlesByCat";
	public static final String API_METHOD_BUS_LINES = "getBuslines";
	public static final String API_METHOD_CANCEL_ORDER = "cancelOrder";
	public static final String API_METHOD_CHANGE_PASSWORD = "changePassword";
	public static final String API_METHOD_CHECK_LOGIN = "checkLoginSession";
	public static final String API_METHOD_COMFIRM_ORDER = "comfirmOrder";
	public static final String API_METHOD_COMPLAINT = "complaint";
	public static final String API_METHOD_DISHES = "getDishes";
	public static final String API_METHOD_DISHES_COMMENT = "getDishesComment";
	public static final String API_METHOD_DISHES_CONDITION = "getDishesType";
	public static final String API_METHOD_DISHES_DETAIL = "getDishesDetail";
	public static final String API_METHOD_DISHES_DETAILS = "getDishesDetails";
	public static final String API_METHOD_EMPLOYEE_LOGIN = "login";
	public static final String API_METHOD_EMPLOYEE_UPDATE = "saveEmployeeDetail";
	public static final String API_METHOD_GET_DISHES_COMMENT = "getOrderComment";
	public static final String API_METHOD_GET_ORDER = "getOrder";
	public static final String API_METHOD_GET_ORDER_DETAIL = "getOrderDetail";
	public static final String API_METHOD_HAS_NEW = "hasNew";
	public static final String API_METHOD_LINE_DETAIL = "lineDetail";
	public static final String API_METHOD_LIST_NOTICE = "articlesByCat";
	public static final String API_METHOD_LOGIN = "driverLogin";
	public static final String API_METHOD_ORDER_STATUS = "getOrderState";
	public static final String API_METHOD_ORDER_TYPE = "getOrderType";
	public static final String API_METHOD_PAGING = "getPaging";
	public static final String API_METHOD_PAGING_DETAIL = "getPagingDetail";
	public static final String API_METHOD_PAGING_TYPE = "getPagingType";
	public static final String API_METHOD_PUBLISH_NOTICE = "publishNotice";
	public static final String API_METHOD_PUBLISH_PAGING = "publishPaging";
	public static final String API_METHOD_SAVE_COMPLAINT = "saveComplaint";
	public static final String API_METHOD_SAVE_ORDER = "saveOrder";
	public static final String API_METHOD_SUBMIT_ORDER_COMMENT = "saveDishesComment";
	public static final String API_METHOD_SUBMIT_PAGING = "savePagingComment";
	public static final String API_METHOD_SUPPLIER_DETAIL = "getSupplierDetail";
	public static final String BASE_URL;
	public static final String BASE_URL2;
	public static final String CANCEL_ORDER;
	public static final String CHANGE_PASSWORD;
	public static final String CHECK_LOGING_SESSION;
	public static final String COMFIRM_ORDER;
	public static final String COOKIE_KEY = "Cookie";
	public static final String DO_EMPLOYEE_LOGIN;
	public static final String GET_ARTICLES;
	public static final String GET_BUSGEO;
	public static final String GET_COMPLAINT;
	public static final String GET_COMPLAINT_DETAIL;
	public static final String GET_DISHES;
	public static final String GET_DISHES_COMMENT;
	public static final String GET_DISHES_CONDITION;
	public static final String GET_DISHES_DETAIL;
	public static final String GET_DISHES_DETAILS;
	public static final String GET_LINEDETAIL;
	public static final String GET_ORDER;
	public static final String GET_ORDER_COMMENT;
	public static final String GET_ORDER_DETAIL;
	public static final String GET_ORDER_STATUS;
	public static final String GET_ORDER_TYPE;
	public static final String GET_PAGING;
	public static final String GET_PAGING_DETAIL;
	public static final String GET_PAGING_TYPE;
	public static final String GET_SUB_CATE;
	public static final String GET_SUPPLIER_DETAIL;
	public static final String GET_TOP_CATE;
	public static final String HAS_NEW;
	public static final String HAS_NEW_NOTICE = "hasNewNotice";
	public static final String KEY = "banchetong";
	public static final String LABEL_METHOD = "method";
	public static final String LABEL_TIME = "timestamp";
	public static final String LABEL_TOKEN = "token";
	public static final String MONITOR_BUS;
	public static final String PHPSESSID = "PHPSESSID";
	public static final String PUBLISH_PAGING;
	public static final String SAVE_COMPLAINT;
	public static final String SAVE_ORDER;
	public static final String SET_COOKIE_KEY = "Set-Cookie";
	public static final String SUBMIT_ORDER_DISHES_COMMENT;
	public static final String SUBMIT_PAGING_DISHES_COMMENT;
	public static final String UPDATE_EMPLOYEE_DETAIL;
	public static int VERSION = 1;
	public static String WEBROOT;

	static {
		if (2 == VERSION) {
			WEBROOT = "http://as.kollway.com";
		} else if (3 == VERSION) {
			WEBROOT = "http://192.168.1.8/WebAdminService";
		} else {
			WEBROOT = "http://211.151.82.84:35257";
		}
		BASE_URL = WEBROOT + "/index.php/Api";
		GET_BUSGEO = BASE_URL + "/getBuslines";
		MONITOR_BUS = BASE_URL + "/monitorBus";
		GET_LINEDETAIL = BASE_URL + "/lineDetail";
		GET_TOP_CATE = BASE_URL + "/getTopCates";
		GET_SUB_CATE = BASE_URL + "/getSubCates";
		GET_ARTICLES = BASE_URL + "/articlesByCat";
		BASE_URL2 = WEBROOT + "/index.php";
		GET_DISHES_CONDITION = BASE_URL2 + "/DishesApi/getDishesType";
		GET_DISHES = BASE_URL2 + "/DishesApi/getDishes";
		GET_DISHES_COMMENT = BASE_URL2 + "/DishesApi/getDishesComment";
		GET_DISHES_DETAIL = BASE_URL2 + "/DishesApi/getDishesDetail";
		GET_DISHES_DETAILS = BASE_URL2 + "/DishesApi/getDishesDetails";
		GET_SUPPLIER_DETAIL = BASE_URL2 + "/SupplierApi/getSupplierDetail";
		DO_EMPLOYEE_LOGIN = BASE_URL2 + "/UserApi/login";
		CHECK_LOGING_SESSION = BASE_URL2 + "/UserApi/checkLoginSession";
		UPDATE_EMPLOYEE_DETAIL = BASE_URL2 + "/UserApi/saveEmployeeDetail";
		PUBLISH_PAGING = BASE_URL2 + "/PagingApi/publishPaging";
		GET_COMPLAINT_DETAIL = BASE_URL2 + "/SupplierApi/getComplaintDetail";
		GET_COMPLAINT = BASE_URL2 + "/UserApi/getComment";
		GET_ORDER_TYPE = BASE_URL2 + "/OrderApi/getOrderType";
		GET_ORDER_STATUS = BASE_URL2 + "/OrderApi/getOrderState";
		SAVE_ORDER = BASE_URL2 + "/OrderApi/saveOrder";
		CANCEL_ORDER = BASE_URL2 + "/OrderApi/cancelOrder";
		COMFIRM_ORDER = BASE_URL2 + "/OrderApi/comfirmOrder";
		GET_ORDER_COMMENT = BASE_URL2 + "/OrderApi/getOrderComment";
		GET_ORDER_DETAIL = BASE_URL2 + "/OrderApi/getOrderDetail";
		SUBMIT_ORDER_DISHES_COMMENT = BASE_URL2 + "/DishesApi/saveDishesComment";
		GET_ORDER = BASE_URL2 + "/UserApi/getOrder";
		SAVE_COMPLAINT = BASE_URL2 + "/SupplierApi/saveComplaint";
		GET_PAGING_TYPE = BASE_URL2 + "/PagingApi/getPagingType";
		GET_PAGING = BASE_URL2 + "/UserApi/getPaging";
		GET_PAGING_DETAIL = BASE_URL2 + "/PagingApi/getPagingDetail";
		SUBMIT_PAGING_DISHES_COMMENT = BASE_URL2 + "/PagingApi/savePagingComment";
		CHANGE_PASSWORD = BASE_URL2 + "/UserApi/changePassword";
		HAS_NEW = BASE_URL2 + "/UserApi/hasNew";
	}

	private static long timestamp() {
		return System.currentTimeMillis();
	}

	private static String sign(String paramString1, long timestamp, String paramString2) {
		StringBuilder sb = new StringBuilder();
		synchronized (sb) {
			sb.append(paramString1);
			sb.append(timestamp);
			sb.append(paramString2);
			paramString1 = MD5.encode(sb.toString().toLowerCase());
			sb.delete(0, sb.length());
			return paramString1;
		}
	}

	private static String buildQueryString(String paramString1, String methodName) {
		try {
			long timestamp = timestamp();
			String token = sign(methodName, timestamp, paramString1);
			StringBuilder sb = new StringBuilder();
			try {
				sb.append(String.format("method=%s", new Object[] { methodName }));
				sb.append(String.format("&timestamp=%s", new Object[] { Long.valueOf(timestamp) }));
				sb.append(String.format("&token=%s", new Object[] { token }));
				methodName = sb.toString();
				sb.delete(0, sb.length());
				return methodName;
			} finally {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return methodName;
	}

	public static String buildUrl(String paramString, Map<String, String> paramMap) {
		StringBuilder localStringBuilder = new StringBuilder();
		localStringBuilder.append(buildQueryString("banchetong", paramString));
		if (paramMap != null) {
			Iterator<String> it = paramMap.keySet().iterator();

			while (it.hasNext()) {
				String str1 = (String) it.next();
				try {
					String str2 = URLEncoder.encode((String) paramMap.get(str1), "utf-8");
					localStringBuilder.append('&');
					localStringBuilder.append(str1);
					localStringBuilder.append('=');
					localStringBuilder.append(str2);
				} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
					localUnsupportedEncodingException.printStackTrace();
				}
			}
		}
		return localStringBuilder.toString();
	}

	public static String getAPIToken(String paramString) {
		return sign(paramString, System.currentTimeMillis(), "banchetong");
	}

	public static String getFullURL(String paramString) {
		return getTokenURL(BASE_URL + "/" + paramString, paramString, null);
	}

	public static String getFullURL(String paramString1, String paramString2) {
		return getTokenURL(paramString1, paramString2, null);
	}

	public static String getFullURL(String paramString, Map<String, String> paramMap) {
		return getTokenURL(BASE_URL + "/" + paramString, paramString, paramMap);
	}

	public static final String getSessionCookie(Map<String, String> paramMap) {
		if ((paramMap.containsKey("Set-Cookie")) && (((String) paramMap.get("Set-Cookie")).startsWith("PHPSESSID"))) {
			String s = (String) paramMap.get("Set-Cookie");
			if (s.length() > 0) {
				return s.split(";")[0].split("=")[1];
			}
		}
		return null;
	}

	public static String getTokenURL(String paramString1, String paramString2, Map<String, String> paramMap) {
		paramString2 = buildUrl(paramString2, paramMap);
		StringBuilder sb = new StringBuilder();
		sb.append(paramString1);
		sb.append("?");
		sb.append(paramString2);
		return sb.toString();
	}

	public static final void main(String[] paramArrayOfString) {
		System.out.println(getFullURL("getBuslines"));
		System.out.println(getFullURL("monitorBus"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "868120205237610");
		System.out.println(getFullURL("lineDetail", paramMap));
	}

//	public static final void setSessionCookie(Map<String, String> paramMap, String paramString) {
//		if (!TextUtils.isEmpty(paramString)) {
//			StringBuilder localStringBuilder = new StringBuilder();
//			localStringBuilder.append("PHPSESSID");
//			localStringBuilder.append("=");
//			localStringBuilder.append(paramString);
//			if (paramMap.containsKey("Cookie")) {
//				localStringBuilder.append("; ");
//				localStringBuilder.append((String) paramMap.get("Cookie"));
//			}
//			paramMap.put("Cookie", localStringBuilder.toString());
//		}
//	}
}

class MD5 {
	private static char[] a = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
	private static MessageDigest b;

	static {
		try {
			b = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			System.out.println("Get MD5 Digest failed.");
			localNoSuchAlgorithmException.printStackTrace();
		}
	}

	public static final String encode(String paramString) {
		return hexString(b.digest(paramString.getBytes()));
	}

	public static String hexString(byte[] paramArrayOfByte) {
		int i = 0;
		if ((paramArrayOfByte == null) || (paramArrayOfByte.length <= 0)) {
			return "";
		}
		int k = paramArrayOfByte.length;
		char[] arrayOfChar = new char[k * 2];
		int j = 0;
		for (;;) {
			if (i >= k) {
				return new String(arrayOfChar);
			}
			int m = paramArrayOfByte[i];
			int n = j + 1;
			arrayOfChar[j] = a[(m >>> 4 & 0xF)];
			j = n + 1;
			arrayOfChar[n] = a[(m & 0xF)];
			i += 1;
		}
	}
}
