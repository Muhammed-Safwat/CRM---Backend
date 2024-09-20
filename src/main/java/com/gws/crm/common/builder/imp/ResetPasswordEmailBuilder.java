package com.gws.crm.common.builder.imp;

import com.gws.crm.common.builder.EmailBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

public class ResetPasswordEmailBuilder extends EmailBuilder {

    public ResetPasswordEmailBuilder(MimeMessage mimeMessage) {
        super(mimeMessage);
    }

    public MimeMessage createEmail(String sendTo, String url) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        try {
            helper.setFrom("info@estshir.com");
            helper.setSubject("RNB - Reset Password");
            helper.setTo(sendTo);
            helper.setText(buildEmail(url), true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return mimeMessage;
    }

    private String buildEmail(String url) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Password Reset - RNB</title>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"margin:0; padding:0; background-color:#f0f0f5; font-family: 'Raleway', sans-serif; color:#000;\">\n" +
                "\n" +
                "    <!-- Wrapper Table -->\n" +
                "    <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\" style=\"border-spacing: 0; width: 100%; margin: 0 auto; background-color: #f0f0f5; height: 100%; padding: 40px 0;\">\n" +
                "        <tr>\n" +
                "            <td align=\"center\">\n" +
                "                <!-- Card Container -->\n" +
                "                <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\" style=\"max-width:600px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); overflow: hidden;\">\n" +
                "                    <!-- Header Section -->\n" +
                "                    <tr>\n" +
                "                        <td style=\"background-color: #579dff; padding: 28px; text-align: center; color: #ffffff; font-size: 28px; font-weight: bold; border-top-left-radius: 8px; border-top-right-radius: 8px;\">\n" +
                "                            Reset Your Password\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "\n" +
                "                    <!-- Main Content -->\n" +
                "                    <tr>\n" +
                "                        <td style=\"padding: 40px 60px 10px; font-size: 16px; color: #555555; line-height: 1.6;\">\n" +
                "                            <p style=\"margin-bottom: 10px;\">We have received a request to reset your password. To ensure the security of your account, please follow the instructions below to set a new password:</p>\n" +
                "                            <ol style=\"padding-left: 20px; margin: 20px 0;\">\n" +
                "                                <li style=\"margin-bottom: 10px;\">Click on the following link: <strong>[Reset Password Button]</strong></li>\n" +
                "                                <li style=\"margin-bottom: 10px;\">You will be redirected to a page where you can enter your new password.</li>\n" +
                "                                <li style=\"margin-bottom: 10px;\">Choose a strong password that includes a combination of uppercase and lowercase letters, numbers, and special characters.</li>\n" +
                "                                <li style=\"margin-bottom: 10px;\">Confirm your new password by entering it again.</li>\n" +
                "                                <li style=\"margin-bottom: 10px;\">Click on the \"Save Password\" button to save your changes.</li>\n" +
                "                            </ol>\n" +
                "                            <p style=\"margin-bottom: 10px;\">If you did not request a password reset, please ignore this email. Your account will remain secure.</p>\n" +
                "                            <p style=\"font-weight: bold; margin-bottom: 10px;\">Note: This link expires after 30 minutes.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "\n" +
                "                    <!-- Button Section -->\n" +
                "                    <tr>\n" +
                "                        <td style=\"text-align: center; padding: 30px 10px 40px;\">\n" +
                "                            <a href=" + url + " style=\"display: inline-block; padding: 14px 28px; background-color: #579dff; color: #ffffff; text-decoration: none; border-radius: 25px; font-size: 16px; font-weight: bold;\">Reset Your Password</a>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "\n" +
                "                    <!-- Footer Section -->\n" +
                "                    <tr>\n" +
                "                        <td style=\"text-align: center; padding: 20px; font-size: 14px; line-height: 1.6; color: #777777; background-color: #f9f9ff; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;\">\n" +
                "                            <p style=\"margin: 0;\">UNSUBSCRIBE | PRIVACY POLICY | WEB</p>\n" +
                "                            <p style=\"margin: 0;\">© RNB. All rights reserved.</p>\n" +
                "                            <p style=\"margin-top: 20px; font-size: 12px; color: #999;\">This is an automated message, please do not reply.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
    }
}
