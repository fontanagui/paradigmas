package modelo;

public interface Combatente {
    int getAtk();
    int getDef();
    boolean isVivo();
    void receberDano(int dano);

    default void atacar(Combatente alvo) {
            alvo.receberDano(getAtk());
    }
}
