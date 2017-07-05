package cn.ssdut153.shop.common.kit;

import com.jfinal.kit.StrKit;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件发送工具类
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class EmailKit {

    private static final Logger log = LoggerFactory.getLogger(EmailKit.class);
    private static String emailServer;
    private static String fromEmail;
    private static String password;

    /**
     * 初始化发送邮箱的信息
     *
     * @param fromEmail 发送邮件的邮箱
     */
    public static void init(String fromEmail) {
        EmailKit.fromEmail = fromEmail;
    }

    /**
     * 初始化发送邮箱的信息
     *
     * @param emailServer 邮件服务器地址
     * @param fromEmail   发送邮件的邮箱
     * @param password    发件邮箱密码
     */
    public static void init(String emailServer, String fromEmail, String password) {
        EmailKit.emailServer = emailServer;
        EmailKit.fromEmail = fromEmail;
        EmailKit.password = password;
    }

    /**
     * 发送邮件
     *
     * @param toEmail 发送到的邮箱
     * @param title   邮件标题
     * @param content 邮件内容
     * @return 发送成功后返回的内容
     * @throws java.lang.RuntimeException 发送邮件失败
     */
    public static String sendEmail(String toEmail, String title, String content) {
        return sendEmail(emailServer, fromEmail, password, toEmail, title, content);
    }

    /**
     * 发送邮件
     *
     * @param emailServer 邮件服务器地址
     * @param fromEmail   发送邮件的邮箱
     * @param password    发件邮箱密码
     * @param toEmail     发送到的邮箱
     * @param title       邮件标题
     * @param content     邮件内容
     * @return 发送成功后返回的内容
     * @throws java.lang.RuntimeException 发送邮件失败
     */
    private static String sendEmail(String emailServer, String fromEmail, String password, String toEmail, String title, String content) {

        SimpleEmail email = new SimpleEmail();
        if (StrKit.notBlank(emailServer)) {
            email.setHostName(emailServer);
        } else {
            // 默认使用本地 postfix 发送，这样就可以将postfix 的 mynetworks 配置为 127.0.0.1 或 127.0.0.0/8 了
            email.setHostName("127.0.0.1");
        }

        // 如果密码为空，则不进行认证
        if (StrKit.notBlank(password)) {
            email.setAuthentication(fromEmail, password);
        }

        email.setCharset("utf-8");
        try {
            email.addTo(toEmail);
            email.setFrom(fromEmail);
            email.setSubject(title);
            email.setMsg(content);
            return email.send();
        } catch (EmailException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String ret = sendEmail(
                "smtp.test.com",              // 邮件发送服务器地址
                "no-reply@test.com",        // 发件邮箱
                "password",                    // 发件邮箱密码
                "test@test.com",        // 收件地址
                "邮件标题",              // 邮件标题
                "测试邮件");                // 邮件内容
        System.out.println("发送返回值: " + ret);
    }

}
