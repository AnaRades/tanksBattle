package tanks.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import tanks.battle.models.tank.Tank;

import java.util.List;

public interface TankRepository extends MongoRepository<Tank, String> {

    Tank findByName(String name);
    List<Tank> findAll();
}
