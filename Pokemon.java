
// import poke.ProjetPokemon.Entite;

public class Pokemon extends Entite {
    private int maxHp;
    private int hp;
    private int attack;
    private int xp;
    private int level;
    private String[] attacks;

    public Pokemon(String name, int maxHp, int attack, String[] attacks) {
        super(name);
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.xp = 0;
        this.level = 1;
        this.attacks = attacks;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public String[] getAttacks() {
        return attacks;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    public void takeDamage(int damage, String type) {
        takeDamage(damage);
    }

    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }
    
    public void gainXp(int amount) {
        xp += amount;
        while (xp >= level * 100) {
            xp -= level * 100;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        increaseStats();
    }
    
    private void increaseStats() {
        maxHp += 10;
        attack += 5;
        hp = maxHp;
    }
}
