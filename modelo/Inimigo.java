package modelo;
import tabuleiro.Posicao;
import java.util.Random;


public class Inimigo extends Personagem {
    public static final int VALOR_MIN = 1;
    public static final int VALOR_MAX = 3;
    
    private int numero;

    public Inimigo(int numero, Posicao posicao, Random aleatorio){
        this(numero, posicao, VALOR_MIN + aleatorio.nextInt(VALOR_MAX - VALOR_MIN +1)); }


    private Inimigo (int numero, Posicao posicao, int valor) {
        super(valor, valor, posicao);
        setNumero(numero);
    }


    public int getNumero(){
        return  numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override 
    public String getNome(){
        return "Inimigo " + numero;
    }
}