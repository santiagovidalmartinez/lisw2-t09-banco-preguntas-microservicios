package co.edu.unicauca.review.repository;

import co.edu.unicauca.review.model.Review;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ReviewRepository {

    private final Map<Long, Review> store = new ConcurrentHashMap<>();

    public Review save(Review review) {
        store.put(review.getId(), review);
        return review;
    }

    public Optional<Review> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
