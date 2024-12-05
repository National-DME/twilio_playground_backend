import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.spring_boot.repository.TwilioRepository;

@Service 
public class TwilioService {

    @Autowired
    private TwilioRepository twilioRepository;

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public TwilioService() {
        Twilio.init(accountSid, authToken);
    }

    public String sendMessage(String to, String body) {
        Message message = Message.creator(
            new com.twilio.type.PhoneNumber(to),
            new com.twilio.type.PhoneNumber(twilioPhoneNumber),
            body
        ).create();
        return message.getSid();
    }

    public String saveMessage(String from, String body) {
        SmsMessage smsMessage = new SmsMessage(from, body);
        twilioRepository.save(smsMessage);
    }
}