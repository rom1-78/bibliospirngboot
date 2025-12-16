package fr.ensitech.biblio.repository;

import fr.ensitech.biblio.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IPasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    void deleteByUserId(Long userId);
}