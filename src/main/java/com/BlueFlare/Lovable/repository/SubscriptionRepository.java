package com.BlueFlare.Lovable.repository;

import com.BlueFlare.Lovable.entity.Subscription;
import com.BlueFlare.Lovable.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> statusSet);

    boolean existsByStripeSubscriptionId(String subscriptionId);

   Optional< Subscription> findByStripeSubscriptionId(String gatewaySubscriptionId);
}
