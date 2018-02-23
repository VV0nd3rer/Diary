package com.kverchi.diary.domain;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by Liudmyla Melnychuk on 21.12.2017.
 */
@Entity
@Table(name="messages")
public class Message {
    @Id
    @Column(name="message_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int messageId;
    private String text;

    /*@Column(name="sender_id")
        private int senderId;*/
    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="conversation_id")
    private Conversation conversation;

    @Column(name="sent_datetime")
    private ZonedDateTime sentDatetime;

    @Column(name="is_read")
    private boolean read;

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public ZonedDateTime getSentDatetime() {
        return sentDatetime;
    }

    @PrePersist
    @PreUpdate
    public void setSentDatetime() {
        this.sentDatetime = ZonedDateTime.now();
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

   public User getSender() {
       return sender;
   }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }


}
