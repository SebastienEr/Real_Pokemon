// package poke.ProjetPokemon;

// import poke.ProjetPokemon.Pokemon;

public class TypeHerbe extends Pokemon {
    public TypeHerbe(String name, int maxHp, int attack, String[] attacks) {
        super(name, maxHp, attack, attacks);
    }

    public String getWeakness() {
        return "Feu";
    }

    @Override
    public void takeDamage(int damage, String type) {
        if (type.equals("Feu")) {
            damage *= 2;
        }
        super.takeDamage(damage);
    }
}