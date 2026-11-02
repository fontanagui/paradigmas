package modelo;
import tabuleiro.Posicao;
import java.util.List;

public class JogadorSuporte extends Jogador implements Curandeiro {
    public static final int ATK= 1;
    public static final int DEF= 7;

    private JogadorSimples parceiro;

    public JogadorSuporte(JogadorSimples parceiro, Posicao posicao){
        super ("P2",ATK,DEF,posicao);
        if (ATK>= JogadorSimples.ATK){
            throw new  IllegalStateException("O suporte deve ter ataque menor que o jogador simples");
        }
        setParceiro(parceiro);
        }

    public JogadorSimples getParceiro() {
        return parceiro;
    }

    public void setParceiro (JogadorSimples parceiro) {
        if (parceiro==null){
            throw new IllegalArgumentException("O jogador de suporte exige um jogador simples");
        }
        this.parceiro =parceiro;
    }

    @Override 
    public String getTipo () {
        return "suporte";
    }

    @Override 
    public List<TipoAcao> acoesPermitidas(){
        List<TipoAcao> acoes = super.acoesPermitidas();
        acoes.add(TipoAcao.RECUPERAR_DEFESA);
        return acoes;
    }

    @Override 
    public void recuperarDefesa(Jogador alvo) {
        alvo.ganharDefesa(RECUPERACAO);
    }

    }

