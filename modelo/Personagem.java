package modelo;
import tabuleiro.Posicao;
public abstract class Personagem implements Combatente {
    private int atk;
    private int def;
    private Posicao posicao;

    protected Personagem(int atk, int def, Posicao posicao) {
        setAtk(atk);
        setDef(def);
        setPosicao(posicao);
    }

    @Override 
    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        if (atk < 0) {
            throw new IllegalArgumentException("O valor de ataque não pode ser negativo.");
        }
        this.atk = atk;
    }

    @Override 
    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        if (posicao == null) {
            throw new IllegalArgumentException("A posição não pode ser nula.");
        }
        this.posicao = posicao;
    }


    @Override 
    public boolean isVivo() {
        return def > 0;
    }

    @Override 
    public void receberDano(int dano) {
        setDef(def - dano);
    }

    public void ganharDefesa(int quantidade) {
        setDef(def + quantidade);
    }

    public String getAtkDef(){
        return atk + "/" + Math.max(def, 0);
    }

    public abstract String getNome();
}