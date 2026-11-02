package tabuleiro;

import java.util.Random;
import java.util.Set;
public class SetorOculto extends Setor {
    public SetorOculto (Posicao posicao, Set <Direcao> portas) {
        super(posicao, portas);
    }
    

    @Override 
    public  String getTipo() {
        return "oculto";
    }
    

    @Override 
    public boolean permiteEscolherAlvo() {
        return false;
    }

    @Override 
    public boolean ataqueAtinge (Random aleatorio) {
        return aleatorio.nextBoolean();
    }
}
