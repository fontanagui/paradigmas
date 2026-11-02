package tabuleiro;
import modelo.Inimigo;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Setor {

    private  Posicao posicao;
    private  Set<Direcao> portas = EnumSet.noneOf(Direcao.class);
    private List <Inimigo> inimigos= new ArrayList<>();
    private boolean visitado;

    protected Setor (Posicao posicao, Set<Direcao> portas) {
        setPosicao(posicao);
        setPortas(portas);
    }

    public abstract String getTipo();

    public boolean permiteEscolherAlvo () {
        return true;
    }

    public boolean ataqueAtinge (Random aleatorio) {
        return true;
    }
    
    public boolean permiteProcura(){
        return true;
    }

    public Posicao getPosicao(){
        return posicao;
    }

    public  void setPosicao(Posicao posicao){
        if (posicao == null) {
            throw new IllegalArgumentException(" Posicao nao pode ser nula");
        }
        this.posicao= posicao;
    }

    public Set<Direcao> getPortas() {
        Set<Direcao> copia = EnumSet.noneOf(Direcao.class);
        copia.addAll(portas);
        return copia;
    }

    public void setPortas (Set<Direcao> portas) {
        if (portas == null) {
            throw new IllegalArgumentException("Portas nao  podem ser nulas");
        }
        this.portas= EnumSet.noneOf(Direcao.class);
        this.portas.addAll(portas);

    }

    public boolean isVisitado(){
        return visitado;
    }

    public void setVisitado(boolean visitado){
        this.visitado=visitado;
    }

    public List <Inimigo> getInimigosVivos() {
        return inimigos.stream().filter(Inimigo::isVivo).toList();
    }

    public void addInimigo(Inimigo inimigo){
        inimigos.add(inimigo);
    }

    public boolean havePorta (Direcao direcao) {
        return portas.contains(direcao);
    }
    public boolean temInimigosVivos(){
        return  inimigos.stream().anyMatch(Inimigo::isVivo);
    }
}

