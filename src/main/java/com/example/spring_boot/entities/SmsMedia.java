package com.example.spring_boot.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "sms_media")
public class SmsMedia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private SmsMessage smsMessage;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name ="media_content_type")
    private String mediaContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
       this.id = id; 
    }

    public SmsMessage getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(SmsMessage smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaContentType() {
        return mediaContentType;
    }

    public void setMediaContentType(String mediaContentType) {
        this.mediaContentType = mediaContentType;
    }
}
