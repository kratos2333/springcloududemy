package com.kevinlearn.accounts.repository;

import com.kevinlearn.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Optional<Accounts> findByCustomerId(Long customerId);

    // notes: good practice to annotate modify db api call with these two annotations
    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);

}
