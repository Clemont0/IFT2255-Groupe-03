public class Intervenant extends Utilisateur{
    private TypeIntervenant type;
    private int id;

    public Intervenant(int id, String nom, String courriel, String mdp, TypeIntervenant type){
        super(nom,courriel,mdp);
        this.id = id;
        this.type = type;
    }

    public String getType() {
        return type.name();
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean estValide() {
        boolean cond = id >= 10000000 && id <= 99999999;

        return super.estValide() && cond;
    }

    @Override
    public String toString() {
        String string = super.toString();
        StringBuilder sb = new StringBuilder();

        sb.append(string);
        sb.append(" Type: ").append(type).append("\n");
        sb.append(" Identifiant: ").append(id).append("\n");

        return sb.toString();
    }
}
