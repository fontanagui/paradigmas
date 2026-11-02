package tabuleiro;

public record Posicao (int x, int y) {
    public Posicao vizinha (Direcao direcao) {
        return new Posicao(x + direcao.getDeltaX(), y + direcao.getDeltaY());
    }

    @Override 
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}

