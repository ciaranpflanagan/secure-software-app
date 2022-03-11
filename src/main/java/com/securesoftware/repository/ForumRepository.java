package com.securesoftware.repository;


import com.securesoftware.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<ForumPost, Long> {
    ForumPost findByParentId(Long parent_id);
}
