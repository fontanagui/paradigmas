package tabuleiro;



public enum Direcao {
    CIMA (-1, 0, "Cima"),
    BAIXO (1, 0, "Baixo"),
    ESQUERDA (0, -1, "Esquerda"),
    DIREITA (0, 1, "Direita");

    private final int deltaX;
    private final int deltaY;
    private final String nome;

    Direcao(int deltaX, int deltaY, String nome) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.nome = nome;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public String getNome() {
        return nome;
    }       

    public Direcao oposta() {
        return switch (this) {
            case CIMA -> BAIXO;
            case BAIXO -> CIMA;
            case ESQUERDA -> DIREITA;
            case DIREITA -> ESQUERDA;
        };
    }
}

