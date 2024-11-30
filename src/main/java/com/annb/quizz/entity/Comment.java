package com.annb.quizz.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "quizz_id", nullable = false)
    private Quizz quizz;
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false) // Link to Review
    private Review review;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // Reference to parent comment for replies

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> replies = new ArrayList<>(); // Replies to the comment

    private String username; // ID of the user who left the comment
    private String content; // Comment text

    // Helper method for bidirectional relationship
    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParentComment(this);
    }
}