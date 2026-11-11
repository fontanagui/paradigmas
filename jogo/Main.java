package jogo;

import modelo.Inimigo;
import modelo.Jogador;
import modelo.JogadorSuporte;
import modelo.TipoAcao;
import tabuleiro.Direcao;
import tabuleiro.Setor;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        System.out.println("=== Jogo de Tabuleiro ===");
        System.out.println("Encontre a fonte em até 25 ciclos. Os jogadores começam no centro [3, 3].");

        while (!jogo.terminou()) {
            mostrarEstado(jogo);
            Jogador jogador = jogo.getJogadorAtual();
            Setor setor = jogo.getTabuleiro().getSetor(jogador.getPosicao());

            List<String> opcoes = new java.util.ArrayList<>();
            opcoes.add("Mover");
            for (TipoAcao acao : jogador.acoesPermitidas()) {
                opcoes.add(acao.getDescricao());
            }
            opcoes.add("Sair do jogo");

            int escolha = escolher("Escolha uma ação:", opcoes);
            String opcao = opcoes.get(escolha);

            try {
                if (opcao.equals("Mover")) {
                    mover(jogo, jogador, setor);
                } else if (opcao.equals(TipoAcao.ATACAR.getDescricao())) {
                    atacar(jogo, jogador, setor);
                } else if (opcao.equals(TipoAcao.PROCURAR.getDescricao())) {
                    int dado = jogo.procurar(jogador);
                    System.out.println("Dado: " + dado + " -> " + descreverProcura(dado));
                } else if (opcao.equals(TipoAcao.RECUPERAR_DEFESA.getDescricao())) {
                    recuperar(jogo, (JogadorSuporte) jogador);
                } else {
                    System.out.println("Jogo encerrado.");
                    return;
                }
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println(">> Jogada inválida: " + e.getMessage());
            }
        }

        mostrarResultado(jogo);
    }

    private static void mostrarEstado(Jogo jogo) {
        Jogador jogador = jogo.getJogadorAtual();
        Setor setor = jogo.getTabuleiro().getSetor(jogador.getPosicao());

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Ciclo " + jogo.getCiclo() + " | Vez de: " + jogador.getNome());
        System.out.println("P1: " + jogo.getJogadorSimples().getAtkDef() + " em "
            + jogo.getJogadorSimples().getPosicao()
            + (jogo.getJogadorSimples().isVivo() ? "" : " (eliminado)"));
        System.out.println("P2: " + jogo.getJogadorSuporte().getAtkDef() + " em "
            + jogo.getJogadorSuporte().getPosicao()
            + (jogo.getJogadorSuporte().isVivo() ? "" : " (eliminado)"));
        System.out.println("Setor " + jogador.getPosicao() + " (" + setor.getTipo() + ")");

        StringBuilder portas = new StringBuilder();
        for (Direcao d : Direcao.values()) {
            if (setor.havePorta(d)) {
                portas.append(d.getNome()).append(" ");
            }
        }
        System.out.println("Portas: " + portas);

        List<Inimigo> inimigos = setor.getInimigosVivos();
        if (inimigos.isEmpty()) {
            System.out.println("Inimigos: nenhum");
        } else {
            System.out.println("Inimigos:");
            for (Inimigo inimigo : inimigos) {
                System.out.println("  - " + inimigo.getNome() + " (" + inimigo.getAtkDef() + ")");
            }
        }
    }

    private static void mover(Jogo jogo, Jogador jogador, Setor setor) {
        List<Direcao> disponiveis = new java.util.ArrayList<>();
        List<String> nomes = new java.util.ArrayList<>();
        for (Direcao d : Direcao.values()) {
            if (setor.havePorta(d)) {
                disponiveis.add(d);
                nomes.add(d.getNome());
            }
        }
        if (disponiveis.isEmpty()) {
            System.out.println("Não há portas neste setor.");
            return;
        }
        int escolha = escolher("Para onde?", nomes);
        jogo.mover(jogador, disponiveis.get(escolha));

        if (jogo.getVencedor() != null) {
            return;
        }
        Setor novo = jogo.getTabuleiro().getSetor(jogador.getPosicao());
        System.out.println("Você entrou no setor " + jogador.getPosicao() + " (" + novo.getTipo() + ").");
        if (novo.temInimigosVivos()) {
            System.out.println("Há inimigos neste setor!");
        }
    }

    private static void atacar(Jogo jogo, Jogador jogador, Setor setor) {
        List<Inimigo> inimigos = setor.getInimigosVivos();
        Inimigo alvo = null;

        if (!inimigos.isEmpty() && setor.permiteEscolherAlvo()) {
            List<String> nomes = new java.util.ArrayList<>();
            for (Inimigo inimigo : inimigos) {
                nomes.add(inimigo.getNome() + " (" + inimigo.getAtkDef() + ")");
            }
            alvo = inimigos.get(escolher("Qual inimigo atacar?", nomes));
        }

        boolean acertou = jogo.atacar(jogador, alvo);
        System.out.println(acertou ? "O ataque acertou!" : "O ataque errou.");
    }

    private static void recuperar(Jogo jogo, JogadorSuporte suporte) {
        List<String> nomes = List.of(
            jogo.getJogadorSimples().getNome(),
            jogo.getJogadorSuporte().getNome());
        int escolha = escolher("Quem recupera defesa?", nomes);
        Jogador alvo = escolha == 0 ? jogo.getJogadorSimples() : jogo.getJogadorSuporte();
        jogo.recuperarDefesa(suporte, alvo);
        System.out.println(alvo.getNome() + " recuperou defesa. Agora: " + alvo.getAtkDef());
    }

    private static String descreverProcura(int dado) {
        return switch (dado) {
            case 4 -> "+1 de defesa";
            case 5 -> "+2 de defesa";
            case 6 -> "1 de dano em todos os inimigos do setor";
            default -> "nada encontrado";
        };
    }

    private static void mostrarResultado(Jogo jogo) {
        System.out.println();
        System.out.println("=== Fim de jogo ===");
        Jogador vencedor = jogo.getVencedor();
        if (vencedor != null) {
            System.out.println(vencedor.getNome() + " encontrou a fonte e venceu!");
        } else if (!jogo.getJogadorSimples().isVivo() && !jogo.getJogadorSuporte().isVivo()) {
            System.out.println("Os dois jogadores foram eliminados. Derrota.");
        } else {
            System.out.println("O limite de ciclos foi atingido. Derrota.");
        }
        System.out.println("A fonte estava em " + jogo.getTabuleiro().getFonte() + ".");
    }

    private static int escolher(String pergunta, List<String> opcoes) {
        while (true) {
            System.out.println(pergunta);
            for (int i = 0; i < opcoes.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + opcoes.get(i));
            }
            System.out.print("> ");
            if (!entrada.hasNextLine()) {
                System.out.println("Entrada encerrada.");
                System.exit(0);
            }
            String linha = entrada.nextLine().trim();
            try {
                int numero = Integer.parseInt(linha);
                if (numero >= 1 && numero <= opcoes.size()) {
                    return numero - 1;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.println("Opção inválida, digite um número de 1 a " + opcoes.size() + ".");
        }
    }
}
