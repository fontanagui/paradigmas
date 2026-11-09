package tabuleiro;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;


public class Tabuleiro {

    public static final int TAMANHO = 5;
    public static final Posicao CENTRO = new Posicao(3,3);

    private static final String [] PORTAS_LATERAIS = {
        "1101",
        "1011",
        "1111",
        "0110",
        "1011"
        };
        
    private static final String[] PORTAS_VERTICAIS = {
        "11010",
        "01110",
        "10111",
        "11001"
        };
    

    private final Setor [][] setores  = new Setor[TAMANHO][TAMANHO];
    private Posicao fonte;

    public Tabuleiro(Random aleatorio) {
        for (int l=1; l<= TAMANHO; l++) {
            for (int c=1;c<=TAMANHO;c++) {
                Posicao p = new Posicao(l,c);
                setores[l-1][c-1]= criaSetor(p,p.equals(CENTRO),aleatorio);
            }
        }
        setFonte(sortearFonte(aleatorio));
        getSetor(CENTRO).setVisitado(true);
        if(alcancaveis() != TAMANHO*TAMANHO){
            throw new IllegalStateException("setores inalcancaveis");
        }
    }

    private Setor criaSetor(Posicao p ,boolean centro, Random aleatorio) {
        Set <Direcao> portas =  EnumSet.noneOf(Direcao.class);
        for (Direcao d:Direcao.values()) {
            if (havePorta(p,d)) {
                portas.add(d);
            }
        }
        if (centro){
            return new SetorNormal(p, portas);
        }
        int sorteio = aleatorio.nextInt(4);
        return switch (sorteio){
            case 0 -> new SetorOculto(p, portas);
            case 1 -> new SetorPrivado(p, portas);
            default -> new SetorNormal(p, portas);
        };
    }

    private boolean havePorta(Posicao p, Direcao d){
        Posicao v = p.vizinha(d);
        if (!dentro(v)) {
            return false;
        }
        return switch (d){
            case DIREITA -> PORTAS_LATERAIS[p.x() - 1].charAt(p.y() - 1) == '1';
            case ESQUERDA -> PORTAS_LATERAIS[p.x() - 1].charAt(p.y() - 2) == '1';
            case BAIXO -> PORTAS_VERTICAIS[p.x() - 1].charAt(p.y() - 1) == '1';
            case CIMA -> PORTAS_VERTICAIS[p.x() - 2].charAt(p.y() - 1) == '1';
        };
    }
    private Posicao sortearFonte(Random aleatorio) {
        Posicao p;
        do {
            p= new Posicao(1+aleatorio.nextInt(TAMANHO),1+aleatorio.nextInt(TAMANHO));
        } while(p.equals(CENTRO));
        return p;
    }

    private  int alcancaveis() {
        Set<Posicao> vistos = new HashSet<>();
        Deque<Posicao> fila = new ArrayDeque<>();
        fila.add(CENTRO);
        vistos.add(CENTRO);
        while (!fila.isEmpty()) {
            Posicao atual = fila.poll();
            for(Direcao d : getSetor(atual).getPortas()) {
                Posicao v = atual.vizinha(d);
                if(vistos.add(v)){
                    fila.add(v);
                 }
            }
            
        }
        return vistos.size();
    }

    private boolean dentro(Posicao p) {
        return p.x() >= 1 && p.x() <= TAMANHO
            && p.y() >= 1 && p.y() <= TAMANHO;
    }

    public Setor getSetor(Posicao p) {
        if (p == null || !dentro(p)) {
            throw new IllegalArgumentException("A posição deve estar dentro do tabuleiro.");
        }
        return setores[p.x() - 1][p.y() - 1];
    }

    public Posicao getFonte() {
        return fonte;
    }

    public void setFonte(Posicao fonte) {
        if (fonte == null || !dentro(fonte) || fonte.equals(CENTRO)) {
            throw new IllegalArgumentException("A fonte deve estar dentro do tabuleiro e fora do centro.");
        }
        this.fonte = fonte;
    }
}

