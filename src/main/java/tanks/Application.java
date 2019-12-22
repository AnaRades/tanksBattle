package tanks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tanks.battle.models.map.Map;
import tanks.battle.models.map.Row;
import tanks.battle.models.tank.Tank;
import tanks.battle.models.tank.TankBuilder;
import tanks.battle.utils.FACING;
import tanks.battle.utils.Position;
import tanks.mongo.MapRepository;
import tanks.mongo.TankRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static tanks.battle.utils.Constants.PANZER;
import static tanks.battle.utils.Constants.SOVIET;

@SpringBootApplication
public class Application {

	@Autowired
	private TankRepository tankRepositoryGame;

	@Autowired
	private MapRepository mapRepositoryGame;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void init() {

		enterGameData();
		Game.init(tankRepositoryGame.findByName(PANZER), tankRepositoryGame.findByName(SOVIET), mapRepositoryGame.findAll());
	}

	/*
		Enter tank properties and map details in database
	 */
	private void enterGameData() {
		//clear data
		tankRepositoryGame.deleteAll();
		mapRepositoryGame.deleteAll();

		// enter tank details
		TankBuilder tankBuilder = new TankBuilder();
		Tank soviet = tankBuilder.withName(SOVIET).withDamage(4).withHealth(70)
				.withFacing(FACING.BACKWARDS).withPosition(new Position(7, 31)).build();

		tankBuilder = new TankBuilder();
		Tank panzer = tankBuilder.withName(PANZER).withDamage(8).withHealth(60)
				.withFacing(FACING.FORWARD).withPosition(new Position(2,7)).build();
		tankRepositoryGame.save(panzer);
		tankRepositoryGame.save(soviet);

		//enter map details
		mapRepositoryGame.save(generateRandomMap(10, 35));
		mapRepositoryGame.save(generateRandomMap(10, 35));
		mapRepositoryGame.save(generateRandomMap(10, 35));
		mapRepositoryGame.save(generateRandomMap(10, 35));
		mapRepositoryGame.save(generateRandomMap(10, 35));
	}

	/*For new maps each time, use dynamicRandom*/
	private static Random dynamicRandom = new Random(System.currentTimeMillis());
	private static  Random staticRandom = new Random(100);
	private static Map generateRandomMap(int height, int width) {
		List<Row> rows = new ArrayList<>(height);

		for (int i = 0; i < height; i++) {
			Row row = new Row();
			for (int j = 0; j < width; j++) {
				row.add((staticRandom.nextInt(300)%20)>17);
			}
			rows.add(row);
		}
		return new Map(rows);
	}
}
