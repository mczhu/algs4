import java.util.HashMap;
import java.util.HashSet;
// import java.util.Scanner;
import java.io.File;
import java.util.Objects;

public class BaseballElimination {

    private final int numberOfTeams;
    private String[] teams;
    private HashMap<String, Integer> teamIndices;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private boolean[] isEliminated;
    private HashMap<String, HashSet<String>> certificateOfElimination;

    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        In in = new In(filename);
        numberOfTeams = in.readInt();

        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams]; 
        remaining = new int[numberOfTeams]; 
        against = new int[numberOfTeams][numberOfTeams];
        isEliminated = new boolean[numberOfTeams];
        certificateOfElimination = new HashMap<String, HashSet<String>>();
        
        teams = new String[numberOfTeams];
        teamIndices = new HashMap<String, Integer>();

        for (int i = 0; i < numberOfTeams; i++) {
            teams[i] = in.readString();
            teamIndices.put(teams[i], i);
            wins[i] =  in.readInt();
            losses[i] =  in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < numberOfTeams; j++)
                against[i][j] = in.readInt();
        }


        // TODO: check when numberOfTeams <= 2


        final int s = 0;
        final int nGameIdx = (numberOfTeams - 2) * (numberOfTeams - 1) / 2;
        final int t =  nGameIdx + numberOfTeams;

        for (int i = 0; i < numberOfTeams; i++) {
            System.out.println(i);
            // Trivial elimination
            HashSet<String> eTeams = new HashSet<String>();
            for (int t1 = 0; t1 < numberOfTeams; t1++) {
                if (t1 == i) continue;
                if (wins[i] + remaining[i] < wins[t1]) {
                    isEliminated[i] = true;
                    eTeams.add(teams[t1]);
                }
            }

            if (!isEliminated[i]) {
                // number of vertices is the number of team combinations + number of teams + 2 
                FlowNetwork network = new FlowNetwork(nGameIdx + numberOfTeams + 1);
                int count = 1;
                for (int t1 = 0; t1 < numberOfTeams - 1; t1++) {
                    if (t1 == i) continue;
                    for (int t2 = t1 + 1; t2 < numberOfTeams; t2++) {
                        if (t2 == i) continue;
                        // System.out.format("%d %d %d\n", t1, t2, count);
                        network.addEdge(new FlowEdge(s, count, against[t1][t2]));
                        network.addEdge(new FlowEdge(count, (t1 < i) ? (nGameIdx + 1 + t1) : (nGameIdx + t1),
                                                     Double.POSITIVE_INFINITY));
                        network.addEdge(new FlowEdge(count, (t2 < i) ? (nGameIdx + 1 + t2) : (nGameIdx + t2),
                                                     Double.POSITIVE_INFINITY));
                        count++;
                    }
                }
                for (int t1 = 0; t1 < numberOfTeams; t1++) {
                    if (t1 == i) continue;
                    network.addEdge(new FlowEdge((t1 < i) ? (nGameIdx + 1 + t1) : (nGameIdx + t1),
                                                 t, wins[i] + remaining[i] - wins[t1]));
                }
                // System.out.println(network.toString());
                int maxCapacity = 0;
                for (FlowEdge edge : network.adj(s))
                    maxCapacity += edge.capacity();
                FordFulkerson ff = new FordFulkerson(network, s, t);
                if (ff.value() == maxCapacity)
                    isEliminated[i] = false;
                else {
                    isEliminated[i] = true;
                    for (int idx = 0; idx < numberOfTeams - 1; idx++) {
                        if (ff.inCut(idx + nGameIdx + 1))
                            eTeams.add(teams[(idx < i) ? idx : (idx + 1)]);
                    }
                }
            }
            certificateOfElimination.put(teams[i], eTeams);
        }
    }

        
    public int numberOfTeams() {
        // number of teams
        return numberOfTeams;
    }

    public Iterable<String> teams() {
        // all teams
        HashSet<String> teams = new HashSet<String>();
        for (int i = 0; i < numberOfTeams; i++)
            teams.add(this.teams[i]);
        return teams;
    }

    public int wins(String team) {
        // number of wins for given team
        // TODO: exception if team not in table
        if (!teamIndices.containsKey(team)) throw new IllegalArgumentException();
        return wins[teamIndices.get(team)];
    }
    
    public int losses(String team) {
        // number of losses for given team
        if (!teamIndices.containsKey(team)) throw new IllegalArgumentException();
        return losses[teamIndices.get(team)];
    }

    public int remaining(String team) {
        // number of remaining games for given team
        if (!teamIndices.containsKey(team)) throw new IllegalArgumentException();
        return remaining[teamIndices.get(team)];
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        if (!teamIndices.containsKey(team1) || !teamIndices.containsKey(team2)) 
            throw new IllegalArgumentException();
        return against[teamIndices.get(team1)][teamIndices.get(team2)];
    }

    public boolean isEliminated(String team) {
        // is given team eliminated?
        if (!teamIndices.containsKey(team)) throw new IllegalArgumentException();
        return isEliminated[teamIndices.get(team)];
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        if (!teamIndices.containsKey(team)) throw new IllegalArgumentException();
        if (!isEliminated[teamIndices.get(team)]) return null;
        return certificateOfElimination.get(team);
    }

    public static void main(String[] args) {

        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
            // System.out.println(team);
            // System.out.println(division.isEliminated(team));
            // for (String opponent : division.teams()) {
            //     System.out.println(String.format("%4d", division.against(team, opponent)));
            // }
        }
    }
}
