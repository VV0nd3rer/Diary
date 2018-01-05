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
    private User user;
    @Column(name="receiver_id")
    private int receiverId;
    @Column(name="is_read")
    private boolean isRead;
    @Column(name="message_datetime")
    private ZonedDateTime messageDatetime;

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

   /* public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }*/
   public User getUser() {
       return user;
   }

    public void setUser(User user) {
        this.user = user;
    }
    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public ZonedDateTime getMessageDatetime() {
        return messageDatetime;
    }

    public void setMessageDatetime(ZonedDateTime messageDatetime) {
        this.messageDatetime = messageDatetime;
    }
}
