package jogo;

import modelo.Inimigo;
import modelo.Jogador;
import modelo.JogadorSimples;
import modelo.JogadorSuporte;
import modelo.TipoAcao;
import tabuleiro.Direcao;
import tabuleiro.Posicao;
import tabuleiro.Setor;
import tabuleiro.Tabuleiro;
import java.util.List;
import java.util.Random;

public class Jogo {

    private final Random aleatorio;
    private final Tabuleiro tabuleiro;
    private final JogadorSimples jogadorSimples;
    private final JogadorSuporte jogadorSuporte;
    private Jogador jogadorAtual;
    private Jogador vencedor;
    private int ciclo;
    private int acoes;
    private boolean movimentou;

    public Jogo() {
        aleatorio = new Random();
        tabuleiro = new Tabuleiro(aleatorio);
        jogadorSimples = new JogadorSimples(Tabuleiro.CENTRO);
        jogadorSuporte = new JogadorSuporte(jogadorSimples, Tabuleiro.CENTRO);
        jogadorAtual = jogadorSimples;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public JogadorSimples getJogadorSimples() {
        return jogadorSimples;
    }

    public JogadorSuporte getJogadorSuporte() {
        return jogadorSuporte;
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public Jogador getVencedor() {
        return vencedor;
    }

    public int getCiclo() {
        return ciclo;
    }

    public boolean terminou() {
        return vencedor != null || ciclo > 25
            || (!jogadorSimples.isVivo() && !jogadorSuporte.isVivo());
    }

    public void mover(Jogador jogador, Direcao direcao) {
        validarTurno(jogador);
        if (acoes > 0 || movimentou) {
            throw new IllegalStateException("O jogador já se movimentou neste turno");
        }

        Setor setorAtual = tabuleiro.getSetor(jogador.getPosicao());
        if (setorAtual.temInimigosVivos()) {
            throw new IllegalStateException("O jogador não pode sair de um setor com inimigos vivos");
        }
        if (direcao == null || !setorAtual.havePorta(direcao)) {
            throw new IllegalArgumentException("Não existe uma porta nessa direção");
        }

        Posicao posicao = jogador.getPosicao().vizinha(direcao);
        Setor setor = tabuleiro.getSetor(posicao);
        jogador.setPosicao(posicao);
        movimentou = true;

        if (posicao.equals(tabuleiro.getFonte())) {
            vencedor = jogador;
            return;
        }

        if (!setor.isVisitado()) {
            setor.setVisitado(true);
            int quantidade = 1 + aleatorio.nextInt(3);
            for (int numero = 1; numero <= quantidade; numero++) {
                setor.addInimigo(new Inimigo(numero, posicao, aleatorio));
            }
        }
    }

    public boolean atacar(Jogador jogador, Inimigo alvo) {
        validarAcao(jogador, TipoAcao.ATACAR);
        Setor setor = tabuleiro.getSetor(jogador.getPosicao());
        List<Inimigo> inimigos = setor.getInimigosVivos();
        if (inimigos.isEmpty()) {
            throw new IllegalStateException("Não há inimigos vivos neste setor");
        }

        boolean atingiu = setor.ataqueAtinge(aleatorio);
        if (atingiu) {
            if (setor.permiteEscolherAlvo()) {
                if (alvo == null || !inimigos.contains(alvo)) {
                    throw new IllegalArgumentException("Escolha um inimigo vivo deste setor");
                }
            } else {
                alvo = inimigos.get(aleatorio.nextInt(inimigos.size()));
            }
            jogador.atacar(alvo);
        }

        finalizarAcao();
        return atingiu;
    }

    public int procurar(Jogador jogador) {
        validarAcao(jogador, TipoAcao.PROCURAR);
        Setor setor = tabuleiro.getSetor(jogador.getPosicao());
        if (!setor.permiteProcura()) {
            throw new IllegalStateException("Não é permitido procurar neste setor");
        }

        int resultado = 1 + aleatorio.nextInt(6);
        if (resultado == 4) {
            jogador.ganharDefesa(1);
        } else if (resultado == 5) {
            jogador.ganharDefesa(2);
        } else if (resultado == 6) {
            for (Inimigo inimigo : setor.getInimigosVivos()) {
                inimigo.receberDano(1);
            }
        }

        finalizarAcao();
        return resultado;
    }

    public void recuperarDefesa(JogadorSuporte jogador, Jogador alvo) {
        validarAcao(jogador, TipoAcao.RECUPERAR_DEFESA);
        if (alvo != jogador && alvo != jogadorSimples) {
            throw new IllegalArgumentException("O alvo deve ser um dos jogadores");
        }
        if (!alvo.isVivo()) {
            throw new IllegalStateException("Um jogador eliminado não pode recuperar defesa");
        }
        if (!jogador.getPosicao().equals(alvo.getPosicao())) {
            throw new IllegalStateException("Os jogadores devem estar no mesmo setor");
        }

        jogador.recuperarDefesa(alvo);
        finalizarAcao();
    }

    private void validarAcao(Jogador jogador, TipoAcao tipoAcao) {
        validarTurno(jogador);
        if (ciclo == 0 && !movimentou) {
            throw new IllegalStateException("O jogador deve sair do centro antes de agir");
        }
        if (!jogador.acoesPermitidas().contains(tipoAcao)) {
            throw new IllegalArgumentException("Ação não permitida para este jogador");
        }
    }

    private void validarTurno(Jogador jogador) {
        if (terminou()) {
            throw new IllegalStateException("O jogo já terminou");
        }
        if (jogador == null || jogador != jogadorAtual || !jogador.isVivo()) {
            throw new IllegalArgumentException("Não é o turno deste jogador");
        }
    }

    private void finalizarAcao() {
        acoes++;
        if (acoes < 2) {
            return;
        }

        if (jogadorAtual == jogadorSimples && jogadorSuporte.isVivo()) {
            jogadorAtual = jogadorSuporte;
            acoes = 0;
            movimentou = false;
            return;
        }

        turnoInimigos();
        ciclo++;
        jogadorAtual = jogadorSimples.isVivo() ? jogadorSimples : jogadorSuporte;
        acoes = 0;
        movimentou = false;
    }

    private void turnoInimigos() {
        for (int linha = 1; linha <= Tabuleiro.TAMANHO; linha++) {
            for (int coluna = 1; coluna <= Tabuleiro.TAMANHO; coluna++) {
                Posicao posicao = new Posicao(linha, coluna);
                Setor setor = tabuleiro.getSetor(posicao);
                for (Inimigo inimigo : setor.getInimigosVivos()) {
                    atacarJogadores(inimigo, setor);
                }
            }
        }
    }

    private void atacarJogadores(Inimigo inimigo, Setor setor) {
        for (Jogador jogador : List.of(jogadorSimples, jogadorSuporte)) {
            if (jogador.isVivo() && jogador.getPosicao().equals(setor.getPosicao())) {
                atacarJogador(inimigo, jogador);
            }
        }
    }

    private void atacarJogador(Inimigo inimigo, Jogador jogador) {
        int resultado = 1 + aleatorio.nextInt(6);
        if (resultado % 2 == 0) {
            inimigo.atacar(jogador);
        }
    }
}
