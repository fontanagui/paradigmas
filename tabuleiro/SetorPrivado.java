package tabuleiro;

import java.util.Set;


public class SetorPrivado extends Setor{
    public SetorPrivado(Posicao posicao, Set <Direcao> portas) {
        super(posicao, portas);}

    @Override 
    public String getTipo (){
        return "privado";

    }


    @Override 
    public boolean permiteProcura(){
        return false;
    }
}
