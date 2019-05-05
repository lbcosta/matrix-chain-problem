import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Trabalho {
    static int[][] qtde;
    static int[][] corte;
    public static void main(String[] args) {
        int[] p = {8 ,3 ,1 ,10, 1};
        int n = 4;
        int multiplicacoes;

        char alg = 'm';
        String file = "/home/leo/IdeaProjects/Av2/src/entrada.txt";

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setMatrixes(n);
        switch(alg) {
            case 'm':
                multiplicacoes = memoization(1, n, p);
            case 'd':
                multiplicacoes = bottomUp(p);
            case 'g':
                multiplicacoes = greedy(1, n, p);
            default:
                multiplicacoes = 0;
        }

        if(multiplicacoes != 0) {
            printMatrixChain(corte, 1, n);
            System.out.println(" " + multiplicacoes);
        }
    }

    public static void setMatrixes(int n) {
        qtde = new int[n+1][n+1];
        corte = new int[n+1][n+1];
        for(int i = 1; i <= n; i++) {
            for(int j = i; j <= n; j++) {
                qtde[i][j] = Integer.MAX_VALUE;
                corte[i][j] = Integer.MAX_VALUE;
            }
        }
    }

//    public static void printMatrixes(int n) {
//        System.out.println();
//        for(int i = 0; i <= n; i++) {
//            for(int j = 0; j <= n; j++) {
//                if(qtde[i][j] < Integer.MAX_VALUE) System.out.print(qtde[i][j] + " | ");
//                else System.out.print("∞ | ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//        for(int i = 0; i <= n; i++) {
//            for(int j = 0; j <= n; j++) {
//                if(corte[i][j] < Integer.MAX_VALUE) System.out.print(corte[i][j] + " | ");
//                else System.out.print("x | ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }

    public static int memoization(int i, int j, int[] p) {
        if(qtde[i][j] < Integer.MAX_VALUE) return qtde[i][j];
        if(i == j) {
            qtde[i][j] = 0;
            return 0;
        }
        int qMin = Integer.MAX_VALUE;
        for(int k = i; k < j; k++) {
            int q = memoization(i, k, p) + memoization(k+1, j, p) + p[i-1] * p[k] * p[j];
            if(q < qMin) {
                qtde[i][j] = q;
                corte[i][j] = k;
                qMin = q;
            }
        }
        return qMin;
    }

    public static int bottomUp(int[] p) {
        int n = p.length - 1;

        for(int i = 1; i <= n; i++) {
            qtde[i][i] = 0;
        }

        for(int l = 2; l <= n; l++) {
            for(int i = 1 ;i <= n - l + 1; i++) {
                int j = i + l - 1;
                qtde[i][j] = Integer.MAX_VALUE;
                for(int k = i; k <= j - 1; k++) {
                    int q = qtde[i][k] + qtde[k+1][j] + p[i-1] * p[j] * p[k];
                    if(q < qtde[i][j]) {
                        qtde[i][j] = q;
                        corte[i][j] = k;
                    }
                }
            }
        }

        return qtde[1][n];
    }


    public static int greedy(int i, int j, int[] p) {
        if(i == j) return 0;
        int qMin = Integer.MAX_VALUE;
        for(int k = i; k < j; k++) {
            int q = p[i-1] * p[k] * p[j];
            if(q < qMin) {
                qMin = q;
                qtde[i][j] = qMin;
                corte[i][j] = k;
            }
        }
        qMin = qMin + greedy(i, corte[i][j], p) + greedy(corte[i][j] + 1, j, p);

        return qMin;
    }

    static void printMatrixChain(int[][]s, int i, int j) {
        if (i == j) {
            System.out.print("A" + i);
        } else {
            System.out.print("(");
            printMatrixChain(s, i, s[i][j]);
            printMatrixChain(s, s[i][j]+1, j);
            System.out.print(")");
        }
    }

}

