package com.kverchi.diary.domain;

import javax.persistence.*;

/**
 * Created by Liudmyla Melnychuk on 22.1.2018.
 */
@Entity
@Table(name="conversations")
public class Conversation {
    @Id
    @Column(name="conversation_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int conversationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id1")
    private User user1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id2")
    private User user2;

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}
