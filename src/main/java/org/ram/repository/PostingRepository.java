package org.ram.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.ram.entity.Posting;

import java.util.Optional;

@ApplicationScoped
public class PostingRepository implements PanacheRepository<Posting> {

    public Optional<Posting> findByPostingNumber(String postingNumber) {
        if(postingNumber == null) {
            return Optional.empty();
        }
        Posting pos = find("postingNumber", postingNumber).firstResult();
        return Optional.ofNullable(pos);
    }

    public Posting save(Posting posting) {
        persistAndFlush(posting);
        return posting;
    }

}
