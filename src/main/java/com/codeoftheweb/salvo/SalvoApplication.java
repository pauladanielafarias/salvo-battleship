package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
	/*PASSWORD ENCODER */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}


	/*COMMAND LINE RUNNER*/
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ScoreRepository scoreRepository) {
		return (args) -> {
			/* 1) creating the players*/
			Player p1 = new Player("Jack", "Bauer", "j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player p2 = new Player("Chloe", "O'Brian", "c.obrian@ctu.gov", passwordEncoder().encode("42"));
			Player p3 = new Player("Kim", "Bauer", "kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			Player p4 = new Player("Tony", "Almeida", "t.almeida@ctu.gov", passwordEncoder().encode("mole"));

			/*saving the players*/
			playerRepository.save(p1);
			playerRepository.save(p2);
			playerRepository.save(p3);
			playerRepository.save(p4);

			/* 2) creating the games*/
			Game g1 = new Game(LocalDateTime.now());
			Game g2 = new Game(LocalDateTime.now().plusHours(1));
			Game g3 = new Game(LocalDateTime.now().plusHours(2));
			Game g4 = new Game(LocalDateTime.now().plusMinutes(30));
			Game g5 = new Game(LocalDateTime.now().plusMinutes(15));
			Game g6 = new Game(LocalDateTime.now().plusHours(1));
			Game g7 = new Game(LocalDateTime.now().plusMinutes(20));
			Game g8 = new Game(LocalDateTime.now());


			/*saving the games*/
			gameRepository.save(g1);
			gameRepository.save(g2);
			gameRepository.save(g3);
			gameRepository.save(g4);
			gameRepository.save(g5);
			gameRepository.save(g6);
			gameRepository.save(g7);
			gameRepository.save(g8);




			/* 3) creating the gamePlayers*/
			GamePlayer gp1 = new GamePlayer(LocalDateTime.now(), g1, p1);
			GamePlayer gp2 = new GamePlayer(LocalDateTime.now(), g1, p2);

			GamePlayer gp3 = new GamePlayer(LocalDateTime.now(), g2, p1);
			GamePlayer gp4 = new GamePlayer(LocalDateTime.now(), g2, p2);

			GamePlayer gp5 = new GamePlayer(LocalDateTime.now(), g3, p2);
			GamePlayer gp6 = new GamePlayer(LocalDateTime.now(), g3, p4);

			GamePlayer gp7 = new GamePlayer(LocalDateTime.now(), g4, p2);
			GamePlayer gp8 = new GamePlayer(LocalDateTime.now(), g4, p1);

			GamePlayer gp9 = new GamePlayer(LocalDateTime.now(), g5, p4);
			GamePlayer gp10 = new GamePlayer(LocalDateTime.now(), g5, p1);

			GamePlayer gp11 = new GamePlayer(LocalDateTime.now(), g6, p3);
			//GamePlayer gp12 = new GamePlayer(LocalDateTime.now(), g6);

			GamePlayer gp13 = new GamePlayer(LocalDateTime.now(), g7, p4);
			//GamePlayer gp14 = new GamePlayer(LocalDateTime.now(), g7);

			GamePlayer gp15 = new GamePlayer(LocalDateTime.now(), g8, p3);
			GamePlayer gp16 = new GamePlayer(LocalDateTime.now(), g8, p4);



			/* 4) creating the ships*/

			Ship s1 = new Ship(new ArrayList<>(Arrays.asList("H2", "H3", "H4")), "destroyer");
			Ship s2 = new Ship(new ArrayList<>(Arrays.asList("E1", "F1", "G1")), "submarine");
			Ship s3 = new Ship(new ArrayList<>(Arrays.asList("B4", "B5")), "patrolBoat");
			Ship s4 = new Ship(new ArrayList<>(Arrays.asList("B5", "C5", "D5")), "destroyer");
			Ship s5 = new Ship(new ArrayList<>(Arrays.asList("F1", "F2")), "patrolBoat");

			Ship s6 = new Ship(new ArrayList<>(Arrays.asList("B5", "C5", "D5")), "destroyer");
			Ship s7 = new Ship(new ArrayList<>(Arrays.asList("C6", "C7")), "patrolBoat");
			Ship s8 = new Ship(new ArrayList<>(Arrays.asList("A2", "A3", "A4")), "submarine");
			Ship s9 = new Ship(new ArrayList<>(Arrays.asList("G6", "H6")), "patrolBoat");

			Ship s10 = new Ship(new ArrayList<>(Arrays.asList("B5", "C5", "D5")), "destroyer");
			Ship s11 = new Ship(new ArrayList<>(Arrays.asList("C6", "C7")), "patrolBoat");
			Ship s12 = new Ship(new ArrayList<>(Arrays.asList("A2", "A3", "A4")), "submarine");
			Ship s13 = new Ship(new ArrayList<>(Arrays.asList("G6", "H6")), "patrolBoat");

			Ship s14 = new Ship(new ArrayList<>(Arrays.asList("B5", "C5", "D5")), "destroyer");
			Ship s15 = new Ship(new ArrayList<>(Arrays.asList("C6", "C7")), "patrolBoat");
			Ship s16 = new Ship(new ArrayList<>(Arrays.asList("A2", "A3", "A4")), "submarine");
			Ship s17 = new Ship(new ArrayList<>(Arrays.asList("G6", "H6")), "patrolBoat");

			Ship s18 = new Ship(new ArrayList<>(Arrays.asList("B5", "C5", "D5")), "destroyer");
			Ship s19 = new Ship(new ArrayList<>(Arrays.asList("C6", "C7")), "patrolBoat");
			Ship s20 = new Ship(new ArrayList<>(Arrays.asList("A2", "A3", "A4")), "submarine");
			Ship s21 = new Ship(new ArrayList<>(Arrays.asList("G6", "H6")), "patrolBoat");

			Ship s22 = new Ship(new ArrayList<>(Arrays.asList("B5", "C5", "D5")), "destroyer");
			Ship s23 = new Ship(new ArrayList<>(Arrays.asList("C6", "C7")), "patrolBoat");

			Ship s24 = new Ship(new ArrayList<>(Arrays.asList("B5", "C5", "D5")), "destroyer");
			Ship s25 = new Ship(new ArrayList<>(Arrays.asList("G6", "H6")), "patrolBoat");

			Ship s26 = new Ship(new ArrayList<>(Arrays.asList("A2", "A3", "A4")), "sumbarine");
			Ship s27 = new Ship(new ArrayList<>(Arrays.asList("G6", "H6")), "patrolBoat");



			/* Another way to make a new list
			List<String> list = new ArrayList<>();
			list.add("a1");
			*/

			/*adding the ships to the gamePlayers*/
			gp1.addShip(s1);
			gp1.addShip(s2);
			gp1.addShip(s3);

			gp2.addShip(s4);
			gp2.addShip(s5);

			gp3.addShip(s6);
			gp3.addShip(s7);

			gp4.addShip(s8);
			gp4.addShip(s9);

			gp5.addShip(s10);
			gp5.addShip(s11);

			gp6.addShip(s12);
			gp6.addShip(s13);

			gp7.addShip(s14);
			gp7.addShip(s15);

			gp8.addShip(s16);
			gp8.addShip(s17);

			gp9.addShip(s18);
			gp9.addShip(s19);

			gp10.addShip(s20);
			gp10.addShip(s21);

			gp11.addShip(s22);
			gp11.addShip(s23);

			gp15.addShip(s24);
			gp15.addShip(s25);

			gp16.addShip(s26);
			gp16.addShip(s27);


			/* 5) creating the salvoes*/
			Salvo salvo1 = new Salvo(new ArrayList<>(Arrays.asList("B5", "C5", "F1")), 1);
			Salvo salvo2 = new Salvo(new ArrayList<>(Arrays.asList("B4", "B5", "B6")), 1);
			Salvo salvo3 = new Salvo(new ArrayList<>(Arrays.asList("F2", "D5")), 2);
			Salvo salvo4 = new Salvo(new ArrayList<>(Arrays.asList("E1", "H3", "A2")), 2);

			Salvo salvo5 = new Salvo(new ArrayList<>(Arrays.asList("A2", "A4", "G6")), 1);
			Salvo salvo6 = new Salvo(new ArrayList<>(Arrays.asList("B5", "D5", "C7")), 1);
			Salvo salvo7 = new Salvo(new ArrayList<>(Arrays.asList("A3", "H6")), 2);
			Salvo salvo8 = new Salvo(new ArrayList<>(Arrays.asList("C5", "C6")), 2);

			Salvo salvo9 = new Salvo(new ArrayList<>(Arrays.asList("G6", "H6", "A4")), 1);
			Salvo salvo10 = new Salvo(new ArrayList<>(Arrays.asList("H1", "H2", "H3")), 1);
			Salvo salvo11 = new Salvo(new ArrayList<>(Arrays.asList("A2", "A3", "D8")), 2);
			Salvo salvo12 = new Salvo(new ArrayList<>(Arrays.asList("E1", "F2", "G3")), 2);

			Salvo salvo13 = new Salvo(new ArrayList<>(Arrays.asList("A3", "A4", "F7")), 1);
			Salvo salvo14 = new Salvo(new ArrayList<>(Arrays.asList("B5", "C6", "H1")), 1);
			Salvo salvo15 = new Salvo(new ArrayList<>(Arrays.asList("A2", "G6", "H6")), 2);
			Salvo salvo16 = new Salvo(new ArrayList<>(Arrays.asList("C5", "C7", "D5")), 2);

			Salvo salvo17 = new Salvo(new ArrayList<>(Arrays.asList("A1", "A2", "A3")), 1);
			Salvo salvo18 = new Salvo(new ArrayList<>(Arrays.asList("B5", "B6", "C7")), 1);
			Salvo salvo19 = new Salvo(new ArrayList<>(Arrays.asList("G6", "G7", "G8")), 2);
			Salvo salvo20 = new Salvo(new ArrayList<>(Arrays.asList("C6", "D6", "E6")), 2);
			Salvo salvo21 = new Salvo(new ArrayList<>(Arrays.asList("H1", "H8")), 3);



			/* Another way to make a new list
			List<String> list = new ArrayList<>();
			list.add("a1");
			*/

			/*adding the salvoes to the gamePlayers*/
			gp1.addSalvo(salvo1);
			gp2.addSalvo(salvo2);
			gp1.addSalvo(salvo3);
			gp2.addSalvo(salvo4);

			gp3.addSalvo(salvo5);
			gp4.addSalvo(salvo6);
			gp3.addSalvo(salvo7);
			gp4.addSalvo(salvo8);

			gp5.addSalvo(salvo9);
			gp6.addSalvo(salvo10);
			gp5.addSalvo(salvo11);
			gp6.addSalvo(salvo12);

			gp7.addSalvo(salvo13);
			gp8.addSalvo(salvo14);
			gp7.addSalvo(salvo15);
			gp8.addSalvo(salvo16);

			gp9.addSalvo(salvo17);
			gp10.addSalvo(salvo18);
			gp9.addSalvo(salvo19);
			gp10.addSalvo(salvo20);
			gp10.addSalvo(salvo21);


			/* 6) saving the game players*/
			gamePlayerRepository.save(gp1);
			gamePlayerRepository.save(gp2);
			gamePlayerRepository.save(gp3);
			gamePlayerRepository.save(gp4);
			gamePlayerRepository.save(gp5);
			gamePlayerRepository.save(gp6);
			gamePlayerRepository.save(gp7);
			gamePlayerRepository.save(gp8);
			gamePlayerRepository.save(gp9);
			gamePlayerRepository.save(gp10);
			gamePlayerRepository.save(gp11);
			//gamePlayerRepository.save(gp12);
			gamePlayerRepository.save(gp13);
			//gamePlayerRepository.save(gp14);
			gamePlayerRepository.save(gp15);
			gamePlayerRepository.save(gp16);



			/* 7) creating the scores*/
			Score score1 = new Score(p1, g1, 1, LocalDateTime.now());
			Score score2 = new Score(p2, g1, 0, LocalDateTime.now());

			Score score3 = new Score(p1, g2, 0.5, LocalDateTime.now());
			Score score4 = new Score(p2, g2, 0.5, LocalDateTime.now());

			Score score5 = new Score(p2, g3, 1, LocalDateTime.now());
			Score score6 = new Score(p4, g3, 0, LocalDateTime.now());

			Score score7 = new Score(p2, g4, 0.5, LocalDateTime.now());
			Score score8 = new Score(p1, g4, 0.5, LocalDateTime.now());

			Score score9 = new Score(p4, g5, LocalDateTime.now());
			Score score10 = new Score(p1, g5, LocalDateTime.now());

			Score score11 = new Score(p3, g6, LocalDateTime.now());
			Score score12 = new Score(g6, LocalDateTime.now());

			Score score13 = new Score(g7, LocalDateTime.now());
			Score score14 = new Score(g7, LocalDateTime.now());

			Score score15 = new Score(p3, g8, LocalDateTime.now());
			Score score16 = new Score(p4, g8, LocalDateTime.now());


			/* saving the scores*/
			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);
			scoreRepository.save(score7);
			scoreRepository.save(score8);
			scoreRepository.save(score9);
			scoreRepository.save(score10);
			scoreRepository.save(score11);
			scoreRepository.save(score12);
			scoreRepository.save(score13);
			scoreRepository.save(score14);
			scoreRepository.save(score15);
			scoreRepository.save(score16);

		};
	}

}
