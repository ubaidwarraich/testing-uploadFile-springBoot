package com.javainuse.db;

import com.javainuse.model.Event;
import com.javainuse.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface newsRepository extends JpaRepository<News, Long> {
    @Query(value="select *from news order by date desc limit 10",nativeQuery = true)
    List<News> findRecent();
}
