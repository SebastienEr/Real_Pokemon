// package poke.ProjetPokemon;

// import poke.ProjetPokemon.Pokemon;

public class TypeFeu extends Pokemon {
    public TypeFeu(String name, int maxHp, int attack, String[] attacks) {
        super(name, maxHp, attack, attacks);
    }

    public String getWeakness() {
        return "Eau";
    }

    @Override
    public void takeDamage(int damage, String type) {
        if (type.equals("Eau")) {
            damage *= 2;
        }
        super.takeDamage(damage);
    }
}