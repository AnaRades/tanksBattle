package tanks.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import tanks.battle.models.map.Map;

import java.util.List;
import java.util.Optional;

public interface MapRepository  extends MongoRepository<Map, String> {

    Optional<Map> findById(String id);
    List<Map> findAll();

}
