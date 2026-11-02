package modelo;

public enum TipoAcao {
    ATACAR("Atacar"),
    PROCURAR("Procurar itens"),
    RECUPERAR_DEFESA( "Recuperar defesa");
    
    private final String descricao;

    TipoAcao(String descricao) {
        this.descricao= descricao;
    }

    public String getDescricao() {
        return descricao;
    }
} 
