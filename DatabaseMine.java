public class DatabaseMine implements DatabaseInterface {
    /*
     * Cette classe permet de créer un Node qui contient le key et le value
     * La raison de sa création estde pouvoir mettre dans un index le key et le value dans la liste
     * Permet de simplifier le setting et getting des variables de key et value de chaque index dans le array */
    public class Node {
        private String key, value;
        public Node(String key, String value) {
            this.key = key;
            this.value = value;
        }
        public String getKey() {
            return key;
        }
        public String getValue() {
            return value;
        }
        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }
    }

    /*
     * Nous avons ici un nombre premier pour la grandeur de la liste
     * On a aussi une variable de classe du nombre total de sondage 
     * On a aussi le nombre total de collisions (displacements)
     * Aussi une variable size pour connaître le nombres de passwords ajoutés à la liste
     */
    int N; 
    int totalNumOfProbes;
    int displacements;
    int size;
    private Node[] nodes;

    // this is a prime number that gives the number of addresses
    // these constructors must create your hash tables with enough positions N
    // to hold the entries you will insert; you may experiment with primes N
    // here you pick suitable default N
    public DatabaseMine(int N) {
        this.N = N;
        this.size = 0;
        this.nodes = new Node[N];
        this.totalNumOfProbes = 0;
        this.displacements = 0;
    } 

    public DatabaseMine() {
        this.N = 203323;
        this.size = 0;
        this.nodes = new Node[this.N];
        this.totalNumOfProbes = 0;
        this.displacements = 0;
    }

    public double loadingFactor() {
        return ((double)size()/(double) N);
    }
    
    // here the N is given by the user
    int hashFunction(String key) {
        int address=key.hashCode()%N;
        return (address>=0)?address:(address+N);
    }


    /**
     * Ici, il faut qu'on considère des différents conditions, dont l'une est qu'on peut tout de suite insér3r
       l'élément dans la liste parce que c'est vide à cet endroit
     * L'autre condition est que les valeurs sont les mêmes donc on peut juste changer la valeur à cet index
     * La dernière est que l'espace est occupé et n'est pas égal à la valeur donc il faut qu'on fait du sondage linéaire
       jusqu'à temps qu'on trouve la même valeur ou un endroit vide pour stocker l'élément.
     */
    // Stores plainPassword and corresponding encryptedPassword in a map.
	 // if there was a value associated with this key, it is replaced, 
	 // and previous value returned; otherwise, null is returned
	 // The key is the encryptedPassword the value is the plainPassword

    public String save(String plainPassword, String encryptedPassword){
        int index = hashFunction(encryptedPassword);
        int address = hashFunction(encryptedPassword);
        Node node = new Node(encryptedPassword, plainPassword);
        String value = null;
        if(nodes[index] == null) {
            nodes[index] = node;  
            size++;
        } else if (plainPassword.equals(nodes[index].getValue())){
            value = nodes[index].getValue();
            nodes[index].setValue(plainPassword);
            return value;
        } else {
            do {
                index++;
                totalNumOfProbes = totalNumOfProbes + (index - address +1);
                displacements++;
            } while (nodes[index] != null && !(plainPassword.equals(nodes[index].getValue())));
            if(nodes[index] == null) {
                nodes[index] = node;
                size++;
            } else {
                value = nodes[index].getValue();
                nodes[index].setValue(plainPassword);
            }      
        }
        return value;
    }

	// returns plain password corresponding to encrypted password 
	public String decrypt(String encryptedPassword) {
        Node node = nodes[hashFunction(encryptedPassword)];
        int index = hashFunction(encryptedPassword);
        for(int i = 0; i < N; i++) {
            if(node == null) {
                return "No such key";
            } else if (encryptedPassword.equals(node.getKey())) {
                return node.getValue();
            } else {
                index++;
                node = nodes[index];
            }
        }   
        return "No such key";    
    }
	
	// returns the number of password pairs stored in the database
    public int size() {
        return size;
    }

    // important statistics must be collected (here or during construction) and
    // printed here: size, number of indexes, load factor, average number of probes
    // and average number of displacements (see output example below)
    public void printStatistics() { 
        System.out.println("Our output for the program above is: ");
        System.out.println("*** DatabaseMine Statistics ***");
        System.out.println("Number of indexes: "+N);
        System.out.println("Load factor is: "+loadingFactor());
        System.out.println("Average number of probes is: "+(double)totalNumOfProbes/(double)size());
        System.out.println("Number of displacements due to collisions are: "+displacements);
        System.out.println("*** End DatabaseMine Statistics ***");
    }
    
}  