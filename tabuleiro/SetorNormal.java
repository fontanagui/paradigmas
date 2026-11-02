package tabuleiro;

import java.util.Set;

public class SetorNormal extends Setor {

    public  SetorNormal(Posicao posicao, Set<Direcao> portas) {
        super(posicao, portas);
        }
    

        @Override 
        public String getTipo(){
            return "normal";
        }
}
