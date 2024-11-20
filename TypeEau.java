// package poke.ProjetPokemon;

// import poke.ProjetPokemon.Pokemon;

public class TypeEau extends Pokemon {
    public TypeEau(String name, int maxHp, int attack, String[] attacks) {
        super(name, maxHp, attack, attacks);
    }

    public String getWeakness() {
        return "Herbe";
    }

    @Override
    public void takeDamage(int damage, String type) {
        if (type.equals("Herbe")) {
            damage *= 2;
        }
        super.takeDamage(damage);
    }
}