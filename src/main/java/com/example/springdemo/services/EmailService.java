package com.example.springdemo.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class EmailService {

//    private final String sendGridApiKey = System.getenv("SENDGRID_API_KEY");

    private final SendGrid sendGrid = new SendGrid("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");

    /**
     * tokenServices is used to handle information of the token.
     */
    private final TokenService tokenService = new TokenService();

    /**
     * userService is used to handel information of the user.
     */
    private final UserService userService = new UserService();


    /**
     * The method is used to generate random number.
     *
     * @return Return random number.
     */
    public int generateRandomNumber() {
        Random random = new Random();

        int min = 100000;
        int max = 999999;

        return random.nextInt(max - min + 1) + min;
    }


    /**
     * The method is used to send email contains authentication code to the email address of the user.
     *
     * @param toEmail Email address of the user.
     */
    public int sendEmail(String toEmail) {
        String tokenValue = String.valueOf(generateRandomNumber());
        Email from = new Email("tinh4451190119@st.qnu.edu.vn");
        Email to = new Email(toEmail);

        String emailContent = "" +
                "<div style=\"width: 400px; padding: 0; margin: 0; border: 2px solid #9e9c9c; box-shadow: 0 0 20px rgba(0, 0, 0, 0.3); font-family: sans-serif;\">\n" +
                "        <h2 style=\"margin: 0; padding: 10px 20px; background-color: #007bff; color: #fff;\">Forum web</h2>\n" +
                "        <p style=\"padding: 10px 20px;font-size: 18px;\">Your authentication code is: <span style=\"font-weight: bold; font-size: 20px;\">" + tokenValue + "</span></p>\n" +
                "        <p style=\"margin: 0; padding: 10px 20px; background-color: #e2dfdf;font-size: 15px;\">The authentication code is valid for 5 minutes.</p>\n" +
                " </div>";

        Content content = new Content("text/html", emailContent);

        Mail mail = new Mail(from, "Authentication code to change password", to, content);

        Request request = new Request();
        try {
            tokenService.saveToken(toEmail, tokenValue);
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                return 1;
            } else {
                System.out.println("Error sending email: " + response.getBody());
                return -1;
            }
        } catch (IOException ex) {
            System.out.println("Error sending email: " + ex.getMessage());
            return -1;
        }
    }


}