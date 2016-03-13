import java.util.Scanner;
import java.math.BigInteger;

public class FastFib {
    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        System.out.println(St(n - 1));
    }
    private static BigInteger St(int n) {
        int i,j,z;
        BigInteger[][] a = new BigInteger[][]{{BigInteger.ONE, BigInteger.ZERO}, {BigInteger.ZERO, BigInteger.ONE}};
        BigInteger[][] b = new BigInteger[][]{{BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ZERO}};
        while(n!=0) {
            if ( n % 2 != 0) {
                BigInteger[][] c = new BigInteger[][]{{BigInteger.ZERO, BigInteger.ZERO}, {BigInteger.ZERO, BigInteger.ZERO}};
                for (i = 0; i < 2; i++) {
                    for (j = 0; j < 2; j++) {
                        for (z = 0; z < 2; z++) {
                            c[i][j] = c[i][j].add(a[i][z].multiply(b[z][j]));
                        }
                    }
                }
                a=c;
            }
            n >>= 1;
            BigInteger[][] d = new BigInteger[][]{{BigInteger.ZERO, BigInteger.ZERO}, {BigInteger.ZERO, BigInteger.ZERO}};
            for (i = 0; i < 2; i++) {
                for (j = 0; j < 2; j++) {
                    for (z = 0; z < 2; z++) {
                        d[i][j] = d[i][j].add(b[i][z].multiply(b[z][j]));
                    }
                }
            }
            b=d;
        }
        return a[1][0].add(a[1][1]);
    }
}
