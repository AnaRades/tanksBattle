package tanks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tanks.battle.models.map.Map;
import tanks.battle.models.map.Row;
import tanks.battle.models.tank.Tank;
import tanks.battle.models.tank.TankBuilder;
import tanks.battle.models.tank.utils.FACING;
import tanks.battle.models.tank.utils.Position;
import tanks.mongo.MapRepository;
import tanks.mongo.TankRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

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
	}


	private void enterGameData() {
		//clear data
		tankRepositoryGame.deleteAll();
		mapRepositoryGame.deleteAll();

		// enter tank details
		TankBuilder tankBuilder = new TankBuilder();
		Tank soviet = tankBuilder.withName("Soviet").withDamage(30).withHealth(70)
				.withFacing(FACING.BACKWARDS).withPosition(new Position(10, 7)).build();

		tankBuilder = new TankBuilder();
		Tank panzer = tankBuilder.withName("Panzer").withDamage(40).withHealth(90)
				.withFacing(FACING.FORWARD).withPosition(new Position(2,7)).build();
		tankRepositoryGame.save(panzer);
		tankRepositoryGame.save(soviet);

		//enter map details
		mapRepositoryGame.save(createGameMap());

		// TBR: fetch all tanks
		System.out.println("Tanks found with findAll():");
		System.out.println("-------------------------------");
		for (Tank tank : tankRepositoryGame.findAll()) {
			System.out.println(tank.getName());
		}
		System.out.println();

		// fetch a map by ID
		System.out.println("Map found by ID:0 :");
		System.out.println("--------------------------------");
		System.out.println(mapRepositoryGame.findById("map-0").get());

	}

	private Map createGameMap() {
		ArrayList<Row> rows = new ArrayList<>(11);

		boolean[] row1 =   new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[] row2 =   new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[]  row3 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[]  row4 =  new boolean[]{true, true, true, true, true, false, false, false, false, true, true, true, false, false};
		boolean[]  row5 =  new boolean[]{true, true, true, true, true, false, false, false, false, false, true, true, false, false};
		boolean[]  row6 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[]  row7 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[]  row8 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[]  row9 =  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[]  row10 = new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[]  row11=  new boolean[]{true, true, true, false, false, false, false, false, true, true, true, true, false, false};
		boolean[][] map = new boolean[][]{row1, row2, row3, row4, row5, row6, row7, row8, row9, row10, row11};

		for(int i=0; i<11; i++) {
			rows.add(new Row(map[i]));
		}
		return new Map(rows);
	}

}
