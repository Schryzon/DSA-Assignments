/**
 * FF7 flavored data classes. Simple POJOs used as payloads in nodes.
 */
public class FF7_Types{
    public static class Character implements Comparable<Character>{
        public String name;
        public int hp;
        public int level;

        public Character(String name, int hp, int level) {
            this.name = name;
            this.hp = hp;
            this.level = level;
        }

        @Override
        public int compareTo(Character other){
            return this.name.compareToIgnoreCase(other.name);
        }

        @Override
        public String toString() {
            return String.format("%s (Lv %d, HP %d)", this.name, this.level, this.hp);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof Character)) {
                return false;
            }
            Character other = (Character) o;
            return this.name.equals(other.name);
        }
    }

    public static class Materia implements Comparable<Materia>{
        public String name;
        public int grade;

        public Materia(String name, int grade) {
            this.name = name;
            this.grade = grade;
        }

        @Override
        public int compareTo(Materia other) {
            if (other == null) return 1;
            int name_comp = this.name.compareToIgnoreCase(other.name);
            if (name_comp != 0) return name_comp;

            // When either grade is 0 (user placeholder), treat as equal
            if (this.grade == 0 || other.grade == 0) return 0;

            return Integer.compare(this.grade, other.grade);
        }

        @Override
        public String toString() {
            return String.format("%s (G%d)", this.name, this.grade);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof Materia)) {
                return false;
            }
            Materia other = (Materia) o;
            return this.name.equals(other.name) && this.grade == other.grade;
        }
    }
}
