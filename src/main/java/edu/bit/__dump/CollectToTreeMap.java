package edu.bit.__dump;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CollectToTreeMap {


    private Map<String, Skill> skillMap = new HashMap<>();

    public SortedMap<String, Long> skill_nApplicants() {
        return new TreeMap<>(skillMap.values().stream().collect(Collectors.toMap(Skill::getName, Skill::getNumApplicants)));
    }


    public static class Skill {

        private String name;
        private Long numApplicants;

        public Skill(String name) {
            super();
            this.name = name;
            this.numApplicants = 0L;
        }

        public void plusOneApplicant() {
            this.numApplicants++;
        }

        public Long getNumApplicants() {
            return numApplicants;
        }

        public String getName() {
            return name;
        }
    }
}