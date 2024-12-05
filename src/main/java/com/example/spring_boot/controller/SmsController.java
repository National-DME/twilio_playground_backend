import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private TwilioService twilioService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String to, @RequestParam String body) {
        return twilioService.sendMessage(to, body);
    }

    @PostMapping("/incoming")
    public void receiveMessage(@RequestBody MultiValueMap<String, String> formParams) {
        String from = formParams.getFirst("From");
        String body = formParams.getFirst("Body");
        // Store message in database
    }
}