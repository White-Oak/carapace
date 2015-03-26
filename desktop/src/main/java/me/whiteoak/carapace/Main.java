package me.whiteoak.carapace;

import java.util.List;

public class Main {

    /**
     * cArAPACE	Annimon jAva PAge CliEnt
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	Carapace carapace = new Carapace(Oak.OAK);
	carapace.authorize();
	List<Topic> lastTopics = carapace.getLastTopics();
	lastTopics.forEach(lastTopic -> System.out.println(lastTopic));
    }
}
