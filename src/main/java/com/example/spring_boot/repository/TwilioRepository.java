import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entities.SmsMessage;

public interface TwilioRepository extends JpaRepository<SmsMessage, Long> {
    
}