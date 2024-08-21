package com.tinqinacademy.comments.persistence.repositories;

import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

  Page<Comment> findAllByRoomId(UUID roomId, Pageable pageable);
  Optional<Comment>findByIdAndPublishedBy(UUID id, UUID publishedBy);
}
