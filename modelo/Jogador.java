package modelo;
import tabuleiro.Posicao;
import java.util.ArrayList;
import java.util.List;

public abstract class Jogador extends Personagem {
    private String id;
    protected Jogador (String id, int atk, int def, Posicao posicao){
        super(atk,def,posicao);
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id==null || id.isBlank()){
            throw new IllegalArgumentException("O id não pode ser nulo ou vazio.");
        }
        this.id = id;
    }
    @Override 
    public String getNome(){
        return id + " (" + getTipo() + ")";
    }


    public abstract String getTipo();

    public List <TipoAcao> acoesPermitidas() {
        List <TipoAcao> acoes = new ArrayList<>();
        acoes.add(TipoAcao.ATACAR);
        acoes.add(TipoAcao.PROCURAR);
        return acoes;
    }
}

