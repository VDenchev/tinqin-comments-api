package com.tinqinacademy.comments.persistence.entities.comment;

import com.tinqinacademy.comments.persistence.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "comments")
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Comment extends BaseEntity {

  @Column(name = "content", nullable = false, length = 1000)
  private String content;

  @Column(name = "first_name", nullable = false, length = 40)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 40)
  private String lastName;

  @Column(name = "sender_id", nullable = false)
  private UUID senderId;

  @Column(name = "room_id", nullable = false)
  private UUID roomId;

  @Column(name = "last_edited_by", nullable = false)
  private UUID lastEditedBy;

  @CreationTimestamp
  private LocalDateTime publishDate;

  @UpdateTimestamp
  private LocalDateTime lastEditedDate;
}
