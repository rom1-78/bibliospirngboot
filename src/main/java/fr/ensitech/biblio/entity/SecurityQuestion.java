package fr.ensitech.biblio.entity;

public enum SecurityQuestion {
    VILLE_NAISSANCE("Dans quelle ville êtes-vous né(e) ?"),
    NOM_ANIMAL("Quel était le nom de votre premier animal de compagnie ?"),
    NOM_ECOLE("Quel est le nom de votre école primaire ?"),
    LIVRE_PREFERE("Quel est le titre de votre livre préféré ?"),
    NOM_JEUNE_FILLE_MERE("Quel est le nom de jeune fille de votre mère ?");

    private final String question;

    SecurityQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}