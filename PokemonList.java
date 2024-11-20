// package poke.ProjetPokemon;

import java.util.Random;

public class PokemonList {
    public static final Pokemon SALAMECHE = new TypeFeu("Salameche", 100, 200, new String[]{"Flamethrower:400", "Scratch:20"});
    public static final Pokemon BULBIZARRE = new TypeHerbe("Bulbizarre", 120, 15, new String[]{"Vine Whip:30", "Tackle:10"});
    public static final Pokemon CARAPUCE = new TypeEau("Carapuce", 110, 20, new String[]{"Water Gun:30", "Tackle:10"});

    private static final Pokemon[] POKEMONS = {SALAMECHE, BULBIZARRE, CARAPUCE};

    public static Pokemon getRandomPokemon() {
        Random random = new Random();
        return POKEMONS[random.nextInt(POKEMONS.length)];
    }

    public static Pokemon getPlayerPokemon() {
        return SALAMECHE;
    }
}
