package com.tinqinacademy.comments.persistence.repositories;

import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
