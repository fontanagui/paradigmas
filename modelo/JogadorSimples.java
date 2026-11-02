package modelo;
import tabuleiro.Posicao;

public class JogadorSimples extends Jogador {

    public static final int ATK = 2;
    public static final int DEF = 6;


    public JogadorSimples (Posicao posicao){
        super ("P1", ATK, DEF, posicao);
    }

    @Override 
    public String getTipo () {
        return "simples";
    }
}