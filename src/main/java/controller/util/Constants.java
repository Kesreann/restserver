package controller.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants {

	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);

	public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

	public static final String EMAIL_REGEXP = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+";
	public static final String EMAIL_REGEXP_MESSAGE = "Invalid email.";
	public static final String PASSWORD_REGEXP = "^(?=.*[a-z])(?=.*[A-Z])((?=.*[\\\\ !\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])|(?=(.*\\d){1}))[0-9a-zA-Z\\\\ !\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]{8,}";
	public static final String PASSWORD_REGEXP_MESSAGE = "One small letter, one capital letter, one number or one special character \" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\" and minimum length of 8.";
	public static final String CLIENTNAME_REGEXP = "^(?=.*[a-zA-Z])[0-9a-zA-Z]{3,}";
	public static final String CLIENTNAME_REGEXP_MESSAGE = "Must contain at least one letter and minimum length of 3.";

	public static String SERVER_URL = "";
	public static String CLIENT_URL = "";
	public static String DMS_URL = "";
	public static String LEDGER_URL = "";
	public static final String AVATAR_STORE = "user_avatar";
	public static final String LOGO_STORE = "client_logo";

	public static final SimpleDateFormat XML_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public static final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static final SimpleDateFormat LEDGER_FORM_DATE = new SimpleDateFormat("yyyyMMdd");

	public static final String DATE_FULL_TIME_PATTERN = "yyyy-MM-ddTHH:mm:ss.SSS";
}
