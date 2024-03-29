package com.springboot.blog.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data // Add getters...
@AllArgsConstructor  //
@NoArgsConstructor
@Entity
@Table(name = "post")
@RequiredArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, unique = true)
    @NonNull
    private String title;
    @Column(name = "description", nullable = false)
    @NonNull
    private String description;
    @Column(name = "content", nullable = false)
    @NonNull
    private String content;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
